
import java.util.Random;

public class CreatureFactory {
    World world;
    Random rand;
    PathFinder pf;
    ItemFactory itemFactory;

    String[] sylables;

    public CreatureFactory(World w, ItemFactory factory){
        world = w;
        rand = new Random();
        pf = new PathFinder(w);
        itemFactory = factory;
        
        String vowels = "aeiouy";
        String consonants = "bcdfghjklmnpqrstvwxz";

        sylables = new String[6];

        for (int i = 0; i < sylables.length; i++){
            String part = "";
            part += consonants.charAt(rand.nextInt(consonants.length()));
            part += vowels.charAt(rand.nextInt(vowels.length()));

            if (rand.nextDouble() < 0.25)
                part += consonants.charAt(rand.nextInt(consonants.length()));

            if (rand.nextDouble() < 0.25)
                part = vowels.charAt(rand.nextInt(vowels.length())) + part;

            part = part.replace("q", "qu");
            sylables[i] = part;
        }
    }

    private String name(){
        return name(4);
    }
    
    private String name(int length){
        String name = sylables[rand.nextInt(sylables.length)];

        while (name.length() < length)
            name += sylables[rand.nextInt(sylables.length)];

        name = name.substring(0,1).toUpperCase() + name.substring(1);
        
        return name;
    }
    
    public Creature Player(){
        Creature creature = new Creature(world, 0, 0, "Trystan", "player", '@', AsciiPanel.brightWhite);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10;
        creature.defence = 5;
        creature.canSpeak = true;
        creature.controller = new PlayerController(creature);
        return creature;
    }

    public Creature HeroFighter(){
        Creature creature = new Creature(world, 0, 0, name(), "fighter", '@', AsciiPanel.red);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 15 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new FighterController(creature, pf);
        Item personalWeapon = itemFactory.weapon();
        personalWeapon.name = creature.personalName + "'s" + personalWeapon.name;
        personalWeapon.modAttack += 5;
        personalWeapon.modDefence += 5;
        creature.equip(personalWeapon);
        creature.equip(itemFactory.heavyArmor());
        return creature;
    }

    public Creature HeroSamuri(){
        Creature creature = new Creature(world, 0, 0, name(), "samuri", '@', AsciiPanel.brightRed);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 15 + rand.nextInt(5);
        creature.defence = 10 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new SamuriController(creature, pf);
        creature.equip(itemFactory.katana());
        creature.equip(itemFactory.mediumArmor());
        return creature;
    }

    public Creature HeroSlayer(){
        Creature creature = new Creature(world, 0, 0, name(), "slayer", '@', AsciiPanel.cyan);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 15 + rand.nextInt(5);
        creature.defence = 15 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.healthRate /= 1.5;
        creature.controller = new SlayerController(creature, pf);
        creature.equip(itemFactory.mrPointy());
        return creature;
    }

    public Creature HeroMonk(){
        Creature creature = new Creature(world, 0, 0, name(), "monk", '@', AsciiPanel.brightCyan);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 12 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence =12 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new MonkController(creature, pf);
        return creature;
    }

    public Creature HeroPreist(){
        Creature creature = new Creature(world, 0, 0, name(), "priest", '@', AsciiPanel.magenta);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 12 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence =12 + rand.nextInt(5) + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new PriestController(creature, pf);
        creature.equip(itemFactory.mace());
        creature.equip(itemFactory.armor());
        return creature;
    }

    public Creature HeroWizzard(){
        Creature creature = new Creature(world, 0, 0, name(), "wizzard", '@', AsciiPanel.brightMagenta);
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new WizzardController(creature, pf);
        creature.equip(itemFactory.robes());
        return creature;
    }

    public Creature Villager(){
        Creature creature = new Creature(world, 0, 0, name(), "villager", '@', AsciiPanel.brightBlack);
        creature.maxHp = 40;
        creature.hp = creature.maxHp;
        creature.attack = 1 + rand.nextInt(5);
        creature.defence = 1 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new VillagerController(creature, pf);
        return creature;
    }

    public Creature Skeleton(){
        Creature creature = new Creature(world, 0, 0, "", "skeleton", 's', AsciiPanel.white);
        creature.maxHp = 10 + rand.nextInt(10);
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature Zombie(){
        Creature creature = new Creature(world, 0, 0, "", "zombie", 'z', AsciiPanel.white);
        creature.maxHp = 40 + rand.nextInt(10);
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.isSlow = true;
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature Ghost(){
        Creature creature = new Creature(world, 0, 0, "", "ghost", 'g', AsciiPanel.brightBlack);
        creature.maxHp = 1 + rand.nextInt(100);
        creature.hp = creature.maxHp;
        creature.attack = 1 + rand.nextInt(5);
        creature.defence = 100;
        creature.vision = 50;
        creature.canWalkThroughWalls = true;
        creature.canBeDecapitated = false;
        creature.controller = new UndeadController(creature, pf, this);
        return creature;
    }

    public Creature Vampire(){
        Creature creature = new Creature(world, 0, 0, "", "vampire", 'v', AsciiPanel.white);
        creature.maxHp = 50 + rand.nextInt(20);
        creature.hp = creature.maxHp;
        creature.attack = 5 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.controller = new UndeadController(creature, pf, this);
        creature.canStealLife = true;
        creature.canNotGoIndoors = true;
        creature.hasBlood = true;
        creature.canSpeak = true;
        creature.equip(itemFactory.weapon());
        creature.equip(itemFactory.armor());
        return creature;
    }

    public Creature Lich(){
        Creature creature = new Creature(world, 0, 0, name(8), "lich", 'L', AsciiPanel.magenta);
        creature.maxHp = 100;
        creature.hp = creature.maxHp;
        creature.attack = 1 + rand.nextInt(5);
        creature.defence = 1 + rand.nextInt(5);
        creature.controller = new UndeadController(creature, pf, this);
        creature.canSpeak = true;
        creature.equip(itemFactory.lichStaff());
        return creature;
    }
}
