package dungeonmania.Goals;

import dungeonmania.DungeonManiaController;

public class BouldersGoal extends GoalComponent {

    /** 
     * Constructor for boulders goal
     */
    public BouldersGoal() {
        setGoalType("boulders");
    }

    @Override
    public Boolean requirementCheck() {
        return DungeonManiaController.boulderCheck();
    }

    @Override
    public String goalPrint() {
        return ":boulder";
    }
}