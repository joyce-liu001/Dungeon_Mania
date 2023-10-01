package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonManiaController;
import dungeonmania.util.Position;

public class Wire extends StaticEntity {
    List<FloorSwitch> switches = new ArrayList<>();
    /**
     * Constructor for a wire object
     * @param position Position of wire
     * @param id Id of wire
     */
    public Wire(Position position, String id) {
        super(position, id, "wire", false, false);
        updateSwitch();
    }

    /**
     * update the switch and 
     */
    public void updateSwitch() {
        for (StaticEntity elemeEntity: DungeonManiaController.getStaticEntities()) {
            if (elemeEntity instanceof FloorSwitch && Position.isAdjacent(this.getPosition(), elemeEntity.getPosition())) {
                FloorSwitch switch1 = (FloorSwitch) elemeEntity;
                switches.add(switch1);
            }
        }
    }

    /**
     * @return a list of switches that connected to the wire
     */
    public List<FloorSwitch> getSwitches() {
        return switches;
    }
}