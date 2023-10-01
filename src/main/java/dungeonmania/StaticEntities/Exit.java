package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class Exit extends StaticEntity {
    /**
     * Constructor for the exit static entity.
     * @param position Location of the exit.
     */
    public Exit(Position position, String id) {
        super(position, id, "exit", false, false);
    }
}
