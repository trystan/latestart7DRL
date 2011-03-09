
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GuiController implements KeyListener {
    private final static int start = 1;
    private final static int win = 2;
    private final static int lose = 3;
    private final static int play = 4;

    private int stage;

    private AsciiPanel panel;
    private World world;
    private Creature target;
    
    public GuiController(AsciiPanel p) {
        panel = p;
        stage = start;
        reset();
    }

    public void reset(){
        Random rand = new Random();
        world = new World();
        CreatureFactory factory = new CreatureFactory(world);

        target = factory.Player();
        world.placeCreature(target, rand);

        world.placeCreature(factory.HeroFighter(), rand);
        world.placeCreature(factory.HeroTheif(), rand);
        world.placeCreature(factory.HeroWizzard(), rand);

        for (int i = 0; i < 100; i++){
            world.placeCreature(factory.Zombie(), rand);
        }
        
        for (int i = 0; i < 100; i++){
            world.placeCreature(factory.Blob(rand), rand);
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        switch (stage) {
            case start:
            case win:
            case lose:
                if (ke.getKeyChar() == ' '){
                    stage = play;
                    reset();
                }
            break;
            case play:
                boolean endTurn1 = true;
                boolean endTurn2 = true;
                switch (ke.getKeyChar()) {
                    case '8':
                    case 'k': target.moveBy(0, -1); break;
                    case '2':
                    case 'j': target.moveBy(0, 1); break;
                    case '4':
                    case 'h': target.moveBy(-1, 0); break;
                    case '6':
                    case 'l': target.moveBy(1, 0); break;
                    case '7':
                    case 'y': target.moveBy(-1, -1); break;
                    case '1':
                    case 'b': target.moveBy(-1, 1); break;
                    case '9':
                    case 'u': target.moveBy(1, -1); break;
                    case '3':
                    case 'n': target.moveBy(1, 1); break;
                    case '5':
                    case '.': target.moveBy(0, 0); break;
                    default: endTurn1 = false;
                }
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_UP: target.moveBy(0, -1); break;
                    case KeyEvent.VK_DOWN: target.moveBy(0, 1); break;
                    case KeyEvent.VK_LEFT: target.moveBy(-1, 0); break;
                    case KeyEvent.VK_RIGHT: target.moveBy(1, 0); break;
                    default: endTurn2 = false;
                }

                if (endTurn1 || endTurn2)
                    world.update();
                break;
            default: stage = 0; break;
        }

        currentScreen();
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void currentScreen() {
        if (target.hp < 1)
            stage = lose;
        
        switch (stage) {
            case start: startScreen(); break;
            case win: winScreen(); break;
            case lose: loseScreen(); break;
            case play: playScreen(); break;
            default: wtfScreen(); break;
        }
        panel.repaint();
    }

    public void startScreen() {
        panel.clear();
        panel.writeCenter("late start, a 2011 7DRL", 1);
        panel.writeCenter("   by Trystan Spangler", 2);
        panel.writeCenter("-- press space to start --", panel.getHeightInCharacters() - 2);
    }

    public void winScreen() {
        panel.clear();
        panel.writeCenter("win", 1);
        panel.writeCenter("-- press space to restart --", panel.getHeightInCharacters() - 2);
    }

    public void loseScreen() {
        panel.clear();
        panel.writeCenter("lose", 1);
        panel.writeCenter("-- press space to restart --", panel.getHeightInCharacters() - 2);
    }

    public void wtfScreen() {
        panel.clear();
        panel.write("wtf? Invalid game state.", panel.getHeightInCharacters() - 4, 2);
        panel.writeCenter("-- press space to restart --", panel.getHeightInCharacters() - 2);
    }

    public void playScreen() {
        panel.clear();

        int viewWidth = 80;
        int viewHeight = 23;
        int vx = Math.max(0, Math.min(target.x - viewWidth / 2, world.width - viewWidth));
        int vy = Math.max(0, Math.min(target.y - viewHeight / 2, world.height - viewHeight));

        for (int x = vx; x < vx + viewWidth; x++) {
            for (int y = vy; y < vy + viewHeight; y++) {
                panel.write(world.getGlyph(x,y), x - vx, y - vy, world.getColor(x, y));
            }
        }

        for (Creature creature : world.creatures){
            int cx = creature.x - vx;
            int cy = creature.y - vy;

            if (cx < 0 || cx >= viewWidth || cy < 0 || cy >= viewHeight)
                continue;
            
            panel.write(creature.glyph, cx, cy, creature.color);
        }


        if (target.target != null){
            infoPanel(target, 1);
            infoPanel(target.target, -1);
        }

        writeMessages();

        panel.write(world.getName(target.x, target.y), 71, panel.getHeightInCharacters() - 1);
        panel.write(" " + target.name + " (" + target.x + "," + target.y + ")", 0, panel.getHeightInCharacters() - 1);
    }

    private void writeMessages(){
        int startY = panel.getHeightInCharacters() - target.messages.size() - 1;
        for (int i = 0; i < target.messages.size(); i++){
            panel.writeCenter(target.messages.get(i), startY+i, target.messageColors.get(i));
        }
        target.messages.clear();
        target.messageColors.clear();
    }

    private void infoPanel(Creature creature, int left){
        int bottom = panel.getHeightInCharacters() - 1;
        int right = panel.getWidthInCharacters();
        int panelWidth = 20;

        if (left < 0)
            left = right - panelWidth + left;
        
        panel.write(pad("   " + creature.name, panelWidth), left, bottom-4);
        panel.write(pad("  hp:" + creature.hp, panelWidth), left, bottom-3);
        panel.write(pad(" atk:" + creature.attack, panelWidth), left, bottom-2);
        panel.write(pad(" def:" + creature.defence, panelWidth), left, bottom-1);
    }

    private String pad(String str, int length){
        while (str.length() < length){
            str += " ";
        }
        return str;
    }
}
