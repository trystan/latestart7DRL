
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

    private String input;
    private int stage;

    private AsciiPanel panel;
    private World world;
    private PlayerController controller;

    private ArrayList<String> currentMessages;
    private ArrayList<Color> currentMessageColors;
    
    public GuiController(AsciiPanel p, PlayerController c) {
        panel = p;
        stage = start;
        controller = c;
        input = "";
    }

    public void reset(){
        Random rand = new Random();
        currentMessages = new ArrayList<String>();
        currentMessageColors = new ArrayList<Color>();

        world = new World();
        ItemFactory itemFactory = new ItemFactory();
        CreatureFactory creatureFactory = new CreatureFactory(world, itemFactory);
        
        controller.target = creatureFactory.Player();
        controller.target.personalName = input;
        world.placeInVillage(controller.target, rand);

        world.placeInVillage(creatureFactory.HeroFighter(), rand);
        world.placeInVillage(creatureFactory.HeroWizzard(), rand);
        world.placeInVillage(creatureFactory.HeroMonk(), rand);
        world.placeInVillage(creatureFactory.HeroPreist(), rand);
        world.placeInVillage(creatureFactory.HeroSamuri(), rand);

        for (int i = 0; i < 12; i++){
            world.placeInVillage(creatureFactory.Villager(), rand);
        }

        for (int i = 0; i < 4; i++){
            world.placeAnywhere(creatureFactory.Vampire(), rand);
        }
        for (int i = 0; i < 4; i++){
            world.placeAnywhere(creatureFactory.Ghost(), rand);
        }
        for (int i = 0; i < 10; i++){
            world.placeAnywhere(creatureFactory.Zombie(), rand);
        }
        for (int i = 0; i < 10; i++){
            world.placeAnywhere(creatureFactory.Skeleton(), rand);
        }

        for (int i = 0; i < 10; i++){
            world.placeInVillage(itemFactory.armor(), rand);
            world.placeInVillage(itemFactory.weapon(), rand);
        }
        for (int i = 0; i < 5; i++){
            world.placeAnywhere(itemFactory.armor(), rand);
            world.placeAnywhere(itemFactory.weapon(), rand);
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        switch (stage) {
            case start:
                if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (input.length() > 0)
                        input = input.substring(0,input.length() - 1);
                } else if(ke.getKeyChar() == '\n')  {
                    stage = play;
                    reset();
                } else if (ke.getKeyChar() >= ' ' && ke.getKeyChar() <= '~') {
                    input += ke.getKeyChar();
                }
            case win:
            case lose:
                if (ke.getKeyChar() == '\n'){
                    stage = play;
                    reset();
                }
            break;
            case help:
                if (ke.getKeyChar() == '\n'){
                    stage = play;
                }
                break;
            case play:
                if (ke.getKeyChar() == '?')
                    stage = help;
                else if(controller.keyPressed(ke))
                    world.update();
                break;
            default: stage = 0; break;
        }

        currentScreen();
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void currentScreen() {
        if (stage == play) {
            if (controller.target.hp < 1 || controller.target.isZombie())
                stage = lose;
            else if (world.ticks >= world.ticksPerMinute * 720)
                stage = win;
        }

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
        panel.write("What is your name today? ", 3, 5);
        panel.write(input);
        
        if (input.length() > 0)
            panel.writeCenter("-- press enter to start --", panel.getHeightInCharacters() - 2);
    }

    public void winScreen() {
        panel.clear();
        panel.writeCenter("win", 1);
        panel.writeCenter("-- press enter to restart --", panel.getHeightInCharacters() - 2);
    }

    public void loseScreen() {
        panel.clear();
        panel.writeCenter("lose", 1);
        panel.writeCenter("-- press enter to restart --", panel.getHeightInCharacters() - 2);
    }

    public void wtfScreen() {
        panel.clear();
        panel.write("wtf? Invalid game state.", panel.getHeightInCharacters() - 4, 2);
        panel.writeCenter("-- press enter to restart --", panel.getHeightInCharacters() - 2);
    }

    public void helpScreen() {
        panel.clear();
        panel.writeCenter("help", 1);

        panel.write(" Can you survive the night and help protect the villagers from the undead?", 3, 3);
        panel.write("Each hero has special abilities to help defend the village. The secret to ",3, 4);
        panel.write("staying alive is to find the right location, equipment, and allies.", 3, 5);

        panel.write("@ = you, another hero, or a villager", 3, 7);
        panel.write("s = skeleton. Weak but you never know where one will show up", 3, 8);
        panel.write("z = zombie. Slow but they can turn humans into zombies", 3, 9);
        panel.write("g = ghost. Can move through walls", 3, 10);
        panel.write("v = vampire. Can't go indoors, heal when they attack and use equipment", 3, 11);
        panel.write("L = lich. A powerfull undead magic user", 3, 12);

        panel.write("swords can decapitate weakened opponents", 3, 14);
        panel.write("spears can counter attack opponents", 3, 15);
        panel.write("maces can knock opponents back", 3, 16);
        panel.write("some heros and enemies have their own special equipment", 3, 17);

        panel.write("[hjklyubn] or arrow keys or numpad to move", 3, 19);
        panel.write("[g] or [,] to pickup or swap weapon or armor", 3, 20);

        panel.writeCenter("-- press enter to continue --", panel.getHeightInCharacters() - 2);
    }

    public void playScreen() {
        panel.clear();

        int viewWidth = 80;
        int viewHeight = 24;
        int vx = Math.max(0, Math.min(controller.target.x - viewWidth / 2, world.width - viewWidth));
        int vy = Math.max(0, Math.min(controller.target.y - viewHeight / 2, world.height - viewHeight));

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

            if (item.x == controller.target.x && item.y == controller.target.y)
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

        infoPanel(controller.target, 1);

        if (controller.target.attacking != null){    
            infoPanel(controller.target.attacking, 22);
        } else if (itemHere != null) {
            infoPanel(controller.target, itemHere, 22);
        }

        scoreboard();
        writeMessages();
    }

    private void writeMessages(){
        if (!controller.target.messages.isEmpty()){
            currentMessages = (ArrayList<String>)controller.target.messages.clone();
            currentMessageColors = (ArrayList<Color>)controller.target.messageColors.clone();
            controller.target.messages.clear();
            controller.target.messageColors.clear();
        }

        int startY = panel.getHeightInCharacters() - currentMessages.size();
        for (int i = 0; i < currentMessages.size(); i++){
            panel.writeCenter(" " + currentMessages.get(i) + " ", startY+i, currentMessageColors.get(i));
        }
    }

    private void scoreboard(){
        int panelWidth = 15;
        int top = 1;
        int left = panel.getWidthInCharacters() - panelWidth - 1;

        String hours = "" + ((720 - world.ticks / world.ticksPerMinute) / 60);
        String minutes = "" + ((720 - world.ticks / world.ticksPerMinute) % 60);
        
        if (minutes.length() == 0)
            minutes = "00";
        else if (minutes.length() == 1)
            minutes = "0" + minutes;


        panel.clear(' ', left, top, panelWidth, 4);
        panel.write(" " + hours + ":" + minutes + " left", left, top+0, AsciiPanel.brightWhite);
        panel.write("    heroes: " + world.heroCount, left, top+1, AsciiPanel.green);
        panel.write(" villagers: " + world.villagerCount, left, top+2, AsciiPanel.white);
        panel.write("    undead: " + world.undeadCount, left, top+3, AsciiPanel.red);
    }

    private void infoPanel(Creature creature, int left){
        int panelWidth = 20;

        if (left < 0)
            left = panel.getWidthInCharacters() - panelWidth + left;

        String weaponName = creature.weapon != null ? " " + creature.weapon.name : "";
        String armorName = creature.armor != null ? " " + creature.armor.name : "";

        panel.clear(' ',left, 1, panelWidth, 5);
        panel.write(" " + creature.getName(), left, 1);
        panel.write("  hp:" + creature.hp + "/" + creature.maxHp, left, 3);
        panel.write(" atk:" + creature.attack + weaponName, left, 4);
        panel.write(" def:" + creature.defence + armorName, left, 5);
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

        panel.clear(' ', left, 1, panelWidth, 5);
        panel.write(" " + item.name, left, 1);
        
        panel.write("  hp:" + item.modHp, left, 3);
        panel.write(diffHpStr, diffHp > 0 ? AsciiPanel.green : AsciiPanel.red);

        panel.write(" atk:" + item.modAttack, left, 4);
        panel.write(diffAtkStr, diffAtk > 0 ? AsciiPanel.green : AsciiPanel.red);

        panel.write(" def:" + item.modDefence, left, 5);
        panel.write(diffDefStr, diffDef > 0 ? AsciiPanel.green : AsciiPanel.red);
    }
}
