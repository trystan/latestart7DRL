
import java.applet.Applet;
import java.util.Random;

public class AppletMain extends Applet {
    
    public AppletMain(){
        AsciiPanel panel = new AsciiPanel(80, 24);
        add(panel);

        Random rand = new Random();
        World world = new World();
        Creature player = new Creature(world, 0, 0, "player", '@', AsciiPanel.brightWhite);
        placeCreature(player, world, rand);

        for (int i = 0; i < 100; i++){
            Creature zombie = new Creature(world, 0, 0, "zombie", 'z', AsciiPanel.brightWhite);
            zombie.controller = new CreatureController(zombie);
            placeCreature(zombie, world, rand);
        }

        GuiController gui = new GuiController(panel, world, player);
        addKeyListener(gui);
        gui.startScreen();
    }

    private void placeCreature(Creature creature, World world, Random rand){
        do {
            creature.x = rand.nextInt(world.width);
            creature.y = rand.nextInt(world.height);
            
            for (Creature other : world.creatures){
                if (other.x == creature.x && other.y == creature.y)
                    continue;
            }
        } while (!creature.canMoveBy(0, 0));

        world.creatures.add(creature);
    }
}