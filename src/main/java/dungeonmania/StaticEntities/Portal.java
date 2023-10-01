package dungeonmania.StaticEntities;

import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private Portal otherPortal;
    private String colour;
    /**
     * Constructor for portal static entity.
     * @param position Location of the portal.
     * @param colour Colour of the portal to link it to its counterpart.
     */
    public Portal(Position position, String colour, String id) {
        super(position, id, "portal", false,  false);
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public Portal getOtherPortal() {
        return otherPortal;
    }
    
    /**
     * Sets the corresponding portal only if it has the same colour.
     * @param otherPortal Input portal.
     */
    public void setOtherPortal(Portal otherPortal) {
        if (otherPortal.getColour().equals(colour)) {
            this.otherPortal = otherPortal;
        }
    }

    /**
     * Move one character from one portal to another.
     * @param direction Direction of movement.
     * @param character Character that is being teleported.
     * @return New position of the entity. 
     */
    public Position teleport(Entity character, Direction direction) {
        return otherPortal.getPosition().translateBy(direction);
    }
}
