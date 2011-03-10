
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

    public boolean goTo(int x, int y){
        if (!canPathfind)
            return false;

        if (path != null && path.size() > 20)
            return false;

        path = pathFinder.findPath(target, target.x, target.y, x, y, target.distanceTo(x,y) * 4);

        return path != null && path.size() > 0;
    }
    
    public void update() {
        if (--walkCountdown > 0) {
            return;
        }

        if (canPathfind) {
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

                if (isAlly(other)){
                    if (!lastSeenNames.containsKey(other.name))
                        target.tell(other, "Hi " + other.name + "!");
                    else if (lastSeenNames.get(other.name) < target.age - 100)
                        target.tell(other, "Hey " + other.name + "! Good to see you again.");
                    
                    lastSeenNames.put(other.name, target.age);

                    if (dist >= closestAllyDist)
                        continue;

                    closestAllyDist = dist;
                    closestAlly = other;
                    
                } else {
                    if (lastSeenNames.containsKey(other.name)
                            && lastSeenNames.get(other.name) > target.age + 50)
                        target.tell(other, "I haven't forgotten about you " + other.name + "....");

                    if (other.isHero())
                        lastSeenNames.put(other.name, target.age);

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
        }

        trimPath();
        
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
        if (!target.canSpeak)
            return;
        
        if (!hatedNames.contains(other.name)){
            if (isAlly(other))
                target.tellNearby("Hey, " + other.name + " just attacked me!");
            target.tell(other, "You've made a new enemy today " + other.name + "...");
            hatedNames.add(other.name);
        }
    }

    public boolean isAlly(Creature other){
        return (""+other.glyph).toLowerCase().equals((""+target.glyph).toLowerCase())
                && !hatedNames.contains(other.name);
    }
}
