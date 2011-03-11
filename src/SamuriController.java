
public class SamuriController extends HeroController {

    public SamuriController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        target.tell(other, other.personalName);
    }

    @Override
    public void regreet(Creature other){
        
    }

    @Override
    public void onTakeDamage(int amount){
        
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Priest! Over here!");
    }

    @Override
    public void onDied(){
        target.tellNearby("Arghhhh!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if (rand.nextDouble() < 0.33)
            target.tellNearby("yells", "hiyah");
    }

    @Override
    public void onKilled(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby( "For the villagers!");
        else if(rand.nextDouble() < 0.33)
            target.tellNearby( "Die " + other.personalTitle + ".");
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
    public boolean beforeBittenByZombie(){
        commitSeppiku();
        return false;
    }


    private void commitSeppiku(){
        target.doAction("brandishes a tanto");
        target.hp = 0;
        target.world.tellAll(target.color, target.getName() + " has committed seppiku");
    }
}