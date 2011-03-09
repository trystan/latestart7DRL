
import java.awt.Color;
import java.util.ArrayList;

public class Creature {
    public int x;
    public int y;
    public String name;
    public char glyph;
    public Color color;
    public CreatureController controller;

    public World world;

    public Creature target;

    public int hp;
    public int attack;
    public int defence;
    public int vision;

    public ArrayList<String> messages;
    public ArrayList<Color> messageColors;

    public Creature(World w, int cx, int cy, String n, char g, Color c) {
        world = w;
        x = cx;
        y = cy;
        name = n;
        glyph = g;
        color = c;

        hp = 60;
        attack = 10;
        defence = 5;
        vision = 9;

        messages = new ArrayList<String>();
        messageColors = new ArrayList<Color>();
    }

    public void update(){
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

            return other.glyph != glyph;
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

    public void attack(Creature other){
        other.hp -= Math.max(1, attack - other.defence);

        target = other;

        if (glyph == '@' && other.hp < 1)
            world.tellAll(color, name + " killed " + other.name);
        else if(other.glyph == '@' && other.hp < 1)
            world.tellAll(other.color, other.name + " was killed by " + name);
    }

    void tell(Color color, String message) {
        messageColors.add(color);
        messages.add(message);
    }
}
