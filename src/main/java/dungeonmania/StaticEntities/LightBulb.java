package dungeonmania.StaticEntities;

import dungeonmania.util.Position;

public class LightBulb extends StaticEntity {
    Boolean isOn;
    String logic = null;
    
    /**
     * Constructor for LightBulb static entity
     * @param position Location of the LightBulb
     * @param id String, id for LightBulb 
     */
    public LightBulb(Position position, String id) {
        super(position, id, "light_bulb_off", false, false);
        isOn = false;
    }

    /**
     * Constructor for LightBulb static entity
     * @param position Location of the LightBulb
     * @param id String, id for LightBulb
     * @param logic String for logic 
     */
    public LightBulb(Position position, String id, String logic) {
        super(position, id, "light_bulb_off", false, false);
        this.logic = logic;
        this.isOn = false;
    }

    /**
     * getter for logic
     * @return the logic of the lightbulb
     */
    public String getLogic() {
        return logic;
    }

    /**
     * setter for state
     * @param boolean to set the state
     */
    public void setState(Boolean isOn) {
        if (isOn) {
            super.setType("light_bulb_on");
        } else {
            super.setType("light_bulb_off");
        }
        this.isOn = isOn;
    }

    /**
     * getter for state
     * @return boolean to see weather the lightbulb is on
     */
    public boolean isOn() {
        return isOn;
    }
}
