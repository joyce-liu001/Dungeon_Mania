package dungeonmania.StaticEntities;

import java.util.ArrayList;
import java.util.HashMap;

import dungeonmania.MovingEntites.Enemy;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;
    private HashMap<Enemy, Integer> enemyTicks = new HashMap<>();
    private HashMap<Enemy, Boolean> enemyCanMove = new HashMap<>();
    /**
     * Constructor for the swamp tile.
     * @param position Position of the swamp tile.
     * @param id Id given to the swamp tile.
     * enemyTicks keeps track of how long a certain enemy is stuck for. 
     * enemyCanMove keeps track of if an enemy can move or not. 
     */
    public SwampTile(Position position, String id, int movementFactor) {
        super(position, id, "swamp_tile", false, false);
        this.movementFactor = movementFactor;
    }
    
    public int getMovementFactor() {
        return movementFactor;
    }
    
    /**
     * Add an enemy to HashMap of enemies that are stuck. 
     * @param enemy Enemy that is stuck. 
     */
    public void addEnemy(Enemy enemy) {
        if (enemyTicks.containsKey(enemy)) {
            update();
        } else {    
            enemyTicks.put(enemy, 1);
            enemyCanMove.put(enemy, false);
            enemy.setCanMove(false);
        }
    }

    /** 
     * Adds 1 tick to each enemy stuck and frees them if the required ticks 
     * has been reached. 
     */
    public void update() {
        for (Enemy enemy : enemyTicks.keySet()) {
            Integer currTick = enemyTicks.get(enemy);
            enemyTicks.replace(enemy, currTick + 1);
            if (currTick + 1 == movementFactor) {
                enemyCanMove.replace(enemy, true);
            } 
        }
        ArrayList<Enemy> removeList = new ArrayList<>();
        for (Enemy enemy : enemyCanMove.keySet()) {
            if (enemyCanMove.get(enemy)) {
                enemy.setCanMove(true);
                removeList.add(enemy);
            }
        }
        for (Enemy enemy : removeList) {
            enemyTicks.remove(enemy);
            enemyCanMove.remove(enemy);
        }
    }
}
