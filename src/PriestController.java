
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
                && rand.nextInt(other.maxHp) > other.hp)
            healOther(other);
        else if (!other.isHuman()) {
            visibleUndead++;
            
            if (other.weapon != null && rand.nextDouble() < 0.1)
                target.tellNearby("The " + other.personalTitle + " has a " + other.weapon.name + "!");
            else if(other.armor != null && rand.nextDouble() < 0.1)
                target.tellNearby("The " + other.personalTitle + " is wearing " + other.armor.name + "!");
        }
    }
    
    @Override
    public void greet(Creature other){
        if (rand.nextBoolean())
            target.tell(other, "How can I help you " + other.personalName + "?");
    }
    
    @Override
    public void regreet(Creature other){
        target.tell(other, "Protect the village " + other.personalName + ".");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("I could use some help with this " + other.personalTitle + ".");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("yeals", "A little help here?");
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

    @Override
    public void onRandomShoutout(){
        switch (rand.nextInt(4)){
            case 0:
                if (target.world.villagerCount > 0)
                    target.tellNearby("Protect the villagers!");
                else
                    target.tellNearby("Protect the village!");
                break;
            case 1: target.tellNearby("Stay together now!"); break;
            case 2: target.tellNearby("Who needs healing?"); break;
            case 3:
                if (target.world.villagerCount > 0)
                    target.doAction("prays for the villagers");
                else
                    target.doAction("prays for the village");
                break;
        }
    }


    private void turnUndead(){
        target.doAction("performs a priestly ritual");

        for (Creature other : target.world.creatures){
            if (!other.isHuman() && target.distanceTo(other.x, other.y) <= target.vision){
                target.attack(other, 10);
            }
        }
    }
    
    private void healOther(Creature other){
        target.doAction("points at " + other.personalName + " and prays");

        target.healDamage(5 + rand.nextInt(6));
    }
}