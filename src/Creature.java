
import java.awt.Color;
import java.util.ArrayList;

public class Creature {
    public int x;
    public int y;
    public int level;
    public int exp;
    public String personalName;
    public String personalTitle;
    public char glyph;
    public Color color;
    public CreatureController controller;
    public boolean canSpeak;
    public int age;

    public String getName(){
        if (personalName.isEmpty())
            return "a " + personalTitle;
        if (personalTitle.isEmpty())
            return personalName;
        else
            return personalName + " the " + personalTitle;
    }

    public World world;

    public Creature attacking;

    public boolean isSlow;
    public boolean canWalkThroughWalls;
    public int healthRate;
    public boolean canStealLife;

    public boolean canSwapWeapons;
    public boolean canSwapArmor;
    public boolean canBeDecapitated;
    public boolean canNotGoIndoors;
    public boolean hasBlood;
    public boolean canFly;

    public Item weapon;
    public Item armor;

    public int maxHp;
    public int hp;
    public int attack;
    public int defence;
    public int vision;

    public ArrayList<String> messages;
    public ArrayList<Color> messageColors;

    public Creature(World w, int cx, int cy, String n, String t, char g, Color c) {
        world = w;
        x = cx;
        y = cy;
        personalName = n;
        personalTitle = t;
        glyph = g;
        color = c;

        maxHp = 60;
        hp = maxHp;
        attack = 10;
        defence = 5;
        vision = 12;
        canSpeak = isHuman();
        canSwapWeapons = isHuman();
        canSwapArmor = isHuman();

        canBeDecapitated = true;
        healthRate = isHuman() ? 30 : 0;
        hasBlood = isHuman();
        
        messages = new ArrayList<String>();
        messages.add("Press '?' for help");
        messageColors = new ArrayList<Color>();
        messageColors.add(AsciiPanel.brightWhite);
    }

