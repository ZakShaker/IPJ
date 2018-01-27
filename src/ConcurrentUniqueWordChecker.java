import java.util.*;
import java.util.concurrent.*;

public class ConcurrentUniqueWordChecker extends MutlipleUniqueWordChecker {

    public ConcurrentUniqueWordChecker(Set<SerialCharacterReader> readers, AllowedWordSymbols allowedWordSymbols) {
        super(new ConcurrentHashMap<>(), readers, allowedWordSymbols);
    }

}
