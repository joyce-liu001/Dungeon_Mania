package dungeonmania.CollectableEntity;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;


public class Anduril extends CarriableEntity {
    private static int countId = 0;
    
    /**
     * Constructor of Anduril
     * @param position of Anduril
     */
    public Anduril(Position position) {
        countId += 1;
        String curId = "anduril" + countId;
        setId(curId);
        setPosition(position);
        setType("anduril");
    }

	@Override
	public void pickBy(Player player) {
        player.addToBag(this);
        
	}

	@Override
	public void use(Player player) {
        player.setAttackDamage(3 * player.getAttackDamage());
	}
}



