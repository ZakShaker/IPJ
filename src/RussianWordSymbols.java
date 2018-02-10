import java.util.ArrayList;

public class RussianWordSymbols extends AllowedWordSymbols{

    RussianWordSymbols(){
        super(c -> c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я' || c >= '0' && c <= '9' || c == '-',
                c -> String.valueOf(c).matches("[!\"#$%&'()*+,.\\/:;<=>?@/[/]^_ |§±~`{}\n]"),
                word -> word.isEmpty() || word.matches("[ \n\t]+")
        );
    }
}
