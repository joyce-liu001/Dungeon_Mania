package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
public class LogicSwitchTest {
    @Test
    public void testSwitchLightAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        LightBulb lightBulb = new LightBulb(new Position(2, 1), "light1", "and");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities();
        staticList.add(lightBulb);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(lightBulb.isOn()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(lightBulb.isOn());
        // second switch on
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(lightBulb.isOn());
        // third switch on
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(lightBulb.isOn());
    }

    @Test
    public void testSwitchDoorAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        SwitchDoor door = new SwitchDoor(new Position(2, 1), 1, "switch_door1",  "and");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities(); 
        staticList.add(door);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(door.getState() instanceof OpenDoorState); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(door.getState() instanceof OpenDoorState);
        // second switch on
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(door.getState() instanceof OpenDoorState);
        // third switch on
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(door.getState() instanceof OpenDoorState);
    }

    @Test
    public void testSwitchBombAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        Bomb bomb = new Bomb(new Position(2, 1), "and");
        DungeonManiaController.getCarriableEntities().add(bomb);
        assertFalse(DungeonManiaController.getStaticEntities().isEmpty()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(DungeonManiaController.getStaticEntities().isEmpty());
        // second switch on
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);

        assertTrue(DungeonManiaController.getStaticEntities().isEmpty());
    }

    @Test
    public void testSwitchLightOr() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        LightBulb lightBulb = new LightBulb(new Position(2, 1), "light1", "or");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities(); 
        staticList.add(lightBulb);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(lightBulb.isOn()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(lightBulb.isOn());
        // second switch on, the light also be activated
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(lightBulb.isOn());
    }
    
    @Test
    public void testSwitchDoorOr() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        SwitchDoor door = new SwitchDoor(new Position(2, 1), 1, "switch_door1",  "or");

        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities(); 
        staticList.add(door);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(door.getState() instanceof OpenDoorState); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(door.getState() instanceof OpenDoorState);
        // second switch on, the light also be activated
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertTrue(door.getState() instanceof OpenDoorState);
    }

    @Test
    public void testSwitchBombOr() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        Bomb bomb = new Bomb(new Position(2, 1), "or");
        DungeonManiaController.getCarriableEntities().add(bomb);
        assertFalse(DungeonManiaController.getStaticEntities().isEmpty()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(DungeonManiaController.getStaticEntities().size() == 1);
    }

    @Test
    public void testSwitchLightNot() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        LightBulb lightBulb = new LightBulb(new Position(2, 1), "light_bulb1", "not");
        DungeonManiaController.getStaticEntities().add(lightBulb);

        controller.tick(null, Direction.NONE);
        assertTrue(lightBulb.isOn()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(lightBulb.isOn());
    }

    @Test
    public void testSwitchDoorNot() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        SwitchDoor door = new SwitchDoor(new Position(2, 1), 1, "switch_door1",  "not");
        DungeonManiaController.getStaticEntities().add(door);
        controller.tick(null, Direction.NONE);
        assertTrue(door.getState() instanceof OpenDoorState); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(door.getState() instanceof OpenDoorState);
    }
    
    @Test
    public void testSwitchLightXor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        LightBulb lightBulb = new LightBulb(new Position(2, 1), "light1", "xor");
        DungeonManiaController.getStaticEntities().add(lightBulb);
        assertFalse(lightBulb.isOn()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(lightBulb.isOn());
        // second switch on, the light cannot be activated
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertFalse(lightBulb.isOn());
    }

    @Test
    public void testSwitchDoorXor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        SwitchDoor door = new SwitchDoor(new Position(2, 1), 1, "switch_door1",  "xor");
        DungeonManiaController.getStaticEntities().add(door);
        assertFalse(door.getState() instanceof OpenDoorState); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(door.getState() instanceof OpenDoorState);
        // second switch on, the light cannot be activated
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        assertFalse(door.getState() instanceof OpenDoorState);
    }

    @Test
    public void testSwitchBombXor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitch", "standard");
        Bomb bomb = new Bomb(new Position(2, 1), "xor");        
        DungeonManiaController.getCarriableEntities().add(bomb);
        assertFalse(DungeonManiaController.getStaticEntities().isEmpty()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertTrue(DungeonManiaController.getStaticEntities().size() == 1);
    }

    @Test
    public void testSwitchLightCoAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitchCoAnd", "standard");
        LightBulb lightBulb = new LightBulb(new Position(1, 1), "co_and");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities(); 
        staticList.add(lightBulb);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(lightBulb.isOn()); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(lightBulb.isOn());
    }
    
    @Test
    public void testSwitchDoorCoAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitchCoAnd", "standard");
        SwitchDoor door = new SwitchDoor(new Position(1, 1), 1, "switch_door1",  "co_and");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities(); 
        staticList.add(door);
        DungeonManiaController.setStaticEntities(staticList);
        assertFalse(door.getState() instanceof OpenDoorState); 
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(door.getState() instanceof OpenDoorState);
    }

    @Test
    public void testSwitchBombCoAnd() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("logicSwitchCoAnd", "standard");
        Bomb bomb = new Bomb(new Position(1, 1), "xor");        
        DungeonManiaController.getCarriableEntities().add(bomb);
        assertFalse(bomb.getIsRemove());  
        // first switch on
        controller.tick(null, Direction.UP);
        assertFalse(bomb.getIsRemove());  
    }

    @Test
    public void testBlubOn() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("bulbOn", "standard");
        
        LightBulb lightBulb = null;
        for(StaticEntity e : DungeonManiaController.getStaticEntities()) {
            if (e instanceof LightBulb) {
                lightBulb = (LightBulb) e;
            }
        }
        assertFalse(lightBulb.isOn());
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        assertTrue(lightBulb.isOn());
    }

    @Test
    public void testWire() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("wire", "standard");
        
        SwitchDoor door = new SwitchDoor(new Position(3, 1), 1, "door1");
        DungeonManiaController.getStaticEntities().add(door);
        LightBulb lightBulb = new LightBulb(new Position(3, 5), "wire1");
        DungeonManiaController.getStaticEntities().add(lightBulb);
        Bomb bomb = new Bomb(new Position(5, 3));
        DungeonManiaController.getCarriableEntities().add(bomb);
        
        assertTrue(door.getState() instanceof ClosedDoorState);
        assertFalse(lightBulb.isOn());
        
        controller.tick(null, Direction.RIGHT);
        // test door open
        assertTrue(door.getState() instanceof OpenDoorState);

        // test bulb on
        assertTrue(lightBulb.isOn());

        // test bomb blast
        assertTrue(DungeonManiaController.getStaticEntities().size() == 6);
    }
}
