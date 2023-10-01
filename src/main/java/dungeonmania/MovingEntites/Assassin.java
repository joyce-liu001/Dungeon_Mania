package dungeonmania.MovingEntites;

import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

import java.util.Objects;

public class Assassin extends Mercenary{
    private static int id = 0;
    public Assassin(Position p) {
        super(p);
        id += 1;
        String curId = "Assassin" + id;
        this.setId(curId);
        this.setHealth(80.0);
        if (!Objects.equals(DungeonManiaController.getGameMode(), "peaceful")) {
            this.attackDamage = 40;
        }
    }

    /**
     * player tries to bride the Assassin
     * if the player does not have enough treasure or the player is too far away from the Assassin
     * it will throw exception
     * @param player the one player in the map
     */
    @Override
    public void bride(Player player) {
        Position mP = this.getPosition();
        Position p = player.getPosition();
        if (Position.isAdjacent(mP, p)) {
            if (player.getTheSceptre()) {
                this.setAlly(true);
                this.setMindContorlTime(10);
                return;
            }
            if (player.getBalance() >= 1) {
                for (CarriableEntity C : DungeonManiaController.getPlayer().getBag()) {
                    if (C.getType().equals("treasure") || C.getType().equals("sun_stone")) {
                        CarriableEntity c1 = player.getTheRing();
                        if (c1 != null) {
                            C.use(player);
                            c1.use(player);
                            break;
                        } else {
                            throw new InvalidActionException("you dont have the Ring");
                        }
                    }
                }
            } else {
                throw new InvalidActionException("you dont have enough money");
            }
        } else {
            throw new InvalidActionException("you are far away from the mercenary");
        }
        this.setAlly(true);
        this.setIsInteractable(false);
    }
}
