
public class SlayerController extends HeroController {

    public SlayerController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
    }

    @Override
    public void regreet(Creature other){
        target.tell(other, "Let's go " + other.personalName + ".");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Oof!");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Heal me priest!");
    }

    @Override
    public void onDied(){
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        switch (rand.nextInt(10)){
            case 0: target.tellNearby("Take that!"); break;
            case 1: target.tellNearby("That's gotta hurt!"); break;
            case 2: target.tellNearby("Ha!"); break;
        }
    }

    @Override
    public void onKilled(Creature other){
        if (other.personalTitle.equals("vampire"))
            target.tellNearby("Another one bites the dust.");
    }

    @Override
    public void onDecapitated(Creature other){
    }

    @Override
    public void onCounterAttacked(Creature other){
    }

    @Override
    public void onKnockback(int distance){
    }
}