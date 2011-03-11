
public class VillagerController extends NonPlayerController {

    public VillagerController(Creature c, PathFinder pf){
        super(c,pf);
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        double r = rand.nextDouble();

        if (r < 0.125)
            target.tellNearby("Help! " + other.getName());
        else if (r < 0.125)
            target.tellNearby(other.getName() + "! Ahh!");
    }
}
