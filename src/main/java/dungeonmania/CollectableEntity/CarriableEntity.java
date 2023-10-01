package dungeonmania.CollectableEntity;

import dungeonmania.Entity;
import dungeonmania.MovingEntites.Player;


public abstract class CarriableEntity extends Entity{
    private boolean isRemove = false;
    /**
     * Pick by the player and add this in the bag.
     * @param player the player
     */
    public abstract void pickBy(Player player);

    /**
     * Player use the entity and remove it from bag.
     * @param player the player.
     */
    public abstract void use(Player player);

    /**
     * setter for isRemove
     * @param boolean whether this is to be removed
     */
    public void setRemove(boolean isRemove) {
        this.isRemove = isRemove;
    }
    
    /**
     * getter for isRemove
     * @return a boolean to see if this is removed
     */
    public boolean getIsRemove() {
        return this.isRemove;
    }
}
