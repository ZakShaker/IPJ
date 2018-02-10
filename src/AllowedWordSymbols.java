public class AllowedWordSymbols {
    private PartOfWordDetector partOfWordDetector;
    private PunctuationDetector punctuationDetector;
    private EmptyWordDetector emptyWordDetector;

    AllowedWordSymbols(PartOfWordDetector partOfWordDetector, PunctuationDetector punctuationDetector, EmptyWordDetector emptyWordDetector) {
        this.partOfWordDetector = partOfWordDetector;
        this.punctuationDetector = punctuationDetector;
        this.emptyWordDetector = emptyWordDetector;
    }

    public boolean isPartOfWord(char c) {
        return partOfWordDetector.isPartOfWord(c);
    }

    public boolean isPunctuation(char c) {
        return punctuationDetector.isPunctuation(c);
    }

    public boolean wordIsEmpty(String word) {
        return emptyWordDetector.wordIsEmpty(word);
    }
}
