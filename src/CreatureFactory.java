
import java.awt.Color;
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
        String name = sylables[rand.nextInt(sylables.length)];

        while (name.length() < 4)
            name += sylables[rand.nextInt(sylables.length)];

        name = name.substring(0,1).toUpperCase() + name.substring(1);
        
        return name;
    }
    
    public Creature Player(){
        Creature creature = new Creature(world, 0, 0, "player", '@', AsciiPanel.brightWhite, "you");
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 11;
        creature.canSpeak = true;
        return creature;
    }

    public Creature HeroTheif(){
        Creature creature = new Creature(world, 0, 0, name() + " the theif", '@', AsciiPanel.brightBlack, "sneaky");
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5);
        creature.defence =10 + rand.nextInt(5);
        creature.vision = 11;
        creature.canSpeak = true;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.controller.moveWaitTime = 0;
        creature.equip(itemFactory.knife());
        creature.equip(itemFactory.lightArmor());
        return creature;
    }

    public Creature HeroFighter(){
        Creature creature = new Creature(world, 0, 0, name() + " the fighter", '@', AsciiPanel.brightRed, "strong");
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 15 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.vision = 11;
        creature.canSpeak = true;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.controller.moveWaitTime = 0;
        creature.equip(itemFactory.sword());
        creature.equip(itemFactory.heavyArmor());
        return creature;
    }

    public Creature HeroMonk(){
        Creature creature = new Creature(world, 0, 0, name() + " the monk", '@', AsciiPanel.cyan, "balanced");
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 12 + rand.nextInt(5) + rand.nextInt(5);
        creature.defence =12 + rand.nextInt(5) + rand.nextInt(5);
        creature.vision = 11 + rand.nextInt(5);
        creature.canSpeak = true;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.controller.moveWaitTime = 0;
        return creature;
    }

    public Creature HeroWizzard(){
        Creature creature = new Creature(world, 0, 0, name() + " the wizzard", '@', AsciiPanel.brightMagenta, "magical");
        creature.maxHp = 60;
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.vision = 15;
        creature.canSpeak = true;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.controller.moveWaitTime = 0;
        creature.equip(itemFactory.knife());
        creature.equip(itemFactory.robes());
        return creature;
    }

    public Creature Zombie(){
        Creature creature = new Creature(world, 0, 0, name() + " the zombie", 'z', AsciiPanel.brightWhite, "undead");
        creature.maxHp = 40 + rand.nextInt(10);
        creature.hp = creature.maxHp;
        creature.attack = 10 + rand.nextInt(5);
        creature.defence = 5 + rand.nextInt(5);
        creature.vision = 10;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.controller.moveWaitTime = 2;
        return creature;
    }
}
