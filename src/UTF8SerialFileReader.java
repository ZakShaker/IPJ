import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class UTF8SerialFileReader implements SerialCharacterReader {

    private int bufferSize = 4096;
    private Charset cs = Charset.forName("utf-8");
    private RandomAccessFile randomAccessFile;
    private FileChannel inChanel;
    private ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
    private byte[] unrecognizedBytes = new byte[0];
    private int readed = -1;
    private CharBuffer chbuf;
    private int currentBufferSize;
    private ByteBuffer widerBuffer; // buffer with bytes from the file + not decoded bytes

    public UTF8SerialFileReader(File inputUTF8File) throws FileNotFoundException, IOException {
        init(inputUTF8File);
    }

    public UTF8SerialFileReader(File inputUTF8File, int bufferSize) throws FileNotFoundException, IOException {
        this.bufferSize = bufferSize;
        init(inputUTF8File);
    }

    @Override
    public int next() throws IOException {
        if (readed != -1) {
            // passing by through characters till the penultimate
            if (chbuf.position() < chbuf.limit() - 1) {
                char c = chbuf.get();
                return c;
            } else {
                // checking if the last character has been recognized
                char lastCharacter = chbuf.get();
                if (lastCharacter == '�' || lastCharacter == ' ') {
                    // getting the last unrecognized bytes in the buffer
                    unrecognizedBytes = takeUnrecognizedBytes(
                            widerBuffer,
                            takeRecognizedBytes(chbuf.limit() - 1)
                    );
                    readNextChunkIntoBuffer();
                    return next();
                } else {
                    unrecognizedBytes = takeUnrecognizedBytes(
                            widerBuffer,
                            takeRecognizedBytes(chbuf.limit())
                    );
                    readNextChunkIntoBuffer();
                    return lastCharacter;
                }
            }
        } else {
            return -1; // the file is over - nothing to read
        }
    }

    @Override
    public void close() throws IOException {
        if (randomAccessFile != null) {
            randomAccessFile.close();
        }
    }

    private void init(File inputUTF8File) throws FileNotFoundException, IOException {
        try {
            randomAccessFile = new RandomAccessFile(inputUTF8File, "r");
            inChanel = randomAccessFile.getChannel(); //этих файл-каналов можно сделать сколько угодно над одним фалом даже
            readNextChunkIntoBuffer();
        } catch (FileNotFoundException fileException) {
            System.out.println("The file " + inputUTF8File.getAbsolutePath() + " is not found");
            fileException.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("Error while reading the file " + inputUTF8File.getAbsolutePath());
            ioException.printStackTrace();
        }
    }


    private char[] takeRecognizedBytes(int recognizedCharsNumber) {
        char[] recognizedChars = new char[recognizedCharsNumber];
        chbuf.flip();
        chbuf.get(recognizedChars, 0, recognizedCharsNumber);
        return recognizedChars;
    }

    //read the next chunk of bytes into the buffer
    private void readNextChunkIntoBuffer() throws IOException {
        buffer.clear(); // clearing and reading next chunk of bytes
        readed = inChanel.read(buffer);

        currentBufferSize = bufferSize + unrecognizedBytes.length;
        // preparing the buffer of characters to convert bytes

        widerBuffer = ByteBuffer.allocate(currentBufferSize);
        widerBuffer.put(unrecognizedBytes);
        buffer.flip(); //setting the position in the buffer to the first not read character
        widerBuffer.put(buffer);
        widerBuffer.flip();
        chbuf = cs.decode(widerBuffer);
    }


    //returns bytes at the end of the byte buffer that were not decoded into real symbols
    public static byte[] takeUnrecognizedBytes(ByteBuffer buffer, char[] decodedChars) {
        int unrecognizedBytesNumber = buffer.limit() - consumedBytesUTF8(decodedChars);
        byte[] unrecognizedBytes = new byte[unrecognizedBytesNumber];

        for (int i = 0; i < unrecognizedBytesNumber; i++) {
            int j = buffer.limit() - unrecognizedBytesNumber + i;
            unrecognizedBytes[i] = buffer.get(j);
        }
        return unrecognizedBytes;
    }

    //returns number of bytes consumed by particular chars
    public static int consumedBytesUTF8(char[] chars) {
        int b = 0;
        for (int i = 0; i < chars.length; i++) {
            int skip = 0;
            int more;
            if (chars[i] <= 0x007f) {
                more = 1;
            } else if (chars[i] <= 0x07FF) {
                more = 2;
            } else if (chars[i] <= 0xd7ff) {
                more = 3;
            } else if (chars[i] <= 0xDFFF) {
                // surrogate area, consume next 4 char as well
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }
            b += more;
            i += skip;
        }
        return b;
    }
}
