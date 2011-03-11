
public class PriestController extends HeroController {

    private int visibleUndead;

    public PriestController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void update(){
        if (visibleUndead > rand.nextInt(100))
            turnUndead();
        
        visibleUndead = 0;
        super.update();
    }
    
    @Override
    public void see(Creature other){
        super.see(other);

        if (isAlly(other)
                && other.hp < other.maxHp
                && rand.nextDouble() < 0.05)
            healOther(other);
        else if (!other.isHuman())
            visibleUndead++;
    }

    @Override
    public void greet(Creature other){
        target.tell(other, "How can I help you " + other.personalName + "?");
    }

    @Override
    public void regreet(Creature other){
        target.tell(other, "How can I help you " + other.personalName + "?");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        
    }

    @Override
    public void onLowHealth(){

    }

    @Override
    public void onDied(){
        target.tellNearby("Carry on without me!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if(rand.nextDouble() < 0.50)
            target.tellNearby( "Wretched " + other.personalTitle + ".");
    }

    @Override
    public void onKilled(Creature other){
        if(rand.nextDouble() < 0.50)
            target.tellNearby( "Die vile " + other.personalTitle + ".");
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



    private void turnUndead(){
        target.doAction("performs a priestly ritual");

        for (Creature other : target.world.creatures){
            if (!other.isHuman() && target.distanceTo(other.x, other.y) <= target.vision){
                other.doAction("looks pained");
                other.takeDamage(10);
            }
        }
    }
    
    private void healOther(Creature other){
        target.doAction("points at " + other.personalName + " and prays");

        other.hp += 5 + rand.nextInt(6);

        if (other.hp > other.maxHp)
            other.maxHp = other.hp;
    }
}