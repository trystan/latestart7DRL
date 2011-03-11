
public class WizzardController extends HeroController {

    public WizzardController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        target.tell(other, "Greetings....");
    }

    @Override
    public void regreet(Creature other){
        target.tell(other, "We meet again....");
    }

    @Override
    public void onTakeDamage(int amount){
        if (rand.nextDouble() < 0.25)
            teleportSelf();
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Must... heal... soon....");
    }

    @Override
    public void onDied(){
        target.tellNearby("Arghhhh!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){

    }

    @Override
    public void onKilled(Creature other){
        if (Math.random() < 0.01)
            target.tell(other, "One less " + other.personalTitle + "....");
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


    private void teleportSelf(){
        target.doAction("waves his hands");
        do
        {
            target.x += rand.nextInt(20) + rand.nextInt(20) - 20;
            target.y += rand.nextInt(20) + rand.nextInt(20) - 20;
        } while (!target.canEnter(target.x, target.y));
    }
}