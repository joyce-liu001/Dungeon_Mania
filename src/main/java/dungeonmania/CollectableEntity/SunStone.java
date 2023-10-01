package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class SunStone extends CarriableEntity {
    private static int countId = 0;
    /**
     * Constructor for sunstone
     * @param position of sunstone
     */
    public SunStone(Position position) {
        countId += 1;
        String curId = "sun_stone" + countId;
        setId(curId);
        setPosition(position);
        setType("sun_stone");
    }

    @Override
    public void pickBy(Player player) {
       player.addToBag(this);
       player.setBalance(player.getBalance() + 1);
    }

    @Override
    public void use(Player player) {
    }
    
}
