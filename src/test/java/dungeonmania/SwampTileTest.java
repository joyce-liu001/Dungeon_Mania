package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntites.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SwampTileTest {
    public ArrayList<StaticEntity> hollowBox(int x, int y) {
        ArrayList<StaticEntity> entityList = new ArrayList<>();
        // Make a hollow box
        int count = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (i == 0 || j == 0 || i == x - 1 || j == y - 1) {
                    entityList.add(new Wall(new Position(i, j), 
                        Integer.toString(count)));
                    ++count;
                }
            }
        }
        return entityList;
    }

    // Test a player moving through swamp tile (should have no affect)
    @Test
    public void testPlayerSwampTile() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(5, 3);
        SwampTile swampTile = new SwampTile(new Position(2, 1), "swampTile", 2);
        walls.add(swampTile);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), swampTile.getPosition());
        
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(player.getPosition(), swampTile.getPosition());
        assertEquals(player.getPosition(), new Position(3, 1));
    }

    // Test a zombie moving on the swamp tile (should have an effect)
    @Test
    public void testZombieSwampTile() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(4, 3);
        SwampTile swampTile = new SwampTile(new Position(2, 1), "swampTile", 2);
        walls.add(swampTile);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(5, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> zombieList = new ArrayList<>();
        Zombie zombie = new Zombie(new Position(1,1));
        zombieList.add(zombie);
        DungeonManiaController.setMovingEntities(zombieList);
        
        controller.tick(null, Direction.DOWN);
        assertEquals(zombie.getPosition(), swampTile.getPosition());

        // Should still be stuck
        controller.tick(null, Direction.DOWN);
        assertEquals(zombie.getPosition(), swampTile.getPosition());
        // One more tick to get it unstuck
        controller.tick(null, Direction.DOWN);

        assertNotEquals(zombie.getPosition(), swampTile.getPosition());
        assertTrue(Position.isAdjacent(zombie.getPosition(), swampTile.getPosition()));

        controller.tick(null, Direction.DOWN);
        // Back on the swamp tile
        assertEquals(zombie.getPosition(), swampTile.getPosition());
        // Try to move again (should be stuck again)
        controller.tick(null, Direction.DOWN);
        assertEquals(zombie.getPosition(), swampTile.getPosition());
        // One more tick to get it unstuck
        controller.tick(null, Direction.DOWN);

        assertNotEquals(zombie.getPosition(), swampTile.getPosition());
        assertTrue(Position.isAdjacent(zombie.getPosition(), swampTile.getPosition()));
    }

    // Test a mercenary moving on the swamp tile (should have an effect)
    @Test
    public void testMercSwampTile() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(10, 3);
        SwampTile swampTile = new SwampTile(new Position(2, 1), "swampTile", 2);
        walls.add(swampTile);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(4, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> mercList = new ArrayList<>();
        Mercenary mercenary = new Mercenary(new Position(1,1));
        mercList.add(mercenary);
        player.attach(mercenary);
        DungeonManiaController.setMovingEntities(mercList);
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(mercenary.getPosition(), swampTile.getPosition());

        // Should still be stuck
        controller.tick(null, Direction.RIGHT);
        assertEquals(mercenary.getPosition(), swampTile.getPosition());

        controller.tick(null, Direction.RIGHT);
        assertNotEquals(mercenary.getPosition(), swampTile.getPosition());
    }

    // Test a mercenary moving on the swamp tile (should have an effect)
    @Test
    public void testLongSwampTile() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(10, 3);
        SwampTile swampTile = new SwampTile(new Position(2, 1), "swampTile", 5);
        walls.add(swampTile);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(4, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> mercList = new ArrayList<>();
        Mercenary mercenary = new Mercenary(new Position(1,1));
        mercList.add(mercenary);
        player.attach(mercenary);
        DungeonManiaController.setMovingEntities(mercList);
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(mercenary.getPosition(), swampTile.getPosition());

        // Should still be stuck
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
            assertEquals(mercenary.getPosition(), swampTile.getPosition());
        }
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(mercenary.getPosition(), swampTile.getPosition());
    }
}
