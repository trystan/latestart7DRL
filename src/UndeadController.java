
public class UndeadController extends NonPlayerController {

    CreatureFactory factory;

    public UndeadController(Creature c, PathFinder pf, CreatureFactory cf){
        super(c,pf);

        factory = cf;
    }

    @Override
    public void see(Creature other){
        if (isAlly(other))
            return;
        
        if (target.personalTitle == "lich" && rand.nextDouble() < 0.05)
            lichMagic(other);
    }
    
    @Override
    public void onLowHealth(){

    }

    @Override
    public void onDied(){

    }

    @Override
    public void onInflictDamage(Creature other, int damage){
        if (rand.nextDouble() < 0.1)
            target.tell(other, "Die human!");
    }

    @Override
    public void onKilled(Creature other){
        if (other.isHero())
            target.tellNearby(other.getName() + " has fallen!");
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




    private void lichMagic(Creature other){
        switch (rand.nextInt(3)){
            case 0:
                target.doAction("points at " + other.getName());
                other.doAction("screams in pain");
                other.hear(AsciiPanel.white, "You feel icy cold.");
                other.takeDamage(rand.nextInt(5) + rand.nextInt(5));
                break;
            case 1:
                target.doAction("says an ancient chant");

                for (Creature c : target.world.creatures){
                    if (c.distanceTo(target.x, target.y) >= c.vision)
                        continue;

                    c.hear(AsciiPanel.white, "You feel ill.");
                    c.takeDamage(rand.nextInt(5) + rand.nextInt(5));
                }
                break;
            case 2:
                target.doAction("strikes the ground with his staff");

                for (int ox = - 1; ox < 2; ox++){
                    for (int oy = - 1; oy < 2; oy++){
                        if (ox==0 && oy==0)
                            continue;

                        for (Creature c : target.world.creatures){
                            if (c.x == target.x+ox && c.y == target.y+oy)
                                continue;
                        }

                        Creature summoned = factory.Skeleton();
                        if (summoned.canBeAt(target.x+ox, target.y+oy)){
                            summoned.x = target.x+ox;
                            summoned.y = target.y+oy;
                            target.summon(summoned);
                        }
                    }
                }
                
                break;
        }
    }
}
