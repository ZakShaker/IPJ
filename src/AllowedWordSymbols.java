/**
 * Created by user on 27.01.2018.
 */
public interface AllowedWordSymbols {
    boolean isPartOfWord(char c);

    boolean isPunctuation(char c);

    //returns true if the sequence of chars in a word is considered as empty
    boolean wordIsEmpty(String word);
}