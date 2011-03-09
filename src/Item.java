
import java.awt.Color;


public class Item {
    public int x;
    public int y;
    public String name;
    public char glyph;
    public Color color;

    public boolean equipped;
    
    public int modHp;
    public int modAttack;
    public int modDefence;

    public Item(int ix, int iy, String n, char g, Color c) {
        x = ix;
        y = iy;
        name = n;
        glyph = g;
        color = c;
    }

    public void attack(Creature user, Creature target){
        
    }
}
