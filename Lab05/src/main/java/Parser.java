import java.util.*;

/*
S'→S$
S→BA
A→+BA|e
B→DC
C→*DC|e
D→(S)|a
* */
public class Parser {
    private final char END_OF_INPUT = '\0';
    private final char START_NONTERMINAL = 'Z';
    private final char[] TERMINALS = {
            'a', '+', '*', '(', ')', '$', END_OF_INPUT
    };
    private final char[] NONTERMINALS = {
            START_NONTERMINAL, 'S', 'A', 'B', 'C', 'D'
    };
    private final Map<Character, String> SEMANTICS = new TreeMap<>();
    final char[][][] lookupTable = new char[NONTERMINALS.length][TERMINALS.length][];
    final Map<Character, List<Character>> alternatives = new TreeMap<>();
    private final Random random = new Random(42);

    private void addLookupValue(char nonTerminal, char inputTerminal, char[] rightHandSide) {
        // reverse rightHandSide
        for (int i = 0; i < rightHandSide.length / 2; i++) {
            char temp = rightHandSide[i];
            rightHandSide[i] = rightHandSide[rightHandSide.length - i - 1];
            rightHandSide[rightHandSide.length - i - 1] = temp;
        }
        int iNonTerminal = Arrays.binarySearch(NONTERMINALS, nonTerminal);
        int iTerminal = Arrays.binarySearch(TERMINALS, inputTerminal);
        lookupTable[iNonTerminal][iTerminal] = rightHandSide;
        alternatives.computeIfAbsent(nonTerminal, unused -> new ArrayList<>()).add(inputTerminal);
    }

    private boolean isTerminal(char ch) {
        return Arrays.binarySearch(TERMINALS, ch) >= 0;
    }

    public Parser() {
        // enables Arrays.binarySearch
        Arrays.sort(TERMINALS);
        Arrays.sort(NONTERMINALS);

        addLookupValue('Z', '(', new char[]{'4', 'S', '$'});
        addLookupValue('Z', 'a', new char[]{'4', 'S', '$'});

        addLookupValue('S', '(', new char[]{'B', 'A'});
        addLookupValue('S', 'a', new char[]{'B', 'A'});

        addLookupValue('A', '$', new char[]{'3'});
        addLookupValue('A', ')', new char[]{});
        addLookupValue('A', '+', new char[]{'+', 'B', '1' ,'A'});

        addLookupValue('B', '(', new char[]{'D', 'C'});
        addLookupValue('B', 'a', new char[]{'D', 'C'});


        addLookupValue('C', '$', new char[]{});
        addLookupValue('C', '+', new char[]{});
        addLookupValue('C', ')', new char[]{});
        addLookupValue('C', '*', new char[]{'*', 'D', '2', 'C'});

        addLookupValue('D', '(', new char[] {'(', 'S', ')'});
        addLookupValue('D', 'a', new char[] {'0', 'a'});

        SEMANTICS.put('0', "pull a");
        SEMANTICS.put('1', "add");
        SEMANTICS.put('2', "mul");
        SEMANTICS.put('3', "close");
        SEMANTICS.put('4', "open");
    }

    private char[] lookup(char nonTerminal, char inputTerminal) {
        int iNonTerminal = Arrays.binarySearch(NONTERMINALS, nonTerminal);
        int iTerminal = Arrays.binarySearch(TERMINALS, inputTerminal);
        return lookupTable[iNonTerminal][iTerminal];
    }

    public enum Verdict {
        UnknownSymbol,
        Accepted,
        Rejected
    }

    public record ParseResult(Verdict verdict, List<String> meaning) {}

