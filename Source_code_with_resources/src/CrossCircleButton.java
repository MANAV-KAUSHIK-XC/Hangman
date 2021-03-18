import javax.swing.*;
import java.awt.*;

public class CrossCircleButton extends JButton {
    boolean circle = false;
    boolean cross = false;

    CrossCircleButton(String text) {
        super(text);
        this.setOpaque(false);
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
