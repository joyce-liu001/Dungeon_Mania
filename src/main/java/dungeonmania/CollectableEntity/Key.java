package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Key extends CarriableEntity {
    private static int countId = 0;
    private int keyInt;

    /**
     * Constructor of key
     * @param position position of key
     * @param key int of corresponding
     */
    public Key(Position position, int keyInt) {
        countId += 1;
        String curId = "key" + countId;
        setId(curId);
        setPosition(position);
        setType("key");
        this.keyInt = keyInt;
    }

    /**
     * getter for int/counter of key 
     * @return ketInt
     */
    public int getKey() {
        return this.keyInt;
    }
    
    @Override
    public void pickBy(Player player) {
        // The player can carry only one key at a time, 
        // and only one door has a lock that fits the key.
        for (CarriableEntity e: player.getBag()) {
            if (e instanceof Key) {
                return;
            }
        }
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        // The key disappears once it is used to open its corresponding door
        player.removeFromBag(this);
        setRemove(true);
    } 
}
