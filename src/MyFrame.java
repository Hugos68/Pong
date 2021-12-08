import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    JFrame frame;

    MyFrame() {
        frame = new JFrame();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setTitle("Pong!");
        this.setVisible(true);
    }
}
