
import java.util.HashMap;

public class HeroController extends NonPlayerController {

    protected HashMap<String, Integer> lastSeenNames;

    public HeroController(Creature c, PathFinder pf) {
        super(c, pf);
        lastSeenNames = new HashMap<String,Integer>();
    }

    @Override
    public void update(){
        if (rand.nextDouble() < 0.01)
            onRandomShoutout();

        super.update();
    }

    @Override
    public void see(Creature other) {
        if (!other.isHero())
            return;
        
        if (!lastSeenNames.containsKey(other.getName())) {
            greet(other);
        } else if (lastSeenNames.get(other.getName()) < target.age - 100) {
            regreet(other);
        }

        lastSeenNames.put(other.getName(), target.age);
    }

    public void greet(Creature other){
        
    }

    public void regreet(Creature other){
        
    }

    public void onRandomShoutout(){

    }
}
