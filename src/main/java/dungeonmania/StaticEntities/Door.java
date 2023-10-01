package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.CollectableEntity.Key;
import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyInt;
    private Key key;
    DoorState closedState;
    DoorState openState;
    DoorState currState;

    /**
     * Constructor for door static entity.
     * @param position Location of the door.
     * @param key Key that will unlock this door. 
     */
    public Door(Position position, Integer keyInt, String id) {
        super(position, id, "door", false, false);
        this.keyInt = keyInt;
        this.key = null;
        currState = new ClosedDoorState(this);
    }
    
    /**
     * Only sets the key with the right keyInt.
     * @param key Input key.
     */
    public void setKey(Key key) {
        if (key.getKey() == keyInt) {
            this.key = key;
        }
    }

    public Key getKey() {
        return key;
    }
    
    /**
     * Method to use the key. Assumes player has key in their inventory.
     * @param player Player using key.
     */
    public void useKey(Player player) {
        currState.useKey();
        if (!player.hasSunStone()) {
            key.use(player);
        }
    }

    /** 
     * State strategy to switch between closed door and open door.
     * Method to switch states is the use of a key.
     */
    public void changeState() {
        currState = new OpenDoorState(this);
    }

    public void setState(DoorState doorState) {
        currState = doorState;
    }


    public DoorState getState() {
        return currState;
    }

    /**
     * Method to check if an entity can move through the door or not.
     * @return Boolean on whether or not an entity can move through or not.
     */
    public Boolean canMove(Entity entity) {
        return currState.canMove(entity);
    }
}
