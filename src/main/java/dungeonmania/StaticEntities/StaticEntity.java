package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Position;

public class StaticEntity extends Entity {

    /**
     * Constructor for a static entity. 
     * @param position Position of the entity.
     * @param id Id of the entitiy.
     * @param type Type of the entity.
     * @param isInteractable Whether or not you can interact with the entity.
     * @param canTeleport Whether the entity can teleport or not. 
     */
    public StaticEntity(Position position, String id, String type, 
        Boolean isInteractable, Boolean canTeleport) {
        this.setPosition(position.asLayer(0));
        this.setId(id);
        this.setType(type);
        this.setIsInteractable(isInteractable);
        this.setCanTeleport(canTeleport);
    }
}
