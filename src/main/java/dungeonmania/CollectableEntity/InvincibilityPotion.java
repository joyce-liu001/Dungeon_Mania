package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class InvincibilityPotion extends CarriableEntity {
    private int timeLimit = 10;
    private static int countId = 0;

    /**
     * Constructor for invincibility potion
     * @param position of invincibility potion
     */
    public InvincibilityPotion(Position position) {
        countId += 1;
        String curId = "invincibility_potion" + countId;
        setId(curId);
        setPosition(position);
        setType("invincibility_potion");
    }
    
    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        player.setInvincible(this.timeLimit);
        player.removeFromBag(this);
        setRemove(true);
    }
}
