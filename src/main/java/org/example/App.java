package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App
{
    // nbr max d'erreurs.
    private static final int MAX_ERR = 8;

    public static void main( String[] args )
    {
        App app = new App();
        app.demarre();
    }

    // methode pour demarrer le jeux
    private void demarre() {
        System.out.println("App lancé !");

        // Récupération des mots depuis le fichier JSON
        String[] words = getWordsFromJson();

        // Si aucun mot disponible ou si la liste est vide
        if (words == null || words.length == 0) {
            System.out.println("Aucun mot disponible.");
            return;
        }

        // Lancer le jeu du pendu avec la liste de mots récupérée
        LancerLePendu(words);
    }

    // Lit les mots et retourne
    public String[] getWordsFromJson() {
        // Parseur JSON
        JSONParser parser = new JSONParser();
        try {
            // Chargement du fichier JSON
            FileReader reader = new FileReader(
                    getClass().getResource("/json/myJson.json").getPath()
            );

            // Lecture du fichier JSON
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray jsonArray = (JSONArray) jsonObject.get("words");

            // Conversion du tableau JSON en string
            String[] words = new String[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                words[i] = (String) jsonArray.get(i);
            }
            return words;

        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
            return null;
        }
    }

    // Méthode qui gère la logique
    public void LancerLePendu(String[] words) {

        // Sélect un mot aléatoire dans la liste des mots
        String wordToGuess = words[new Random().nextInt(words.length)];

        // Conversion du mot en un tableau de caractères
        char[] wordArray = wordToGuess.toCharArray();

        // Progression
        char[] progress = new char[wordArray.length];

        // Remplie le tableau de progression
        Arrays.fill(progress, '_');

        int errors = 0; // Compteur d'erreurs
        boolean wordGuessed = false; // Indicateur si le mot a été deviné entièrement
        Scanner scanner = new Scanner(System.in);

        /*
        * Boucle principale du jeu qui se poursuit tant que le joueur
        n'a pas atteint le nombre maximum d'erreurs et n'a pas deviné le mot
        * */
        while (errors < MAX_ERR && !wordGuessed) {
            // Print
            System.out.println("Mot à deviner : " + new String(progress));
            System.out.println("Nombre d'erreurs : " + errors + "/" + MAX_ERR);
            System.out.println("\r");
            System.out.println(); // Espace pour lisibilité
            System.out.print("Devinez une lettre : ");

            // Read la lettre
            char guessedLetter = scanner.nextLine().toLowerCase().charAt(0);

            boolean correctGuess = false;

            // Parcourir le mot à deviner pour vérifier si la lettre devinée est présente
            for (int i = 0; i < wordArray.length; i++) {
                if (wordArray[i] == guessedLetter) {
                    progress[i] = guessedLetter; // Remplace les - par la lettre guessed
                    correctGuess = true;
                }
            }

            // Si la lettre n'est pas correcte, incrémenter
            if (!correctGuess) {
                errors++;
                System.out.println("Lettre incorrecte !");
            }

            // Si le joueur a complété le mot, passer a true
            if (Arrays.equals(progress, wordArray)) {
                wordGuessed = true;
            }
        }

        // Fin du jeu
        if (wordGuessed) {
            System.out.println("Félicitations ! Vous avez deviné le mot : " + wordToGuess);
        } else {
            System.out.println("Dommage ! Vous avez perdu. Le mot était : " + wordToGuess);
        }
    }
}
