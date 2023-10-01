package dungeonmania.CollectableEntity;
import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;

public class HealthPotion extends CarriableEntity {
    private static int countId = 0;

    /**
     * Constructor for health potion
     * @param position of health potion 
     */
    public HealthPotion(Position position) {
        countId += 1;
        String curId = "health_potion" + countId;
        setId(curId);
        setPosition(position);
        setType("health_potion");
    }

    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        if (player.isComsumedHealthPotion()) {
            // Health potions may only be consumed once.
            return;
        }
        player.setFullHealth();
        player.setComsumedHealthPotion(true);
        player.removeFromBag(this);
        setRemove(true);
    }   
}
