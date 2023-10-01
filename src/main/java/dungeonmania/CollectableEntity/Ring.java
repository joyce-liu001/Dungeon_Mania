package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Ring extends CarriableEntity {
    private static int countId = 0;
    /**
     * Constructor for ring
     * @param position of ring
     */
    public Ring(Position position) {
        countId += 1;
        String curId = "one_ring" + countId;
        setId(curId);
        setPosition(position);
        setType("one_ring");
    }
    
    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        player.setFullHealth();
        player.removeFromBag(this);
        setRemove(true);
    }
}
