package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class TimeTravelPortal extends StaticEntity {
	/**
	 * Constructor for a wall object
	 * @param position Position of wall
	 * @param id Id of wall
	 */
	public TimeTravelPortal(Position position, String id) {
		super(position, id, "time_travelling_portal", false, false);
	}

}
