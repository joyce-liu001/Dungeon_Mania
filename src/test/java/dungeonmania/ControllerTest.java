package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.Direction;

public class ControllerTest {
    @Test
    public void testNewGame() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse r1 = controller.newGame("advanced", "standard");
        assertEquals(r1.getDungeonName(),"advanced.json");
    }

    @Test
    public void testBadNewGame() {
        DungeonManiaController controller = new DungeonManiaController();
        assertThrows(IllegalArgumentException.class, () -> {
            controller.newGame("advanced", "Easy");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            controller.newGame("notAdvanced", "standard");
        });
    }

    @Test
    public void testSaveGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.saveGame("save1");
        assertTrue(controller.allGames().contains("save1"));

        controller.newGame("advanced", "hard");
        controller.saveGame("save2");
        assertTrue(controller.allGames().contains("save2"));
    }

    @Test
    public void testLoadGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyAnd", "standard");
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        DungeonResponse save1 = controller.saveGame("save1");
        assertTrue(controller.allGames().contains("save1"));
        List<EntityResponse> entityList1 = save1.getEntities();
        List<ItemResponse> itemList1 = save1.getInventory();

        DungeonResponse oldSave = controller.loadGame("save1");
        List<EntityResponse> entityList2 = oldSave.getEntities();
        List<ItemResponse> itemList2 = oldSave.getInventory();
        assertEquals(entityList1, entityList2);
        assertEquals(itemList1, itemList2);
    }

    @Test
    public void testBadLoadGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.saveGame("save1");
        assertTrue(controller.allGames().contains("save1"));

        controller.newGame("maze", "hard");
        controller.saveGame("save2");
        assertTrue(controller.allGames().contains("save2"));

        assertThrows(IllegalArgumentException.class, () -> {
            controller.loadGame("badSave");
        });
    }

    @Test
    public void testSaveAndLoadChangedGame() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("easyAnd", "standard");
        DungeonResponse save1 = controller.saveGame("save1");
        assertTrue(controller.allGames().contains("save1"));
        List<EntityResponse> entityList1 = save1.getEntities();

        // Do things (move around, use items)
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        DungeonResponse save1_1 = controller.saveGame("save1");
        List<EntityResponse> entityList1_1 = save1_1.getEntities();

        assertNotEquals(entityList1, entityList1_1);

        DungeonResponse load1 = controller.loadGame("save1");
        List<EntityResponse> entityList2 = load1.getEntities();
        assertEquals(entityList2, entityList1_1);
    }

    @Test
    public void testAllGames() {
        File dir = new File("./saves");
        for(File file : dir.listFiles()){
            file.delete();
        }
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "peaceful");
        controller.saveGame("save1");

        controller.newGame("advanced", "standard");
        controller.saveGame("save2");

        controller.newGame("advanced", "hard");
        controller.saveGame("save3");

        controller.newGame("maze", "peaceful");
        controller.saveGame("save4");

        controller.newGame("maze", "standard");
        controller.saveGame("save5");

        controller.newGame("maze", "hard");
        controller.saveGame("save6");

        assertEquals(controller.allGames().size(), 6);
    }

    @Test
    public void testTickBadItem() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        assertThrows(IllegalArgumentException.class, () -> {
            controller.tick("badItem", null);
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.tick("bomb", null);
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.tick("invisibility_potion", null);
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.tick("health_potion", null);
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.tick("invincibility_potion", null);
        });
    }

    @Test
    public void testBadBuild() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.build("weapon");
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.build("bow");
        });

        assertThrows(InvalidActionException.class, () -> {
            controller.build("shield");
        });
    }

    // @Test
    // public void testBadMercInteract() {
    //     DungeonManiaController controller = new DungeonManiaController();
    //     controller.newGame("mercenaryAction", "standard");

    //     assertThrows(IllegalArgumentException.class, () -> {
    //         controller.interact("badID");
    //     });

    //     // Not in range to bribe mercenary
    //     assertThrows(InvalidActionException.class, () -> {
    //         controller.interact("Mercenary1");
    //     });

    //     controller.tick(null, Direction.RIGHT);
    //     // Now in range but not enough money
    //     assertThrows(InvalidActionException.class, () -> {
    //         controller.interact("Mercenary1");
    //     });
    // }

    @Test
    public void testBadSpawnerInteract() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("interactSpawner", "standard");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.interact("badID");
        });

        // Not in range to destroy spawner
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("spawner15");
        });

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        // Now in range but not weapon to destroy it
        assertThrows(InvalidActionException.class, () -> {
            controller.interact("spawner15");
        });
    }
}