    public ParseResult parse(String input) {
        Deque<Character> stack = new ArrayDeque<>();
        stack.push(END_OF_INPUT);
        stack.push(START_NONTERMINAL);
        Deque<Character> inputS = new ArrayDeque<>();
        List<String> meaning = new ArrayList<>();
        
        // Проверка входной строки
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!isTerminal(c) || c == END_OF_INPUT) {
                return new ParseResult(Verdict.UnknownSymbol, null);
            }
            inputS.add(c);
        }
        inputS.add(END_OF_INPUT);
        
        // Пока стек и входной поток не пусты
        while (!stack.isEmpty() && !inputS.isEmpty()) {
            boolean flag = false;

            if ((stack.getFirst() != END_OF_INPUT) && (isTerminal(stack.getFirst())))
                System.out.println("Происходит анализ символа: " + stack.getFirst());
            
            if (inputS.getFirst() == stack.getFirst()) {
                stack.pop();
                inputS.pop();
                continue;
            }
            
            if (isTerminal(stack.getFirst())) {
                if (stack.getFirst() != END_OF_INPUT) System.out.println("Ошибка: терминал " + stack.getFirst() + " не совпадает с входом.");
                return new ParseResult(Verdict.Rejected, null);
            }
            
            if (isSemantic(stack.getFirst())) {
                String semanticAction = SEMANTICS.get(stack.pop());
                meaning.add(semanticAction);
                flag = true;
                if (flag == true) {
                    System.out.println("( " + meaning.get(meaning.size() - 1) + " )");
                    flag = false;
                }
                continue;
            }
            
            var rightHandSide = lookup(stack.pop(), inputS.getFirst());
            if (rightHandSide == null) {
                if (inputS.getFirst() != END_OF_INPUT) System.out.println("Ошибка: нет продукции для " + inputS.getFirst());
                return new ParseResult(Verdict.Rejected, null);
            }
            
            for (char c : rightHandSide) {
                stack.push(c);
            }
        }
        
        if (stack.isEmpty() && inputS.isEmpty()) {
            return new ParseResult(Verdict.Accepted, meaning);
        } else {
            return new ParseResult(Verdict.Rejected, null);
        }
    }    
    
    private boolean isSemantic(char c) {
        return SEMANTICS.containsKey(c);
    }

    public String generate() {
        Deque<Character> stack = new ArrayDeque<>();
        stack.push(START_NONTERMINAL);
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            if (isTerminal(stack.getFirst())) {
                sb.append(stack.pop());
                continue;
            }
            if (isSemantic(stack.getFirst())) {
                stack.pop();
                continue;
            }
            char nonTerminal = stack.pop();
            var  terminals = alternatives.get(nonTerminal);
            for (char c : lookup(nonTerminal, terminals.get(random.nextInt(terminals.size())))) {
                stack.push(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in, "UTF-8");
        char choice;
    
        printMenu();
    
        do {
            System.out.print("\nВыберите вариант: ");
            choice = scanner.next().charAt(0);
            scanner.nextLine(); // Очистка буфера
    
            switch (choice) {
                case '1':
                    printSlesh();
                    System.out.print("Введите строку для проверки: ");
                    String inputText = scanner.nextLine();
                    if ( inputText.length() == 0 ) {
                        System.out.println("Пустая строка не удовлетворяет грамматике!");
                        printSlesh();
                        break;
                    }
                    ParseResult result = parser.parse(inputText);
                    switch (result.verdict) {
                        case UnknownSymbol -> System.out.println("Цепочка содержит символы не из алфавита.");
                        case Accepted -> {
                            System.out.print("Цепочка удовлетворяет грамматике.");
                            //result.meaning.forEach(System.out::print);
                            System.out.println();
                        }
                        case Rejected -> System.out.println("Цепочка НЕ удовлетворяет грамматике.");
                    }
                    printSlesh();
                    break;
    
                case '2':
                    printSlesh();
                    System.out.println("Случайная подходящая цепочка: " + parser.generate());
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
        System.out.println("[1] - Ввести строку для проверки");
        System.out.println("[2] - Сгенерировать подходящую цепочку");
        System.out.println("[0] - Завершить работу программы");
        printSnow();
    }
    
}
