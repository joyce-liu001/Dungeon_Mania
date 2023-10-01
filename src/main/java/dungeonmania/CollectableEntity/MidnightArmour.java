package dungeonmania.CollectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class MidnightArmour extends CarriableEntity implements Buildable {
    private int countId = 0;
    private int attackDamage = 20;

    /**
     * Constructor of MidnightArmour
     */
    public MidnightArmour() {
        countId += 1;
        String curId = "midnight_armour" + countId;
        setId(curId);
        this.setPosition(new Position(1,1));
        setType("midnight_armour");
    }

	@Override
	public void pickBy(Player player) {	
	}

	@Override
	public void use(Player player) {
        player.setAttackDamage(player.getAttackDamage()+attackDamage);	
	}

	@Override
	public void build(Player player) {
		int removedArmour = 0;
        List<CarriableEntity> removeEntities = new ArrayList<>();
        for (CarriableEntity e: player.getBag()) {
            if (e instanceof Armour && removedArmour < 1) {
                removeEntities.add(e);
                removedArmour++;
            }
        }
        for (CarriableEntity e: removeEntities) {
            e.use(player);
            if (player.getBag().contains(e)) {
                player.removeFromBag(e);
                e.setRemove(true);
            }
        }
        player.addToBag(this);
        player.setArmour(true);
	}
}
