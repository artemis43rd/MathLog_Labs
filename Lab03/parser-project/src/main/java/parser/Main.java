package parser;

import java.util.*;

public class Main {
    static Generator generator;
    static Validator validator;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        validator = new Validator();
        generator = new Generator();
        char choice;

        printMenu();

        do {
            System.out.print("\nВыберите вариант: ");
            choice = scanner.next().charAt(0);

            switch (choice) {
                case '1':
                    printSlesh();
                    System.out.print("Введите предложение для проверки: ");
                    scanner.nextLine();  // Очистка буфера

                    String inputText = scanner.nextLine();
                    validator.CheckStr(inputText);
                    printSlesh();
                    break;

                case '2':
                    printSlesh();
                    System.out.println("(1) - Генерация утвердительного предложения");
                    System.out.println("(2) - Генерация отрицательного предложения");
                    System.out.println("(3) - Генерации вопросительного предложения");
                    scanner.nextLine();

                    boolean flag=true;
                    while (flag) {
                        System.out.print("Выберите какое предложение сгенерировать: ");
                        char choice2;
                        choice2 = scanner.next().charAt(0);

                        switch (choice2) {
                            case '1':
                                System.out.println("Сгенерированная цепочка: " + generator.GenerateP());
                                flag=false;
                                break;
                            case '2':
                                System.out.println("Сгенерированная цепочка: " + generator.GenerateN());
                                flag=false;
                                break;
                            case '3':
                                System.out.println("Сгенерированная цепочка: " + generator.GenerateQ());
                                flag=false;
                                break;
                            
                            default:
                                System.out.println("Неверный выбор! Пожалуйста, выберите снова.\n");
                        }
                    }
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
        System.out.println("[1] - Ввести предложение для проверки");
        System.out.println("[2] - Сгенерировать предложение");
        System.out.println("[0] - Завершить работу программы");
        printSnow();
    }
}
