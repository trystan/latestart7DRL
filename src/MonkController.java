
public class MonkController extends HeroController {

    public MonkController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        target.doAction("bows to " + other.personalName);
        target.tell(other, "Hello.");
    }

    @Override
    public void regreet(Creature other){
        target.tellNearby("Glad you're still not a zombie " + other.personalName + ".");
    }

    @Override
    public void onTakeDamage(int amount){
        if (target.hp < target.maxHp
                && rand.nextDouble() < 0.02)
            healSelf();
    }

    @Override
    public void onLowHealth(){
        if (Math.random() < 0.5) {
            target.tellNearby("Monk, Heal Thyself!");
            healSelf();
        } else {
            target.tellNearby("I need some healing!");
        }
    }

    @Override
    public void onDied(){
        target.tellNearby("Farewell earthly body!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        double r = Math.random();

        if (r < 0.01)
            target.tellNearby("Judo chop!");
        else if (r < 0.02)
            target.tellNearby("Takedown!");
        else if (r < 0.03)
            target.tellNearby("Hi-yeah!");
        else if (r < 0.04)
            target.tellNearby("Iron Fist!");
        else if (r < 0.05)
            target.tellNearby("Iron Foot!");
        else if (r < 0.06)
            target.tellNearby("Flying armbar!");
        else if (rand.nextDouble() < 0.125)
            target.attack(other); // Quick hands = free attack
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


    private void healSelf(){
        target.doAction("concentrates");

        target.hp += 5 + rand.nextInt(6);

        if (target.hp > target.maxHp)
            target.maxHp = target.hp;
    }
}