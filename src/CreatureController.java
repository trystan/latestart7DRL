
import java.util.Random;

public class CreatureController {

    public Creature target;
    protected Random rand;

    public CreatureController(Creature c){
        target = c;
        rand = new Random();
    }

    public void update(){
        
    }
    
    public void see(Creature other){

    }

    public void onLowHealth(){

    }

    public void onDied(){

    }

    public void onInflictDamage(Creature other, int damage){

    }

    public boolean isAlly(Creature other){
        return (other.glyph == '@' && target.glyph == '@')
            || (other.glyph != '@' && target.glyph != '@');
    }
}
