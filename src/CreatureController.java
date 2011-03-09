
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CreatureController {

    public Creature target;
    private Random rand;
    private int walkCountdown;
    public int moveWaitTime;
    public PathFinder pathFinder;
    private ArrayList<Point> path;
    private ArrayList<String> hatedNames;
    private HashMap<String,Integer> lastSeenNames;

    public boolean canPathfind;

    public CreatureController(Creature t, PathFinder pf) {
        target = t;
        rand = new Random();
        walkCountdown = rand.nextInt(5);
        pathFinder = pf;
        path = null;
        hatedNames = new ArrayList<String>();
        lastSeenNames = new HashMap<String,Integer>();
        moveWaitTime = 3;
    }

    public void update() {
        if (--walkCountdown > 0) {
            return;
        }

        if (canPathfind) {
            Creature closest = null;
            int closestDist = 1000000;
            
            for (Creature other : target.world.creatures) {
                if (other == target) {
                    continue;
                }

                if (Math.abs(target.x - other.x) > target.vision
                 || Math.abs(target.y - other.y) > target.vision) {
                    continue;
                }

                if (isAlly(other)){
                    if (!lastSeenNames.containsKey(other.name))
                        target.tell(other, "Hi!");
                    else if (lastSeenNames.get(other.name) > target.age + 50)
                        target.tell(other, "Hey " + other.name + "! Good to see you again.");
                    
                    lastSeenNames.put(other.name, target.age);
                } else {
                    if (lastSeenNames.containsKey(other.name)
                            && lastSeenNames.get(other.name) > target.age + 50)
                        target.tell(other, "I haven't forgotten about you " + other.name + ".");

                    lastSeenNames.put(other.name, target.age);
                }

                int dist = Math.min(Math.abs(target.x-other.x), Math.abs(target.y-other.y));

                if (dist >= closestDist)
                    continue;
                
                closestDist = dist;
                closest = other;
            }

            if (closest != null)
                path = pathFinder.findPath(target, target.x, target.y, closest.x, closest.y);
        }
        
        if (path != null && path.size() > 0) {
            followPath();
        } else {
            meander();
        }
        walkCountdown = moveWaitTime;
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

    public void onAttackedBy(Creature other){
        if (hatedNames.contains(other.name)){
            if (target.hp + target.defence < target.attack)
                target.tell(other, ": Have mercy on me!");
        } else {
            target.tell(other, "I won't forget that " + other.name + "!");
            hatedNames.add(other.name);
        }
    }

    public boolean isAlly(Creature other){
        return other.glyph == target.glyph && !hatedNames.contains(other.name);
    }
}
