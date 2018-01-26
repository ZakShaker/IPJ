import java.util.ArrayList;

/*
Необходимо разработать программу, которая получает на вход список ресурсов, содержащих текст,
 и проверяет уникальность каждого слова. Каждый ресурс должен быть обработан в отдельном потоке,
  текст не должен содержать инностранных символов, только кириллица, знаки препинания и цифры. В
   случае нахождения дубликата, программа должна прекращать выполнение с соответсвующим сообщением.
    Все ошибки должны быть корректно обработаны.
 */
public class Main {

    public static void main(String[] args) {

        ArrayList<String> inputFileNames = new ArrayList<>();
        for (int i = 0; i < 800; i++) {
            i++;
            String fileName = "text_" + i + ".txt";
            inputFileNames.add(fileName);
        }

        try {
            String repeatedWord = MultithreadUniqueWordChecker.checkUniqueness("input.txt");
            if (repeatedWord == null) {
                System.out.println("The text is unique");
            } else {
                System.out.println("The text is not unique. Repeated word is \"" + repeatedWord + "\"");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            System.out.println("Done!");
        }
    }
}