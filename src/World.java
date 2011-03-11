
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class World {
    public static final int water = 0;
    public static final int dirt = 1;
    public static final int grass = 2;
    public static final int shrub = 3;
    public static final int tree  = 4;
    public static final int unknown = 5;
    public static final int openDoor = 6;
    public static final int closedDoor = 7;
    public static final int insideFloor = 8;
    public static final int floor = 9;
    public static final int wall = 10;

    public List<Creature> creatures;
    public List<Item> items;
    public int[][] tiles;
    public boolean[][] gore;
    public int width;
    public int height;
    private Random rand;
    public int ticks;
    public int ticksPerMinute;

    public int heroCount;
    public int villagerCount;
    public int undeadCount;

    public boolean didDoOnlyTwoMessage;

    public World(){
        creatures = new ArrayList<Creature>();
        items = new ArrayList<Item>();
        rand = new Random();
        ticks = 0;
        ticksPerMinute = 3;
        create();
    }

    public boolean isImpassable(int tile, boolean canWalkThroughWalls, boolean canNotGoIndoors){
        return tile == water 
                || tile == wall && !canWalkThroughWalls
                || tile == openDoor && canNotGoIndoors
                || tile == closedDoor && canNotGoIndoors
                || tile == insideFloor && canNotGoIndoors;
    }

    public void tellAll(Color color, String message){
        for (Creature creature : creatures){
            creature.hear(color, message);
        }
    }

    public void update(){
        ticks++;
        Object[] creatureArray = creatures.toArray();
        for (Object creature : creatureArray){
            ((Creature)creature).update();
        }

        heroCount = 0;
        villagerCount = 0;
        undeadCount = 0;
        
        ArrayList<Creature> died = new ArrayList<Creature>();
        for (Creature creature : creatures){
            if (creature.hp < 1)
                died.add(creature);
            else if (creature.isHero())
                heroCount++;
            else if (creature.isCommoner())
                villagerCount++;
            else
                undeadCount++;
        }
        creatures.removeAll(died);

        if (heroCount == 2 && !didDoOnlyTwoMessage)
            onlyTwoMessage();

        if (ticks % 60 == 0)
            spawnEnemies();
    }

    public void onlyTwoMessage(){
        didDoOnlyTwoMessage = true;
        Creature player = null;
        Creature other = null;
        for (Creature creature : creatures){
            if (!creature.isHero())
                continue;

            if (creature.personalTitle.equals("player"))
                player = creature;

            if (!creature.personalTitle.equals("player"))
                other = creature;
        }
        other.tell(other, "It's just you and me now, " + player.personalName + ".");
    }

    public void spawnEnemies(){
        String message = " are attacking from the ";
        int x = 0;
        int y = 0;

        CreatureFactory factory = new CreatureFactory(this, new ItemFactory());

        switch (rand.nextInt(4)){
            case 0: x = rand.nextInt(width); message += "north"; break;
            case 1: x = rand.nextInt(width); message += "south"; y=height; break;
            case 2: y = rand.nextInt(height); message += "west"; break;
            case 3: y = rand.nextInt(height); message += "east"; x=width; break;
        }

        ArrayList<Creature> group = new ArrayList();
        switch (rand.nextInt(4)){
            case 0: message = "Skeletons" + message;
                for (int i = 0; i < 20; i++) {
                    group.add(factory.Skeleton());
                }
            break;
            case 1: message = "Zombies" + message;
                for (int i = 0; i < 10; i++) {
                    group.add(factory.Zombie());
                }
            break;
            case 2: message = "Ghosts" + message;
                for (int i = 0; i < 8; i++) {
                    group.add(factory.Ghost());
                }
            break;
            case 3: message = "Vampires" + message;
                for (int i = 0; i < 4; i++) {
                    group.add(factory.Vampire());
                }
            break;
        }


        if (group.isEmpty())
            return;

         int tries = 0;
         while (!group.isEmpty() && tries++ < 1000){
            Creature c = group.get(0);
            c.x = x + rand.nextInt(20) - 10;
            c.y = y + rand.nextInt(20) - 10;
            if (c.canBeAt(c.x, c.y)){
                creatures.add(group.remove(0));
            }
         }

         tellAll(AsciiPanel.brightWhite, "!! " + message + " !!");
    }

    public char getGlyph(int x, int y) {
        switch (tiles[x][y]) {
            case water: return '~';
            case dirt:  return 249;
            case grass: return 249;
            case shrub: return '*';
            case tree:  return 6;
            case insideFloor: return 249;
            case floor: return 249;
            case wall:  return '#';
            case openDoor:  return '/';
            case closedDoor:  return '+';
            default:    return ' ';
        }
    }

    public Color getColor(int x, int y) {
        if (gore[x][y])
            return AsciiPanel.red;
        
        switch (tiles[x][y]) {
            case water: return AsciiPanel.blue;
            case dirt:  return AsciiPanel.yellow;
            case grass: return AsciiPanel.green;
            case shrub: return AsciiPanel.green;
            case tree:  return AsciiPanel.green;
            case insideFloor: return AsciiPanel.white;
            case floor: return AsciiPanel.white;
            case wall:  return AsciiPanel.white;
            case openDoor:  return AsciiPanel.yellow;
            case closedDoor:  return AsciiPanel.yellow;
            default:    return AsciiPanel.black;
        }
    }

    public void addGore(int x, int y){
        gore[x][y] = true;
    }

    public void placeAnywhere(Creature creature, Random rand){
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

    public void placeInVillage(Creature creature, Random rand){
        do {
            creature.x = rand.nextInt(width);
            creature.y = rand.nextInt(height);

            for (Creature other : creatures){
                if (other.x == creature.x && other.y == creature.y)
                    continue;
            }
        } while (tiles[creature.x][creature.y] != insideFloor);

        creatures.add(creature);
    }

    public void placeAnywhere(Item item, Random rand){
        do {
            item.x = rand.nextInt(width);
            item.y = rand.nextInt(height);

            for (Item other : items){
                if (other.x == item.x && other.y == item.y)
                    continue;
            }

        } while (isImpassable(tiles[item.x][item.y], false, false));

        items.add(item);
    }

    public void placeInVillage(Item item, Random rand){
        do {
            item.x = rand.nextInt(width);
            item.y = rand.nextInt(height);

            for (Item other : items){
                if (other.x == item.x && other.y == item.y)
                    continue;
            }
        } while (tiles[item.x][item.y] != insideFloor);

        items.add(item);
    }

    private void create() {
        int size = 8;
        tiles = new int[size][size];
        gore = new boolean[size][size];
        width = size;
        height = size;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                tiles[x][y] = rand.nextInt(unknown);
            }
        }
        
        for (int zoom : new int[]{2,2,4}) {
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
        gore = new boolean[width][height];

        addCity(width / 2, height / 2);
    }

    private void randomize() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = tiles[x][y] % unknown;

                switch (tiles[x][y]) {
                    case grass:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = shrub;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = dirt;
                        break;
                    case shrub:
                        if (rand.nextDouble() < 0.05) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        break;
                    case tree:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.1) tiles[x][y] = shrub;
                        break;
                    case dirt:
                        if (rand.nextDouble() < 0.1) tiles[x][y] = grass;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = shrub;
                        else if(rand.nextDouble() < 0.01) tiles[x][y] = tree;
                        break;
                }
            }
        }
    }

    private void addCity(int cx, int cy){
        int dist = 5;
        for (int i = 0; i < 20; i++) {
            addHouse(cx + rand.nextInt(dist) + rand.nextInt(dist) - dist,
                     cy + rand.nextInt(dist) - dist / 2,
                     2 + rand.nextInt(3));
            dist++;
        }
    }

    private boolean canAddHouse(int cx, int cy, int s){
        for (int x = 0; x < s*2+1; x++){
            for (int y = 0; y < s*2+1; y++){
                int tile = tiles[cx+x-s][cy+y-s];

                if (tile == openDoor || tile == closedDoor)
                    return false;
                
                if (x==0 || y==0 || x==s*2 || y==s*2) {
                    if (tile == wall)
                        return false;
                } else if (tile == wall || tile == floor ||  tile == insideFloor) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addHouse(int cx, int cy, int s){
        if (!canAddHouse(cx, cy, s))
            return;
        
        for (int x = -1; x < s*2+2; x++){
            for (int y = -1; y < s*2+2; y++){
                if (x==-1 || y==-1 || x==s*2+1 || y==s*2+1) {
                    int tile = tiles[cx+x-s][cy+y-s];
                    if (tile != wall && tile != closedDoor && tile != openDoor)
                        tiles[cx+x-s][cy+y-s] = floor;
                } else if (x==0 || y==0 || x==s*2 || y==s*2) {
                    tiles[cx+x-s][cy+y-s] = wall;
                } else {
                    tiles[cx+x-s][cy+y-s] = insideFloor;
                }
            }
        }

        do
        {
            switch (rand.nextInt(4)){
                case 0: tiles[cx][cy-s] = closedDoor; break;
                case 1: tiles[cx][cy+s] = closedDoor; break;
                case 2: tiles[cx-s][cy] = closedDoor; break;
                case 3: tiles[cx+s][cy] = closedDoor; break;
            }
        } while (rand.nextDouble() < 0.33);
    }
}
