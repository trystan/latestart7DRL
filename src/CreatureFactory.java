
import java.util.Random;

public class CreatureFactory {
    World world;
    Random rand;
    PathFinder pf;
    ItemFactory itemFactory;

    String vowels;
    String consonants;

    String[] sylables;

    public CreatureFactory(World w, ItemFactory factory){
        world = w;
        rand = new Random();
        pf = new PathFinder(w);
        itemFactory = factory;
        
        vowels = "aeiou";
        consonants = shuffle("bcdfghjklmnpqrstvwxzy");

        consonants = consonants.substring(consonants.length() / 2);
    }

    static String shuffle(String cards){
	if (cards.length()<=1)
	    return cards;

	int split=cards.length()/2;

	String temp1=shuffle(cards.substring(0,split));
	String temp2=shuffle(cards.substring(split));

	if (Math.random() > 0.5)
	    return temp1 + temp2;
	else
	    return temp2 + temp1;
    }

    private String shortName(){
        return name(4 + rand.nextInt(4));
    }

    private String longName(){
        String n = name(8 + rand.nextInt(4));
        if (rand.nextDouble() < 0.5){
            int i = rand.nextInt(n.length() - 2) + 1;
            n = n.substring(0,i) + "'" + n.substring(i);
        }
        return n;
    }

    private String name(int length){
        String name = "";

        while (name.length() < length){
            if (name.length() % 2 == 0)
                name += vowels.charAt(rand.nextInt(vowels.length()));
            else
                name += consonants.charAt(rand.nextInt(consonants.length()));
        }

        if (rand.nextBoolean()){
           name = vowels.charAt(rand.nextInt(vowels.length())) + name;
           name = name.substring(0, name.length() - 1);
        }

        name = name.substring(0,1).toUpperCase() + name.substring(1);
        
        return name;
    }

    private Item add(Item item){
        world.items.add(item);
        return item;
    }
    
    public Creature player(){
        Creature creature = new Creature(world, 0, 0, "Trystan", "player", '@', AsciiPanel.brightWhite);
        creature.level = 1;
        creature.maxHp = 80;
        creature.hp = creature.maxHp;
        creature.attack = 15;
        creature.defence = 15;
        creature.canSpeak = true;
        creature.controller = new PlayerController(creature);
        creature.equip(add(itemFactory.weapon()));
        creature.equip(add(itemFactory.armor()));
        return creature;
    }

    public Creature heroFighter(){
        Creature creature = new Creature(world, 0, 0, shortName(), "fighter", '@', AsciiPanel.red);
        creature.level = 3;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new FighterController(creature, pf);
        creature.canSwapWeapons = false;
        Item personalWeapon = itemFactory.weapon();
        personalWeapon.name = creature.personalName + "'s" + personalWeapon.name;
        personalWeapon.modAttack += 5;
        personalWeapon.modDefence += 5;
        creature.equip(add(personalWeapon));
        creature.equip(add(itemFactory.heavyArmor()));
        return creature;
    }

    public Creature heroSamuri(){
        Creature creature = new Creature(world, 0, 0, shortName(), "samuri", '@', AsciiPanel.brightRed);
        creature.level = 3;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new SamuriController(creature, pf);
        creature.canSwapWeapons = false;
        creature.equip(add(itemFactory.katana()));
        creature.equip(add(itemFactory.mediumArmor()));
        return creature;
    }

    public Creature heroSlayer(){
        Creature creature = new Creature(world, 0, 0, shortName(), "slayer", '@', AsciiPanel.cyan);
        creature.level = 2;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 15 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 15 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.healthRate /= 2;
        creature.controller = new SlayerController(creature, pf);
        creature.canSwapWeapons = false;
        creature.equip(add(itemFactory.mrPointy()));
        return creature;
    }

    public Creature heroMonk(){
        Creature creature = new Creature(world, 0, 0, shortName(), "monk", '@', AsciiPanel.brightCyan);
        creature.level = 1;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence =10 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.canSwapWeapons = false;
        creature.canSwapArmor = false;
        creature.controller = new MonkController(creature, pf);
        return creature;
    }

