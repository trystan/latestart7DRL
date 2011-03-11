
public class WizzardController extends HeroController {

    public WizzardController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other) {
        target.tell(other, "Greetings " + other.name + "!");
    }

    @Override
    public void regreet(Creature other) {
        
    }

    @Override
    public void onLowHealth(){
        if (Math.random() < 0.5)
            target.tellNearby("I am in need of healing!");
        else
            target.tellNearby("I am gravely wounded....");
    }

    @Override
    public void onDied(){
        if (Math.random() < 0.5)
            target.tellNearby("Arhhh!");
        else
            target.tellNearby("I have failed....");
    }

    public void onInflictDamage(Creature other, int damage){
        if (Math.random() < 0.01)
            target.tellNearby("Ha!");
    }
}
