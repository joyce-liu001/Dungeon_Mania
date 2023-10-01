package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.MovingEntites.Player;

public class ClosedDoorState implements DoorState {
    Door door;
    /**
     * Constructor for a closed door.
     * @param door Door this state is associated with. 
     */
    public ClosedDoorState(Door door) {
        this.door = door;
    }

    @Override
    /**
     * Method to possibly unlock the door and transition to a new state,
     * @param key Integer represented by the key,
     */
    public void useKey() {
        door.changeState();
    }

    @Override
    public Boolean canMove(Entity entity) {
        if (entity.getType().equals("player")) {
            Player player = (Player)entity;
            if (player.getBag().contains((Object)door.getKey())) {
                door.useKey(player);
                return true;
            }
        }
        return false;
    }
}
