package pl.edu.agh.hangman;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class Hangman {

    public static final String[] HANGMANPICS = new String[]{
            "  +---+\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    "  |   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|   |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    "      |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    " /    |\n" +
                    "      |\n" +
                    "=========",
            "  +---+\n" +
                    "  |   |\n" +
                    "  O   |\n" +
                    " /|\\  |\n" +
                    " / \\  |\n" +
                    "      |\n" +
                    "========"
    };

    public static String getWord(int min, int max) {
        try {
            URL url = new URL("http://api.wordnik.com/v4/words.json/randomWords?hasDictionaryDef=true&minCorpusCount=0&minLength="+min+"&maxLength="+max+"&limit=1&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String[] input = in.readLine().split("\"");

            in.close();
            con.disconnect();

            String word = input[5];

            return word;
        }
        catch(Exception e) {
            System.out.println("error");
            return "";
        }
    }

    public static String createDisplayWord(int length) {
        String blindWord = "";
        for(int x = 0; x < length; x++) {
            blindWord += "_";
        }
        return blindWord;
    }

    public static String newDisplayedWord(char letter, String word, String blindWord) {
        String newBlindWord = "";
        for(int x = 0; x < blindWord.length(); x++) {
            if(blindWord.charAt(x) != '_') {
                newBlindWord += blindWord.charAt(x);

            }
            else if(word.charAt(x) == letter) {
                newBlindWord += letter;
            }
            else {
                newBlindWord += "_";
            }
        }
        return newBlindWord;
    }

    public static void displayGameState(int stage) {
        System.out.println(HANGMANPICS[stage]);
        System.out.println("\n");
        System.out.print("guess> ");
    }

    public static void endGameMessage(boolean czyWygrana, int stage, String word) {
        Scanner input = new Scanner(System.in);
        if(czyWygrana) {
            System.out.println("Wygrales");
        }
        else {
            System.out.println(HANGMANPICS[stage]);
            System.out.println("Przegrales");
            System.out.println("To slowo to "+word);
        }
    }

    public static void clearConsole() {
        try {
            Runtime.getRuntime().exec("cls");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void playGame(String customWord) {
        Scanner userInput = new Scanner(System.in);
        Scanner userInput2 = new Scanner(System.in);
        String word;
        if(customWord.equals("")) {
            System.out.println("Podaj minimalna dlugosc slowa: ");
            int min = userInput.nextInt();
            System.out.println("Podaj maksymalna dlugosc slowa: ");
            int max = userInput.nextInt();
            word = getWord(min, max);
        }
        else {
            word = customWord;
        }

        String blindWord = createDisplayWord(word.length());
        boolean czyWygrana = false;
        int stage = 0;

        while(stage < (HANGMANPICS.length - 1) && czyWygrana == false) {
            clearConsole();
            System.out.println(blindWord + "\n");
            displayGameState(stage);
            String letter = userInput2.nextLine();
            if(!word.contains(letter)) {
                stage++;
            }
            else {
                blindWord = newDisplayedWord(letter.charAt(0), word, blindWord);
                czyWygrana = true;
                for(int x = 0; x < blindWord.length(); x++) {
                    if(blindWord.charAt(x) == '_') {
                        czyWygrana = false;
                    }
                }
            }


        }
        czyWygrana = true;
        for(int x = 0; x < blindWord.length(); x++) {
            if(blindWord.charAt(x) == '_') {
                czyWygrana = false;
            }
        }

        endGameMessage(czyWygrana, stage, word);
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while(true) {
            playGame("");
            System.out.println("Czy chcesz zagrac ponownie?");
            if(input.nextLine().equals("T")) {
                System.out.println("Czy chcesz wybrac swoje haslo?");
                if(input.nextLine().equals("T")) {
                    System.out.println("Podaj slowo:");
                    String word = input.nextLine();
                    playGame(word);
                }
                else {
                    playGame("");
                }
            }
            else {
                break;
            }
        }

    }
}
