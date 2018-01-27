import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) throws IOException {

        HashSet<SerialCharacterReader> readers = new HashSet<>(799);

        for (int i = 1; i <= 799; i++) {
            String fileName = "texts/text_" + i + ".txt";
            readers.add(
                    new UTF8SerialFileReader(
                            new File(fileName)
                    )
            );
        }

        // Test #1
        ConcurrentUniqueWordChecker concurrentUniqueWordChecker =
                new ConcurrentUniqueWordChecker(
                        readers,
                        new RussianWordSymbols()
                );

        long startTime = System.currentTimeMillis();

        try {
            if (concurrentUniqueWordChecker.checkUniqueness()) {
                System.out.println("\n <<< The text doesn't have repeats! >>>");
            } else {
                System.out.println("\n <<< The text has repeats :-/ >>>");
            }
        } catch (ForeignCharacterException e) {
            System.out.println("\n Sources have foreign characters");
        }

        System.out.println("\n Time consumed when Concurrent: " + (System.currentTimeMillis() - startTime));


        // Test #2
        SynchronizedUniqueWordChecker synchronizedUniqueWordChecker =
                new SynchronizedUniqueWordChecker(
                        readers,
                        new RussianWordSymbols()
                );

        startTime = System.currentTimeMillis();

        try {
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