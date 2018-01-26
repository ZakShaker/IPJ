import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MultithreadUniqueWordChecker {
    //сюда пошлём названия файлов


    //returns repeated word or null if the file is unique
    public static String checkUniqueness(String fileName) throws IOException, ExecutionException, InterruptedException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            ConcurrentHashMap<String, Integer> uniqueWords = new ConcurrentHashMap<>();

            ExecutorService executor = Executors.newCachedThreadPool();
            ArrayList<Future<Boolean>> resourceUniquenessResults = new ArrayList<>();
            String lineOfText;

            while ((lineOfText = reader.readLine()) != null) {
                UniqueWordChecker checker = new UniqueWordChecker(uniqueWords, lineOfText);
                resourceUniquenessResults.add(executor.submit(checker));
            }

            //теперь смотрим, готов ли кто-нибудь - если да, то выводим повторение
            try {
                if (!checkActivelyForUniqueness(executor, resourceUniquenessResults)) {
                    for (Map.Entry<String, Integer> word : uniqueWords.entrySet()) {
                        if (word.getValue() > 1) {
                            printWordsToFile(uniqueWords.entrySet());
                            return word.getKey();
                        }
                    }
                }
            } catch (ExecutionException execException) {
                System.out.println("Execution Error in some of resources");
                throw execException;
            } catch (InterruptedException interException) {
                System.out.println("Interrupted thread Exception in some of resources");
                throw interException;
            }
            printWordsToFile(uniqueWords.entrySet());
        } catch (IOException ex) {
            System.out.println("Cannot open file named " + fileName);
        }
        return null;
    }

    private static boolean checkActivelyForUniqueness(ExecutorService executor, List<Future<Boolean>> results) throws ExecutionException, InterruptedException {
        Stack<Future<Boolean>> activeTasks = new Stack<>();
        for (Future<Boolean> result : results) {
            activeTasks.push(result);
        }
        while (!activeTasks.empty() && !executor.isTerminated()) {
            Future<Boolean> result = activeTasks.peek();
            if (result.isDone() && !result.isCancelled()) {
                try {
                    Boolean unique = result.get();
                    if (!unique) {
                        //we found one task where a repetition occurred - no need to execute other tasks
                        executor.shutdownNow();
                        return false;
                    } else {
                        //we don't need to check this task again - it is has no repetitions
                        activeTasks.pop();
                    }
                } catch (ExecutionException execException) {
                    executor.shutdownNow();
                    System.out.println("Error of execution inside thread");
                    execException.printStackTrace();
                    throw execException;
                } catch (InterruptedException interException) {
                    executor.shutdownNow();
                    System.out.println("Error intteruption inside thread");
                    throw interException;
                }
            }

        }
        return true;
    }

    private static void printWordsToFile(Set<Map.Entry<String, Integer>> words) {
        try (FileWriter writer = new FileWriter("output.txt")) {
            for (Map.Entry<String, Integer> word : words) {
                writer.write(word.getKey() + " " + word.getValue() + " \n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
