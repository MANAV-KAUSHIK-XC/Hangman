import javax.swing.*;

public class Frame extends JFrame {
    Frame() {
        initialise();
        this.add(new HangmanPanel());
        this.revalidate();
        this.repaint();
    }

    private void initialise() {
        this.setTitle("Hangman! - By Manav");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setResizable(true);
        this.setVisible(true);
    }
}
