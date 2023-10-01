package dungeonmania.Goals;

import dungeonmania.DungeonManiaController;

public class EnemiesGoal extends GoalComponent {

    /** 
     * Constructor for enemies goal
     */
    public EnemiesGoal() {
        setGoalType("enemies");
    }
    
    @Override
    public Boolean requirementCheck() {
        return DungeonManiaController.zeroEnemies();
    }

    @Override
    public String goalPrint() {
        return ":enemies";
    }
}
