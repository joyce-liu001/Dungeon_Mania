package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Sword extends CarriableEntity {
    private int durability = 1;
    private static int countId = 0;

    /**
     * constructor for sword
     * @param position of sword
     */
    public Sword(Position position) {
        countId += 1;
        String curId = "sword" + countId;
        setId(curId);
        setPosition(position);
        setType("sword");
    }

    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        durability--;
        if (durability == 0) {
            player.removeFromBag(this);
            setRemove(true);
        }
    }
}
