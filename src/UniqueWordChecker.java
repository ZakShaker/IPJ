import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.Callable;

public class UniqueWordChecker implements Callable<Boolean> {
    private UniqueChecker uniqueChecker;
    private SerialCharacterReader reader;
    private Map<String, Integer> uniqueWords;
    private AllowedWordSymbols allowedWordSymbols;

    public UniqueWordChecker(Map<String, Integer> uniqueWords, SerialCharacterReader reader, AllowedWordSymbols allowedWordSymbols) {
        UniqueChecker checker = (UniqueChecker) Proxy.newProxyInstance(
                UniqueChecker.class.getClassLoader(),
                new Class[]{UniqueChecker.class},
                new UniqueWordCheckerInvocationHandler()
        );

        init(checker, uniqueWords, reader, allowedWordSymbols);
    }

    private void init(UniqueChecker uniqueChecker, Map<String, Integer> uniqueWords, SerialCharacterReader reader, AllowedWordSymbols allowedWordSymbols) {
        this.uniqueChecker = uniqueChecker;
        this.reader = reader;
        this.uniqueWords = uniqueWords;
        this.allowedWordSymbols = allowedWordSymbols;
    }

    @Override
    public Boolean call() throws ForeignCharacterException, IOException {
        Boolean result = uniqueChecker.checkUniqueness(reader, uniqueWords, allowedWordSymbols);
        //closing the reader after checking
        reader.close();
        return result;
    }

}
