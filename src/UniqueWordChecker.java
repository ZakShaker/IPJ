import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class UniqueWordChecker implements Callable<Boolean> {
    private SerialCharacterReader reader;
    private Map<String, Integer> uniqueWords;
    private AllowedWordSymbols allowedWordSymbols;

    public UniqueWordChecker(SerialCharacterReader reader, AllowedWordSymbols allowedWordSymbols) {
        init(new HashMap<>(), reader, allowedWordSymbols);
    }

    public UniqueWordChecker(Map<String, Integer> uniqueWords, SerialCharacterReader reader, AllowedWordSymbols allowedWordSymbols) {
        init(uniqueWords, reader, allowedWordSymbols);
    }

    private void init(Map<String, Integer> uniqueWords, SerialCharacterReader reader, AllowedWordSymbols allowedWordSymbols) {
        this.reader = reader;
        this.uniqueWords = uniqueWords;
        this.allowedWordSymbols = allowedWordSymbols;
    }

    @Override
    public Boolean call() throws ForeignCharacterException, IOException {
        StringBuilder word = new StringBuilder();

        int c = reader.next();
        while (c != -1) {
            //building a word with appropriate symbols
            if (allowedWordSymbols.isPartOfWord((char) c)) {
                word.append((char) c);
            } else {
                if (!allowedWordSymbols.isPunctuation((char) c)) {
                    System.out.println("Foreign symbol");
                    throw new ForeignCharacterException("The source has a foreign character. Only cyrillic letters, digits and punctuation are allowed.");
                }
                String wordString = word.toString().toLowerCase();
                if (allowedWordSymbols.wordIsEmpty(wordString)) {
                    word = new StringBuilder();
                } else {
                    Integer counts = uniqueWords.put(wordString, 1);
                    if (counts == null) {
                        System.out.print(word + "=");
                        word = new StringBuilder();
                    } else {
                        uniqueWords.put(wordString, counts + 1); //TODO: может просто менять этот интеджер - если это ссылка - то есть без доп обращения к карте?
                        return false;
                    }
                }
            }

            c = reader.next();
        }
        return true;
    }

}
