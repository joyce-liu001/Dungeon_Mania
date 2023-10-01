package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CarriableEntity {
    private static int countId = 0;

    /**
     * Constructor for invisibility potion
     * @param position position of invisibility potion
     */
    public InvisibilityPotion(Position position) {
        countId += 1;
        String curId = "invisibility_potion" + countId;
        setId(curId);
        setPosition(position);
        setType("invisibility_potion");
    }
    
    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        player.removeFromBag(this);
        setRemove(true);
    }
}