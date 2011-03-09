
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class World {
    public static final int water = 0;
    public static final int dirtFloor = 1;
    public static final int dirtWall  = 2;
    public static final int grass = 3;
    public static final int shrub = 4;
    public static final int tree  = 5;
    public static final int rockFloor = 6;
    public static final int rockWall  = 7;
    public static final int unknown = 8;

    public List<Creature> creatures;
    public int[][] tiles;
    public int width;
    public int height;
    private Random rand;
    
    public World(){
        creatures = new ArrayList<Creature>();
        rand = new Random();
        create();
    }

    public void update(){
        Object[] creatureArray = creatures.toArray();
        for (Object creature : creatureArray){
            ((Creature)creature).update();
        }

        ArrayList<Creature> died = new ArrayList<Creature>();
        for (Creature creature : creatures){
            if (creature.hp < 1)
                died.add(creature);
        }
        creatures.removeAll(died);
    }

    public String getName(int x, int y){
        switch (tiles[x][y]) {
            case water: return "water";
            case dirtFloor:  return "dirt";
            case dirtWall:   return "dirt";
            case grass: return "grass";
            case shrub: return "bushes";
            case tree:  return "tree";
            case rockFloor: return "rock";
            case rockWall:  return "rock";
            default:    return "(unknown)";
        }
    }

    public double getImpassability(int x, int y){
        switch (tiles[x][y]) {
            case water: return 0.9;
            case dirtFloor:  return 0.0;
            case dirtWall:   return 0.7;
            case grass: return 0.0;
            case shrub: return 0.2;
            case tree:  return 0.4;
            case rockFloor: return 0.0;
            case rockWall:  return 0.9;
            default:    return 0.5;
        }
    }

    public char getGlyph(int x, int y) {
        switch (tiles[x][y]) {
            case water: return '~';
            case dirtFloor:  return 249;
            case dirtWall:   return 177;
            case grass: return 249;
            case shrub: return '*';
            case tree:  return 6;
            case rockFloor: return 249;
            case rockWall:  return 177;
            default:    return ' ';
        }
    }

    public Color getColor(int x, int y) {
        switch (tiles[x][y]) {
            case water: return AsciiPanel.blue;
            case dirtFloor:  return AsciiPanel.yellow;
            case dirtWall:  return AsciiPanel.yellow;
            case grass: return AsciiPanel.green;
            case shrub: return AsciiPanel.green;
            case tree:  return AsciiPanel.green;
            case rockFloor: return AsciiPanel.white;
            case rockWall: return AsciiPanel.white;
            default:    return AsciiPanel.black;
        }
    }

    public void placeCreature(Creature creature, Random rand){
        do {
            creature.x = rand.nextInt(width);
            creature.y = rand.nextInt(height);

            for (Creature other : creatures){
                if (other.x == creature.x && other.y == creature.y)
                    continue;
            }
        } while (!creature.canMoveBy(0, 0));

        creatures.add(creature);
    }

    private void create() {
        int size = 8;
        tiles = new int[size][size];
        width = size;
        height = size;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x][y] = rand.nextInt(unknown);
            }
        }
        
        for (int zoom : new int[]{2,4,2,2}) {
            size *= zoom;
            int[][] tiles2 = new int[size][size];

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    tiles2[x][y] = tiles[x/zoom][y/zoom];
                }
            }

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {

                    ArrayList<Integer> candidates = new ArrayList<Integer>();
                    for (int mx = -1; mx < 2; mx++) {
                        for (int my = -1; my < 2; my++) {

                            if (x+mx < 0 || x+mx >= size || y+my < 0 || y+my >= size)
                                continue;

                            candidates.add(tiles2[x+mx][y+my]);
                        }
                    }
                    Collections.shuffle(candidates);
                    tiles2[x][y] = candidates.get(0);
                }
            }
            
            tiles = tiles2;
            width = size;
            height = size;

            randomize();
        }
    }

    private void randomize() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = tiles[x][y] % unknown;

                switch (tiles[x][y]) {
                    case grass:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = shrub;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = dirtFloor;
                        break;
                    case shrub:
                        if (rand.nextDouble() < 0.05) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        break;
                    case tree:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.1) tiles[x][y] = shrub;
                        break;
                    case dirtWall:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = dirtFloor;
                        break;
                    case dirtFloor:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = shrub;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        break;
                    case rockFloor:
                        tiles[x][y] = grass;
                }
            }
        }
    }
}
