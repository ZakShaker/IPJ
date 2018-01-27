import java.io.IOException;

public interface SerialCharacterReader {
    int next() throws IOException; // returns next character in int code

    void close() throws IOException; // closes the input
}
