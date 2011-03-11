
import java.awt.Color;


public class Item {
    public int x;
    public int y;
    public String name;
    public char glyph;
    public Color color;
    public String details;
    
    public boolean equipped;
    
    public int modHp;
    public int modAttack;
    public int modDefence;

    public boolean doesKnockback;
    public boolean doesDecapitate;
    public boolean doesDefensiveAttack;

    public Item(int ix, int iy, String n, char g, Color c, String d) {
        x = ix;
        y = iy;
        name = n;
        glyph = g;
        color = c;
        details = d;
    }

    public void attack(Creature user, Creature target){
        if (doesKnockback && Math.random() < 0.5){
            int dx = Math.max(-1, Math.min(target.x - user.x, 1));
            int dy = Math.max(-1, Math.min(target.y - user.y, 1));

            int dist = 2 + (int)(Math.random() * 6);
            int realDist = 0;

            while (--dist > 0 && target.moveBy(dx, dy, true)){
                realDist++;
            }
            user.controller.onKnockback(realDist);

        } else if (doesDecapitate
                && target.hp <= user.attack - target.defence){
            target.hp = 0;
            user.controller.onDecapitated(target);
        }
    }
}
