import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class UniqueWordChecker implements Callable<Boolean> {
    private StringBuilder sourceString;
    private ConcurrentHashMap<String, Integer> uniqueWords;

    public UniqueWordChecker(ConcurrentHashMap<String, Integer> uniqueWords, String sourceString) {
        this.uniqueWords = uniqueWords;
        this.sourceString = new StringBuilder(sourceString);
        if (sourceString.charAt(sourceString.length() - 1) != '\n') {
            this.sourceString.append('\n');
        }
    }

    @Override
    public Boolean call() throws ForeignCharacterException {
        System.out.println("\n = = = start = = =");
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {
            char c = sourceString.charAt(i);
            //building a word with appropriate symbols
            if (isPartOfWord(c)) {
                word.append(c);
            } else {
                if (!isPunctuation(c)) {
                    System.out.println("Foreign symbol");
                    throw new ForeignCharacterException("The source has a foreign character. Only cyrillic letters, digits and punctuation are allowed.");
                }
                String wordString = word.toString().toLowerCase();
                if (wordIsEmpty(wordString)) {
                    word = new StringBuilder();
                } else {
                    if (uniqueWords.put(wordString, 1) == null) {
                        System.out.print(word + "=");
                        word = new StringBuilder();
                    } else {
                        uniqueWords.put(wordString, uniqueWords.get(wordString) + 1);
                        System.out.println("\n= = = end = = =");
                        return false;
                    }
                }
            }
        }
        System.out.println("\n = = = end = = =");
        return true;
    }

    //The next words are considered as entire words
    //'диван-кровать'
    //'Турбо500'
    //'Яшка'
    //'999'

    private boolean isPartOfWord(char c) {
        return c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я' || c >= '0' && c <= '9' || c == '-';
    }

    private boolean wordIsEmpty(String word) {
        return word.isEmpty() || word.matches("[ \n\t]+");
    }

    private boolean isPunctuation(char c) {
        return c == '!' ||
                c == '"' ||
                c == '#' ||
                c == '$' ||
                c == '%' ||
                c == '&' ||
                c == '\'' ||
                c == '(' ||
                c == ')' ||
                c == '*' ||
                c == '+' ||
                c == ',' ||
                c == '.' ||
                c == '\\' ||
                c == '/' ||
                c == ':' ||
                c == ';' ||
                c == '<' ||
                c == '=' ||
                c == '>' ||
                c == '?' ||
                c == '@' ||
                c == '[' ||
                c == ']' ||
                c == '^' ||
                c == '_' ||
                c == ' ' ||
                c == '|' ||
                c == '§' ||
                c == '±' ||
                c == '~' ||
                c == '`' ||
                c == '{' ||
                c == '}' ||
                c == '\n';
    }
}
