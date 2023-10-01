package dungeonmania.Goals;

import java.util.ArrayList;

public class AndNode extends BoolComponent {
    /**
     * Constructor for a node to AND two goals together
     */
    public AndNode() {
        super(new ArrayList<>());
    }
    
    @Override
    public Boolean evaluate() {
        Boolean result = getBoolNodes().get(0).evaluate();
        for (int i = 1; i < getBoolNodes().size(); i++) {
            result = result && getBoolNodes().get(i).evaluate();
        }
        return result;
    }

    @Override
    public String goalPrint() {
        String goalString = "( " + getBoolNodes().get(0).goalPrint();
        for (int i = 1; i < getBoolNodes().size(); i++) {
            goalString += " AND " + getBoolNodes().get(i).goalPrint();
        }
        return goalString + " )";
    }
}
