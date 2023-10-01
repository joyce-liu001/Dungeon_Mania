package dungeonmania.Goals;

import dungeonmania.DungeonManiaController;

public class ExitGoal extends GoalComponent {
    
    /** 
     * Constructor for exit goal
     */
    public ExitGoal() {
        setGoalType("exit");
    }

    @Override
    public Boolean requirementCheck() {
        return DungeonManiaController.playerOnExit();
    }

    @Override
    public String goalPrint() {
        return ":exit";
    }
}
