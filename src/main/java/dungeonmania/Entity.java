package dungeonmania;

import dungeonmania.util.Position;
import java.io.Serializable;

public abstract class Entity implements Serializable {
    private Position position;
    private String id;
    private String type;
    private Boolean isInteractable = false;
    private Boolean canTeleport;
    
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsInteractable() {
        return isInteractable;
    }

    public void setIsInteractable(Boolean isInteractable) {
        this.isInteractable = isInteractable;
    }

    public Boolean getCanTeleport() {
        return canTeleport;
    }

    public void setCanTeleport(Boolean canTeleport) {
        this.canTeleport = canTeleport;
    }
}
