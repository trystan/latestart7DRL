
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

    public boolean doesCritical;
    public boolean doesDecapitate;
    public boolean doesDoubleAttack;
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
        if (doesCritical
                && Math.random() < 0.1){
            target.hp -= 5;
            target.maxHp -= 5;
            user.hear(user.color, user.name + " critically damaged " + target.name);
        } else if (doesDecapitate 
                && target.hp <= user.attack - target.defence
                && Math.random() < 0.5){
            target.hp = 0;

            if (user.glyph == '@')
                user.world.tellAll(user.color, user.name + " decapitated " + target.name);
            else
                user.hear(user.color, user.name + " decapitated " + target.name);
        } else if (doesDoubleAttack
                && target.hp > 0
                && Math.random() < 0.125){
            user.world.tellAll(user.color, user.name + " quickly hit " + target.name);
            user.attack(target);
        }
    }
}
