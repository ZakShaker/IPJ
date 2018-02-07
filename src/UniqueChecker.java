import java.io.IOException;
import java.util.Map;

public interface UniqueChecker {
    boolean checkUniqueness(SerialCharacterReader reader, Map<String, Integer> uniqueWords, AllowedWordSymbols allowedWordSymbols) throws IOException, ForeignCharacterException;
}
