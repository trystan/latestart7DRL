package latestart7drl;

import java.awt.Color;

public class Creature {
    public int x;
    public int y;
    public char glyph;
    public Color color;

    private World world;

    public Creature(World w, int cx, int cy, char g, Color c) {
        world = w;
        x = cx;
        y = cy;
        glyph = g;
        color = c;
    }

    public boolean canMoveBy(int mx, int my) {
        if (x+mx < 0 || x+mx >= world.width
         || y+my < 0 || y+my >= world.height)
            return false;
        
        switch (world.tiles[x+mx][y+my]) {
            case World.dirtWall:
            case World.rockWall:
            case World.water: return false;
            default: return true;
        }
    }
    
    public void moveBy(int mx, int my) {
        if (canMoveBy(mx,my)) {
            x += mx;
            y += my;
        }
    }
}
