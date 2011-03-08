package latestart7drl;

import asciiPanel.AsciiPanel;
import java.applet.Applet;
import java.util.Random;

public class AppletMain extends Applet {
    
    public AppletMain(){
        AsciiPanel panel = new AsciiPanel(80, 24);
        add(panel);

        Random rand = new Random();
        World world = new World();
        Creature player = new Creature(world, 0, 0, '@', AsciiPanel.brightWhite);

        do {
            player.x = rand.nextInt(world.width);
            player.y = rand.nextInt(world.height);
        } while (!player.canMoveBy(0, 0));

        world.creatures.add(player);
        
        GuiController gui = new GuiController(panel, world, player);
        addKeyListener(gui);
        gui.startScreen();
    }
}