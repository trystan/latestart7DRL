
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

    public boolean isSlow;
    public boolean canWalkThroughWalls;
    public boolean canHeal;
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
        vision = 11;
        healCountdown = 20;
        canSpeak = false;

        canHeal = isHuman();
        
        messages = new ArrayList<String>();
        messageColors = new ArrayList<Color>();
    }

    public void becomeZombie(){
        world.tellAll(color, name + " has risen as a zombie!");
        
        name = "zobmie " + name;
        glyph = 'z';
        // keep the same color so you know who it was
        maxHp = (int)(maxHp * 0.8);
        hp = maxHp;
        isSlow = true;
        canSpeak = false;
    }

    public boolean isZombie(){
        return glyph == 'z' || glyph == 'Z';
    }

    public boolean isHuman(){
        return glyph == '@';
    }
    
    public boolean isHero(){
        return isHuman() && color != AsciiPanel.brightBlack;
    }

    public boolean isCommoner(){
        return isHuman() && color == AsciiPanel.brightBlack;
    }

    public void update(){
        age++;

        if (isZombie() && Math.random() < 0.01)
            tellNearby("brains....");

        if (canHeal && --healCountdown < 1){
            healCountdown = 20;
            if (hp < maxHp)
                hp++;
        }
        
        controller.update();
    }
    
    public boolean canEnter(int tx, int ty){
        if (tx < 0 || tx >= world.width
         || ty < 0 || ty >= world.height)
            return false;

        if (world.isImpassable(world.tiles[tx][ty], canWalkThroughWalls))
            return false;

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

    public boolean canBeAt(int tx, int ty){
        if (tx < 0 || tx >= world.width
         || ty < 0 || ty >= world.height)
            return false;

        if (world.isImpassable(world.tiles[tx][ty], canWalkThroughWalls))
            return false;

        return true;
    }

    public boolean canMoveBy(int mx, int my) {
        if (x+mx < 0 || x+mx >= world.width
         || y+my < 0 || y+my >= world.height)
            return false;
        
        if (world.isImpassable(world.tiles[x+mx][y+my], canWalkThroughWalls))
            return false;

        return true;
    }


    public boolean moveBy(int mx, int my) {
        return moveBy(mx, my, false);
    }
    
    private int openDoorX;
    private int openDoorY;
    private boolean needToCloseDoor;
    public boolean moveBy(int mx, int my, boolean isFlying) {
        target = null;
        for (Creature other : world.creatures){
            if (other != this && other.x == x+mx && other.y == y+my) {
                if (isFlying){
                    takeDamage(5);
                    other.takeDamage(2);
                    return false;
                } else if (other.glyph == glyph) {
                    swapPlaces(other);
                    return false;
                } else {
                    attack(other);
                    return false;
                }
            }
        }
        
        if (canMoveBy(mx,my)) {
            if (world.tiles[x+mx][y+my] == World.closedDoor) {
                world.tiles[x+mx][y+my] = World.openDoor;
                openDoorX = x+mx;
                openDoorY = y+my;
                needToCloseDoor = true;
            } else {
                x += mx;
                y += my;

                if (!needToCloseDoor && world.tiles[x][y] == World.openDoor){
                    openDoorX = x;
                    openDoorY = y;
                    needToCloseDoor = true;
                } else if (needToCloseDoor && isHero()
                    && world.tiles[openDoorX][openDoorY] == World.openDoor
                    && distanceTo(openDoorX, openDoorY) == 2) {
                    world.tiles[openDoorX][openDoorY] = world.closedDoor;
                    x -= mx;
                    y -= my;
                    needToCloseDoor = false;
                }
            }
            return true;
        }
        return false;
    }

    public void swapPlaces(Creature other){
        int tempx = other.x;
        int tempy = other.y;

        other.x = x;
        other.y = y;

        x = tempx;
        y = tempy;
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
        hp = 0;
        controller.onDied();
        unequip(armor);
        unequip(weapon);
    }

    public void takeDamage(int amount){
        if (hp > maxHp * 0.25 && hp-amount < maxHp * 0.25)
            controller.onLowHealth();
        else
            controller.onTakeDamage(amount);
        
        hp -= amount;

        if (hp < 1){
            die();
        }
    }
    
    public void attack(Creature other){
        int damage = Math.max(1, attack - other.defence);
        other.takeDamage(damage);

        if (other.target == null)
            other.target = this;
        
        if (isHero())
            controller.onInflictDamage(other, damage);

        if (weapon != null && other.hp > 0){
            weapon.attack(this, other);
        }

        target = other;

        if (other.hp > 0
                && other.weapon != null
                && other.weapon.doesDefensiveAttack
                && Math.random() < 0.5){
            other.attack(this);
            other.controller.onCounterAttacked(this);
        }

        if((other.isHero() || other.isCommoner()) && other.hp == 0)
            world.tellAll(other.color, other.name + " was killed by " + name);

        if (other.hp == 0 && other.isHuman() && isZombie())
            other.becomeZombie();
    }

    public void hear(Color color, String message) {
        messageColors.add(color);
        messages.add(message);
    }

    public void tell(Creature other, String message){
        if (canSpeak)
            other.hear(color, name + " says: " + message);
    }

    public void tellNearby(String message){
        if (!canSpeak)
            return;
        
        for (Creature other : world.creatures){

            if (distanceTo(other.x, other.y) > vision)
                continue;

            other.hear(color, name + " shouts: " + message);
        }
    }

    public int distanceTo(int ox, int oy){
        return Math.max(Math.abs(x-ox), Math.abs(y-oy));
    }
}
