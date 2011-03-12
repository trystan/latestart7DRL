
public class SlayerController extends HeroController {

    public SlayerController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
    }

    @Override
    public void regreet(Creature other){
        int tile = target.world.tiles[other.x][other.y];

        if (tile == World.insideFloor)
            target.tell(other, "You're safe from vampires as long as you stay indoors.");
        else
            target.tell(other, "Let's go " + other.personalName + ".");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
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
    }

    @Override
    public void onKilled(Creature other){
        if (other.personalTitle.equals("vampire"))
            target.tellNearby("Another one bites the dust.");
        else {
            switch (rand.nextInt(10)){
                case 0: target.tellNearby("Take that!"); break;
                case 1: target.tellNearby("That's gotta hurt!"); break;
                case 2: target.tellNearby("Ha!"); break;
            }
        }
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