package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;

public interface Buildable{
    /**
     * To build the entity
     * @param player the player
     */
    public void build(Player player);
}
