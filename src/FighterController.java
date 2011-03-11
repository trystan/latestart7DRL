
public class FighterController extends HeroController {
    private int rageCounter;

    String priestName;
    
    public FighterController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        if (other.personalTitle.equals("priest"))
            priestName = other.getName();
        
        if (rand.nextBoolean())
            target.tell(other, "Hail " + other.personalTitle + "!");
    }

    @Override
    public void regreet(Creature other){
        target.tellNearby("Still alive " + other.personalName + "? Ha!");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.1)
            target.doAction("grunts");

        if (rageCounter == 0 && rand.nextDouble() < 0.05)
            startRage();
    }

    @Override
    public void onLowHealth(){
        if (priestName == null)
            target.tellNearby("Where's that priest? I need healin'.");
        else
            target.tellNearby("Where's " + priestName + "? I need healin'.");
    }

    @Override
    public void onDied(){
        target.tellNearby("groans","Arghhhhhh!");
    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if (rand.nextInt(50) < damage)
            target.tellNearby("Take that " + other.personalTitle + "!");
        else if (rand.nextDouble() < 0.05)
            target.tellNearby("Follow me!");
    }

    @Override
    public void onKilled(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Anyone else havin' as much fun as I am?");
        else if (rand.nextDouble() < 0.5)
            target.doAction("grins widely");
        else if (rageCounter == 0 && rand.nextDouble() < 0.25)
            startRage();
    }

    @Override
    public void onDecapitated(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Off with your head!");
    }

    @Override
    public void onCounterAttacked(Creature other){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Stupid " + other.personalTitle + " ran into my " + target.weapon.name + "!");
    }

    @Override
    public void onKnockback(int distance){
        if (rand.nextDouble() < 0.1)
            target.tellNearby("Ha! Look at em fly!");
    }

    @Override
    public void onRandomShoutout(){
        switch (rand.nextInt(4)){
            case 0: target.tellNearby("Bring it on!"); break;
            case 1: target.tellNearby("Don't make me mad!"); break;
            case 2: target.tellNearby("Let's smash some skulls!"); break;
            case 3: target.tellNearby("Come on!"); break;
        }
    }


    

    public void startRage(){
        rageCounter = 20 + rand.nextInt(20);
        
        target.tellAll(target.color, target.getName() + " has gone berserk!");
        target.doAction("looks crazy");
        target.maxHp += 15;
        target.healDamage(15);
        target.attack += 15;
        target.defence += 15;
    }
    
    public void rage(){
        switch (rand.nextInt(20)){
            case 0: target.tellNearby("IMMA KILL ALL DEADZ!"); break;
            case 1: target.tellNearby("KILL KILL KILL!"); break;
            case 2: target.tellNearby("DIE!"); break;
            case 3: target.tellNearby("UNSTOPABLE!"); break;
            case 4: target.tellNearby("RAHHH!"); break;
        }
        if (--rageCounter < 1)
            endRage();
    }

    public void endRage(){
        rageCounter = 0;
        target.tellAll(target.color, target.getName() + " has tired.");
        target.doAction("looks tired");
        target.maxHp -= 15;
        target.takeDamage(15);
        target.attack -= 15;
        target.defence -= 15;
    }
}