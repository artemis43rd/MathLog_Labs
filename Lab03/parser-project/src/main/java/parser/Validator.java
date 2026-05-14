package parser;

import java.util.*;

public class Validator {
    Dictionary dict;
    int curr_index;
    ArrayList<String> tokens;
    String currWord;

    public Validator() {
        dict = new Dictionary();
    }

    void NextWord() throws RuntimeException {
        curr_index++;
        if (curr_index<tokens.size())
            currWord=tokens.get(curr_index);
        else throw new RuntimeException("9"); //Next
    };

    void SentenceP() throws RuntimeException {
        curr_index=0;
        currWord=tokens.get(0);
        SubjectGroup();
        vGroup();
        if (dict.findWord(currWord) == "adjAction") {
            AdjAction();
        }

        if(dict.findWord(currWord) == "prepositions") {
            Preposition();
            Place();
        }
        if (currWord.equals(".")) return;

        else {
            Link();
            SubjectGroup();
            vGroup();
            if (dict.findWord(currWord) == "adjAction") {
                AdjAction();
            }
        }
        if (currWord.equals(".")) return;

        else {
            Preposition();
            Place();
        }
        if (currWord.equals(".")) return; else throw new RuntimeException("0");
    }

    void SentenceQ() throws RuntimeException {
        curr_index=0;
        currWord=tokens.get(0);
        vGroup();
        if (dict.findWord(currWord) == "adjAction") {
            AdjAction();
        }
        SubjectGroup();
        if (currWord.equals("?")) return;

        else {
            Preposition();
            Place();
        }
        if (currWord.equals("?")) return; else throw new RuntimeException("0");
    }

    void SentenceN() throws RuntimeException {
        curr_index=0;
        currWord=tokens.get(0);
        SubjectGroup();
        vGroup();
        if (dict.findWord(currWord) == "adjAction") {
            AdjAction();
        }
        NextWord();
        if (currWord.equals(".")) return;

        else {
            Preposition();
            Place();
        }
        if (currWord.equals(".")) return; else throw new RuntimeException("0"); //знаки
    }
    

    void SubjectGroup() throws RuntimeException {
        if (!Pronoun() && !Adjective()) throw new RuntimeException("1"); //подлежащее
     
    };

    boolean Pronoun() {
        if (dict.pronouns.contains(currWord)) NextWord();
        else return false;

        return true;
    }

    boolean Adjective() {
        if (dict.adjectives.contains(currWord)) {
            NextWord();
            if (dict.nounObjects.contains(currWord)) NextWord();
            else return false;
        }
        else return false;

        return true;
    }

    void Link() throws RuntimeException {
        if (dict.links.contains(currWord)) NextWord();
        else throw new RuntimeException("3"); //союз
    }

    void vGroup() throws RuntimeException {
        if (!vAction() && !vMove() && !vState()) throw new RuntimeException("2"); //глаголы
    }

    boolean vState() {
        if (dict.vStates.contains(currWord)){
            NextWord();
        }
        else return false;

        return true;
    }

    boolean vMove() {
        if (dict.vMoves.contains(currWord)){
            NextWord();
        }
        else return false;
        
        return true;
    }

    boolean vAction() {
        if (dict.vActions.contains(currWord)) {
            NextWord();
        }
        else return false;

        return true;
    }

    void AdjAction() throws RuntimeException {
        if (dict.adjAction.contains(currWord)) NextWord();
        else throw new RuntimeException("4"); //прилагательное
    }

    void Preposition() throws RuntimeException {
        if (dict.prepositions.contains(currWord)) NextWord();
        else throw new RuntimeException("5"); //предлоги
    }

    void Place() throws RuntimeException {
        if (dict.places.contains(Character.toUpperCase(currWord.charAt(0)) + currWord.substring(1))) NextWord();
        else throw new RuntimeException("6"); //места
    }



    public void CheckStr(String str) {
        if (str.length() == 0) {
            System.out.println("Пустая строка не принадлежит грамматике!");
            return;
        }
        
        ArrayList<String> tokens = preprocessStr(str);
        if (tokens.size() == 0) {
            System.out.println("Ошибка обработки введённой строки. Возможно введены недопустимые символы");
            return;
        }
        setTokens(tokens);

        String lastWord = tokens.get(tokens.size() - 1);
        try {
            if (lastWord.equals("?")) {
                SentenceQ();
                System.out.println("Предложение принадлежит грамматике! Это вопросительное предложение в настоящем времени!");
            }
            
            else if (lastWord.equals(".") && tokens.contains("nicht")) {
                SentenceN();
                System.out.println("Предложение принадлежит грамматике! Это отрицательное предложение в настоящем времени!");
            }
            
            else if (lastWord.equals(".")) {
                SentenceP();
                System.out.println("Предложение принадлежит грамматике! Это утвердительное предложение в настоящем времени!");
            }
            
            else System.out.println("Предложение должно заканчиваться точкой или вопросительным знаком! Попробуйте снова.");
            return;
        }
        catch (RuntimeException e) {
            String message = e.getMessage();
            switch (message) {
                case "0":
                    System.out.println("Ошибка знака препинания.");
                    break;
                case "1":
                    System.out.println("Подлежащее содержит ошибку.");
                    break;
                case "2":
                    System.out.println("Глаголы содержат ошибку.");
                    break;
                case "3":
                    System.out.println("Союз между частями предложения содержит ошибку.");
                    break;
                case "4":
                    System.out.println("Глагольное прилагательное содержит ошибку.");
                    break;
                case "5":
                    System.out.println("Предлог содержит ошибку.");
                    break;
                case "6":
                    System.out.println("Место действия содержит ошибку.");
                    break;
                case "9":
                    System.out.println("Ошибка обработки следующего слова.");
                    break;
                default:
                    System.out.println("Неизвестная ошибка!");
                    break;
            }
            return;
        }
    }

    void setTokens(ArrayList<String> tokens) { this.tokens = tokens; }

    public static ArrayList<String> preprocessStr(String str) {
        str = str.trim();
        String lastChar = str.substring(str.length() - 1);
        str = str.substring(0, str.length() - 1);
        str = str.replaceAll("[^a-zA-ZäöüÄÖÜß -]", "");

        ArrayList<String> result = new ArrayList<>();
        String[] words = str.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase();
            result.add(word);
        }

        result.add(lastChar);
        return result;
    }
}