    public Creature heroPreist(){
        Creature creature = new Creature(world, 0, 0, shortName(), "priest", '@', AsciiPanel.magenta);
        creature.level = 4;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence =10 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new PriestController(creature, pf);
        creature.canSwapArmor = false;
        creature.equip(add(itemFactory.mace()));
        creature.equip(add(itemFactory.holyArmor()));
        return creature;
    }

    public Creature heroWizzard(){
        Creature creature = new Creature(world, 0, 0, shortName(), "wizzard", '@', AsciiPanel.brightMagenta);
        creature.level = 1;
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new WizzardController(creature, pf);
        creature.canSwapArmor = false;
        creature.equip(add(itemFactory.robes()));
        return creature;
    }

    public Creature villager(){
        Creature creature = new Creature(world, 0, 0, shortName(), "villager", '@', AsciiPanel.brightBlack);
        creature.level = 1;
        creature.maxHp = 50 + rand.nextInt(20);
        creature.hp = creature.maxHp;
        creature.attack = 1 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 1 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.likesIndoors = rand.nextBoolean();
        creature.controller = new VillagerController(creature, pf);
        return creature;
    }

    public Creature skeleton(){
        Creature creature = new Creature(world, 0, 0, "", "skeleton", 's', AsciiPanel.white);
        creature.level = 1;
        creature.maxHp = 20 + rand.nextInt(20);
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(16);
        creature.defence = 5 + rand.nextInt(16);
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature zombie(){
        Creature creature = new Creature(world, 0, 0, "", "zombie", 'z', AsciiPanel.white);
        creature.level = 2;
        creature.maxHp = 50 + rand.nextInt(20);
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(6);
        creature.defence = 10 + rand.nextInt(6);
        creature.isSlow = true;
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature ghost(){
        Creature creature = new Creature(world, 0, 0, "", "ghost", 'g', AsciiPanel.brightBlack);
        creature.level = 3;
        creature.maxHp = 1 + rand.nextInt(51);
        creature.hp = creature.maxHp;
        creature.attack = 1;
        creature.defence = 100;
        creature.vision = 50;
        creature.canFly = true;
        creature.canWalkThroughWalls = true;
        creature.canBeDecapitated = false;
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature vampire(){
        Creature creature = new Creature(world, 0, 0, "", "vampire", 'v', AsciiPanel.white);
        creature.level = 5;
        creature.maxHp = 50 + rand.nextInt(20);
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.controller = new UndeadController(creature, pf, this);
        creature.canStealLife = true;
        creature.canNotGoIndoors = true;
        creature.hasBlood = true;
        creature.canSpeak = true;
        creature.canSwapWeapons = true;
        creature.canSwapArmor = true;
        creature.equip(add(itemFactory.weapon()));

        if (rand.nextDouble() < 0.25)
            creature.equip(add(itemFactory.unholyArmor()));
        else
            creature.equip(add(itemFactory.armor()));
        return creature;
    }

    public Creature lich(){
        Creature creature = new Creature(world, 0, 0, longName(), "lich", 'L', AsciiPanel.magenta);
        creature.level = 5 + rand.nextInt(6);
        creature.maxHp = 100;
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5) + rand.nextInt(5);
        creature.controller = new UndeadController(creature, pf, this);
        creature.canSpeak = true;
        creature.equip(add(itemFactory.lichStaff()));
        creature.equip(add(itemFactory.unholyRobes()));
        return creature;
    }

    public Creature badGuy(){
        switch (rand.nextInt(14)){
            case 0: return lich();
            case 1:
            case 2: return vampire();
            case 3:
            case 4:
            case 5: return ghost();
            case 6:
            case 7:
            case 8:
            case 9: return zombie();
            case 10:
            case 11:
            case 12:
            default: return skeleton();
        }
    }
}
