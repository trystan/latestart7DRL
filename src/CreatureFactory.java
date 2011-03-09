
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
        Creature creature = new Creature(world, 0, 0, "player", '@', AsciiPanel.brightWhite);
        creature.hp = 60;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 11;
        return creature;
    }

    public Creature HeroTheif(){
        Creature creature = new Creature(world, 0, 0, name() + " the theif", '@', AsciiPanel.brightBlack);
        creature.hp = 60;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 11;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.equip(itemFactory.knife());
        creature.equip(itemFactory.lightArmor());
        return creature;
    }

    public Creature HeroFighter(){
        Creature creature = new Creature(world, 0, 0, name() + " the fighter", '@', AsciiPanel.brightRed);
        creature.hp = 60;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 11;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.equip(itemFactory.sword());
        creature.equip(itemFactory.heavyArmor());
        return creature;
    }

    public Creature HeroWizzard(){
        Creature creature = new Creature(world, 0, 0, name() + " the wizzard", '@', AsciiPanel.brightMagenta);
        creature.hp = 60;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 11;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        creature.equip(itemFactory.knife());
        creature.equip(itemFactory.robes());
        return creature;
    }

    public Creature Zombie(){
        Creature creature = new Creature(world, 0, 0, name() + " the zombie", 'z', AsciiPanel.brightWhite);
        creature.hp = 40;
        creature.attack = 10;
        creature.defence = 5;
        creature.vision = 6;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = true;
        return creature;
    }

    public Creature Blob(Random rand){
        Color[] blobColors = new Color[]{ AsciiPanel.red, AsciiPanel.yellow, AsciiPanel.green, AsciiPanel.cyan, AsciiPanel.blue, AsciiPanel.magenta };

        Color c = blobColors[rand.nextInt(blobColors.length)];
        Creature creature = new Creature(world, 0, 0, "some blob", 'b', c);
        creature.hp = 5 + rand.nextInt(6);
        creature.attack = 2 + rand.nextInt(5);
        creature.defence = rand.nextInt(3);
        creature.vision = 1;
        creature.controller = new CreatureController(creature, pf);
        creature.controller.canPathfind = false;
        return creature;
    }
}
