package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.StaticEntities.FloorSwitch;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.StaticEntities.Wire;
import dungeonmania.util.Position;


public class HelperLogic {
    
    /**
     * @param entity check the entity can be active or not
     * @param logic the logic of the given entity, it can be null
     * @return true, if entity can be active, return true. If not, return false.
     */
    public static boolean logic(Entity entity, String logic) {
        // check how many active switch around the entity
        // then check how much active switch the around wire connect
        List<FloorSwitch> activeSwitches = new ArrayList<>();
        
        for (StaticEntity e: DungeonManiaController.getStaticEntities()) {
            if (e instanceof FloorSwitch && Position.isAdjacent(e.getPosition(), entity.getPosition())) {
                if (((FloorSwitch) e).getSwitchedOn() && !activeSwitches.contains(e)) {
                    activeSwitches.add((FloorSwitch) e);
                }
            } else if (e instanceof Wire && Position.isAdjacent(e.getPosition(), entity.getPosition())) {
                for (FloorSwitch s : ((Wire) e).getSwitches()) {
                    if (!activeSwitches.contains(s)) {
                        activeSwitches.add(s);
                    }
                }
            }
        }

        int numActiveSwitches = activeSwitches.size();

        if (logic != null) {
            if (logic.equals("and") && numActiveSwitches >= 2) {
                return true;
            } else if (logic.equals("or") && numActiveSwitches >= 1) {
                return true;
            } else if (logic.equals("xor") && numActiveSwitches == 1) {
                return true;
            } else if (logic.equals("not") && numActiveSwitches == 0) {
                return true;
            }
            return false;
        } else if (numActiveSwitches > 0) {
            return true;
        }
        return false;
    }

}