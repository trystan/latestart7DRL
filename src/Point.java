
import java.util.ArrayList;
import java.util.Collections;


public class Point {
    public int x;
    public int y;

    public Point(int px, int py){
        x = px;
        y = py;
    }


    public ArrayList<Point> getNeighbors(World world) {
        ArrayList<Point> points = new ArrayList<Point>(8);

        for (int xo = -1; xo < 2; xo++) {
            for (int yo = -1; yo < 2; yo++) {
                if (x + xo < 0 || x + xo >= world.width || y + yo < 0 || y + yo >= world.height) {
                    continue;
                }

                points.add(new Point(x + xo, y + yo));
            }
        }

        Collections.shuffle(points);

        return points;
    }
}
