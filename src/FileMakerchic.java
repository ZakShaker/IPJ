import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Created by user on 22.01.2018.
 */
public class FileMakerchic {
    public static void main(String[] args) {


        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {

            String lineOfText;
            int i = 0;
            while ((lineOfText = reader.readLine()) != null) {
                i++;
                String fileName = "text_" + i + ".txt";

                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    // перевод строки в байты
                    byte[] buffer = lineOfText.getBytes();
                    fos.write(buffer, 0, buffer.length);
                    fos.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}