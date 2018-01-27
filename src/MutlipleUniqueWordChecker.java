import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.*;

public abstract class MutlipleUniqueWordChecker {
    protected Map<String, Integer> uniqueWords; // map ow words in the source

    protected Set<SerialCharacterReader> readers;
    protected AllowedWordSymbols allowedWordSymbols;
    protected Set<Future<Boolean>> resourceUniquenessResults = new HashSet<>(); //result-resource


    public MutlipleUniqueWordChecker(Map<String, Integer> uniqueWords, Set<SerialCharacterReader> readers, AllowedWordSymbols allowedWordSymbols) {
        this.uniqueWords = uniqueWords;
        this.readers = readers;
        this.allowedWordSymbols = allowedWordSymbols;
    }

    public boolean checkUniqueness() throws ForeignCharacterException {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (SerialCharacterReader r : readers) {
            UniqueWordChecker checker = new UniqueWordChecker(uniqueWords, r, allowedWordSymbols);
            resourceUniquenessResults.add(executor.submit(checker));
        }

        try {
            boolean result = checkActivelyForUniqueness(executor, resourceUniquenessResults);
            if (!hasForeignCharactersException()) {
                return result;
            }
        } catch (ExecutionException e) {
            if (e.getCause().getClass() == ForeignCharacterException.class) {
                throw (ForeignCharacterException) e.getCause();
            }
            return false;
        }
        return false;
    }

    private static boolean checkActivelyForUniqueness(ExecutorService executor, Set<Future<Boolean>> results) throws ExecutionException {
        Stack<Future<Boolean>> activeTasks = new Stack<>();
        for (Future<Boolean> result : results) {
            activeTasks.push(result);
        }
        while (!activeTasks.empty() && !executor.isTerminated()) {
            Future<Boolean> result = activeTasks.peek();
            if (result.isDone() && !result.isCancelled()) { //result.done() - Completion may be due to normal termination or an exception
                try {
                    Boolean unique = result.get(); // can throw an execution exception where we can find foreignCharactersException
                    if (!unique) {
                        //we found one task where a repetition occurred - no need to execute other tasks
                        executor.shutdownNow();
                        return false;
                    } else {
                        //we don't need to check this task again - it is has no repetitions
                        activeTasks.pop();
                    }
                } catch (ExecutionException execException) {
                    // when we try to "get.result" some thread can produce execution exception - foreign
                    executor.shutdownNow();
                    System.out.println("Error of execution inside some thread");
                    //execException.printStackTrace();
                    throw execException;
                } catch (InterruptedException interException) {
                    // when we try to "get.result" it can wait but be interrupted then - in that case we just move on to check other results for uniqueness
                    //executor.shutdownNow();
                    System.out.println("Error intteruption inside some thread");
                }
            }

        }
        return true;
    }

    public void printWordsToFile(File inputFile) {
        try (FileWriter writer = new FileWriter(inputFile)) {
            for (Map.Entry<String, Integer> word : uniqueWords.entrySet()) {
                writer.write(word.getKey() + " " + word.getValue() + " \n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasForeignCharactersException() throws ForeignCharacterException {
        for (Future<Boolean> result : resourceUniquenessResults) {
            try {
                result.get();
            } catch (ExecutionException e) {
                if (e.getCause().getClass() == ForeignCharacterException.class) {
                    throw (ForeignCharacterException) e.getCause();
                }
            } catch (InterruptedException e) {
                //do nothing
            }
        }
        return false;
    }

}
