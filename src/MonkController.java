
public class MonkController extends HeroController {

    int comboCounter;

    public MonkController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void update(){
        comboCounter = 1;
        super.update();
    }
    
    @Override
    public void greet(Creature other){
        if (rand.nextBoolean())
            target.tell(other, "Hello " + other.personalName + ".");
    }

    @Override
    public void regreet(Creature other){
        int percent = (int)(target.world.ticks / target.world.ticksPerMinute / 720);
        if (percent > 50)
            target.tellNearby("The night is young " + other.personalName + " so keep fighting.");
        else if (percent > 10)
            target.tellNearby("Keep fighting " + other.personalName + ". Don't give up!");
        else if (percent <= 10)
            target.tellNearby("The night almost over! We shall prevail " + other.personalName + ".");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.1)
            target.tellNearby(other.personalTitle + "! Over here!");
        
        if (target.hp < target.maxHp / 2
                && rand.nextDouble() < 0.1)
            healSelf();
    }

    @Override
    public void onLowHealth(){
        if (rand.nextDouble() < 0.5) {
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
        double r = rand.nextDouble();

        if (r < 0.01)
            target.tellNearby("Judo chop!");
        else if (r < 0.05)
            target.tellNearby("Takedown!");
        else if (r < 0.10)
            target.tellNearby("Hiyah!");
        else if (r < 0.15)
            target.tellNearby("Iron Fist!");
        else if (r < 0.20)
            target.tellNearby("Iron Foot!");
        else if (r < 0.25)
            target.tellNearby("Flying armbar!");
        else if (r > 0.66)
            quickAttack(other);
    }

    @Override
    public void onKilled(Creature other){
        if (rand.nextDouble() < 0.33)
            target.tellNearby("Victory!");
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
            case 1: target.tellNearby("One knuckle sandwich comming up!"); break;
            case 2: target.tellNearby("Is this night over yet?"); break;
            case 3: target.doAction("looks around"); break;
        }
    }


    private void healSelf(){
        target.doAction("draws on inner power");

        target.healDamage(5 + rand.nextInt(11));
    }

    private void quickAttack(Creature other){
        switch (++comboCounter){
            case 2: target.doAction("double hits"); break;
            case 3: target.doAction("triple hits"); break;
            case 4: target.doAction("quadruple hits"); break;
            default: target.doAction("x" + comboCounter+ " hits"); break;
        }
        
        target.attack(other);
    }
}