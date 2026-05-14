package parser;

import java.util.*;

public class Dictionary {
    ArrayList<String> adjectives = new ArrayList<>(Arrays.asList(
            "schön", "groß", "klein", "neu", "alt",
            "klug", "freundlich", "stark", "schwach", "rot", "blau", "grün",
            "schwarz", "weiß", "lang", "kurz", "schlecht",
            "wichtig", "interessant", "freundlich", "fantastisch"
    ));

    ArrayList<String> nounObjects = new ArrayList<>(Arrays.asList(
            "apfel", "orange", "banane", "buch", "tisch", "stift", 
            "arzt", "lehrer", "ingenieur", "künstler", "notizbuch", 
            "papier", "computer", "mensch", "hund", "katze", 
            "giraffe", "löwe", "welt", "kind", "frau", "platz", 
            "arbeit", "woche", "regierung", "firma"
    ));

    ArrayList<String> pronouns = new ArrayList<>(Arrays.asList(
            "er", "sie", "es", "wir", "sie", "ich"
    ));

    ArrayList<String> vStates = new ArrayList<>(Arrays.asList(
            "glaubt", "hält", "fühlt", "respektiert", "versteht", 
            "hört", "findet", "stellt", "bewundert"
    ));

    ArrayList<String> vMoves = new ArrayList<>(Arrays.asList(
            "kommt", "läuft", "fährt", "reist"
    ));

    ArrayList<String> vActions = new ArrayList<>(Arrays.asList(
            "steht", "sieht", "spricht", "rennt", "tanzt", 
            "singt", "lacht", "weint"
    ));

    ArrayList<String> prepositions = new ArrayList<>(Arrays.asList(
            "von", "zu", "in"
    ));

    ArrayList<String> places = new ArrayList<>(Arrays.asList(
            "Berlin", "München", "Hamburg", "Frankfurt", "Köln", 
            "Düsseldorf", "Stuttgart", "Leipzig", "Dresden"
    ));

    ArrayList<String> adjAction = new ArrayList<>(Arrays.asList(
            "selbstbewusst", "schlecht", "gut", "schnell", 
            "langsam", "schön", ""
    ));

    ArrayList<String> links = new ArrayList<>(Arrays.asList(
            "und", "aber" 
    ));
    
    public String findWord(String word) {
        if (adjectives.contains(word)) return "adjectives";
        if (nounObjects.contains(word)) return "nounObjects";
        if (pronouns.contains(word)) return "pronouns";
        if (vStates.contains(word)) return "vStates";
        if (vMoves.contains(word)) return "vMoves";
        if (vActions.contains(word)) return "vActions";
        if (prepositions.contains(word)) return "prepositions";
        if (places.contains(word)) return "places";
        if (adjAction.contains(word)) return "adjAction";
        if (links.contains(word)) return "links";
        return "Word not found";
    }
}

