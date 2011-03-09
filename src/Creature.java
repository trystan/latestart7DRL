
import java.awt.Color;

public class Creature {
    public int x;
    public int y;
    public String name;
    public char glyph;
    public Color color;
    public CreatureController controller;

    public World world;


    public int hp;
    public int attack;
    public int defence;

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
    }

    public void attack(Creature other){
        other.hp -= Math.max(1, attack - other.defence);
    }
}
