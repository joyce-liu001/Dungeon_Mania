package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class SwitchDoor extends Door {
    String logic = null;
    /**
     * Constructor for switch door static entity
     * @param position Location of the switch door
     * @param key Key that will unlock this switch door
     * @param key id 
     */
    public SwitchDoor(Position position, Integer keyInt, String id) {
        super(position, keyInt, "switch_door");
    }
    
    /**
     * Constructor for switch door static entity
     * @param position Location of the switch door
     * @param key Key that will unlock this switch door
     * @param id of the switch door
     * @param logic String for logic
     */
    public SwitchDoor(Position position, Integer keyInt, String id, String logic) {
        super(position, keyInt, "switch_door");
        this.logic = logic;
    }
    
    public String getLogic() {
        return logic;
    }

    public void setState(Boolean isOn) {
        if (isOn) {
            super.setState(new OpenDoorState(this));
        } else {
            super.setState(new ClosedDoorState(this));
        }
    }

}
