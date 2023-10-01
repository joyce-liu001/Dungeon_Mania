package dungeonmania.Goals;

public abstract class GoalComponent extends BoolNode {
    String goalType;

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }
    
    /**
     * Check to see if the goal requirement has been met or not
     * @return Boolean depending on if goals have been satisfied or not
     */
    public abstract Boolean requirementCheck();

    @Override
    public Boolean evaluate() {
        return requirementCheck();
    }
    
    @Override
    public void addNode(BoolNode node) {
    }
}
