
public class SamuraiController extends HeroController {

    boolean alreadyAttacked;

    public SamuraiController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void update() {
        alreadyAttacked = false;

        if (rand.nextDouble() < 0.25) {
            int nearby = 0;
            for (Creature other : target.world.creatures){
                if (isAlly(other) || target.distanceTo(other.x, other.y) != 1)
                    continue;

                nearby++;
            }
            
            if (nearby > 1){
                circularAttack();
                alreadyAttacked = true;
            }
        }

        super.update();
    }

    @Override
    public void move(){
        if (!alreadyAttacked)
            super.move();
    }
    
    @Override
    public void greet(Creature other){
        if (rand.nextBoolean())
            target.tell(other, "Let's go " + other.personalName + ".");
    }

    @Override
    public void regreet(Creature other){
        int tile = target.world.tiles[other.x][other.y];

        if (tile == World.insideFloor)
            target.tell(other, "Why are you hiding in there " + other.personalName + "?");
        else
            target.tell(other, "Haven't been hiding, have you " + other.personalName + "?");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("I've got a " + other.personalTitle + " over here!");
        else if (other.weapon != null && rand.nextDouble() < 0.1)
            target.tellNearby("Careful! This " + other.personalTitle + " is using a " + other.weapon.name + ".");
        else if(other.armor != null && rand.nextDouble() < 0.1)
            target.tellNearby("Careful! This " + other.personalTitle + " is wearing " + other.armor.name + ".");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Today is a good day to die!");
    }

    @Override
    public void onDied(){
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if(other.armor != null && rand.nextDouble() < 0.1)
            target.tellNearby("Your " + other.armor.name + " is no match for my " + target.weapon.name + ".");
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

    @Override
    public void onRandomShoutout(){
        switch (rand.nextInt(4)){
            case 0: target.tellNearby("Protect the villagers!"); break;
            case 1: target.tellNearby("Spread out!"); break;
            case 2: target.tellNearby("Stay together!"); break;
            case 4: target.tellNearby("Over here!"); break;
            case 5: target.tellNearby("Who needs the aid of my " + target.weapon.name + "!"); break;
            case 6: target.doAction("is surveying the battlefield"); break;
        }
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
        target.die();
        target.tellAll(target.color, target.getName() + " has committed seppiku");
    }
}