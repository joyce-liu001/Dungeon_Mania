package dungeonmania.CollectableEntity;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class Bow extends CarriableEntity implements Buildable {
    private int durability = 1;
    private static int countId = 0;

    /**
     * Constructor for bow
     */
    public Bow() {
        countId += 1;
        String curId = "bow" + countId;
        setId(curId);
        this.setPosition(new Position(1,1));;
        setType("bow");
    }

    @Override
    public void build(Player player) {
        int removedWood = 0;
        int removedArrow = 0;
        List<CarriableEntity> removeEntities = new ArrayList<>();
        for (CarriableEntity e: player.getBag()) {
            if (e instanceof Wood && removedWood < 1) {
                removeEntities.add(e);
                removedWood++;
            } else if (e instanceof Arrow && removedArrow < 3) {
                removeEntities.add(e);
                removedArrow++;
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
    }

    @Override
    public void pickBy(Player player) {
    }

    @Override
    public void use(Player player) {
        durability--;
        player.setAttackDamage(2*player.getAttackDamage());
        if (durability == 0) {
            player.removeFromBag(this);
            setRemove(true);
        }
    }
}
