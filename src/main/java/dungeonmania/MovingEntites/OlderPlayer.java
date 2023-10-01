package dungeonmania.MovingEntites;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;

public class OlderPlayer extends Enemy{


	private ArrayList<Position> futurePosition;
	boolean isDisappear = false;
	static int id = 0;
	private boolean isAlly;
	public boolean isDisappear() {
		return isDisappear;
	}

	public OlderPlayer(double health, int damage, boolean isAllay, ArrayList<Position> futurePosition) {
		setHealth(health);
		setAttackDamage(damage);
		this.isAlly = isAllay;
		this.futurePosition = futurePosition;
		this.setPosition(futurePosition.get(0));
		this.setType("older_player");
		id += 1;
		String curId = "OlderPlayer" + id;
		this.setId(curId);
 	}

	/**
	 * Get the health of the older player
	 *
	 * @return health the current health of the older player
	 */
	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	public boolean isAlly() {
		return isAlly;
	}

	public void setAlly(boolean ally) {
		isAlly = ally;
	}

	/**
	 * get a list of position that the older wil move to in the future
	 * @return a list of position
	 */
	public ArrayList<Position> getFuturePosition() {
		return futurePosition;
	}

	/**
	 * Move the older player
	 *
	 * @param direction The direction the older player move
	 */
	@Override
	public void move(Direction direction) {
		if (this.futurePosition.size() == 1) {
			isDisappear = true;
			return;
		}
		Position next = this.futurePosition.get(1);
		this.setPosition(next);
		futurePosition.remove(next);
	}
}
