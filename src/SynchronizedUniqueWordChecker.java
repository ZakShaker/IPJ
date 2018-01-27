import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class SynchronizedUniqueWordChecker extends MutlipleUniqueWordChecker {
    public SynchronizedUniqueWordChecker(Set<SerialCharacterReader> readers, AllowedWordSymbols allowedWordSymbols) {
        super(Collections.synchronizedMap(new HashMap<>()), readers, allowedWordSymbols);
    }
}
