
public class FighterController extends HeroController {

    public FighterController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        target.tell(other, "Hail " + other.personalTitle + "!");
    }

    @Override
    public void regreet(Creature other){
        target.tellNearby("Still alive " + other.personalName + "? Ha!");
    }

    @Override
    public void onTakeDamage(int amount){
        if (rand.nextDouble() < 0.05)
            target.doAction("grunts");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Where's that priest? I need healin'.");
    }

    @Override
    public void onDied(){
        target.tellNearby("groans","Arghhhhhh!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if (rand.nextInt(50) < damage)
            target.tellNearby("Take that " + other.personalTitle + "!");
    }

    @Override
    public void onKilled(Creature other){
        if (rand.nextDouble() < 0.01)
            target.tellNearby("Anyone else havin' as much fun as I am?");
        else if (rand.nextDouble() < 0.33)
            target.doAction("grins widely");
    }

    @Override
    public void onDecapitated(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Off with your head!");
    }

    @Override
    public void onCounterAttacked(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Stupid " + other.personalTitle + " ran into my " + target.weapon.name + "!");
    }

    @Override
    public void onKnockback(int distance){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Ha! Look at em fly!");
    }
}