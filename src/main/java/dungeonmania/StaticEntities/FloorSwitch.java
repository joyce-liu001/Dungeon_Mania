package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Entity;
import dungeonmania.CollectableEntity.Bomb;
import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private Boolean switchedOn;
    private List<Entity> adjacentEntities;
    private Entity onTop;
    /**
     * Constructor for floor switch entity.
     * @param position Location of the floor switch.
     * switchedOn refers to whether the switch is triggered or not.
     * onTop refers to what entity is on top of the switch.
     */
    public FloorSwitch(Position position, String id) {
        super(position, id, "switch", false, false);
        adjacentEntities = new ArrayList<>();
        switchedOn = false;
        onTop = null;
    }  

    /**
     * getter for isOn
     * @return the boolean to see weather the switch is on
     */
    public Boolean getSwitchedOn() {
        return switchedOn;
    }

    /**
     * setter for isOn
     * @param boolean to set isOn
     */
    public void setSwitchedOn(Boolean switchedOn) {
        this.switchedOn = switchedOn;
    }

    /**
     * Takes in a list and checks what entities are on top of the switch.
     * @param entityList List of entities.
     */
    public void setOnTop(ArrayList<Entity> entityList) {
        onTop = null;
        for (Entity e : entityList) {
            if (e.getPosition().equals(getPosition()) && 
                !e.getType().equals("switch")) {
                onTop = e;
            }
        }
    }

    /**
     * Add an entity to the list of adjacent entities.
     * @param entity Entity adjacent to floor switch.
     */
    public void addEntities(ArrayList<CarriableEntity> entityList) {
        adjacentEntities.clear();
        if (entityList.size() != 0) {
            for (Entity entity: entityList) {
                if (Position.isAdjacent(entity.getPosition(), getPosition())) {
                    adjacentEntities.add(entity);
                }
            }
        }
    }

    /** 
     * Method to check if entity on top is a boulder or not and set switchedOn
     * when appropriate. Will also detonate any bombs around it.
     * @return List of static entities that the bomb has destroyed. 
     */
    public ArrayList<Entity> cycle() {
        if (onTop != null) {
            if (onTop.getType().equals("boulder")) {
                setSwitchedOn(true);
                return detonate();
            } else {
                setSwitchedOn(false);
            }
        } else {
            setSwitchedOn(false);
        }
        return new ArrayList<>();
    }

    /** 
     * Detonate any bombs around the switch when turned on.
     * @return List of static entities that the bomb has destroyed. 
     */
    public ArrayList<Entity> detonate() {
        ArrayList<Entity> removeList = new ArrayList<>();
        for (Entity e : adjacentEntities) {
            if (e instanceof Bomb) {
                Bomb bomb = (Bomb)e;
                removeList.addAll(bomb.updateBlast());
            }
        }
        return removeList;
    }

}
