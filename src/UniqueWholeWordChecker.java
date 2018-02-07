import java.io.IOException;
import java.util.Map;

public class UniqueWholeWordChecker implements UniqueChecker {
    @Override
    public boolean checkUniqueness(SerialCharacterReader reader, Map<String, Integer> uniqueWords, AllowedWordSymbols allowedWordSymbols) throws IOException, ForeignCharacterException {
        StringBuilder word = new StringBuilder();

        int c = reader.next();
        while (c != -1) {
            //building a word with appropriate symbols
            // PLUS+ word.length should be less than 10
            if (allowedWordSymbols.isPartOfWord((char) c) && !(word.length() > 10)) {
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
