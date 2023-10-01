package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class TimeTurner extends CarriableEntity{
    private static int countId = 0;
    /**
     * Constructor for TimeTurner
     * @param position of the TimeTurner
     */
    public TimeTurner(Position position) {
        countId += 1;
        String curId = "time_turner" + countId;
        setId(curId);
        setPosition(position);
        setType("time_turner");
    }

    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
    }
}
