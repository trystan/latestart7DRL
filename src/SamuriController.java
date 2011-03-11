
public class SamuriController extends HeroController {

    public SamuriController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void update() {
        if (rand.nextDouble() < 0.25) {
            int nearby = 0;
            for (Creature other : target.world.creatures){
                if (isAlly(other) || target.distanceTo(other.x, other.y) != 1)
                    continue;

                nearby++;
            }
            
            if (nearby > 1)
                circularAttack();
            else if (rand.nextDouble() < 0.01)
                meditate();
        }

        super.update();
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



    private void meditate(){
        target.doAction("meditates");

        target.attack++;
        target.defence++;
    }

    private void circularAttack(){
        target.doAction("does a spinning attack");
        
        for (Creature other : target.world.creatures){
            if (isAlly(other) || target.distanceTo(other.x, other.y) != 1)
                continue;

            target.attack(other);
        }
    }

    private void commitSeppiku(){
        target.doAction("brandishes a tanto");
        target.hp = 0;
        target.world.tellAll(target.color, target.getName() + " has committed seppiku");
    }
}