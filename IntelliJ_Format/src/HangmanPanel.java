import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
This class deals with the entire GUI interface.
 */
public class HangmanPanel extends JPanel {
    final private Image bgImage = Toolkit.getDefaultToolkit().getImage("res/bg.png");

    HangmanPanel() {
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(new BorderLayout());
        startMainMenu();
    }

     void startMainMenu() {
        this.removeAll();
        this.add(new MainMenuPanel(), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    void startGame() {
        this.removeAll();
        this.add(new GamePanel(), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bgImage, 0, 0, this);   //Draws background
    }


    /*
    Describes the entire interface of the main menu.
     */
    class MainMenuPanel extends JPanel implements ActionListener {
        MainMenuPanel() {
            Font titleFont = new Font("monospace", Font.PLAIN, 50);
            try {
                titleFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res/Chango-Regular.ttf"))).deriveFont(50f);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            this.setOpaque(false);  //Not opaque to let the background from the previous panel be visible.
            GridBagLayout gridBagLayout = new GridBagLayout();
            this.setLayout(gridBagLayout);
            GridBagConstraints constraints = new GridBagConstraints();

            //Adding Title
            JLabel titleLabel = new JLabel("Hangman!");
            titleLabel.setFont(titleFont);
            titleLabel.setOpaque(false);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.NONE;
            constraints.weightx = 0.5;
            this.add(titleLabel, constraints);

            //Adding play button
            JButton playButton = new JButton("Play");
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.fill = GridBagConstraints.NONE;
            constraints.weightx = 0.5;
            constraints.insets = new Insets(100, 0, 0, 0);
            playButton.setOpaque(false);
            playButton.setContentAreaFilled(false);
            this.add(playButton, constraints);
            playButton.addActionListener(this);
            constraints.insets = new Insets(0,0,0,0);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            startGame();    //Start game when play button is pressed.
        }
    }


    /*
    Describes the entire interface of the hang man game.
     */
    class GamePanel extends JPanel {
        final private String[] hangedImagePaths = {"res/0.png", "res/1.png", "res/2.png", "res/3.png", "res/4.png", "res/5.png", "res/6.png", "res/7.png"};
        final private ImageIcon[] hangedImageIcons = new ImageIcon[hangedImagePaths.length];
        final private int maxHangmanStates = hangedImagePaths.length-1;

        private int hangmanState = 0;
        final private JLabel hangedImageLabel = new JLabel();
        final private JLabel displayedWordLabel = new JLabel();
        final private Game game = new Game(maxHangmanStates);
        final private KeyboardAndWinLosePanel keyboardAndWinLosePanel = new KeyboardAndWinLosePanel();


        GamePanel() {
            Font displayedWordFont = new Font("serif", Font.BOLD, 40);
            try {
                displayedWordFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("res/Oswald-Regular.ttf")).deriveFont(40f);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            this.setOpaque(false);  //Not opaque to let the background from the previous panel be visible.
            game.nextWord();
            this.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            initImages();

            //Adding image
            hangedImageLabel.setIcon(hangedImageIcons[0]);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.VERTICAL;
            constraints.weightx = 0.5;
            constraints.gridwidth = 2;
            constraints.gridheight = 2;
            this.add(hangedImageLabel, constraints);

            //Setting up area where the dashes and words appear
            displayedWordLabel.setText(addSpaces(game.getDisplayedWord()));
            displayedWordLabel.setFont(displayedWordFont);
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.fill = GridBagConstraints.NONE;
            constraints.weightx = 0.5;
            constraints.gridwidth = 2;
            constraints.gridheight = 1;
            constraints.insets = new Insets(50,0,0,0);
            this.add(displayedWordLabel, constraints);

            //Adding keyboard panel
            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 0.5;
            constraints.gridwidth = 3;
            constraints.gridheight = 2;
            constraints.insets = new Insets(0,0,0,0);
            this.add(keyboardAndWinLosePanel, constraints);
        }

        void newGame() {    //Resets all variables and rolls over to a new word for the next round.
            game.nextWord();
            hangmanState = 0;
            keyboardAndWinLosePanel.startKeyboardPanel();
            hangedImageLabel.setIcon(hangedImageIcons[hangmanState]);
            displayedWordLabel.setText(addSpaces(game.getDisplayedWord()));

            this.revalidate();
            this.repaint();
        }

        private void initImages() {
            for(int i = 0; i < hangedImagePaths.length; i++) {
                try {
                    BufferedImage img = ImageIO.read(new File(hangedImagePaths[i]));
                    hangedImageIcons[i] = new ImageIcon(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //This method checks if the letter which was pressed is in the word and returns the check's result.
        //It also deals with win and loss.
        boolean letterPressed(char letter) {
            boolean out = false;

            if(game.checkLetter(Character.toUpperCase(letter))) {
                displayedWordLabel.setText(addSpaces(game.getDisplayedWord()));
                out = true;
            } else {
                hangedImageLabel.setIcon(hangedImageIcons[(hangmanState >= maxHangmanStates) ? hangmanState : ++hangmanState]);
            }

            if(game.isLoss()) keyboardAndWinLosePanel.startWinLosePanel(false);
            else if(game.isVictory()) keyboardAndWinLosePanel.startWinLosePanel(true);

            return out;
        }

        //Method to add spaces after every letter of string to create gaps
        public String addSpaces(String str) {
            final int noOfSpaces = 2;

            String outString = "";

            for(int i = 0; i < str.length(); i++) {
                outString += str.charAt(i);
                for(int j = 0; j < noOfSpaces; j++) {
                    outString += " ";
                }
            }
            return outString;
        }

        /*
        This class has interface for the keyboard and also for the winning/losing text that is displayed.
         */
        class KeyboardAndWinLosePanel extends JPanel {
            KeyboardAndWinLosePanel() {
                this.setOpaque(false); //Not opaque to let the background from the previous panel be visible.
                this.setLayout(new BorderLayout());
                startKeyboardPanel();
            }

            public void startWinLosePanel(boolean win) {
                this.removeAll();
                this.add(new WinLosePanel(win), BorderLayout.CENTER);
                revalidate();
                repaint();
            }

            public void startKeyboardPanel() {
                this.removeAll();
                this.add(new KeyBoardPanel(), BorderLayout.CENTER);
                revalidate();
                repaint();
            }


            /*
            Interface which shows the winning/losing text.
             */
            class WinLosePanel extends JPanel implements ActionListener {
                final private Font labelFont = new Font("sans", Font.PLAIN, 25);
                final private JLabel wordLabel = new JLabel();
                final private JLabel textLabel = new JLabel();

                final private String winText = "You guessed it right!";
                final private String loseText = "Better luck next time!";

                final private JButton replayButton = new JButton("Play again");

                WinLosePanel(boolean win) {
                    this.setOpaque(false); //Not opaque to let the background from the previous panel be visible.
                    this.setLayout(new GridBagLayout());
                    GridBagConstraints constraints = new GridBagConstraints();

                    //Adding word label.
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    constraints.ipady = 20;
                    wordLabel.setFont(labelFont);
                    this.add(wordLabel, constraints);

                    //Adding the win/lose text label.
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    constraints.ipady = 20;
                    textLabel.setFont(labelFont);
                    this.add(textLabel, constraints);

                    //Adding the replay/play again button.
                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    constraints.insets = new Insets(30, 0, 0, 0);
                    replayButton.setOpaque(false);
                    replayButton.setContentAreaFilled(false);
                    this.add(replayButton, constraints);
                    replayButton.addActionListener(this);

                    if(win) {
                        textLabel.setForeground(Color.GREEN);
                        textLabel.setText(winText);
                    }
                    else {
                        textLabel.setForeground(Color.RED);
                        wordLabel.setForeground(Color.RED);
                        wordLabel.setText("The word was " + game.getWord()); //If you lose, it shows you what the correct word was
                        textLabel.setText(loseText);
                    }
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    newGame();  //If replay button is pressed, start a new round.
                }
            }


            /*
            This class presents the interface for the Keyboard panel
             */
            class KeyBoardPanel extends JPanel implements ActionListener {
                private Font lettersFont;

                KeyBoardPanel() {
                    try {
                        lettersFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("res/SyneMono-Regular.ttf"))).deriveFont(32f);
                    } catch (FontFormatException | IOException e) {
                        e.printStackTrace();
                    }
                    this.setOpaque(false); //Not opaque to let the background from the previous panel be visible.
                    this.setLayout(new GridLayout(4, 7, 10, 10));

                    addLetters();
                }

                private void addLetters() { //Adds the letter buttons to the keyboard
                    char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                    CrossCircleButton[] lettersButtons = new CrossCircleButton[letters.length];
                    for(int i = 0; i < letters.length; i++) {
                        //Adding letter buttons.
                        lettersButtons[i] = new CrossCircleButton(String.valueOf(letters[i]));
                        lettersButtons[i].setFont(lettersFont);
                        lettersButtons[i].setBorderPainted(false);
                        lettersButtons[i].setContentAreaFilled(false);
                        lettersButtons[i].setOpaque(false);
                        this.add(lettersButtons[i]);
                        lettersButtons[i].addActionListener(this);
                    }
                }

                @Override
                public void actionPerformed(ActionEvent e) {
                    CrossCircleButton sourceButton = (CrossCircleButton) e.getSource();
                    sourceButton.setEnabled(false);

                    //Check if the letter button pressed existed in the word and thus circle or cross the button.
                    if(letterPressed(sourceButton.getText().toCharArray()[0])) {
                        sourceButton.setCircle();
                    }
                    else {
                        sourceButton.setCross();
                    }
                }
            }
        }
    }
}
