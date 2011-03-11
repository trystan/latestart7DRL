
import java.util.HashMap;

public class HeroController extends NonPlayerController {

    protected HashMap<String, Integer> lastSeenNames;

    public HeroController(Creature c, PathFinder pf) {
        super(c, pf);
        lastSeenNames = new HashMap<String,Integer>();
    }

    @Override
    public void see(Creature other) {
        if (!other.isHero())
            return;
        
        if (!lastSeenNames.containsKey(other.name)) {
            greet(other);
        } else if (lastSeenNames.get(other.name) < target.age - 100) {
            regreet(other);
        }

        lastSeenNames.put(other.name, target.age);
    }

    public void greet(Creature other){
        target.tell(other, "Hi " + other.name + "!");
    }

    public void regreet(Creature other){
        target.tell(other, "Hey " + other.name + "! Good to see you again.");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("I'm hurt! I need help!");
    }
}
