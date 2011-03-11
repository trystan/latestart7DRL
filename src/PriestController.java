
public class PriestController extends HeroController {

    public PriestController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void see(Creature other){
        super.see(other);

        if (isAlly(other)
                && other.hp < other.maxHp
                && rand.nextDouble() < 0.05)
            healOther(other);
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
    public void onTakeDamage(int amount){

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

    }

    @Override
    public void onKilled(Creature other){
        
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



    private void healOther(Creature other){
        target.doAction(" points at " + other.personalName + " and prays.");

        other.hp += 5 + rand.nextInt(6);

        if (other.hp > other.maxHp)
            other.maxHp = other.hp;
    }
}