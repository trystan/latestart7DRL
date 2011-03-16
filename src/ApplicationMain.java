
import javax.swing.JFrame;

public class ApplicationMain extends JFrame {

    public ApplicationMain(){
        AsciiPanel panel = new AsciiPanel(80, 24);
        add(panel);

        GuiController gui = new GuiController(panel, new PlayerController(null));

        addKeyListener(gui);
        gui.startScreen();
    }

    static public void main(String argv[]) {
        ApplicationMain app = new ApplicationMain();
        app.pack();
        app.setVisible(true);
    }
}