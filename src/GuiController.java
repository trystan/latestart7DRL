
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GuiController implements KeyListener {
    private final static int start = 1;
    private final static int win = 2;
    private final static int lose = 3;
    private final static int play = 4;
    private final static int help = 5;

    private int stage;

    private AsciiPanel panel;
    private World world;
    private Creature target;
    
    public GuiController(AsciiPanel p) {
        panel = p;
        stage = start;
        currentMessages = new ArrayList<String>();
        currentMessageColors = new ArrayList<Color>();
        reset();
    }

    public void reset(){
        Random rand = new Random();
        world = new World();
        ItemFactory itemFactory = new ItemFactory();
        CreatureFactory creatureFactory = new CreatureFactory(world, itemFactory);
        
        target = creatureFactory.Player();
        world.placeCreature(target, rand);

        world.placeCreature(creatureFactory.HeroFighter(), rand);
        world.placeCreature(creatureFactory.HeroTheif(), rand);
        world.placeCreature(creatureFactory.HeroWizzard(), rand);

        for (int i = 0; i < 100; i++){
            world.placeCreature(creatureFactory.Zombie(), rand);
            world.placeCreature(creatureFactory.Blob(rand), rand);
        }

        for (int i = 0; i < 50; i++){
            world.placeItem(itemFactory.armor(), rand);
            world.placeItem(itemFactory.weapon(), rand);
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
            case help:
                if (ke.getKeyChar() == ' '){
                    stage = play;
                }
                break;
            case play:
                boolean endTurn1 = true;
                boolean endTurn2 = true;
                switch (ke.getKeyChar()) {
                    case '?': stage = help; endTurn1 = false; break;
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
                    case 'g':
                    case ',':
                        boolean found = false;
                        for (Item item : world.items){
                            if (item.x == target.x && item.y == target.y && !item.equipped){
                                target.equip(item);
                                found = true;
                                break;
                            }
                        }
                        if (!found){
                            target.tell(AsciiPanel.white, "There's nothing here to pick up.");
                            endTurn1 = false;
                        }
                        break;
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
            case help: helpScreen(); break;
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

    public void helpScreen() {
        panel.clear();
        panel.writeCenter("help", 1);
        panel.write("@ = you or other heros", 3, 3);
        panel.write("z = zombie", 3, 4);
        panel.write("b = blob", 3, 5);
        panel.write("hjklyubn or arrow keys or numpad to move", 3, 7);
        panel.writeCenter("-- press space to continue --", panel.getHeightInCharacters() - 2);
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

        Item itemHere = null;

        for (Item item : world.items){
            int cx = item.x - vx;
            int cy = item.y - vy;

            if (cx < 0 || cx >= viewWidth || cy < 0 || cy >= viewHeight)
                continue;

            if (item.equipped)
                continue;

            if (item.x == target.x && item.y == target.y)
                itemHere = item;

            panel.write(item.glyph, cx, cy, item.color);
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
        } else if (itemHere != null) {
            infoPanel(target, 1);
            infoPanel(target, itemHere, 22);
        }

        writeMessages();

        panel.write(world.getName(target.x, target.y), 71, panel.getHeightInCharacters() - 1);

        String status = " " + target.name + " (" + target.x + "," + target.y + ")";

        if (target.weapon != null)
            status += " weilding a " + target.weapon.name;

        if (target.armor != null)
            status += " wearing " + target.armor.name;

        panel.write(status, 0, panel.getHeightInCharacters() - 1);
    }

    private ArrayList<String> currentMessages;
    private ArrayList<Color> currentMessageColors;
    private void writeMessages(){
        if (!target.messages.isEmpty()){
            currentMessages = (ArrayList<String>)target.messages.clone();
            currentMessageColors = (ArrayList<Color>)target.messageColors.clone();
            target.messages.clear();
            target.messageColors.clear();
        }

        int startY = panel.getHeightInCharacters() - currentMessages.size() - 1;
        for (int i = 0; i < currentMessages.size(); i++){
            panel.writeCenter(currentMessages.get(i), startY+i, currentMessageColors.get(i));
        }
    }

    private void infoPanel(Creature creature, int left){
        int panelWidth = 20;

        if (left < 0)
            left = panel.getWidthInCharacters() - panelWidth + left;

        String weaponName = creature.weapon != null ? " " + creature.weapon.name : "";
        String armorName = creature.armor != null ? " " + creature.armor.name : "";

        panel.write(pad(" " + creature.name, panelWidth), left, 1);
        panel.write(pad("  hp:" + creature.hp, panelWidth), left, 2);
        panel.write(pad(" atk:" + creature.attack + weaponName, panelWidth), left, 3);
        panel.write(pad(" def:" + creature.defence + armorName, panelWidth), left, 4);
    }

    private void infoPanel(Creature creature, Item item, int left){
        int panelWidth = 20;

        if (left < 0)
            left = panel.getWidthInCharacters() - panelWidth + left;

        int diffHp = 0;
        int diffAtk = 0;
        int diffDef = 0;
        if (creature != null){
            if (item.glyph == ')' && creature.weapon != null){
                diffHp = item.modHp - creature.weapon.modHp;
                diffAtk = item.modAttack - creature.weapon.modAttack;
                diffDef = item.modDefence - creature.weapon.modDefence;
            } else if (item.glyph == ']' && creature.armor != null){
                diffHp = item.modHp - creature.armor.modHp;
                diffAtk = item.modAttack - creature.armor.modAttack;
                diffDef = item.modDefence - creature.armor.modDefence;
            } else {
                diffHp = item.modHp;
                diffAtk = item.modAttack;
                diffDef = item.modDefence;
            }
        }

        char up = (char)24;
        char down = (char)25;
        String diffHpStr = diffHp == 0 ? "" : (diffHp > 0 ? " " + up + diffHp : " " + down + Math.abs(diffHp));
        String diffAtkStr = diffAtk == 0 ? "" : (diffAtk > 0 ? " " + up + diffAtk : " " + down + Math.abs(diffAtk));
        String diffDefStr = diffDef == 0 ? "" : (diffDef > 0 ? " " + up + diffDef : " " + down + Math.abs(diffDef));

        panel.write(pad(" " + item.name, panelWidth), left, 1);
        panel.write(pad("  hp:" + item.modHp + diffHpStr, panelWidth), left, 2);
        panel.write(pad(" atk:" + item.modAttack + diffAtkStr, panelWidth), left, 3);
        panel.write(pad(" def:" + item.modDefence + diffDefStr, panelWidth), left, 4);
    }

    private String pad(String str, int length){
        while (str.length() < length){
            str += " ";
        }
        return str.substring(0, length);
    }
}
