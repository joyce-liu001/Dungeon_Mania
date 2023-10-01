package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.MovingEntites.MovingEntity;
import dungeonmania.MovingEntites.Zombie;
import dungeonmania.util.*;

public class ZombieToastSpawner extends StaticEntity {
    int ticks;
    int tickLimit;

    /**
     * Constructor for zombie toast spawner object.
     * @param position Position of spawner.
     * @param difficulty Difficulty to set tick limit.
     * @param id Id of spawner.
     * ticks is the current number of ticks.
     * ticksLimit is the cap when a new zombie is spawned. 
     */
    public ZombieToastSpawner(Position position, String difficulty, String id) {
        super(position, id, "zombie_toast_spawner", true, false);
        ticks = 0;
        if (difficulty.equals("hard")) {
            tickLimit = 15;
        } else {
            tickLimit = 20;
        }
    }

    /**
     * Method that increments ticks and spawns zombies correspondingly
     */
    public void spawn() {
        ticks += 1;
        if (ticks == tickLimit) {
            ticks = 0;
            List<StaticEntity> staticList = DungeonManiaController.
                getStaticEntities();
            List<CarriableEntity> carryList = DungeonManiaController.
                getCarriableEntities();
            List<MovingEntity> movingList = DungeonManiaController.
                getMovingEntities();
            List<Entity> entityList = new ArrayList<>();
            entityList.addAll(staticList);
            entityList.addAll(carryList);
            entityList.addAll(movingList);
            MovingEntity zombie = addZombie(entityList);
            if (zombie != null) {
                movingList.add(zombie);
            }
        }
    }

    /**
     * Makes a new zombie in a cardinally adjacent square.
     * @param entityList List of entities.
     * @return new a Zombie Toast enemy.
     */
    public MovingEntity addZombie(List<Entity> entityList) {
        List<Position> usedTiles = new ArrayList<Position>();
        Zombie zombie = null;
        for (Entity e : entityList) {
            if (Position.isAdjacent(e.getPosition(), getPosition())) {
                usedTiles.add(e.getPosition());
            }
        }
        for (Direction d : Direction.values()) {
            Position newPos = getPosition().translateBy(d);
            if (!(d.equals(Direction.NONE)) && (!usedTiles.contains(newPos))) {
                Position newPosition = getPosition().translateBy(d);
                zombie = new Zombie(new Position(newPosition.getX(), 
                    newPosition.getY()));
            }
        }
        return zombie;
    }
    
}
