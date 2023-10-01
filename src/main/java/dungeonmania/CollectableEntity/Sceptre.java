package dungeonmania.CollectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Sceptre extends CarriableEntity implements Buildable {
    private int countId = 0;

    /**
     * Constructor of Sceptre
     */
    public Sceptre() {
        countId += 1;
        String curId = "sceptre" + countId;
        setId(curId);
        this.setPosition(new Position(1,1));
        setType("sceptre");
    }

	@Override
	public void pickBy(Player player) {
	}

	@Override
	public void use(Player player) {
	}

    @Override
    public void build(Player player) {
        Boolean useTreasure = false;
        Boolean useWood = false;
        for (CarriableEntity entity : player.getBag()) {
            if (entity instanceof Wood) {
                useWood = true;
            } else if (entity instanceof Treasure) {
                useTreasure = true;
            }
        }

        int removedWood = 0;
        int removedArrow = 0;
        int removedOther = 0;
        
        List<CarriableEntity> removeEntities = new ArrayList<>();
        for (CarriableEntity entity: player.getBag()) {
            if (entity instanceof Wood && useWood && removedWood < 1) {
                removeEntities.add(entity);
                removedWood++;
            } else if (entity instanceof Arrow && !useWood && removedArrow < 2) {
                removeEntities.add(entity);
                removedArrow++;
            } else if (entity instanceof Treasure && useTreasure && removedOther < 1) {
                removeEntities.add(entity);
                removedOther++;
            } else if (entity instanceof Key && !useTreasure && removedOther < 1) {
                removeEntities.add(entity);
                removedOther++;
            }
        }
        for (CarriableEntity entity: removeEntities) {
            entity.use(player);
            if (player.getBag().contains(entity)) {
                player.removeFromBag(entity);
                entity.setRemove(true);
            }
        }
        player.addToBag(this);
    }
}
