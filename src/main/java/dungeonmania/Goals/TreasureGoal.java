package dungeonmania.Goals;

import dungeonmania.DungeonManiaController;

public class TreasureGoal extends GoalComponent {

    /** 
     * Constructor for treasure goal
     */
    public TreasureGoal() {
        setGoalType("treasure");
    }
    
    @Override
    public Boolean requirementCheck() {
        return DungeonManiaController.treasureGoal();
    }

    @Override
    public String goalPrint() {
        return ":treasure";
    }
}