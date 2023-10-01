package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Armour extends CarriableEntity {
    private int durability = 2;
    private static int countId = 0;
    
    /**
     * Constructor for Armour 
     * @param position of Armour
     */
    public Armour(Position position) {
        countId += 1;
        String curId = "armour" + countId;
        setId(curId);
        setPosition(position);
        setType("armour");
    }

    @Override
    public void pickBy(Player player) {
        // can only pick one armour
        if (!player.hasArmour()) {
            player.setArmour(true);
            player.addToBag(this);
        }
    }

    @Override
    public void use(Player player) {
        // provides defence and halves enemy attack
        durability--;
        if (durability == 0) {
            player.removeFromBag(this);
            this.setRemove(true);
            player.setArmour(false);
        }   
    }
}
