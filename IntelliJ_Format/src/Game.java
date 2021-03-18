import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/*
This class mainly deals with sending the words to the GUI interface.
It deals with all the logic.
It reads the file once, counting the number of lines.
Then it chooses a random line on which there is a word.
 */
public class Game {
    private int lines = 0;
    private int lives;
    final private Random random = new Random();
    final private File wordsFile = new File("res/words.txt");

    final private int maxLives;

    private String word;
    private String displayedWord;

    Game(int maxLives) {
        this.maxLives = maxLives;

        Scanner reader = null;
        try {
            reader = new Scanner(wordsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(reader.hasNextLine()) {   //Counting number of lines.
            reader.nextLine();
            lines++;
        }
    }

    public void nextWord() {
        lives = maxLives;
        int line = random.nextInt(lines);   //Select a random line

        Scanner reader = null;
        try {
            reader = new Scanner(wordsFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < line; i++) { //Reach the line just before the chosen line.
            reader.nextLine();
        }
        word = reader.nextLine().toUpperCase(); //Read the chosen line

        displayedWord = "";
        for(int i = 0; i < word.length(); i++) {    //DisplayedWord has _ for each letter which are revealed as the game continues.
            displayedWord += "_";
        }
    }

    public boolean checkLetter(char letter) {   //Check if letter is in word
        boolean hasLetter = false;
        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == letter) {  //Replace _ in displayedWord with actual letter of that place.
                char[] temp = displayedWord.toCharArray();
                temp[i] = letter;
                displayedWord = String.valueOf(temp);
                hasLetter = true;
            }
        }
        if(!hasLetter) lives--;
        return hasLetter;
    }

    public String getDisplayedWord() {
        return displayedWord;
    }

    public boolean isVictory() {
        return displayedWord.equals(word);
    }

    public boolean isLoss() {
        return lives <= 0;
    }

    public String getWord() {
        return word;
    }
}
