import java.io.IOException;

/**
 * Created by user on 27.01.2018.
 */
public interface SerialCharacterReader {
    int next() throws IOException; // returns next character in int code

    void close() throws IOException; // closes the input
}
