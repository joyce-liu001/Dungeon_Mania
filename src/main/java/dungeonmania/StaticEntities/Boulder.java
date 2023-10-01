package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.MovingEntites.MovingEntity;
import dungeonmania.util.*;

public class Boulder extends StaticEntity {

    /**
     * Constructor for a boulder static entity.
     * @param position Location of the boulder.
     */
    public Boulder(Position position, String id) {
        super(position, id, "boulder", false, true);
    }
    
    /**
     * Function to update the movement of a boulder.
     * @param direction Direction of movement.
     */
    public void move(Direction direction) {
        List<StaticEntity> staticList = 
            DungeonManiaController.getStaticEntities();
        List<CarriableEntity> carryList = 
            DungeonManiaController.getCarriableEntities();
        List<MovingEntity> movingList = 
            DungeonManiaController.getMovingEntities();
        List<Entity> entityList = new ArrayList<>();

        entityList.addAll(staticList);
        entityList.addAll(carryList);
        entityList.addAll(movingList);
        
        Position newPosition = this.getPosition().translateBy(direction);
        Boolean hasTeleported = true;
        
        // Move boulder and perform necessary checks
        while (hasTeleported) {
            hasTeleported = false;
            for (Entity entity : entityList) {
                if (entity.getPosition().equals(newPosition)) {
                    if (entity.getType().equals("portal")) {
                        Portal portal = (Portal)entity;
                        newPosition = portal.teleport(this, direction);
                        hasTeleported = true;
                    } else if (entity.getType().equals("switch")) {
                        FloorSwitch floorSwitch = (FloorSwitch)entity;
                        floorSwitch.setSwitchedOn(true);
                    } else if (entity.getType().equals("door")) {
                        Door door = (Door)entity;
                        if (door.canMove(this)) {
                            newPosition = this.getPosition().
                            translateBy(direction);
                        } else {
                            newPosition = this.getPosition();
                        }
                    } else {
                        newPosition = this.getPosition();
                    }
                }
            }
        }
        this.setPosition(newPosition);
    }

}
