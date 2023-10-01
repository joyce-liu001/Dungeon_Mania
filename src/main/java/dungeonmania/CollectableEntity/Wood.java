package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Wood extends CarriableEntity {
    private static int countId = 0;
    /**
     * Constructor for wood
     * @param position of wood
     */
    public Wood(Position position) {
        countId += 1;
        String curId = "wood" + countId;
        setId(curId);
        setPosition(position);
        setType("wood");
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