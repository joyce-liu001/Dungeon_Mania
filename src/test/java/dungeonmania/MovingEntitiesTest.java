package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.CollectableEntity.*;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.Test;

import dungeonmania.MovingEntites.*;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.Random;

public class MovingEntitiesTest {

    static int getDistance(Position p1, Position p2) {
        Position pb = Position.calculatePositionBetween(p1, p2);
        return (int)(Math.pow(pb.getX(), 2) + Math.pow(pb.getY(), 2));
    }


    @Test
    public void testCreateSuccessful() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "standard");

        Player p = DungeonManiaController.getPlayer();
        Position start = new Position(1, 1);
        assertEquals(p.getPosition(), start);
    }
    /*
    case1:the player can successfully move without hitting the wall or other static entity
    */
    @Test
    public void testMovingSuccessful() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "standard");

        // move the player
        controller.tick(null, Direction.DOWN);
        Player p = DungeonManiaController.getPlayer();
        Position targetPosition = new Position(1, 2, 1);
        assertEquals(p.getPosition(), targetPosition);

    }

    /*
    case2: the player can not move across the wall or other static entity
    */
    @Test
    public void testMovingTowardsWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced-2", "standard");

        // only one player
        Player p = DungeonManiaController.getPlayer();
        // get start position
        Position startPosition = p.getPosition();
        // try to move towards the wall
        controller.tick(null, Direction.LEFT);
        assertTrue(DungeonManiaController.getPlayer().getPosition() == startPosition);
    }

    /*
    case3: Mercenary moves towards player
     */
    @Test
    public void testMercenaryMove() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryAction", "standard");
        controller.tick(null, Direction.LEFT);
        Position finalPosition = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()){
            if (e instanceof Mercenary) {
                finalPosition = e.getPosition();
            }
        }
        assertEquals(finalPosition, new Position(5, 1, 1));
    }


    /*
        The mercenary is bribed by the player
     */
    @Test
    public void testMercenaryBribed() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryAction", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        Position mercenaryPosition1 = null, mercenaryPosition2 = null;
        String mercenaryId = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryPosition1 = e.getPosition();
                mercenaryId = e.getId();
            }
        }
        assertEquals(mercenaryPosition1, new Position(4, 1, 1));
        controller.interact(mercenaryId);
        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryPosition2 = e.getPosition();
            }
        }
        assertEquals(mercenaryPosition2, new Position(3, 1, 1));
    }



    /*
        Test case for the spider's movement
     */
    @Test
    public void testSpiderMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spiderMovement", "standard");
        Position p1 = null, p2 = null, p3 = null, p4 = null, p5 = null, p6 = null, p7 = null, p8 = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p1 = e.getPosition();
        }
        assertEquals(p1, new Position(6, 1, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p2 = e.getPosition();
        }
        assertEquals(p2, new Position(6, 0, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p3 = e.getPosition();
        }
        assertEquals(p3, new Position(7, 0, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p4 = e.getPosition();
        }
        assertEquals(p4, new Position(7, 1, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p5 = e.getPosition();
        }
        assertEquals(p5, new Position(7, 2, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p6 = e.getPosition();
        }
        assertEquals(p6, new Position(6, 2, 1));

        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p7 = e.getPosition();
        }
        assertEquals(p7, new Position(5, 2, 1));


        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Spider)
                p8 = e.getPosition();
        }
        assertEquals(p8, new Position(5, 1, 1));
    }

    /*
        Test case for mercenary can not go through a wall
     */
    @Test
    public void testMercenaryMoveToWall() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryToWall", "standard");
        controller.tick(null, Direction.RIGHT);

        Position mercenaryP = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryP = e.getPosition();
            }
        }

        assertEquals(mercenaryP, new Position(6,1));
    }
    /*
        Test case for mercenary can not go through a boulder
     */
    @Test
    public void testMercenaryMoveToBoulder() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryToBoulder", "standard");
        controller.tick(null, Direction.RIGHT);

        Position mercenaryP = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryP = e.getPosition();
            }
        }

        assertEquals(mercenaryP, new Position(6,1));
    }
    /*
        Test case for mercenary can not go through a ZombieSpawner
     */
    @Test
    public void testMercenaryMoveToZombieSpawner() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryToZombieSpawner", "standard");
        controller.tick(null, Direction.RIGHT);

        Position mercenaryP = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryP = e.getPosition();
            }
        }

        assertEquals(mercenaryP, new Position(6,1));
    }
    /*
        Test case for mercenary move faster towards the player who is in a battle
     */
    @Test
    public void testMercenaryMoveToBattlePlayer() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("mercenaryToBattlePlayer", "standard");
        controller.tick(null, Direction.LEFT);

        Position mercenaryP = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                mercenaryP = e.getPosition();
            }
        }

        assertEquals(mercenaryP, new Position(4,1, 1));
    }


    @Test
    public void testSpiderMovement2() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("spiderMovement2", "standard");

        //UP
        Spider spider = new Spider(new Position(6, 7, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(6, 7, 1));
        Boulder b = new Boulder(new Position(5, 7, 0), "b");
        DungeonManiaController.getStaticEntities().add(b);
        assertEquals(spider.getPosition(), new Position(6, 7, 1));
        DungeonManiaController.getStaticEntities().remove(b);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(5, 7, 1));

        //RIGHT
        DungeonManiaController.getMovingEntities().remove(spider);
        spider = new Spider(new Position(5, 7, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(5, 6, 1));
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(5, 6, 1));
        b = new Boulder(new Position(4, 6, 0), "b");
        DungeonManiaController.getStaticEntities().add(b);
        assertEquals(spider.getPosition(), new Position(5, 6, 1));
        DungeonManiaController.getStaticEntities().remove(b);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(4, 6, 1));

        //DOWN
        DungeonManiaController.getMovingEntities().remove(spider);
        spider = new Spider(new Position(5, 6, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(6, 5, 1));
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(6, 5, 1));
        b = new Boulder(new Position(5, 5, 0), "b");
        DungeonManiaController.getStaticEntities().add(b);
        assertEquals(spider.getPosition(), new Position(6, 5, 1));
        DungeonManiaController.getStaticEntities().remove(b);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(5, 5, 1));

        //LEFT
        DungeonManiaController.getMovingEntities().remove(spider);
        spider = new Spider(new Position(6, 5, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(7, 6, 1));
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(7, 6, 1));
        b = new Boulder(new Position(7, 5, 0), "b");
        DungeonManiaController.getStaticEntities().add(b);
        assertEquals(spider.getPosition(), new Position(7, 6, 1));
        DungeonManiaController.getStaticEntities().remove(b);
        controller.tick(null, Direction.LEFT);
        assertEquals(spider.getPosition(), new Position(7, 5, 1));
    }

    @Test
    public void testInvincible1() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("invinciblePotion", "standard");
        controller.tick(null, Direction.RIGHT);
        InvincibilityPotion invincibilityPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof InvincibilityPotion) {
                invincibilityPotion = (InvincibilityPotion) e;
            }
        }
        String potionId = invincibilityPotion.getId();
        controller.tick(potionId, Direction.NONE);

        Spider spider = new Spider(new Position(7, 7, 1));
        Zombie zombie = new Zombie(new Position(13, 13, 1));
        Mercenary mercenary = new Mercenary(new Position(19, 19, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        DungeonManiaController.getMovingEntities().add(zombie);
        DungeonManiaController.getMovingEntities().add(mercenary);
        int spiderPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        int zombiePrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        int mercenaryPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        controller.tick(null, Direction.RIGHT);
        int spiderDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        int zombieDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        int mercenaryDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        assertTrue(spiderDistance >= spiderPrevDistance);
        assertTrue(zombieDistance >= zombiePrevDistance);
        assertTrue(mercenaryDistance >= mercenaryPrevDistance);

        DungeonManiaController.getMovingEntities().remove(spider);
        DungeonManiaController.getMovingEntities().remove(zombie);
        DungeonManiaController.getMovingEntities().remove(mercenary);


        spider = new Spider(new Position(37, 37, 1));
        zombie = new Zombie(new Position(43, 43, 1));
        mercenary = new Mercenary(new Position(49, 49, 1));
        DungeonManiaController.getMovingEntities().add(spider);
        DungeonManiaController.getMovingEntities().add(zombie);
        DungeonManiaController.getMovingEntities().add(mercenary);
        spiderPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombiePrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        controller.tick(null, Direction.RIGHT);
        spiderDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombieDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        assertTrue(spiderDistance >= spiderPrevDistance);
        assertTrue(zombieDistance >= zombiePrevDistance);
        assertTrue(mercenaryDistance >= mercenaryPrevDistance);

        DungeonManiaController.getMovingEntities().remove(spider);
        DungeonManiaController.getMovingEntities().remove(zombie);
        DungeonManiaController.getMovingEntities().remove(mercenary);

        spider = new Spider(new Position(7, 7, 1));
        zombie = new Zombie(new Position(13, 13, 1));
        mercenary = new Mercenary(new Position(19, 19, 1));
        Boulder b1 = new Boulder(new Position(spider.getPosition().translateBy(Direction.UP).getX(), spider.getPosition().translateBy(Direction.UP).getY(), 0), "b1");
        Boulder b2 = new Boulder(new Position(spider.getPosition().translateBy(Direction.LEFT).getX(), spider.getPosition().translateBy(Direction.LEFT).getY(), 0), "b2");
        Boulder b3 = new Boulder(new Position(zombie.getPosition().translateBy(Direction.UP).getX(), zombie.getPosition().translateBy(Direction.UP).getY(), 0), "b3");
        Boulder b4 = new Boulder(new Position(zombie.getPosition().translateBy(Direction.LEFT).getX(), zombie.getPosition().translateBy(Direction.LEFT).getY(), 0), "b4");
        Boulder b5 = new Boulder(new Position(mercenary.getPosition().translateBy(Direction.UP).getX(), mercenary.getPosition().translateBy(Direction.UP).getY(), 0), "b5");
        Boulder b6 = new Boulder(new Position(mercenary.getPosition().translateBy(Direction.LEFT).getX(), mercenary.getPosition().translateBy(Direction.LEFT).getY(), 0), "b6");

        DungeonManiaController.getStaticEntities().add(b1);
        DungeonManiaController.getStaticEntities().add(b2);
        DungeonManiaController.getStaticEntities().add(b3);
        DungeonManiaController.getStaticEntities().add(b4);
        DungeonManiaController.getStaticEntities().add(b5);
        DungeonManiaController.getStaticEntities().add(b6);

        DungeonManiaController.getMovingEntities().add(spider);
        DungeonManiaController.getMovingEntities().add(zombie);
        DungeonManiaController.getMovingEntities().add(mercenary);
        spiderPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombiePrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        controller.tick(null, Direction.RIGHT);
        spiderDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombieDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        assertEquals(spiderDistance, spiderPrevDistance);
        assertEquals(zombieDistance, zombiePrevDistance);
        assertEquals(mercenaryDistance, mercenaryPrevDistance);
        DungeonManiaController.getMovingEntities().remove(spider);
        DungeonManiaController.getMovingEntities().remove(zombie);
        DungeonManiaController.getMovingEntities().remove(mercenary);



        spider = new Spider(new Position(7, 7, 1));
        zombie = new Zombie(new Position(13, 13, 1));
        mercenary = new Mercenary(new Position(19, 19, 1));
        b1 = new Boulder(new Position(spider.getPosition().translateBy(Direction.DOWN).getX(), spider.getPosition().translateBy(Direction.DOWN).getY(), 0), "b1");
        b2 = new Boulder(new Position(spider.getPosition().translateBy(Direction.RIGHT).getX(), spider.getPosition().translateBy(Direction.RIGHT).getY(), 0), "b2");
        b3 = new Boulder(new Position(zombie.getPosition().translateBy(Direction.DOWN).getX(), zombie.getPosition().translateBy(Direction.DOWN).getY(), 0), "b3");
        b4 = new Boulder(new Position(zombie.getPosition().translateBy(Direction.RIGHT).getX(), zombie.getPosition().translateBy(Direction.RIGHT).getY(), 0), "b4");
        b5 = new Boulder(new Position(mercenary.getPosition().translateBy(Direction.DOWN).getX(), mercenary.getPosition().translateBy(Direction.DOWN).getY(), 0), "b5");
        b6 = new Boulder(new Position(mercenary.getPosition().translateBy(Direction.RIGHT).getX(), mercenary.getPosition().translateBy(Direction.RIGHT).getY(), 0), "b6");

        DungeonManiaController.getStaticEntities().add(b1);
        DungeonManiaController.getStaticEntities().add(b2);
        DungeonManiaController.getStaticEntities().add(b3);
        DungeonManiaController.getStaticEntities().add(b4);
        DungeonManiaController.getStaticEntities().add(b5);
        DungeonManiaController.getStaticEntities().add(b6);

        DungeonManiaController.getMovingEntities().add(spider);
        DungeonManiaController.getMovingEntities().add(zombie);
        DungeonManiaController.getMovingEntities().add(mercenary);
        spiderPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombiePrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryPrevDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        controller.tick(null, Direction.RIGHT);
        spiderDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), spider.getPosition());
        zombieDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), zombie.getPosition());
        mercenaryDistance = getDistance(DungeonManiaController.getPlayer().getPosition(), mercenary.getPosition());
        assertEquals(spiderDistance, spiderPrevDistance);
        assertEquals(zombieDistance, zombiePrevDistance);
        assertEquals(mercenaryDistance, mercenaryPrevDistance);

    }

    @Test
    public void zombieMove() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("basic", "standard");
        Zombie zombie = new Zombie(new Position(20,20, 1));
        DungeonManiaController.getMovingEntities().add(zombie);
        Position prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        Position position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));

        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));

        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));

        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));

        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));

        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));


        prevPosition = zombie.getPosition();
        controller.tick(null, Direction.NONE);
        position = zombie.getPosition();
        assertTrue(Position.isAdjacent(prevPosition, position));
    }

    @Test
    public void AssassinMovement() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("AssassinMovement", "standard");
        ArrayList<MovingEntity> movingEntities = DungeonManiaController.getMovingEntities();
        Assassin boss = new Assassin(new Position(4, 1));
        Subject playerSubject = (Subject) DungeonManiaController.getPlayer();
        playerSubject.attach((Observer) boss);
        movingEntities.add(boss);

        DungeonManiaController.getPlayer().setPosition(new Position(1, 1));
        //check move left
        controller.tick(null, Direction.LEFT);
        assertEquals(boss.getPosition(), new Position(3 ,1));
        //check move right
        DungeonManiaController.getPlayer().setPosition(new Position(3, 1));
        boss.setPosition(new Position(1, 1));
        controller.tick(null, Direction.RIGHT);
        assertEquals(boss.getPosition(), new Position(2 ,1));
        //check move up
        DungeonManiaController.getPlayer().setPosition(new Position(1, 1));
        boss.setPosition(new Position(1, 3));
        controller.tick(null, Direction.UP);
        assertEquals(boss.getPosition(), new Position(1 ,2));
        //check move down
        DungeonManiaController.getPlayer().setPosition(new Position(1, 3));
        boss.setPosition(new Position(1, 1));
        controller.tick(null, Direction.DOWN);
        assertEquals(boss.getPosition(), new Position(1 ,2));

    }

    @Test
    public void AssassinBride() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("AssassinBride", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        Position assassinPosition1 = null, assassinPosition2 = null;
        String assassinId = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Assassin) {
                assassinPosition1 = e.getPosition();
                assassinId = e.getId();
            }
        }
        assertEquals(assassinPosition1, new Position(4, 1, 1));
        controller.interact(assassinId);
        controller.tick(null, Direction.LEFT);
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (e instanceof Mercenary) {
                assassinPosition2 = e.getPosition();
                assertEquals(assassinPosition2, new Position(3, 1, 1));
                assertTrue(((Mercenary) e).isAlly());
            }
        }
    }

    @Test
    public void testHydra() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("basic", "standard");
        Hydra hydra = new Hydra(DungeonManiaController.getPlayer().getPosition().translateBy(Direction.RIGHT));
        Anduril anduril = new Anduril(null);
        DungeonManiaController.getPlayer().getBag().add(anduril);
        DungeonManiaController.getMovingEntities().add(hydra);
        Wall w1 = new Wall(hydra.getPosition().translateBy(Direction.UP), "w1");
        Wall w2 = new Wall(hydra.getPosition().translateBy(Direction.DOWN), "w2");
        Wall w3 = new Wall(hydra.getPosition().translateBy(Direction.RIGHT), "w3");
        DungeonManiaController.getStaticEntities().add(w1);
        DungeonManiaController.getStaticEntities().add(w2);
        DungeonManiaController.getStaticEntities().add(w3);
        controller.tick(null, Direction.RIGHT);
        assertTrue(DungeonManiaController.getPlayer().getHealth() > 0);
        controller.tick(null, Direction.LEFT);
        DungeonManiaController.getPlayer().setFullHealth();
        hydra = new Hydra(DungeonManiaController.getPlayer().getPosition().translateBy(Direction.RIGHT));
        Hydra.r = new Random(1);
        DungeonManiaController.getMovingEntities().add(hydra);
        DungeonManiaController.getPlayer().getBag().remove(anduril);
        controller.tick(null, Direction.RIGHT);
        assertTrue(DungeonManiaController.getPlayer().getHealth() <= 0);
    }

    @Test
    public void testRewind() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("basic", "standard");
        DungeonManiaController.getPlayer().setPosition(new Position(1, 1, 1));
        TimeTurner timeTurner = new TimeTurner(new Position(2,1));
        DungeonManiaController.getCarriableEntities().add(timeTurner);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        controller.rewind(5);
        OlderPlayer olderPlayer = null;
        for (MovingEntity m: DungeonManiaController.getMovingEntities()) {
            if (m instanceof OlderPlayer) {
                olderPlayer = (OlderPlayer) m;
                break;
            }
        }
        assert olderPlayer != null;
        assertEquals(new Position(3, 1), olderPlayer.getPosition());

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        boolean vanish = false;
        for (MovingEntity m: DungeonManiaController.getMovingEntities()) {
            if (m instanceof OlderPlayer) {
                vanish = true;
                break;
            }
        }
        assert !vanish;
    }

    @Test
    public void testBattleWithOlderPlayer() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("basic", "standard");
        DungeonManiaController.getPlayer().setPosition(new Position(1, 1, 1));
        TimeTurner timeTurner = new TimeTurner(new Position(2,1));
        DungeonManiaController.getCarriableEntities().add(timeTurner);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.rewind(1);

        DungeonManiaController.getPlayer().addToBag(new Bow());
        controller.tick(null, Direction.LEFT);
        assert DungeonManiaController.getPlayer().getHealth() != 100.0;
    }
}


