package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.*;
import dungeonmania.MovingEntites.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class StaticEntitiesTest {
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

    // Test a player stuck in a 3x3 box
    @Test
    public void testPlayerWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(3, 3);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        Position start = new Position(1, 1);
        controller.tick(null, Direction.DOWN);
        assertEquals(player.getPosition(), start);
    }

    // Test a zombie stuck in a 3x3 box
    @Test
    public void testEnemyWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(3, 3);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(5, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> zombieList = new ArrayList<>();
        zombieList.add(new Zombie(new Position(1, 1)));
        DungeonManiaController.setMovingEntities(zombieList);
        
        Position start = new Position(1, 1);
        controller.tick(null, Direction.DOWN);
        assertEquals(zombieList.get(0).getPosition(), start);
    }

    // Test a spider stuck in a 3x3 box. Except it can move out
    @Test
    public void testSpiderWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> walls = hollowBox(3, 3);
        DungeonManiaController.setStaticEntities(walls);

        Player player = new Player(new Position(5, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> spiderList = new ArrayList<>();
        spiderList.add(new Spider(new Position(1, 1)));
        DungeonManiaController.setMovingEntities(spiderList);

        Position start = new Position(1, 1);
        controller.tick(null, Direction.UP);
        assertNotEquals(spiderList.get(0).getPosition(), start);
        assertEquals(spiderList.get(0).getPosition(), new Position(1, 0));
    }

    // Test pushing a boulder into a wall.
    @Test
    public void testBoulderWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(5, 3);
        Boulder boulder = new Boulder(new Position(2, 1), "boulder1");
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));
        assertEquals(boulder.getPosition(), new Position(3, 1));

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));
        assertEquals(boulder.getPosition(), new Position(3, 1));
    }
    
    // Test trying to pushing two boulders.
    @Test
    public void testMoveOneBoulder() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 3);
        Boulder boulder1 = new Boulder(new Position(2, 1), "boulder1");
        Boulder boulder2 = new Boulder(new Position(4, 1), "boulder2");
        entities.add(boulder1);
        entities.add(boulder2);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));
        assertEquals(boulder1.getPosition(), new Position(3, 1));

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));
        assertEquals(boulder1.getPosition(), new Position(3, 1));
        assertEquals(boulder2.getPosition(), new Position(4, 1));
    }

    // Test of an enemy trying to push a boulder
    @Test
    public void testEnemyBoulder() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(5, 5);
        Boulder boulder = new Boulder(new Position(3, 1), "boulder1");
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);
        
        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<MovingEntity> spiderList = new ArrayList<>();
        Spider spider = new Spider(new Position(2, 2));
        spiderList.add(spider);
        DungeonManiaController.setMovingEntities(spiderList);

        controller.tick(null, Direction.RIGHT);
        assertEquals(spider.getPosition(), new Position(2, 1));
        assertEquals(boulder.getPosition(), new Position(3, 1));

        controller.tick(null, Direction.DOWN);
        assertEquals(spider.getPosition(), new Position(2, 1));
        assertEquals(boulder.getPosition(), new Position(3, 1));

        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(1, 1));
        assertEquals(boulder.getPosition(), new Position(3, 1));
    }

    // Testing functionality of switch and ordering of triggering the switch
    @Test 
    public void testSwitch() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 5);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(4, 2), 
            "switch1");
        Boulder boulder = new Boulder(new Position(3, 2), "boulder1");
        entities.add(floorSwitch);
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 2));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Bomb bomb = new Bomb(new Position(2, 2));
        carryList.add(bomb);
        DungeonManiaController.setCarriableEntities(carryList);

        controller.tick(null, Direction.RIGHT);
        assertTrue(player.getBag().contains(bomb));
        controller.tick(null, Direction.RIGHT);
        // boulder should know be on switch
        assertEquals(boulder.getPosition(), floorSwitch.getPosition());
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(bomb.getId(), null);

        entities = DungeonManiaController.getStaticEntities();
        assertFalse(entities.contains(floorSwitch));
        assertFalse(entities.contains(boulder));
    }

    // Test a non-boulder going on a switch in which it should not trigger
    @Test
    public void testOtherOnSwitch() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 5);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(3, 2), 
            "switch1");
        Boulder boulder = new Boulder(new Position(2, 2), "boulder1");
        entities.add(floorSwitch);
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(3, 1));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Bomb bomb = new Bomb(new Position(4, 1));
        carryList.add(bomb);
        DungeonManiaController.setCarriableEntities(carryList);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.LEFT);

        controller.tick(null, Direction.DOWN);
        assertTrue(entities.contains(floorSwitch));
        assertTrue(entities.contains(boulder));

        controller.tick(null, Direction.DOWN);
        controller.tick(bomb.getId(), null);
        assertTrue(entities.contains(floorSwitch));
        assertTrue(entities.contains(boulder));

        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);

        entities = DungeonManiaController.getStaticEntities();
        assertFalse(entities.contains(floorSwitch));
        assertFalse(entities.contains(boulder));
    }

    // Test switch is not triggered by a spider stepping on it
    @Test
    public void testEnemyOnSwitch() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(5, 5);
        FloorSwitch floorSwitch = new FloorSwitch(new Position(2, 1), 
            "switch1");
        entities.add(floorSwitch);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> spiderList = new ArrayList<>();
        Spider spider = new Spider(new Position(2, 2));
        spiderList.add(spider);
        DungeonManiaController.setMovingEntities(spiderList);

        controller.tick(null, Direction.DOWN);
        assertEquals(spider.getPosition(), floorSwitch.getPosition());
        // ensure switch is not triggered
        assertFalse(floorSwitch.getSwitchedOn());
    }

    // Test the player can't move through a closed door without the right key
    @Test
    public void testClosedDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(4, 3);
        Door door = new Door(new Position(2, 1), 1, "door1"); 
        entities.add(door);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(1, 1));
    }

    // Test a zombie can't move through a closed door 
    @Test
    public void testEnemyClosedDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(4, 3);
        Door door = new Door(new Position(2, 1), 1, "door1"); 
        entities.add(door);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> zombieList = new ArrayList<>();
        Zombie zombie = new Zombie(new Position(1, 1));
        zombieList.add(zombie);
        DungeonManiaController.setMovingEntities(zombieList);

        controller.tick(null, Direction.RIGHT);
        assertEquals(zombie.getPosition(), new Position(1, 1));
    }

    // Test a spider can move through a closed door 
    @Test
    public void testSpiderClosedDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(5, 5);
        Door door = new Door(new Position(3, 1), 1, "door1");
        entities.add(door);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> spiderList = new ArrayList<>();
        Spider spider = new Spider(new Position(2, 2));
        spiderList.add(spider);
        DungeonManiaController.setMovingEntities(spiderList);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(spider.getPosition(), new Position(3, 1));
    }

    // Test pushing a boulder through a closed door
    @Test
    public void testBoulderClosedDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(6, 3);
        Boulder boulder = new Boulder(new Position(2, 1), "boulder1");
        Door door = new Door(new Position(3, 1), 1, "door1");
        entities.add(boulder);
        entities.add(door);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(1, 1));
        assertEquals(boulder.getPosition(), new Position(2, 1));
    }

    // Test opening a door
    @Test
    public void testOpenDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(6, 3);
        Door door = new Door(new Position(3, 1), 1, "door1");
        entities.add(door);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Key key = new Key(new Position(2, 1), 1);
        carryList.add(key);
        door.setKey(key);
        assertTrue(door.getKey() != null);
        DungeonManiaController.setCarriableEntities(carryList);

        controller.tick(null, Direction.RIGHT);
        assertTrue(player.getBag().contains(key));

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(4, 1));
        assertFalse(player.getBag().contains(key));
    }

    @Test
    public void testBoulderOpenDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 4);
        Door door = new Door(new Position(4, 1), 1, "door1");
        Boulder boulder = new Boulder(new Position(2, 1), "boulder1");
        entities.add(door);
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Key key = new Key(new Position(1, 2), 1);
        carryList.add(key);
        door.setKey(key);
        assertTrue(door.getKey() != null);
        DungeonManiaController.setCarriableEntities(carryList);

        controller.tick(null, Direction.DOWN);
        assertTrue(player.getBag().contains(key));

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(4, 1));
        assertFalse(player.getBag().contains(key));

        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.UP);

        // now push the boulder through the door
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        assertEquals(player.getPosition(), new Position(4, 1));
        assertEquals(boulder.getPosition(), new Position(5, 1));
        
    }

    // Test with many doors and keys
    @Test
    public void testManyDoors() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(6, 4);
        Door door1 = new Door(new Position(3, 1), 1, "door1");
        Door door2 = new Door(new Position(3, 2), 2, "door2");
        entities.add(door1);
        entities.add(door2);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        
        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Key key = new Key(new Position(2, 1), 2);
        carryList.add(key);
        DungeonManiaController.setCarriableEntities(carryList);
        
        door1.setKey(key);
        door2.setKey(key);
        assertTrue(door1.getKey() == null);
        assertTrue(door2.getKey() != null);

        controller.tick(null, Direction.RIGHT);
        assertTrue(player.getBag().contains(key));

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));

        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(4, 2));
    }

    // Testing portal functionality
    @Test
    public void testPortals() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 3);
        Portal portal1 = new Portal(new Position(2, 1), "blue", "portal1");
        Portal portal2 = new Portal(new Position(4, 1), "blue", "portal2");
        Portal portal3 = new Portal(new Position(6, 1), "green", "portal3");
        portal1.setOtherPortal(portal2);
        portal2.setOtherPortal(portal1);
        portal1.setOtherPortal(portal3);
        portal3.setOtherPortal(portal1);
        assertEquals(portal1.getOtherPortal(), portal2);
        assertTrue(portal3.getOtherPortal() == null);
        entities.add(portal1);
        entities.add(portal2);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(5, 1));

        controller.tick(null, Direction.LEFT);
        assertEquals(player.getPosition(), new Position(1, 1));
    }

    // Test zombies can't go through portals
    @Test
    public void testEnemyPortals() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(7, 3);
        Portal portal1 = new Portal(new Position(2, 1), "blue", "portal1");
        Portal portal2 = new Portal(new Position(4, 1), "blue", "portal2");
        portal1.setOtherPortal(portal2);
        portal2.setOtherPortal(portal1);
        entities.add(portal1);
        entities.add(portal2);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);

        ArrayList<MovingEntity> zombieList = new ArrayList<>();
        Zombie zombie = new Zombie(new Position(1, 1));
        zombieList.add(zombie);
        DungeonManiaController.setMovingEntities(zombieList);

        controller.tick(null, Direction.RIGHT);
        assertEquals(zombie.getPosition(), new Position(2, 1));
    }

    // Test spiders can go through portals and continue their centering movement
    @Test
    public void testSpiderPortals() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(10, 5);
        Portal portal1 = new Portal(new Position(3, 1), "blue", "portal1");
        Portal portal2 = new Portal(new Position(7, 1), "blue", "portal2");
        portal1.setOtherPortal(portal2);
        portal2.setOtherPortal(portal1);
        entities.add(portal1);
        entities.add(portal2);
        DungeonManiaController.setStaticEntities(entities);

        ArrayList<MovingEntity> spiderList = new ArrayList<>();
        Spider spider = new Spider(new Position(2, 2));
        spiderList.add(spider);
        DungeonManiaController.setMovingEntities(spiderList);

        Player player = new Player(new Position(7, 7));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(spider.getPosition(), new Position(3, 1));
    }

    @Test
    // Test pushing a boulder through a portal
    public void testBoulderInPortals() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(9, 3);
        Boulder boulder = new Boulder(new Position(2, 1), "boulder1");
        Portal portal1 = new Portal(new Position(3, 1), "blue", "portal1");
        Portal portal2 = new Portal(new Position(5, 1), "blue", "portal2");
        portal1.setOtherPortal(portal2);
        portal2.setOtherPortal(portal1);
        entities.add(portal1);
        entities.add(portal2);
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);
        
        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(2, 1));
        assertEquals(boulder.getPosition(), new Position(6, 1));

        controller.tick(null, Direction.RIGHT);
        assertEquals(player.getPosition(), new Position(6, 1));
        assertEquals(boulder.getPosition(), new Position(7, 1));
    }

    // Test for standard difficulty spawner
    @Test
    public void testStandardZombieSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(4, 3);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(1, 1), 
            "standard", "spawner1");
        entities.add(spawner);
        DungeonManiaController.setStaticEntities(entities);

        int zombieCount1 = DungeonManiaController.getMovingEntities().size();
        Player player = new Player(new Position(5, 5));
        DungeonManiaController.setPlayer(player);

        for (int i = 0; i < 5; i++) {
            controller.tick(null, Direction.RIGHT);
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.LEFT);
            controller.tick(null, Direction.UP);
        }

        int zombieCount2 = DungeonManiaController.getMovingEntities().size();
        assertNotEquals(zombieCount1, zombieCount2);
        ArrayList<MovingEntity> enemyList = DungeonManiaController.getMovingEntities();
        Position zombiePos = enemyList.get(enemyList.size() - 1).getPosition();
        assertTrue(Position.isAdjacent(zombiePos, spawner.getPosition()));
    }

    // Test for hard difficulty spawner
    @Test
    public void testHardZombieSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(4, 3);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(2, 1), 
            "hard", "spawner1");
        entities.add(spawner);
        DungeonManiaController.setStaticEntities(entities);

        int zombieCount1 = DungeonManiaController.getMovingEntities().size();
        Player player = new Player(new Position(5, 5));
        DungeonManiaController.setPlayer(player);

        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.LEFT);
            controller.tick(null, Direction.UP);
        }

        int zombieCount2 = DungeonManiaController.getMovingEntities().size();
        assertNotEquals(zombieCount1, zombieCount2);
        ArrayList<MovingEntity> enemyList = DungeonManiaController.getMovingEntities();
        Position zombiePos = enemyList.get(enemyList.size() - 1).getPosition();
        assertTrue(Position.isAdjacent(zombiePos, spawner.getPosition()));
    }

    // Test to destroy a spawner
    @Test
    public void testDestroySpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(5, 3);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(3, 1), 
            "hard", "spawner1");
        entities.add(spawner);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);

        ArrayList<CarriableEntity> carryList = new ArrayList<>(); 
        Sword sword = new Sword(new Position(2, 1));
        carryList.add(sword);
        DungeonManiaController.setCarriableEntities(carryList);

        controller.tick(null, Direction.RIGHT);
        assertTrue(player.getBag().contains(sword));
        int spawnerCount1 = DungeonManiaController.getStaticEntities().size();

        controller.interact("spawner1");
        int spawnerCount2 = DungeonManiaController.getStaticEntities().size();

        assertNotEquals(spawnerCount1, spawnerCount2);
    }

    @Test
    public void testNoSpace() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("empty", "standard");
        ArrayList<StaticEntity> entities = hollowBox(6, 3);
        ZombieToastSpawner spawner = new ZombieToastSpawner(new Position(4, 1), 
            "standard", "spawner1");
        Boulder boulder = new Boulder(new Position(2, 1), "boulder1");
        entities.add(spawner);
        entities.add(boulder);
        DungeonManiaController.setStaticEntities(entities);

        Player player = new Player(new Position(1, 1));
        DungeonManiaController.setPlayer(player);
        int zombieCount1 = DungeonManiaController.getMovingEntities().size();

        for (int i = 0; i < 5; i++) {
            controller.tick(null, Direction.RIGHT);
            controller.tick(null, Direction.DOWN);
            controller.tick(null, Direction.LEFT);
            controller.tick(null, Direction.UP);
        }
        assertEquals(boulder.getPosition(), new Position(3, 1));
        int zombieCount2 = DungeonManiaController.getMovingEntities().size();
        assertTrue(zombieCount2 == zombieCount1 + 3);
    }
}
