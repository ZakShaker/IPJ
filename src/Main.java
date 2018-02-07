import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws IOException {

        HashSet<SerialCharacterReader> readers = new HashSet<>(799);

        for (int i = 1; i <= 4; i++) {
            String fileName = "texts/text_" + i + ".txt";
            readers.add(
                    new UTF8SerialFileReader(
                            new File(fileName)
                    )
            );
        }
        long startTime = System.currentTimeMillis();

        SynchronizedUniqueWordChecker synchronizedUniqueWordChecker =
                new SynchronizedUniqueWordChecker(
                        readers,
                        new RussianWordSymbols()
                );

        startTime = System.currentTimeMillis();

        while (true) {
            try {
                //Здесь ждем
                new BufferedReader(new InputStreamReader(System.in)).readLine();

                if (synchronizedUniqueWordChecker.checkUniqueness()) {
                    System.out.println("\n <<< The text doesn't have repeats! >>>");
                } else {
                    System.out.println("\n <<< The text has repeats :-/ >>>");
                }
            } catch (ForeignCharacterException e) {
                System.out.println("\n Sources have foreign characters");
            }

            System.out.println("\n Time consumed when Synchronized: " + (System.currentTimeMillis() - startTime));

        }



    }

}