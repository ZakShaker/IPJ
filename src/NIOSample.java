import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.RandomAccess;

/**
 * Created by user on 22.01.2018.
 */
public class NIOSample { // делает то же самое, что и FileApp2

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


    public static void main(String[] args) throws IOException {
        int bufferSize = 4096;

        Charset cs = Charset.forName("utf-8");

        RandomAccessFile randomAccessFile = new RandomAccessFile("input.txt", "rw");
        FileChannel inChanel = randomAccessFile.getChannel(); //этих файл-каналов можно сделать сколько угодно над одним фалом даже
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        int readed = inChanel.read(buffer);

        byte[] unrecognizedBytes = new byte[0];

        while (readed != -1) {
            buffer.flip(); //устанавливает буфер в начало даннных, которые считал

            int currentBufferSize = bufferSize + unrecognizedBytes.length;
            // preparing the buffer of characters to convert bytes
            CharBuffer chbuf;

            ByteBuffer widerBuffer = ByteBuffer.allocate(currentBufferSize);
            widerBuffer.put(unrecognizedBytes);
            widerBuffer.put(buffer);
            widerBuffer.flip();
            chbuf = cs.decode(widerBuffer);

            // passing by through characters till the penultimate
            while (chbuf.position() < chbuf.limit() - 1) {
                char c = chbuf.get();
                System.out.print(c);
            }


            System.out.println();


            // checking if the last character has been recognized
            char lastCharacter = chbuf.get();
            if (lastCharacter == '�' || lastCharacter == ' ') {
                // getting the last unrecognized bytes in the buffer
                char[] recognizedChars = new char[chbuf.limit() - 1];
                chbuf.flip();
                chbuf.get(recognizedChars, 0, chbuf.limit() - 1);
                unrecognizedBytes = takeUnrecognizedBytes(widerBuffer, recognizedChars);
            } else {
                char[] recognizedChars = new char[chbuf.limit()];
                chbuf.flip();
                chbuf.get(recognizedChars, 0, chbuf.limit());
                unrecognizedBytes = takeUnrecognizedBytes(widerBuffer, recognizedChars);

                System.out.print(lastCharacter);
            }

            buffer.clear(); //очищаем и считываем новый кусок
            readed = inChanel.read(buffer);
        }
        randomAccessFile.close();
    }

    public static byte[] takeUnrecognizedBytes(ByteBuffer buffer, char[] decodedChars) {
        int unrecognizedBytesNumber = buffer.limit() - consumedBytesUTF8(decodedChars);
        byte[] unrecognizedBytes = new byte[unrecognizedBytesNumber];

        for (int i = 0; i < unrecognizedBytesNumber; i++) {
            int j = buffer.limit() - unrecognizedBytesNumber + i;
            unrecognizedBytes[i] = buffer.get(j);
        }
        return unrecognizedBytes;
    }
}
