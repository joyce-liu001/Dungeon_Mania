package dungeonmania.MovingEntites;

import dungeonmania.StaticEntities.Door;
import java.util.Objects;
import java.util.Random;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.HashMap;

public class Zombie extends Enemy{
    public static Random r = new Random(888);
    private static int id = 0;

    public Zombie(Position p) {
        String mode = DungeonManiaController.getGameMode();
        if(Objects.equals(mode, "peaceful")) {
            this.attackDamage = 0;
        } else {
            this.attackDamage = 10;
            Random r = new Random();
            int randomNum = r.nextInt(4);
            if (randomNum > 2) {
                this.setArmoured(false);
            }
        }
        this.health = 80.0;
        this.fullHealth = 80.0;
        id += 1;
        String curId = "Zombie" + id;
        this.setId(curId);
        this.setType("zombie");
        this.setPosition(p);
    }

    /**
     * check if the zombie can move to the position
     * @param position target position
     * @return false if there is a barrier
     */
    public boolean isMovable(Position position) {
        HashMap<String, ArrayList<? extends Entity>> map = DungeonManiaController.getMap();
        for (Entity entity : map.get("staticEntities")) {
            if (entity.getType().equals("boulder") && entity.getPosition().equals(position)) {
                return false;
            } else if (entity.getType().equals("door") && entity.getPosition().equals(position)) {
                Door door = (Door) entity;
                return door.canMove(this);
            } else if (entity.getType().equals("wall") && entity.getPosition().equals(position)) {
                return false;
            } else if (entity.getType().equals("zombie_toast_spawner") && entity.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether the zombie is adjacent with the given position
     *
     * @param start the given position
     * @return boolean whether the zombie is adjacent with the given position
     */
    public boolean checkAdjacent(Position start) {
        return (isMovable(start.translateBy(Direction.DOWN)) ||
            isMovable(start.translateBy(Direction.UP)) ||
            isMovable(start.translateBy(Direction.LEFT)) ||
            isMovable(start.translateBy(Direction.RIGHT)));
    }

    @Override
    public void move(Direction direction) {
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
            } else {
                targetPosition = currPosition;
                if (checkAdjacent(currPosition)) {
                    while (currPosition.equals(targetPosition)) {
                        int randomNum = r.nextInt(5);
                        switch (randomNum) {
                            case 1:
                                targetPosition = currPosition.translateBy(Direction.UP);
                                if (!isMovable(targetPosition))
                                    targetPosition = currPosition;
                                break;
                            case 2:
                                targetPosition = currPosition.translateBy(Direction.DOWN);
                                if (!isMovable(targetPosition))
                                    targetPosition = currPosition;
                                break;
                            case 3:
                                targetPosition = currPosition.translateBy(Direction.LEFT);
                                if (!isMovable(targetPosition))
                                    targetPosition = currPosition;
                                break;
                            case 4:
                                targetPosition = currPosition.translateBy(Direction.RIGHT);
                                if (!isMovable(targetPosition))
                                    targetPosition = currPosition;
                                break;
                        }
                    }
                }
            }
            this.setPosition(targetPosition);

        }

    }

}
