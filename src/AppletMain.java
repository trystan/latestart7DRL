
import java.applet.Applet;

public class AppletMain extends Applet {
    
    public AppletMain(){
        AsciiPanel panel = new AsciiPanel(80, 24);
        add(panel);

        GuiController gui = new GuiController(panel, new PlayerController(null));

        addKeyListener(gui);
        gui.startScreen();
    }
}