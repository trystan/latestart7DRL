
import java.awt.Color;
import java.util.ArrayList;

public class Creature {
    public int x;
    public int y;
    public String name;
    public char glyph;
    public Color color;
    public CreatureController controller;
    public String details;
    public boolean canSpeak;
    public int age;
    
    public World world;

    public Creature target;

    public int healCountdown;

    public Item weapon;
    public Item armor;

    public int maxHp;
    public int hp;
    public int attack;
    public int defence;
    public int vision;

    public ArrayList<String> messages;
    public ArrayList<Color> messageColors;

    public Creature(World w, int cx, int cy, String n, char g, Color c, String d) {
        world = w;
        x = cx;
        y = cy;
        name = n;
        glyph = g;
        color = c;
        details = d;

        maxHp = 60;
        hp = maxHp;
        attack = 10;
        defence = 5;
        vision = 9;
        healCountdown = 20;
        canSpeak = false;
        
        messages = new ArrayList<String>();
        messageColors = new ArrayList<Color>();
    }

    public void update(){
        age++;
        
        if (--healCountdown < 1){
            healCountdown = 20;
            if (hp < maxHp)
                hp++;
        }
        
        if (controller != null)
            controller.update();
    }
    
    public boolean canEnter(int tx, int ty){
        if (tx < 0 || tx >= world.width
         || ty < 0 || ty >= world.height)
            return false;

        switch (world.tiles[tx][ty]) {
            case World.dirtWall:
            case World.rockWall:
            case World.water: return false;
        }

        for (Creature other : world.creatures){
            if (other == this || other.x != tx || other.y != ty)
                continue;

            if (controller != null)
                return !controller.isAlly(other);
            else
                return glyph == other.glyph;
        }

        return true;
    }

    public boolean canMoveBy(int mx, int my) {
        if (x+mx < 0 || x+mx >= world.width
         || y+my < 0 || y+my >= world.height)
            return false;
        
        switch (world.tiles[x+mx][y+my]) {
            case World.dirtWall:
            case World.rockWall:
            case World.water: return false;
        }

        return true;
    }
    
    public void moveBy(int mx, int my) {
        for (Creature other : world.creatures){
            if (other != this && other.x == x+mx && other.y == y+my) {
                attack(other);
                return;
            }
        }
        
        if (canMoveBy(mx,my)) {
            x += mx;
            y += my;
        }

        target = null;
    }

    public void unequip(Item item){
        if (item == null){
            return;
        } else if (weapon == item){
            weapon = null;
            hear(AsciiPanel.white, "You drop your " + item.name);
        } else if (armor == item){
            armor = null;
            hear(AsciiPanel.white, "You take off your " + item.name);
        } else {
            return;
        }

        hp -= item.modHp;
        attack -= item.modAttack;
        defence -= item.modDefence;
        item.x = x;
        item.y = y;
        item.equipped = false;
    }
    
    public void equip(Item item){
        switch(item.glyph){
            case ')':
                unequip(weapon);
                weapon = item;
                hear(AsciiPanel.white, "You weild a " + item.name);
                break;
            case ']':
                unequip(armor);
                armor = item;
                hear(AsciiPanel.white, "You put on some " + item.name);
                break;
            default: return;
        }
        
        hp += item.modHp;
        attack += item.modAttack;
        defence += item.modDefence;
        item.equipped = true;
    }

    public void die(){
        unequip(armor);
        unequip(weapon);
    }

    public void attack(Creature other){
        other.hp -= Math.max(1, attack - other.defence);
        
        if (other.hp < 0)
            other.hp = 0;

        if (weapon != null && other.hp > 0){
            weapon.attack(this, other);
        }

        target = other;

        if (other.hp > 0 && other.controller != null)
            other.controller.onAttackedBy(this);
        
        if (other.hp > 0
                && other.weapon != null
                && other.weapon.doesDefensiveAttack
                && Math.random() < 0.5)
            other.attack(this);

        if (!canSpeak)
            return;

        if (glyph == '@' && other.hp < 1)
            world.tellAll(color, name + " killed " + other.name);
        else if(other.glyph == '@' && other.hp < 1)
            world.tellAll(other.color, other.name + " was killed by " + name);
    }

    public void hear(Color color, String message) {
        messageColors.add(color);
        messages.add(message);
    }

    public void tell(Creature other, String message){
        if (canSpeak)
            other.hear(color, name + ": " + message);
    }
}
