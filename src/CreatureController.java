
import java.util.Random;


public class CreatureController {

    public Creature target;
    private Random rand;
    private int walkCountdown;

    public CreatureController(Creature t){
        target = t;
        rand = new Random();
        walkCountdown = rand.nextInt(5);
    }

    public void update(){
        if (walkCountdown-- > 0)
            return;
        
        switch (rand.nextInt(9)){
            case 0: target.moveBy(-1, -1); break;
            case 1: target.moveBy(-1,  0); break;
            case 2: target.moveBy(-1,  1); break;
            case 3: target.moveBy( 0, -1); break;
            case 4: target.moveBy( 0,  0); break;
            case 5: target.moveBy( 0,  1); break;
            case 6: target.moveBy( 1, -1); break;
            case 7: target.moveBy( 1,  0); break;
            case 8: target.moveBy( 1,  1); break;
        }
        walkCountdown = 3;
    }
}
