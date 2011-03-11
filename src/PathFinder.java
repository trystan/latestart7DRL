
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PathFinder {

    private World world;

    double[][] cost;
    ArrayList<Point> opened;
    ArrayList<Point> closed;
    HashMap<Point, Point> parents;
    Point start;
    Point end;

    public PathFinder(World w) {
        world = w;
        opened = new ArrayList<Point>();
        closed = new ArrayList<Point>();
        parents = new HashMap<Point, Point>();
    }

    private void reset(){
        cost = new double[world.width][world.height];
        opened.clear();
        closed.clear();
        parents.clear();
    }

    private ArrayList<Point> getNeighbors(int x, int y) {
        ArrayList<Point> points = new ArrayList<Point>(8);

        for (int xo = -1; xo < 2; xo++) {
            for (int yo = -1; yo < 2; yo++) {
                if (x + xo < 0 || x + xo >= world.width || y + yo < 0 || y + yo >= world.height) {
                    continue;
                }

                points.add(new Point(x + xo, y + yo));
            }
        }

        return points;
    }

    private double costToGetToEnd(Point p) {
        if (p == start) {
            return 0;
        }

        return Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
    }

    private double costToGetHere(Point p) {
        if (p == start) {
            return 0;
        }

        return 1 + 0.5 * costToGetHere(parents.get(p));
    }

    private boolean passable(Point p, Creature c){
        return c.canEnter(p.x, p.y);
    }

    private double totalCost(Point p) {
        if (cost[p.x][p.y] == 0)
            cost[p.x][p.y] = costToGetHere(p) + costToGetToEnd(p);
        
        return cost[p.x][p.y];
    }

    private void reParent(Point child, Point parent){
        parents.put(child, parent);
        
        cost[child.x][child.y] = costToGetHere(child) + costToGetToEnd(child);
    }

    public ArrayList<Point> findPath(Creature creature, int sx, int sy, int tx, int ty, int maxTries) {
        reset();
        start = new Point(sx, sy);
        end = new Point(tx, ty);

        for (Point neighbor : getNeighbors(sx, sy)) {
            reParent(neighbor, start);

            if (neighbor != start) {
                opened.add(neighbor);
            }
        }

        int tries = 0;
        while (opened.size() > 0 && tries++ < maxTries) {
            Point best = opened.get(0);

            for (Point other : opened){
                if (totalCost(other) < totalCost(best))
                    best = other;
            }
            opened.remove(best);
            closed.add(best);

            if (best.x == tx && best.y == ty) {
                ArrayList<Point> path = new ArrayList<Point>();
                Point current = best;

                while (current != start) {
                    path.add(current);
                    current = parents.get(current);
                }

                Collections.reverse(path);
                return path;

            } else {
                if (!passable(best, creature))
                    continue;
                
                for (Point neighbor : getNeighbors(best.x, best.y)) {

                    if (opened.contains(neighbor)) {
                        Point bestPathToNeighbor = new Point(neighbor.x, neighbor.y);
                        reParent(bestPathToNeighbor, best);
                        if (totalCost(bestPathToNeighbor) >= totalCost(neighbor)) {
                            continue;
                        }
                    }

                    if (closed.contains(neighbor)) {
                        Point bestPathToNeighbor = new Point(neighbor.x, neighbor.x);
                        reParent(bestPathToNeighbor, best);
                        if (totalCost(bestPathToNeighbor) >= totalCost(neighbor)) {
                            continue;
                        }
                    }

                    reParent(neighbor, best);

                    opened.remove(neighbor);
                    closed.remove(neighbor);
                    opened.add(0, neighbor);
                }
            }
        }
        
        return null;
    }
}
