package readability;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.File;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        File file = new File(args[0]);
        String text = "";

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                text = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + args[0]);
        }

        System.out.println("The text is:");
        System.out.println(text + "\n");
        analyzeText(text);
    }

    private static double countSyllables(String[] words) {
        int syllables = 0;

        for (String word : words) {
            syllables += word.toLowerCase()
                    // Matches an "e" only if it appears at the end of the word ($ means end of string).
                    .replaceAll("e$", "")
                    // Matches two consecutive vowels (e.g., "oo", "ea", "ai")
                    .replaceAll("[aeiouy]{2}", "a")
                    //  Matches any character that is NOT a vowel.
                    //  Removes all consonants and punctuation from the word.
                    .replaceAll("[^aeiouy]", "")
                    // After all transformations, the length of
                    // the remaining string (which contains only vowels) represents the syllable count.
                    .length();
        }
        return syllables;
    }



    private static double countPolysyllables(String[] words) {
        int polysyllables = 0;

        for (String word : words) {
            int syllables = word.toLowerCase()
                    .replaceAll("e$", "")
                    .replaceAll("[aeiouy]{2}", "a")
                    .replaceAll("[^aeiouy]", "")
                    .length();
            // if syllables is greater than 2, increment polysyllables
            if (syllables > 2) {
                polysyllables++;
            }
        }

        return polysyllables;
    }

    private static void analyzeText(String text) {
        String[] sentences = text.split("[!.?]"); // Split sentences by "!" "." or "?"
        int totalWords = 0;
        int wordCount;
        int characters = 0;
        double syllables;
        double polysyllables;
        int punctuationMarks = 0;


        for (String sentence : sentences) {
            sentence = sentence.trim(); // remove leading and trailing whitespaces in sentence
            if (!sentence.isEmpty()) {
                String[] words = sentence.split("\\s+"); // Split words by spaces
                wordCount = words.length;
                totalWords += wordCount;
                // Get number of characters from words, not including punctuation marks
                for (String word : words) {
                    characters += word.length();
                }
            }
        }
        // Get number of characters from punctuation marks in text
        for (char ch : text.toCharArray()) {
            if (String.valueOf(ch).matches("[.!?]")) { // Match punctuation marks
                punctuationMarks++;
            }
        }

        syllables = countSyllables(text.split("\\s"));
        polysyllables = countPolysyllables(text.split("\\s"));

        characters += punctuationMarks;

        System.out.println("Words: " + totalWords);
        System.out.println("Sentences: " + sentences.length);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);

        Scanner scan = new Scanner(System.in);
        System.out.print("\nEnter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String input = scan.nextLine();
        System.out.println();

        switch (input) {
            case "ARI" -> {
                double ariScore = getAutomatedReadabilityIndex(characters, totalWords, sentences);
                System.out.println("Automated Readability Index: " + df.format(ariScore) + " (about " + ageRange(ariScore) + "-year-olds).");
            }
            case "FK" -> {
                double fkScore = getFleschKincaidReadabilityScore(totalWords, sentences, syllables);
                System.out.println("Flesch-Kincaid readability tests: " + df.format(fkScore) + " (about " + ageRange(fkScore) + "-year-olds).");
            }
            case "SMOG" -> {
                double smogScore = getSMOGReadabilityScore(polysyllables, sentences);
                System.out.println("Simple Measure of Gobbledygook: " + df.format(smogScore) + " (about " + ageRange(smogScore) + "-year-olds).");
            }
            case "CL" -> {
                double clScore = getColemanLiauReadabilityScore(characters, totalWords, sentences);
                System.out.println("Coleman-Liau index: " + df.format(clScore) + " (about " + ageRange(clScore) + "-year-olds).");
            }
            case "all" -> {
                double ariScore = getAutomatedReadabilityIndex(characters, totalWords, sentences);
                System.out.println("Automated Readability Index: " + df.format(ariScore) + " (about " + ageRange(ariScore) + "-year-olds).");

                double fkScore = getFleschKincaidReadabilityScore(totalWords, sentences, syllables);
                System.out.println("Flesch-Kincaid readability tests: " + df.format(fkScore) + " (about " + ageRange(fkScore) + "-year-olds).");

                double smogScore = getSMOGReadabilityScore(polysyllables, sentences);
                System.out.println("Simple Measure of Gobbledygook: " + df.format(smogScore) + " (about " + ageRange(smogScore) + "-year-olds).");

                double clScore = getColemanLiauReadabilityScore(characters, totalWords, sentences);
                System.out.println("Coleman-Liau index: " + df.format(clScore) + " (about " + ageRange(clScore) + "-year-olds).");

                double avg = (ariScore + fkScore + smogScore + clScore) / 4;
                System.out.println("\nThis text should be understood in average by " + df.format(avg) + "-year-olds.");
            }
            default -> throw new IllegalStateException("Unexpected value: " + input);
        }

    }

    private static double getAutomatedReadabilityIndex(int characters, int totalWords, String[] sentences) {
        return 4.71 * (1.0 * characters / totalWords) + 0.5 * (1.0 * totalWords/ sentences.length) - 21.43;
    }

    private static double getFleschKincaidReadabilityScore(double words, String[] sentences, double syllables) {
        return 0.39 * (words / sentences.length) + 11.8 *
                (syllables / words) - 15.59;
    }

    private static double getSMOGReadabilityScore(double polysyllables, String[] sentences) {
        return 1.043 * Math.sqrt(polysyllables * ((double) 30 / sentences.length)) + 3.1291;
    }

    private static double getColemanLiauReadabilityScore(double characters, double words, String[] sentences) {
        double L = characters / words * 100;
        double S = sentences.length / words * 100;

        return  0.0588 * L - 0.296 * S - 15.8;
    }

    private static double ageRange(double score) {
        return switch ((int) Math.ceil(score)) {
            case 1 -> 6;
            case 2 -> 7;
            case 3 -> 8;
            case 4 -> 9;
            case 5 -> 10;
            case 6 -> 11;
            case 7 -> 12;
            case 8 -> 13;
            case 9 -> 14;
            case 10 -> 15;
            case 11 -> 16;
            case 12 -> 17;
            case 13 -> 18;
            case 14 -> 22;
            default -> 0;
        };
    }
}
