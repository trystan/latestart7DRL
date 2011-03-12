
public class VillagerController extends NonPlayerController {

    public VillagerController(Creature c, PathFinder pf){
        super(c,pf);
    }

    @Override
    public void update(){
        if (rand.nextDouble() < 0.005){
            if (target.likesIndoors)
                target.tellNearby("Stay inside where it's safer.");
            else
                target.tellNearby("Come on! defend the village!");
        }

        super.update();
    }

    @Override
    public void onTakeDamage(Creature other, int amount){
        double r = rand.nextDouble();

        if (r < 0.125)
            target.tellNearby("Help! A " + other.personalTitle + "!");
        else if (r < 0.125)
            target.tellNearby(other.getName() + "! Ahh!");
    }
}
