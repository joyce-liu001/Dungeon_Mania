package dungeonmania.Goals;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalCheck {
    /**
     * Converts a JSONObject into a boolean node that contains other nodes
     * corresponding to the different goals and how they are linked together. 
     * Then the function evaluates the node.
     * @param json JSONObject containing all the information about the boolean 
     * nodes.
     * @return A boolean evaluating the node, or false if the node is null.
     */
    public static boolean evaluate(JSONObject json) {
        BoolNode node = GoalCheck.construct(json);
        return node != null ? node.evaluate() : false;
    }

    /**
     * Converts a JSONObject into a boolean node that contains other nodes 
     * corresponding to the different goals and how they are linked together.
     * Then the function prints out the overall goal string of that node. 
     * @param json JSONObject containing all the information about the boolean
     * nodes.
     * @return The goal string of the newly created boolean node.
     */
    public static String goalString(JSONObject json) {
        BoolNode node = GoalCheck.construct(json);
        return node != null ? node.goalPrint() : "";
    }

    /**
     * Factory method that creates boolean nodes based off the given JSONObject.
     * @param json JSONObject containing all the information about the boolean
     * nodes.
     * @return The head boolean node containg all the information about the rest
     * of the nodes. 
     */
    public static BoolNode construct(JSONObject json) {
        if (json == null) {return null;}
        Iterator<String> keyList = json.keys();
        // Assume that head node will always be initialised before subnodes are
        // considered
        BoolNode head = null;
        while (keyList.hasNext()) {
            String currKey = keyList.next();
            if (currKey.equals("goal")) {
                String goalType = json.getString(currKey);
                if (goalType.equals("AND")) {
                    head = new AndNode();
                } else if (goalType.equals("exit")) {
                    return new ExitGoal();
                } else if (goalType.equals("enemies")) {
                    return new EnemiesGoal();
                } else if (goalType.equals("boulders")) {
                    return new BouldersGoal();
                } else if (goalType.equals("treasure")) {
                    return new TreasureGoal();
                } else {
                    head = new OrNode();
                } 
            } else {
                JSONArray subgoals = json.getJSONArray(currKey);
                for (int i = 0; i < subgoals.length(); i++) {
                    JSONObject goalObject = subgoals.getJSONObject(i);
                    head.addNode(construct(goalObject));
                }
            }
        }
        return head;
    }
}
