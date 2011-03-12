
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
        if (rand.nextBoolean())
            target.tell(other, "Greetings " + other.personalName + "....");
    }

    @Override
    public void regreet(Creature other){
        int tile = target.world.tiles[other.x][other.y];

        if (tile == World.insideFloor)
            target.tell(other, "I see you've decided to play it safe in there....");
        else
            target.doAction("examines " + other.getName());
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
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
        if (rand.nextDouble() < 0.5)
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
    
    @Override
    public void onRandomShoutout(){
        switch (rand.nextInt(3)){
            case 0: target.doAction("looks annoyed"); break;
            case 1: target.doAction("looks unhappy"); break;
            case 2: target.doAction("looks grumpy"); break;
        }
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
        
        target.attack(other, rand.nextInt(11) + rand.nextInt(11) + rand.nextInt(11));
    }
}