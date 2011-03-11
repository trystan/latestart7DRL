
import java.awt.event.KeyEvent;


public class PlayerController extends CreatureController {

    public PlayerController(Creature c){
        super(c);
    }

    public boolean keyPressed(KeyEvent ke) {
        boolean endTurn1 = true;
        boolean endTurn2 = true;
        switch (ke.getKeyChar()) {
            case '8':
            case 'k': target.moveBy(0, -1); break;
            case '2':
            case 'j': target.moveBy(0, 1); break;
            case '4':
            case 'h': target.moveBy(-1, 0); break;
            case '6':
            case 'l': target.moveBy(1, 0); break;
            case '7':
            case 'y': target.moveBy(-1, -1); break;
            case '1':
            case 'b': target.moveBy(-1, 1); break;
            case '9':
            case 'u': target.moveBy(1, -1); break;
            case '3':
            case 'n': target.moveBy(1, 1); break;
            case '5':
            case '.': target.moveBy(0, 0); break;
            case 'g':
            case ',':
                boolean found = false;
                for (Item item : target.world.items){
                    if (item.x == target.x && item.y == target.y && !item.equipped){
                        target.equip(item);
                        found = true;
                        break;
                    }
                }
                if (!found){
                    target.hear(AsciiPanel.white, "There's nothing here to pick up.");
                    endTurn1 = false;
                }
                break;
            default: endTurn1 = false;
        }
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP: target.moveBy(0, -1); break;
            case KeyEvent.VK_DOWN: target.moveBy(0, 1); break;
            case KeyEvent.VK_LEFT: target.moveBy(-1, 0); break;
            case KeyEvent.VK_RIGHT: target.moveBy(1, 0); break;
            default: endTurn2 = false;
        }

        return endTurn1 || endTurn2;
    }
}
