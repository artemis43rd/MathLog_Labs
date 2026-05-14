import java.util.Random;
import java.util.Scanner;

public class lab02 {

    private enum State {
        START,
        USERNAME,
        AT_SYMBOL,
        DOMAIN,
        DOT,
        TOP_LEVEL_DOMAIN,
        INVALID
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;
        printMenu();

        do {
            System.out.print("\nВыберите вариант: ");
            choice = scanner.next().charAt(0);

            switch (choice) {
                case '1':
                    printSlesh();
                    System.out.print("Введите email для проверки: ");
                    scanner.nextLine();
                    String inputText = scanner.nextLine();
                    validateEmail(inputText);
                    printSlesh();
                    break;
                case '2':
                    printSlesh();
                    System.out.println("Сгенерированный email: " + generateValidEmail());
                    printSlesh();
                    break;
                case '0':
                    printSnow();
                    System.out.println("Работа программы завершена.");
                    printSnow();
                    break;
                case 'm':
                    printMenu();
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
            }
        } while (choice != '0');

        scanner.close();
    }

    public static void printSnow() {
        System.out.println("********************************************");
    }

    public static void printSlesh() {
        System.out.println("////////////////////////////////////////////");
    }

    public static void printMenu() {
        printSnow();
        System.out.println("\t\t   MENU");
        printSnow();
        System.out.println("[M] - Напечатать MENU");
        System.out.println("[1] - Ввести email на проверку");
        System.out.println("[2] - Сгенерировать случайный email");
        System.out.println("[0] - Завершить работу программы");
        printSnow();
    }

    public static void validateEmail(String text) {
        if (!isValidString(text)) {
            System.out.println("Строка не соответствует - Строка содержит символы не из алфавита");
            return;
        }
        
        State currentState = State.START;
        StringBuilder lastDomainSegment = new StringBuilder();

        for (char ch : text.toCharArray()) {
            switch (currentState) {
                case START:
                    if (isValidUsernameCharacter(ch)) {
                        currentState = State.USERNAME;
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case USERNAME:
                    if (isValidUsernameCharacter(ch)) {
                        currentState = State.USERNAME;
                    } else if (ch == '@') {
                        currentState = State.AT_SYMBOL;
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case AT_SYMBOL:
                    if (isValidDomainCharacter(ch)) {
                        currentState = State.DOMAIN;
                        lastDomainSegment.append(ch);
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case DOMAIN:
                    if (isValidDomainCharacter(ch)) {
                        currentState = State.DOMAIN;
                        lastDomainSegment.append(ch);
                    } else if (ch == '.') {
                        currentState = State.DOT;
                        lastDomainSegment.setLength(0); // Очищаем lastDomainSegment
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case DOT:
                    if (isValidTopLevelDomainCharacter(ch)) {
                        currentState = State.TOP_LEVEL_DOMAIN;
                        lastDomainSegment.append(ch);
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case TOP_LEVEL_DOMAIN:
                    if (isValidTopLevelDomainCharacter(ch)) {
                        currentState = State.TOP_LEVEL_DOMAIN;
                        lastDomainSegment.append(ch);
                    } else {
                        currentState = State.INVALID;
                    }
                    break;
                case INVALID:
                    break;
            }
        }

        // Проверяем последний сегмент на соответствие TLD
        if (currentState == State.TOP_LEVEL_DOMAIN && isValidTopLevelDomain(lastDomainSegment.toString())) {
            System.out.println("Строка соответствует");
        } else {
            System.out.println("Строка не соответствует");
        }
    }

    private static boolean isValidString(String text) {
        return text.matches("[a-zA-Z0-9.@+-]+");
    }

    private static boolean isValidUsernameCharacter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                || ch == '.' || ch == '_' || ch == '%' || ch == '+' || ch == '-';
    }

    private static boolean isValidDomainCharacter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '-';
    }

    private static boolean isValidTopLevelDomainCharacter(char ch) {
        return (ch >= 'a' && ch <= 'z');
    }

    private static boolean isValidTopLevelDomain(String tld) {
        return tld.length() >= 2 && tld.length() <= 6 && tld.chars().allMatch(Character::isLetter);
    }

    private static String generateValidEmail() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        int length = random.nextInt(10) + 5;
        for (int i = 0; i < length; i++) {
            char character;
            if (i == 0) {
                character = (char) ('a' + random.nextInt(26)); // Первый символ - буква
            } else {
                character = getRandomUsernameCharacter(random); // Остальные символы - буква, цифра или допустимый спец. символ
            }
            stringBuilder.append(character);
        }
        stringBuilder.append("@");

        length = random.nextInt(7) + 3; // Длина домена от 3 до 9 символов
        for (int i = 0; i < length; i++) {
            char character = (char) ('a' + random.nextInt(26)); // Буква
            stringBuilder.append(character);
        }
        stringBuilder.append('.');

        length = random.nextInt(5) + 2; // Длина TLD от 2 до 6 символов
        for (int i = 0; i < length; i++) {
            char character = (char) ('a' + random.nextInt(26)); // Буква
            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }

    private static char getRandomUsernameCharacter(Random random) {
        String validCharacters = "abcdefghijklmnopqrstuvwxyz0123456789._%+-";
        return validCharacters.charAt(random.nextInt(validCharacters.length()));
    }
}