package dungeonmania.Goals;

import java.util.ArrayList;

public abstract class BoolComponent extends BoolNode {
    ArrayList<BoolNode> boolNodes;
    
    /**
     * Constructor for a boolean component
     * @param boolNodes An arraylist containing the other nodes connected to 
     * this current node
     */
    public BoolComponent(ArrayList<BoolNode> boolNodes) {
        this.boolNodes = boolNodes;
    }

    public ArrayList<BoolNode> getBoolNodes() {
        return boolNodes;
    }    

    @Override
    public void addNode(BoolNode node) {
        boolNodes.add(node);
    }
}