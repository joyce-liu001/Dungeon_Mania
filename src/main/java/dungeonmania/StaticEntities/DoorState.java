package dungeonmania.StaticEntities;

import dungeonmania.Entity;

public interface DoorState {
    /** 
     * Function to use key to switch states.
     */
    public void useKey();

    /**
     * Gets if an entity can move through a door or not.
     * @return True if door is open, false otherwise.
     */
    public Boolean canMove(Entity entity);
}
