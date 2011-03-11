
import java.util.Random;

public class CreatureController {

    public Creature target;
    protected Random rand;

    public CreatureController(Creature c){
        target = c;
        rand = new Random();
    }

    public boolean isAlly(Creature other){
        return other.isHuman() == target.isHuman();
    }

    public void update(){
        
    }
    
    public void see(Creature other){

    }
    
    public void onTakeDamage(int amount){

    }

    public void onLowHealth(){

    }

    public void onDied(){

    }

    public void onInflictDamage(Creature other, int damage){

    }

    public void onKilled(Creature other){
        
    }

    public void onDecapitated(Creature other){

    }

    public void onCounterAttacked(Creature other){
        
    }

    public void onKnockback(int distance){
        
    }
}
