package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.StaticEntities.StaticEntity;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.util.Direction;

public class GoalsTest {
    @Test
    public void testExitGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyExit", "standard");
        String goalString = controller.getGoalString();
        assertEquals(goalString, ":exit");

        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testEnemyGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("enemy", "standard");
        String goalString = controller.getGoalString();
        assertEquals(goalString, ":enemies");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testExitSpawnerGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spawner", "standard");
        String goalString = controller.getGoalString();
        assertEquals(goalString, ":enemies");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        assertEquals(goalString, ":enemies");
        controller.interact("spawner2");
        assertTrue(DungeonManiaController.getStaticEntities().size() == 0);
        assertTrue(DungeonManiaController.getMovingEntities().size() == 1);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testTreasureGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyTreasure", "standard");
        String goalString = controller.getGoalString();
        assertEquals(goalString, ":treasure");

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testBoulderGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyBoulder", "standard");
        String goalString = controller.getGoalString();
        assertEquals(goalString, ":boulder");

        controller.tick(null, Direction.RIGHT);
        assertEquals(goalString, ":boulder");
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testAndGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyAnd", "standard");
        String goalString = controller.getGoalString();
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), goalString);

        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), goalString);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.LEFT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testOrBouldersGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyOr", "standard");
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testOrExitGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyOr", "standard");
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testOrTreasureGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyOr", "standard");
        controller.tick(null, Direction.LEFT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testAndOrGoal() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("AndOr", "standard");
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(controller.getGoalString(), "");

        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testComplexGoalSwitchExit() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("complexGoal1", "standard");
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(controller.getGoalString(), "");

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testComplexGoalEnemiesExit() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("complexGoal1", "standard");
        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities();
        ZombieToastSpawner spawner = null;
        for (StaticEntity staticEntity : staticList) {
            if (staticEntity.getType().equals("zombie_toast_spawner")) {
                spawner = (ZombieToastSpawner)staticEntity;
            }
        }
        assertFalse(spawner == null);
        controller.tick(null, Direction.DOWN);
        assertNotEquals(controller.getGoalString(), "");

        controller.interact(spawner.getId());
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testComplexGoalTreasureExit() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("complexGoal1", "standard");
        controller.tick(null, Direction.DOWN);
        assertNotEquals(controller.getGoalString(), "");

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), "");
    }

    @Test
    public void testComplexAllGoals() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("complexGoal2", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.DOWN);
        assertNotEquals(controller.getGoalString(), "");

        ArrayList<StaticEntity> staticList = DungeonManiaController.getStaticEntities();
        ZombieToastSpawner spawner = null;
        for (StaticEntity staticEntity : staticList) {
            if (staticEntity.getType().equals("zombie_toast_spawner")) {
                spawner = (ZombieToastSpawner)staticEntity;
            }
        }
        assertFalse(spawner == null);
        assertNotEquals(controller.getGoalString(), "");
        
        controller.tick(null, Direction.LEFT);
        controller.interact(spawner.getId());
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(controller.getGoalString(), "");
    }
}
