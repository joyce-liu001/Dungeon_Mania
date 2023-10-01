package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    /**
     * Constructor for a wall object.
     * @param position Position of wall.
     * @param id Id of wall.
     */
    public Wall(Position position, String id) {
        super(position, id, "wall", false, false);
    }
}
