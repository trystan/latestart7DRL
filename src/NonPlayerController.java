
import java.util.ArrayList;

public class NonPlayerController extends CreatureController {

    public PathFinder pathFinder;
    private ArrayList<Point> path;

    public NonPlayerController(Creature t, PathFinder pf) {
        super(t);
        pathFinder = pf;
        path = null;
    }

    public boolean goTo(int x, int y){

        if (path != null && path.size() > 20)
            return false;

        path = pathFinder.findPath(target, target.x, target.y, x, y, target.distanceTo(x,y) * 4);

        return path != null && path.size() > 0;
    }

    @Override
    public void update() {
        if (target.isSlow && target.age % 3 != 0) {
            return;
        }

        Creature closestAlly = null;
        int closestAllyDist = 1000000;
        Creature closest = null;
        int closestDist = 1000000;

        for (Creature other : target.world.creatures) {
            if (other == target) {
                continue;
            }

            int dist = target.distanceTo(other.x, other.y);
            if (dist > target.vision)
                continue;

            see(other);
            if (isAlly(other)){
                if (dist >= closestAllyDist)
                    continue;

                closestAllyDist = dist;
                closestAlly = other;

            } else {
                if (dist >= closestDist)
                    continue;

                closestDist = dist;
                closest = other;
            }
        }

        if (closest != null)
            path = pathFinder.findPath(target, target.x, target.y, closest.x, closest.y, 50);
        else if (closestAlly != null && closestAllyDist > 4)
            path = pathFinder.findPath(target, target.x, target.y, closestAlly.x, closestAlly.y, 50);

        trimPath();
        
        if (path != null && path.size() > 0) {
            followPath();
        } else {
            meander();
        }
    }

    public void trimPath(){
        while (path != null && path.size() > 0
                && target.x == path.get(0).x
                && target.y == path.get(0).y) {
            path.remove(0);
        }
    }

    public void followPath(){
        int mx = Math.max(-1, Math.min(path.get(0).x - target.x, 1));
        int my = Math.max(-1, Math.min(path.get(0).y - target.y, 1));

        target.moveBy(mx, my);
    }

    public void meander(){
        if (target.glyph != '@' && rand.nextDouble() < 0.05){
            path = pathFinder.findPath(target, target.x, target.y,
                    target.world.width / 2 + rand.nextInt(40) - 20,
                    target.world.height / 2 + rand.nextInt(40) - 20, 50);
            return;
        }
        switch (rand.nextInt(9)) {
            case 0:
                target.moveBy(-1, -1);
                break;
            case 1:
                target.moveBy(-1, 0);
                break;
            case 2:
                target.moveBy(-1, 1);
                break;
            case 3:
                target.moveBy(0, -1);
                break;
            case 4:
                target.moveBy(0, 0);
                break;
            case 5:
                target.moveBy(0, 1);
                break;
            case 6:
                target.moveBy(1, -1);
                break;
            case 7:
                target.moveBy(1, 0);
                break;
            case 8:
                target.moveBy(1, 1);
                break;
        }
    }
}
