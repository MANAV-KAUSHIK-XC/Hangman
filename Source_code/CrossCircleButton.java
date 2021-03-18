import javax.swing.*;
import java.awt.*;

/*
This class is a child of JButton and provides the extended functionality
of being able to circled and crossed easily.
 */
public class CrossCircleButton extends JButton {
    private boolean circle = false;
    private boolean cross = false;

    CrossCircleButton(String text) {
        super(text);
        this.setOpaque(false); //Not opaque to let the background from the previous panel be visible.
    }

    public void setCircle() {
        circle = true;
        cross = false;
    }

    public void setCross() {
        cross = true;
        circle = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(2));

        if(circle) {
            g2D.setColor(Color.BLUE);
            g2D.drawOval(0, 0, this.getWidth(), this.getHeight());
        }
        else if(cross) {
            g2D.setColor(Color.RED);
            g2D.drawLine(0, 0, this.getWidth(), this.getHeight());
            g2D.drawLine(this.getWidth(), 0, 0, this.getHeight());
        }
    }
}
