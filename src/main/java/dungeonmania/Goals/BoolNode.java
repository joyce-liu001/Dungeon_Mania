package dungeonmania.Goals;

public abstract class BoolNode {
    private Boolean result;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    /**
     * Evaluates the goal node
     * @return A boolean depending if the goal requirement has been met or not.
     */
    public abstract Boolean evaluate();

    /**
     * Prints out the goal string
     * @return The goal string for the node.
     */
    public abstract String goalPrint();

    /**
     * Adds a goal node to the list of goals
     * @param node New goal node to be added
     */
    public abstract void addNode(BoolNode node);
}

