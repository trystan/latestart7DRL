
import java.util.ArrayList;
import java.util.Random;

public class CreatureController {

    public Creature target;
    private Random rand;
    private int walkCountdown;
    public PathFinder pathFinder;
    private ArrayList<Point> path;

    public CreatureController(Creature t, PathFinder pf) {
        target = t;
        rand = new Random();
        walkCountdown = rand.nextInt(5);
        pathFinder = pf;
        path = null;
    }

    public void update() {
        if (--walkCountdown > 0) {
            return;
        }

        for (Creature other : target.world.creatures) {
            if (other == target || other.glyph != '@') {
                continue;
            }

            if (Math.abs(target.x - other.x) > 12
             || Math.abs(target.y - other.y) > 12) {
                continue;
            }

            path = pathFinder.findPath(target, target.x, target.y, other.x, other.y);
            break;
        }

        if (path != null && path.size() > 0) {
            followPath();
        } else {
            meander();
        }
        walkCountdown = 3;
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
