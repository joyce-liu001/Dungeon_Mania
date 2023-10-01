package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Arrow extends CarriableEntity {
    private static int countId = 0;
    /**
     * Constructor for arrow
     * @param position of arrow
     */
    public Arrow(Position position) {
        countId += 1;
        String curId = "arrow" + countId;
        setId(curId);
        setPosition(position);
        setType("arrow");
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