    public void becomeZombie(){
        tellAll(color, getName() + " has risen as a zombie!");
        
        personalTitle = "zobmie";
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

    public final boolean isHuman(){
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

        if (healthRate > 0 && age % healthRate == 0)
            healDamage(1);
        
        controller.update();

        if (exp > level * 10)
            gainLevel();
    }
    
    public boolean canEnter(int tx, int ty){
        if (!canBeAt(tx, ty))
            return false;

        for (Creature other : world.creatures){
            if (other == this || other.x != tx || other.y != ty)
                continue;

            return !controller.isAlly(other);
        }

        return true;
    }

    public boolean canBeAt(int tx, int ty){
        if (tx < 0 || tx >= world.width
         || ty < 0 || ty >= world.height)
            return false;

        if (world.isImpassable(world.tiles[tx][ty], canFly, canWalkThroughWalls, canNotGoIndoors))
            return false;

        return true;
    }

    public boolean canMoveBy(int mx, int my) {
        return canBeAt(x+mx, y+my);
    }


    public boolean moveBy(int mx, int my) {
        return moveBy(mx, my, false);
    }
    
    private int openDoorX;
    private int openDoorY;
    private boolean needToCloseDoor;
    public boolean moveBy(int mx, int my, boolean isFlying) {
        attacking = null;
        for (Creature other : world.creatures){
            if (other != this && other.x == x+mx && other.y == y+my) {
                if (isFlying){
                    takeDamage(5);
                    other.takeDamage(2);
                    return false;
                } else if (controller.isAlly(other)) {
                    swapPlaces(other);
                    return false;
                } else {
                    attack(other);
                    return false;
                }
            }
        }
        
        if (canMoveBy(mx,my)) {
            if (!canWalkThroughWalls
                && world.tiles[x+mx][y+my] == World.closedDoor) {
                world.tiles[x+mx][y+my] = World.openDoor;
                openDoorX = x+mx;
                openDoorY = y+my;
                needToCloseDoor = true;
            } else {
                x += mx;
                y += my;

                if (!canWalkThroughWalls 
                        && !needToCloseDoor
                        && world.tiles[x][y] == World.openDoor){
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
        if (other.isHero() && !isHero())
            return;

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

        maxHp -= item.modHp;
        if (hp > maxHp)
            hp = maxHp;
        attack -= item.modAttack;
        defence -= item.modDefence;
        drop(item);
    }

    private void drop(Item item){
        ArrayList<Point> candidates = new ArrayList<Point>();
        candidates.add(new Point(x,y));

        int tries = 0;
        while (candidates.size() > 0 && tries++ < 25){
            Point dest = candidates.remove(0);

            boolean occupied = false;
            for (Item other : world.items){
                if (other.x == dest.x && other.y == dest.y && !other.equipped) {
                    occupied = true;
                    break;
                }
            }

            if (!occupied && canBeAt(dest.x, dest.y)){
                item.x = dest.x;
                item.y = dest.y;
                item.equipped = false;
                break;
            } else {
                candidates.addAll(dest.getNeighbors(world));
            }
        }
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

        maxHp += item.modHp;
        attack += item.modAttack;
        defence += item.modDefence;
        item.equipped = true;
    }

    public void gainLevel(){
        exp -= level * 10;
        level++;
        maxHp += 1;
        attack += 1;
        defence += 1;

        healDamage(level);
        
        if (isHero())
            tellAll(color, getName() + " is now level " + level + "!");
    }

    public void die(){
        hp = 0;

        if (hasBlood)
            world.addGore(x, y);
        
        controller.onDied();
        unequip(armor);
        unequip(weapon);
    }

    public void healDamage(int amount){
        takeDamage(-amount);
    }
    
    public void takeDamage(int amount){
        if (hp > maxHp * 0.25 && hp-amount < maxHp * 0.25)
            controller.onLowHealth();
        
        hp -= amount;

        if (hasBlood && amount > 0)
            world.addGore(x, y);

        if (hp < 1){
            die();
        } else if (hp > maxHp)
            hp = maxHp;
    }

    public void attack(Creature other){
        attack(other, 0);
    }
    
    public void attack(Creature other, int magic){
        if (other.hp == 0)
            return;

        int damage = magic == 0 ? Math.max(1, attack - other.defence) : magic;
        other.takeDamage(damage);
        controller.onTakeDamage(other, damage);

        if (canStealLife){
            healDamage(damage / 3);
        }
        
        if (other.attacking == null)
            other.attacking = this;
        
        if (isHero())
            controller.onInflictDamage(other, damage);

        if (weapon != null && other.hp > 0){
            weapon.attack(this, other);
        }

        attacking = other;

        if (other.hp > 0
                && other.weapon != null
                && other.weapon.doesDefensiveAttack
                && Math.random() < 0.5
                && other.distanceTo(x, y) == 1){
            other.attack(this);
            other.controller.onCounterAttacked(this);
        }

        if (other.hp == 0)
            exp += level;

        if(other.isHuman() && other.hp == 0)
            tellAll(other.color, other.getName() + " was killed by " + getName());

        if (other.hp == 0 && other.isHuman() && isZombie() && other.controller.beforeBittenByZombie())
            other.becomeZombie();
    }

    public void hear(Color color, String message) {
        messageColors.add(color);
        messages.add(message);
    }

    public void tellAll(Color color, String message){
        for (Creature creature : world.creatures){
            creature.hear(color, message);
        }
    }

    public void tell(Creature other, String message){
        if (canSpeak)
            other.hear(color, getName() + " says: " + message);
    }

    public void tellNearby(String message){
        tellNearby("shouts", message);
    }
    
    public void tellNearby(String how, String message){
        if (!canSpeak)
            return;
        
        for (Creature other : world.creatures){

            if (distanceTo(other.x, other.y) > other.vision)
                continue;

            if (other == this)
                other.hear(color, "You " + how + ": " + message);
            else
                other.hear(color, getName() + " " + how + ": " + message);
        }
    }

    public void doAction(String message){
        for (Creature other : world.creatures){

            if (distanceTo(other.x, other.y) > other.vision)
                continue;

            other.hear(color, getName() + " " + message);
        }
    }

    public int distanceTo(int ox, int oy){
        return Math.max(Math.abs(x-ox), Math.abs(y-oy));
    }

    public void summon(Creature other){
        world.creaturesToAdd.add(other);
    }
}
