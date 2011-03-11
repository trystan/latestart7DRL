
public class UndeadController extends NonPlayerController {

    public UndeadController(Creature c, PathFinder pf){
        super(c,pf);
    }

    @Override
    public void update(){
       if (target.isZombie() && rand.nextDouble() < 0.05)
            target.tellNearby("moans", "brains....");

        super.update();
    }
}
