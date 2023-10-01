package dungeonmania.MovingEntites;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.json.JSONObject;

public class Spider extends Enemy {
    private static int id = 0;
     static boolean isMovable(Position position) {
         HashMap<String, ArrayList<? extends Entity>> map = DungeonManiaController.getMap();
        for (Entity entity : map.get("staticEntities")) {
            if (entity.getType().equals("boulder") && entity.getPosition().equals(position))
                return false;
        }
        return true;
    }

    private int movingStatus; // Represent the moving status
    private boolean movingDirection; //True for clockwise, false for anti-clockwise

    public Spider(Position p) {
        String mode = DungeonManiaController.getGameMode();
        this.movingStatus = -1;
        this.movingDirection = true;
        this.health = 20.0;
        this.fullHealth = 20.0;
        if(Objects.equals(mode, "standard") || Objects.equals(mode, "hard")) {
            this.attackDamage = 10;
        } else {
            this.attackDamage = 0;
        }
        id += 1;
        String curId = "Spider" + id;
        this.setId(curId);
        this.setType("spider");
        this.setPosition(p);
    }

    /**
     * find the next direction that the spider should go
     */
    public void setNextMovingStatus() {
        if (getMovingDirection())
            this.movingStatus = (this.movingStatus + 1) % 8;
        else
            this.movingStatus = (this.movingStatus - 1) % 8;
        if (getMovingStatus() == -1)
            this.movingStatus = 7;
        if (getMovingStatus() == -2) {
            this.movingStatus = 4;
            modifyMovingDirection();
        }
    }

    public void modifyMovingDirection() {
        this.movingDirection = !this.movingDirection;
    }

    public int getMovingStatus() {
        return movingStatus;
    }

    public boolean getMovingDirection() {
        return movingDirection;
    }

    /**
     * move the spider in specific direction
     * @param direction target direction
     */
    @Override
    public void move(Direction direction) {
        //TODO: Add restriction
        if (this.isCanMove()) {
            Position currPosition = this.getPosition();
            Position targetPosition = currPosition;
            if (DungeonManiaController.getPlayer().getStatus().contains(PlayerStatus.INVINCIBLE)) {
                if (!this.isCloseDirection(this.getPosition(), DungeonManiaController.getPlayer().getPosition(), Direction.UP)
                    && isMovable(currPosition.translateBy(Direction.UP)))
                    targetPosition = currPosition.translateBy(Direction.UP);
                if (!this.isCloseDirection(this.getPosition(), DungeonManiaController.getPlayer().getPosition(), Direction.DOWN)
                    && isMovable(currPosition.translateBy(Direction.DOWN)))
                    targetPosition = currPosition.translateBy(Direction.DOWN);
                if (!this.isCloseDirection(this.getPosition(), DungeonManiaController.getPlayer().getPosition(), Direction.LEFT)
                    && isMovable(currPosition.translateBy(Direction.LEFT)))
                    targetPosition = currPosition.translateBy(Direction.LEFT);
                if (!this.isCloseDirection(this.getPosition(), DungeonManiaController.getPlayer().getPosition(), Direction.RIGHT)
                    && isMovable(currPosition.translateBy(Direction.RIGHT)))
                    targetPosition = currPosition.translateBy(Direction.RIGHT);
            }
            else {
                switch(this.movingStatus) {
                    case -1: // UP for clockwise and DOWN for anti-clockwise
                    case 6:
                    case 5:
                        if (getMovingDirection()) // If is clockwise, go UP , otherwise go DOWN
                            targetPosition = currPosition.translateBy(Direction.UP);
                        else
                            targetPosition = currPosition.translateBy(Direction.DOWN);
        //                targetPosition = new Position(x, y - this.getMovingDirection(), this.getPosition().getLayer());
                        if (!isMovable(targetPosition)) { // If it is blocked, change direction
                            this.modifyMovingDirection();
                            targetPosition = currPosition;
                            if (!isMovable(targetPosition)) { // If it is also block, keep in the same place
                                targetPosition = this.getPosition();
                                break;
                            }
                        }
                        this.setNextMovingStatus();
                        break;
                    case 0: // RIGHT for clockwise and LEFT for anti-clockwise
                    case 7:
                        if (getMovingDirection()) // If is clockwise go RIGHT, otherwise go LEFT
                            targetPosition = currPosition.translateBy(Direction.RIGHT);
                        else
                            targetPosition = currPosition.translateBy(Direction.LEFT);
                        if (!isMovable(targetPosition)) { // If it is blocked, change direction
                            targetPosition = currPosition;
                            this.modifyMovingDirection();
                            if (!isMovable(targetPosition)) { // If it is also block, keep in the same place
                                targetPosition = this.getPosition();
                                break;
                            }
                        }
                        this.setNextMovingStatus();
                        break;
                    case 1: // DOWN for clockwise and UP for anti-clockwise
                    case 2:
                        if (getMovingDirection()) // If is clockwise go DOWN, otherwise go UP
                            targetPosition = currPosition.translateBy(Direction.DOWN);
                        else
                            targetPosition = currPosition.translateBy(Direction.UP);
                        if (!isMovable(targetPosition)) { // If it is blocked, change direction
                            targetPosition = currPosition;
                            this.modifyMovingDirection();
                            if (!isMovable(targetPosition)) { // If it is also block, keep in the same place
                                targetPosition = this.getPosition();
                                break;
                            }
                        }
                        this.setNextMovingStatus();
                        break;
                    case 3: //LEFT
                    case 4:
                        if (getMovingDirection()) // If is clockwise go LEFT, otherwise go RIGHT
                            targetPosition = currPosition.translateBy(Direction.LEFT);
                        else
                            targetPosition = currPosition.translateBy(Direction.RIGHT);
                        if (!isMovable(targetPosition)) { // If it is blocked, change direction
                            targetPosition = currPosition;
                            this.modifyMovingDirection();
                            if (!isMovable(targetPosition)) { // If it is also block, keep in the same place
                                targetPosition = this.getPosition();
                                break;
                            }
                        }
                        this.setNextMovingStatus();
                        break;
            }
            }
            this.setPosition(targetPosition);
        }
    }
}
