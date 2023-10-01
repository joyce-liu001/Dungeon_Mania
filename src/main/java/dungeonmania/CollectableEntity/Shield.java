package dungeonmania.CollectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Shield extends CarriableEntity implements Buildable {
    private int durability = 1;
    private static int countId = 0;

    /**
     * constructor of shield
     * @param type string
     */
    public Shield() {
        countId += 1;
        String curId = "shield" + countId;
        setId(curId);
        this.setPosition(new Position(1,1));
        setType("shield");
    }

    @Override
    public void pickBy(Player player) {
    }

    @Override
    public void use(Player player) {
        durability--;
        if (durability == 0) {
            player.removeFromBag(this);
        }
        setRemove(true);
    }

    @Override
    public void build(Player player) {
        boolean isTreasure = false;
        if (player.getBalance() > 0) {
            isTreasure = true;
        }
        int removedWood = 0;
        int removedOther = 0;
        List<CarriableEntity> removeEntities = new ArrayList<>();
        for (CarriableEntity entity: player.getBag()) {
            if (entity instanceof Wood && removedWood < 2) {
                removeEntities.add(entity);
                removedWood++;
            } else if (entity instanceof Treasure && isTreasure && removedOther < 1) {
                removeEntities.add(entity);
                removedOther++;
            } else if (entity instanceof Key && !isTreasure && removedOther < 1) {
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
