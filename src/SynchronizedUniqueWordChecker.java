import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 27.01.2018.
 */
public class SynchronizedUniqueWordChecker extends MutlipleUniqueWordChecker {
    public SynchronizedUniqueWordChecker(Set<SerialCharacterReader> readers, AllowedWordSymbols allowedWordSymbols) {
        super(Collections.synchronizedMap(new HashMap<>()), readers, allowedWordSymbols);
    }
}
