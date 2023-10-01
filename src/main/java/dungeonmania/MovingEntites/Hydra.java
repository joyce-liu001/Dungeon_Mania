package dungeonmania.MovingEntites;

import dungeonmania.util.Position;
import java.util.Random;

public class Hydra extends Zombie{
	public static Random r = new Random(888);
	private static int id = 0;

	public Hydra(Position p) {
		super(p);
		this.setType("hydra");
		id++;
		this.setId("Hydra" + id);
	}
}
