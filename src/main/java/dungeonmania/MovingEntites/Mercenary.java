package dungeonmania.MovingEntites;

import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.DungeonManiaController;
import dungeonmania.StaticEntities.Door;
import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Mercenary extends Enemy implements Observer{
    private Subject subject;
    public static Random r = new Random(888);
    private boolean isAlly;
    private boolean isScared;
    private static int id = 0;
    private int mindContorlTime = 0;

    public Mercenary(Position p) {
        this.setIsInteractable(true);
        String mode = DungeonManiaController.getGameMode();
        id += 1;
        String curId = "Mercenary" + id;
        this.setId(curId);
        if(Objects.equals(mode, "standard") || Objects.equals(mode, "hard")) {
            this.attackDamage = 20;
            int randomNum = r.nextInt(4);
            if (randomNum > 2) {
                this.setArmoured(true);
            }
        } else {
            this.attackDamage = 0;
        }
        this.health = 40.0;
        this.fullHealth = 40.0;
        this.isAlly = false;
        this.isScared = false;
        this.setPosition(p);
        this.setType("mercenary");
    }

    /**
     * ObserverPattern, mercenary need update position.
     * @param obj the Subject which will notify all observer to update the position
     */
    @Override
    public void update(Subject obj) {
        Player player = (Player) obj;
        int count = 1;
        if (player.getStatus().contains(PlayerStatus.BATTLE)) {
            count = 2;
        }
        for(int i = 0; i < count; i++) {
            if (player.getStatus().contains(PlayerStatus.INVINCIBLE)) { 
                isScared = true;
            }
            move(Direction.NONE);
        }
    }

    /**
     * the player tries to bride the Mercenary
     * if the player does not have enough treasure or the player is too far away from the Assassin
     * it will throw exception
     * @param player the player in the map
     */
    public void bride(Player player) {
        Position mP = this.getPosition();
        Position p = player.getPosition();
        if (Position.isAdjacent(mP, p)) {
            if (player.getTheSceptre()) {
                this.setAlly(true);
                this.setMindContorlTime(10);
                this.setIsInteractable(false);
                return;
            }
            if (player.getBalance() >= 1) {
                for (CarriableEntity C : DungeonManiaController.getPlayer().getBag()) {
                    if (C.getType().equals("treasure") || C.getType().equals("sun_stone")) {
                        C.use(DungeonManiaController.getPlayer());
                        break;
                    }
                }
            } else {
                throw new InvalidActionException("you dont have enough money");
            }
        } else {
            throw new InvalidActionException("you are far away from the mercenary");
        }
        this.isAlly = true;
        this.setIsInteractable(false);
    }

    /**
     * move this object to certain direction
     * @param direction target direction
     */
	@Override
	public void move(Direction direction) {
		// TODO:moving toward to player position, if player is in battle, move faster
        HashMap<Position, Position> dijkstraPrev = DungeonManiaController.getDijkstra();
        if (this.isCanMove()) {
            Position target = this.getPosition();
            Position key = target.asLayer(0);
            if (dijkstraPrev.containsKey(key)) {
                Position newPos = dijkstraPrev.get(key);
                if (newPos != null) {
                    if (isScared) {
                        Position difference = Position.calculatePositionBetween(getPosition(), newPos);
                        if (Math.abs(difference.getX()) == 1) {
                            target = target.translateBy(-difference.getX(), 0);
                        } else if (Math.abs(difference.getY()) == 1) {
                            target = target.translateBy(0, -difference.getY());
                        }
                        isScared = false;
                    } else {
                        target = newPos.asLayer(target.getLayer());
                    }
                }
            }
            if (!limitMove(target)) {
                // todo: moving through portal
                if (this.isAlly && DungeonManiaController.getPlayer().getPosition().equals(target)) {
                    return;
                }
                this.setPosition(target);
            }
        }
	}

    /**
     * check if mercenary is able to move to that position
     * @param position targetPosition
     * @return true if the mercenary can move to target position
     */
    public boolean limitMove(Position position) {
        for (StaticEntity s : DungeonManiaController.getStaticEntities()) {
            if (s.getType().equals("wall") && s.getPosition().equals(position))
                return true;
            if (s.getType().equals("boulder") && s.getPosition().equals(position))
                return true;
            if (s.getType().equals("portal") && s.getPosition().equals(position))
                return true;
            if (s.getType().equals("door") && s.getPosition().equals(position)) {
                Door door = (Door) s;
                //TODO: make sure door state?
                return door.canMove(this);
            }
            if (s.getType().equals("zombie_toast_spawner") && s.getPosition().equals(position))
                return true;
        }
        return false;
    }

    /**
     * check if the mercenary is an ally or not
     * @return true if the mercenary is not hostile
     */
    public boolean isAlly() {
        return isAlly;
    }

    public void setAlly(boolean ally) {
        isAlly = ally;
    }

    public int getMindContorlTime() {
        return mindContorlTime;
    }

    public void setMindContorlTime(int mindContorlTime) {
        this.mindContorlTime = mindContorlTime;
    }
}
