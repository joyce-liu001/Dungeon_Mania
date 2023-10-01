package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Treasure extends CarriableEntity {
    private static int countId = 0;

    /**
     * Constructor for treasure
     * @param position of treasure
     */
    public Treasure(Position position) {
        countId += 1;
        String curId = "treasure" + countId;
        setId(curId);
        setPosition(position);
        setType("treasure");
    }

    @Override
    public void pickBy(Player player) {
        player.setBalance(player.getBalance()+1);
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        player.setBalance(player.getBalance() - 1);
        player.removeFromBag(this);
        setRemove(true);
    }
}

