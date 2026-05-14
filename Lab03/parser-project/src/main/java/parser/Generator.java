package parser;

import java.util.*;

public class Generator {
    class Node {
        String name;
        Vector<Vector<Node>> matrix_roles;
        ArrayList<String> tokens;

        public void setTokens(ArrayList<String> tokens) {
            this.tokens = tokens;
        }

        public void setMatrix_roles(Vector<Vector<Node>> matrix_roles) {
            this.matrix_roles = matrix_roles;
        }

        String getRandomToken() {
            return tokens.get(new Random().nextInt(tokens.size()));
        }

        Vector<Node> getRandomRules() {
            return matrix_roles.get(new Random().nextInt(matrix_roles.size()));
        }

        Node(String name) {
            this.name = name;
            matrix_roles = new Vector<>();
            tokens = new ArrayList<>();
        }
    }

    Node SentP = new Node("SentP");
    Node SentQ = new Node("SentQ");
    Node SentN = new Node("SentN");
    Node SubjectGroup = new Node("SubjectGroup");
    Node vActGroup = new Node("vActGroup");
    Node vStateGroup = new Node("vStateGroup");
    Node vMoveGroup = new Node("vMoveGroup");
    Node Adjective = new Node("Adjective");
    Node NounObject = new Node("NounObject");
    Node Pronoun = new Node("Pronoun");
    Node Preposition = new Node("Preposition");
    Node Place = new Node("Place");
    Node AdjAction = new Node("AdjAction");
    Node Link = new Node("Link");
    Node Not = new Node("Not");
    Node vGroup = new Node("vGroup");

    Generator() {
        Dictionary dict = new Dictionary();

        // Определение структуры утвердительного предложения
        Vector<Vector<Node>> helpVec = new Vector<Vector<Node>>() {{
            add(new Vector<Node>() {{
                add(SubjectGroup);
                add(vGroup); //простое
            }});
            add(new Vector<Node>() {{
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction); //простое с прилагательным
            }});
            add(new Vector<Node>() {{
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction);

                add(Link);
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction); //сложное
            }});
            add(new Vector<Node>() {{
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction);
                add(Link);
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction);
                add(Preposition); //сложное с местом
                add(Place);
            }});
            add(new Vector<Node>() {{
                add(SubjectGroup);
                add(vGroup);
                add(AdjAction);
                add(Preposition); //простое с местом
                add(Place);
            }});
        }};
        SentP.setMatrix_roles(new Vector<>(helpVec));
        helpVec.clear();


        // Определение структуры вопросительного предложения
        helpVec.add(new Vector<Node>() {{
            add(vGroup);
            add(SubjectGroup);
        }});
        helpVec.add(new Vector<Node>() {{
            add(vGroup);
            add(AdjAction);
            add(SubjectGroup);
        }});
        helpVec.add(new Vector<Node>() {{
            add(vGroup);
            add(AdjAction);
            add(SubjectGroup);
            add(Preposition); //с местом
            add(Place);
        }});
        SentQ.setMatrix_roles(new Vector<>(helpVec));
        helpVec.clear();


        // Определение структуры отрицательного предложения
        helpVec.add(new Vector<Node>() {{
            add(SubjectGroup);
            add(vGroup);
            add(Not);
        }});
        helpVec.add(new Vector<Node>() {{
            add(SubjectGroup);
            add(vGroup);
            add(AdjAction);
            add(Not);
        }});
        helpVec.add(new Vector<Node>() {{
            add(SubjectGroup);
            add(vGroup);
            add(AdjAction);
            add(Not);
            add(Preposition); //с местом
            add(Place);
        }});
        SentN.setMatrix_roles(new Vector<>(helpVec));
        helpVec.clear();


        // Определение группы подлежащих
        helpVec.add(new Vector<Node>() {{
            add(Adjective);
            add(NounObject);
        }});
        helpVec.add(new Vector<Node>() {{
            add(Pronoun);
        }});
        SubjectGroup.setMatrix_roles(new Vector<>(helpVec));
        helpVec.clear();


        // Определение группы глаголов
        helpVec.add(new Vector<Node>() {{
            add(vActGroup);
        }});
        helpVec.add(new Vector<Node>() {{
            add(vStateGroup);
        }});
        helpVec.add(new Vector<Node>() {{
            add(vMoveGroup);
        }});
        vGroup.setMatrix_roles(new Vector<>(helpVec));
        helpVec.clear();

        // Определение других групп
        vStateGroup.setTokens(dict.vStates);
        vMoveGroup.setTokens(dict.vMoves);
        vActGroup.setTokens(dict.vActions);
        Adjective.setTokens(dict.adjectives);
        NounObject.setTokens(dict.nounObjects);
        Pronoun.setTokens(dict.pronouns);
        AdjAction.setTokens(dict.adjAction);
        Preposition.setTokens(dict.prepositions);
        Place.setTokens(dict.places);
        Link.setTokens(dict.links);
        Not.setTokens(new ArrayList<>(Collections.singletonList("nicht")));
    }

    String GenerateQ() {
        return GenerateGen(SentQ) + "?";
    }

    String GenerateP() {
        return GenerateGen(SentP) + ".";
    }

    String GenerateN() {
        return GenerateGen(SentN) + ".";
    }

    String GenerateGen(Node role) {
        Vector<Node> vecNode = new Vector<>();
        vecNode.add(role);
        for (int i = 0; i < vecNode.size(); i++) {
            if (!vecNode.get(i).matrix_roles.isEmpty()) {
                Vector<Node> helpVec = vecNode.get(i).getRandomRules();
                vecNode.remove(i);
                vecNode.addAll(i, helpVec);
                i--;
            }
        }
        Vector<String> tokens = new Vector<>();
        for (int i = 0; i < vecNode.size(); i++) {
            tokens.add(vecNode.get(i).getRandomToken());
        }
        String sentence = String.join(" ", tokens);

        if (!sentence.isEmpty()) {
            sentence = Character.toUpperCase(sentence.charAt(0)) + sentence.substring(1);
        }
        return sentence;
    }
}
