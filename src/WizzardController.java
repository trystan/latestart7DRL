
public class WizzardController extends HeroController {

    public WizzardController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void see(Creature other){
        super.see(other);

        if (!isAlly(other) && rand.nextDouble() < 0.1)
            hurtOther(other);
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
    public void onTakeDamage(Creature other, int amount){
        double r = rand.nextDouble();

        if (r < 0.1)
            target.tellNearby("Oof!");

        if (r < 0.25)
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
        if (rand.nextDouble() < 0.1)
            target.tellNearby("That's one less " + other.personalTitle + "....");
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
        target.doAction("waves his hands and teleports");
        do
        {
            target.x += rand.nextInt(20) + rand.nextInt(20) - 20;
            target.y += rand.nextInt(20) + rand.nextInt(20) - 20;
        } while (!target.canEnter(target.x, target.y));
    }

    private void hurtOther(Creature other){
        target.doAction("points at " + other.getName() + " and mumbles");
        
        other.takeDamage(rand.nextInt(11) + rand.nextInt(11) + rand.nextInt(11));
    }
}