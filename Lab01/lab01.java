import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class lab01 {

    public static void main(String[] args) {
        String fileName = "input.txt";
        List<Token> tokenList = new ArrayList<>();
        Map<String, Integer> variableMap = new HashMap<>();
        int[] keywordCount = {1};
        int[] varCount = {1};

        try (PushbackReader reader = new PushbackReader(new FileReader(fileName))) {
            StringBuilder currentToken = new StringBuilder();
            boolean inComment = false;

            while (true) {
                int currentChar = reader.read();

                if (currentChar == -1) {
                    break;
                }

                char currentSymbol = (char) currentChar;

                if (currentSymbol == '/' && reader.ready()) {
                    char nextChar = (char) reader.read();
                    if (nextChar == '/') {
                        inComment = true;
                        continue;
                    } else {
                        reader.unread(nextChar);
                    }
                }

                if (inComment) {
                    if (currentSymbol == '\n') {
                        inComment = false;
                    }
                    continue;
                }

                if (Character.isWhitespace(currentSymbol) || isSeparator(currentSymbol)) {
                    if (currentToken.length() > 0) {
                        if (currentToken.length() <= 16) {
                            addToken(currentToken.toString(), tokenList, variableMap, keywordCount, varCount);
                            currentToken = new StringBuilder();
                        } else {
                            System.out.printf("\nОшибка! Лексема " + currentToken.toString() + " имеет длину более 16 символов!");
                            currentToken = new StringBuilder();
                            continue;
                        }
                    }

                    if (isSeparator(currentSymbol)) {
                        addSeparatorToken(currentSymbol, tokenList);
                    }

                } else {
                    currentToken.append(currentSymbol);
                }

                if (currentSymbol == ':' && reader.ready()) {   //:
                    char nextChar = (char) reader.read();   
                    if (nextChar == '=') { //:=
                        currentToken.append(nextChar);
                        addToken(currentToken.toString(), tokenList, variableMap, keywordCount, varCount);
                        currentToken = new StringBuilder();
                    } else if (nextChar == ':') { //::
                        char nextNextChar = (char) reader.read();
                        if (nextNextChar == '=') { //::=
                            System.out.print("\nОшибка! Неверная лексема: =");
                            //System.out.printcl("\nОшибка! Неверная лексема: " + currentToken.toString());
                        } else {
                            System.out.print("\nОшибка! Неверная лексема: " + currentToken.toString() + nextChar);
                            reader.unread(nextNextChar);
                        }
                    } else if (nextChar == '=') {  //:=
                        char nextNextChar = (char) reader.read();
                        if (nextNextChar == '=') {  //:==
                            System.out.println("\nОшибка! Неверная лексема: " + currentToken.toString() + nextChar + nextNextChar);
                        } else {
                            addToken(":=", tokenList, variableMap, keywordCount, varCount);
                            reader.unread(nextNextChar);
                        }
                    } else {
                        reader.unread(nextChar);
                    }
                }
            }

            if (currentToken.length() > 0) {
                if (currentToken.length() <= 16) {
                    addToken(currentToken.toString(), tokenList, variableMap, keywordCount, varCount);
                }
            }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        System.out.printf("\n\n");
        System.out.printf("%-40s%-40s%-40s\n\n", "Лексема", "Тип", "Значение");
        for (Token token : tokenList) {
            System.out.printf("%-40s%-40s%-40s\n", token.value, token.getType(), token.getValue());
        }
        System.out.printf("\n\n");
    }

    private static boolean isSeparator(char symbol) {
        return symbol == ';' || symbol == '(' || symbol == ')' || symbol == '=';
    }

    private static void addSeparatorToken(char symbol, List<Token> tokenList) {
        switch (symbol) {
            case ';':
                tokenList.add(new Token(";", "Символ", ""));
                break;
            case '(':
                tokenList.add(new Token("(", "Символ", "S0"));
                break;
            case ')':
                tokenList.add(new Token(")", "Символ", "S1"));
                break;
            default:
                break;
        }
    }

    private static void addToken(String value, List<Token> tokenList, Map<String, Integer> variableMap, int[] keywordCount, int[] varCount) {
        if (!value.isEmpty()) {
            switch (value) {
                case ":=":
                    tokenList.add(new Token(value, "Знак присваивания", ""));
                    break;
                case "T":
                    tokenList.add(new Token(value, "Булевая константа", "True"));
                    break;
                case "F":
                    tokenList.add(new Token(value, "Булевая константа", "False"));
                    break;
                case "begin":
                case "end":
                    tokenList.add(new Token(value, "Ключевое слово", "X" + keywordCount[0]++));
                    break;
                case "<":
                case ">":
                case "<=":
                case ">=":
                case "==":
                    tokenList.add(new Token(value, "Знак сравнения", ""));
                    break;
                case "or":
                case "xor":
                case "and":
                case "not":
                case "nand":
                case "nor":
                    tokenList.add(new Token(value, "Логическая операция", ""));
                    break;
                default:
                    if (isNumeric(value)) {
                        tokenList.add(new Token(value, "Вещественная константа", value));
                    } else if (value.matches("[a-zA-Z]+") && keywordCount[0] >= 1) {
                        if (!variableMap.containsKey(value)) {
                            variableMap.put(value, varCount[0]);
                            tokenList.add(new Token(value, "Переменная", value + ":" + varCount[0]++));
                        }
                    } else {
                        System.out.println("\n\nОшибка! Неизвестная лексема: " + value);
                    }
                    break;
            }
        }
    }    

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static class Token {
        String value;
        String type;
        String tokenValue;

        Token(String value) {
            this.value = value;
            this.type = "Неизвестный";
            this.tokenValue = "";
        }

        Token(String value, String type, String tokenValue) {
            this.value = value;
            this.type = type;
            this.tokenValue = tokenValue;
        }

        String getType() {
            return type;
        }

        String getValue() {
            return tokenValue;
        }
    }
}