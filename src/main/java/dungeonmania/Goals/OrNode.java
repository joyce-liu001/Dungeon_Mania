package dungeonmania.Goals;

import java.util.ArrayList;

public class OrNode extends BoolComponent {
    /**
     * Constructor for a node to OR two goals together
     */
    public OrNode() {
        super(new ArrayList<>());
    }

    @Override
    public Boolean evaluate() {
        Boolean result = getBoolNodes().get(0).evaluate();
        for (int i = 1; i < getBoolNodes().size(); i++) {
            result = result || getBoolNodes().get(i).evaluate();
        }
        return result;
    }

    @Override
    public String goalPrint() {
        String goalString = "( " + getBoolNodes().get(0).goalPrint();
        for (int i = 1; i < getBoolNodes().size(); i++) {
            goalString += " OR " + getBoolNodes().get(i).goalPrint();
        }
        return goalString + " )";
    }
}
