package dungeonmania.StaticEntities;

import dungeonmania.Entity;

public class OpenDoorState implements DoorState {
    Door door;
    /**
     * Constructor for an open door.
     * @param door Door this state is associated with.
     */
    public OpenDoorState(Door door) {
        this.door = door;
    }

    @Override
    public void useKey() {
    }

    @Override
    public Boolean canMove(Entity entity) {
        return true;
    }
}
