
public class FighterController extends HeroController {
    private int rageCounter;

    public FighterController(Creature c, PathFinder pf) {
        super(c, pf);
    }

    @Override
    public void greet(Creature other){
        target.tell(other, "Hail " + other.personalTitle + "!");
    }

    @Override
    public void regreet(Creature other){
        target.tellNearby("Still alive " + other.personalName + "? Ha!");
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        if (rand.nextDouble() < 0.05)
            target.doAction("grunts");
    }

    @Override
    public void onLowHealth(){
        target.tellNearby("Where's that priest? I need healin'.");
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
        else if (rageCounter == 0 && rand.nextDouble() < 0.1)
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


    

    public void startRage(){
        rageCounter = 20 + rand.nextInt(20);
        
        target.tellNearby("RAHHHHH!!!");
        target.doAction("looks crazy");
        target.maxHp += 15;
        target.hp += 15;
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
        target.doAction("looks tired");
        target.maxHp -= 15;
        target.hp -= 15;
        target.attack -= 15;
        target.defence -= 15;
    }
}