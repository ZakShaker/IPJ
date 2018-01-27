public class RussianWordSymbols implements AllowedWordSymbols {

    //The next words are considered as entire words
    //'диван-кровать'
    //'Турбо500'
    //'Яшка'
    //'999'
    @Override
    public boolean isPartOfWord(char c) {
        return c >= 'а' && c <= 'я' || c >= 'А' && c <= 'Я' || c >= '0' && c <= '9' || c == '-';
    }

    @Override
    public boolean isPunctuation(char c) {
        return c == '!' ||
                c == '"' ||
                c == '#' ||
                c == '$' ||
                c == '%' ||
                c == '&' ||
                c == '\'' ||
                c == '(' ||
                c == ')' ||
                c == '*' ||
                c == '+' ||
                c == ',' ||
                c == '.' ||
                c == '\\' ||
                c == '/' ||
                c == ':' ||
                c == ';' ||
                c == '<' ||
                c == '=' ||
                c == '>' ||
                c == '?' ||
                c == '@' ||
                c == '[' ||
                c == ']' ||
                c == '^' ||
                c == '_' ||
                c == ' ' ||
                c == '|' ||
                c == '§' ||
                c == '±' ||
                c == '~' ||
                c == '`' ||
                c == '{' ||
                c == '}' ||
                c == '\n';
    }

    @Override
    public boolean wordIsEmpty(String word) {
        return word.isEmpty() || word.matches("[ \n\t]+");
    }
}
