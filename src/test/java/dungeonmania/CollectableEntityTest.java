package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

import dungeonmania.CollectableEntity.*;
import dungeonmania.MovingEntites.*;
import dungeonmania.StaticEntities.Wall;
import dungeonmania.StaticEntities.ZombieToastSpawner;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class CollectableEntityTest {   
    
    /**
     * Test picking up the collectable entites
     * The map contains all the collectable entities, and having two keys
     * Asserct that the player can pick up all the collectable entities,
     * excluding the send key
     */
    @Test
    public void testpickBySuccessful() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("pickUp", "standard");
        List<CarriableEntity> startingEntities = DungeonManiaController.getCarriableEntities();

        // add the key that cannot be pick up (the second key)
        List<Entity> keys = startingEntities.stream().filter(e -> e instanceof Key).collect(Collectors.toList());
        String key2Id = keys.get(1).getId();

        // add the only player
        Player player = DungeonManiaController.getPlayer();
        
        // try to move towards right pick up one treasure
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one key
        controller.tick(null, Direction.RIGHT);
        // try to move towards right but not pick up key again
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one health_potion
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one invincibility_potion
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one invisibility_potion
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one wood
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one arrow
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one bomb
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one sword
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one armour
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one ring
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up sun_stone
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up anduril
        controller.tick(null, Direction.RIGHT);


        // check player balance(treasure and sun stone)
        assertEquals(2, player.getBalance());
        // check key and sun stone
        assertTrue(player.hasKey());
        assertTrue(player.hasSunStone());

        
        List<String> entitiesInBagExp = new ArrayList<>();
        startingEntities.stream().forEach(e -> entitiesInBagExp.add(e.getId()));
        entitiesInBagExp.remove(key2Id);
        
        // check entities in bag
        List<String> entitiesInBagActual = new ArrayList<>();
        for (CarriableEntity e : player.getBag()) {
            entitiesInBagActual.add(e.getId());
        }
        assertEquals(entitiesInBagExp, entitiesInBagActual);
    }

    /**
     * test go to original position and pick by again.
     */
    @Test
    public void testpickByRepeat() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("pickUp", "standard");
        // add the only player
        Player player = DungeonManiaController.getPlayer();
        
        // try to move towards right pick up one treasure
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one key
        controller.tick(null, Direction.RIGHT);
        // try to move towards right but not pick up key again
        controller.tick(null, Direction.RIGHT);
        // try to move towards right pick up one health_potion
        controller.tick(null, Direction.RIGHT);

        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);

        // do not pick up the second key
        assertTrue(player.getBag().size() == 3);
    }
    
    /**
     * Test the use of health potion. 
     * Use the health potion and kill all enemies, health potion can only consume once.
     */
    @Test
    public void testUseHealthPotion() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useHealthPotion", "standard");
        Player player = DungeonManiaController.getPlayer();
        double fullHealth = player.getHealth();
        HealthPotion healthPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof HealthPotion) {
                healthPotion = (HealthPotion) e;
            }
        }

        String potionId = healthPotion.getId();
        assertThrows(InvalidActionException.class, () -> controller.tick(potionId, null));
        
        // player move to right to kill mercenary
        // player's health is not full
        controller.tick(null, Direction.RIGHT);
        player = DungeonManiaController.getPlayer();
        assertNotEquals(fullHealth, player.getHealth());

        // player pick up health potion
        controller.tick(null, Direction.RIGHT);

        // player consume health potion
        assertDoesNotThrow(() -> controller.tick(potionId, null));
        assertEquals(fullHealth, player.getHealth());
        assertTrue(player.isComsumedHealthPotion());

        // player consumed health potion but no effect
        HealthPotion healthPotion2 = new HealthPotion(player.getPosition());
        player.addToBag(healthPotion2);
        assertTrue(player.isComsumedHealthPotion());
       
        assertDoesNotThrow(() -> controller.tick(healthPotion2.getId(), null));
    }

    /**
     * Test use invincible potion and enemy run away from player.
     * The map contains one player, one enemy and one invincible potion.
     */
    @Test
    public void testUseInvinciblePotionRunAway() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useInvinciblePotion", "standard");
        Player player = DungeonManiaController.getPlayer();
        InvincibilityPotion invincibilityPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof InvincibilityPotion) {
                invincibilityPotion = (InvincibilityPotion) e;
            }
        }
        String potionId = invincibilityPotion.getId();
        assertThrows(InvalidActionException.class, () -> controller.tick(potionId, Direction.RIGHT));

        MovingEntity enemy = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (!(e instanceof Player)) {
                enemy = e;
            }
        }
        
        // get mercenary's position
        Position enemyStartPosition = enemy.getPosition();

        // try to move towards right to pick up the invincible position,
        // the mercenary towards to close player
        controller.tick(null, Direction.RIGHT);
        assertEquals(enemyStartPosition.translateBy(Direction.LEFT), enemy.getPosition());

        // use invincible position, and mercenary towards escape the player, 
        // back to its original position
        assertDoesNotThrow(() ->controller.tick(potionId, null));
        assertDoesNotThrow(() ->controller.tick(null, Direction.RIGHT));
        assertEquals(enemyStartPosition, enemy.getPosition());
        assertTrue(!player.getBag().contains(invincibilityPotion));
    }
    
    /**
     * Test use invincible potion and player kill the enemy not lose health.
     * The map contains one player, one enemy and one invincible potion.
     * Then add one wall to let them battle.
     */
    @Test
    public void testUseInvinciblePotionEndBattle() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useInvinciblePotion", "standard");
        Player player = DungeonManiaController.getPlayer();
        double fullHealth = player.getHealth();
        InvincibilityPotion invincibilityPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof InvincibilityPotion) {
                invincibilityPotion = (InvincibilityPotion) e;
            }
        }
        String potionId = invincibilityPotion.getId();
        assertThrows(InvalidActionException.class, () -> controller.tick(potionId, Direction.RIGHT));

        // try to move towards right to pick up the invincible position,
        // the mercenary towards left to close player
        controller.tick(null, Direction.RIGHT);
        
        // player try to move to right and the mercenary towards left to close player
        controller.tick(null, Direction.RIGHT);

        // now player is next to mercenary, create a wall to stop it from running away
        new Wall(new Position(5,1), "newWall");

        // player use invincible potion and kill mercenary 
        controller.tick(potionId, Direction.RIGHT);
        // check health and bag
        assertEquals(fullHealth, player.getHealth());
        assertTrue(!player.getBag().contains(invincibilityPotion));
    }

    /**
     * Test use invincible potion in hard mode.
     * The map contains one player, one enemy and one invincible potion.
     * The invincible potion not useful.
     */
    @Test
    public void testUseInvinciblePotionInHardMode() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useInvinciblePotion", "hard");
        Player player = DungeonManiaController.getPlayer();
        InvincibilityPotion invincibilityPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof InvincibilityPotion) {
                invincibilityPotion = (InvincibilityPotion) e;
            }
        }
        String potionId = invincibilityPotion.getId();

        MovingEntity enemy = null;
        for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
            if (!(e instanceof Player)) {
                enemy = e;
            }
        }

        // try to move towards right to pick up the invincible position,
        // the mercenary towards left to close player
        controller.tick(null, Direction.RIGHT);
        // the enemy move towards the player
        assertEquals(new Position(5, 1), enemy.getPosition());
        assertEquals(new Position(2, 1), player.getPosition());

        // use invincible position, and mercenary still move towards the player
        controller.tick(potionId, null);
        assertEquals(new Position(4, 1), enemy.getPosition());
        assertEquals(new Position(2, 1), player.getPosition());
        
        controller.tick(null, Direction.RIGHT);
        assertEquals(new Position(3, 1), enemy.getPosition());
        assertEquals(new Position(3, 1), player.getPosition());

        assertFalse(player.getBag().contains(invincibilityPotion));
        // player kill the mercenary and loss health
        assertNotEquals(50.00, player.getHealth());
    }

    /**
     * Test the use of invisible potion
     * The map contains a wall and a invisible potion
     * The player can go through all the entites after he use the invisible potion
     */
    @Test
    public void testUseInvisiblePotion() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useInvisiblePotion", "standard");
        Player player = DungeonManiaController.getPlayer();
        // get player's start position
        Position playerStartPosition = player.getPosition();
        
        // try to move towards right but cannot through the wall
        controller.tick(null, Direction.RIGHT);
        assertEquals(playerStartPosition, player.getPosition());
        
        // try to move towards down to pick up invisible position 
        controller.tick(null, Direction.DOWN);
        
        InvisibilityPotion invisibility_potion = null;
        for (CarriableEntity e : player.getBag()) {
            if (e instanceof InvisibilityPotion) {
                invisibility_potion = (InvisibilityPotion) e;
            }
        }
        String potionId = invisibility_potion.getId();

        // use it
        assertDoesNotThrow(() -> controller.tick(potionId, Direction.NONE));
        assertFalse(player.getBag().contains(invisibility_potion));

        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.RIGHT);
        // player can go through the wall
        assertEquals(playerStartPosition.translateBy(Direction.RIGHT), player.getPosition());
        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.NONE);
        }
        assertFalse(DungeonManiaController.getPlayer().getStatus().contains(PlayerStatus.INVINCIBLE));
    }

    /**
     * Test the use of bomb
     */
    @Test
    public void testUseBomb() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useBomb", "standard");
        
        // pick up bomb
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.UP);

        // push boulder onto the switch
        controller.tick(null, Direction.RIGHT);

        Player player = DungeonManiaController.getPlayer();
        // place the bomb
        Bomb bomb = null;
        for (CarriableEntity e : player.getBag()) {
            if (e instanceof Bomb) {
                bomb = (Bomb) e;
            }
        }
        String bombId = bomb.getId();

        controller.tick(bombId, null);

        // entities adjacent to the bomb are gone 
        assertTrue(DungeonManiaController.getStaticEntities().isEmpty());
        assertFalse(DungeonManiaController.getCarriableEntities().isEmpty());
    }

    /**
     * Test the use of sword to destory spawner
     * The map contains a sword and a spawner
     * If the player have a sword, he can destory the spaw
     * And the durability of sword is once, so sword will be destoried 
     */
    @Test
    public void testUseSword() {
        DungeonManiaController controller = new DungeonManiaController();
        // the game only contains one static entity --spawner
        controller.newGame("useSword", "standard");
        ZombieToastSpawner spawner = (ZombieToastSpawner) DungeonManiaController.getStaticEntities().get(0); 
        // player cannot destory spawner
        assertThrows(InvalidActionException.class, () -> controller.interact(spawner.getId()));

        // check tick cannot use sword
        Sword sword = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof Sword) {
                sword = (Sword) e;
            }
        }
        String swordId = sword.getId();
        
        // pick up sword
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.UP);
        assertThrows(IllegalArgumentException.class, () -> controller.tick(swordId, Direction.RIGHT));

        // destory spawner
        controller.interact(spawner.getId());
        assertTrue(DungeonManiaController.getStaticEntities().isEmpty());
        
    }

    /**
    * Test the use of Armour
    * The map contains two enemies and a health potion
    * Health potion is to set the player to full health and we can
    * compare the impact between using and not using Armour
    * And the durability of Armour is once, so Armour will be destoried
    */
    @Test
    public void testUseArmour() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useArmour", "standard");
        Player player = DungeonManiaController.getPlayer();
        // player killed the enemy
        controller.tick(null, Direction.RIGHT);
        double healthWithOutArmour = player.getHealth();
        assertNotEquals(100.00, healthWithOutArmour);
        // player pick up and use armour
        controller.tick(null, Direction.RIGHT);

        Armour armour = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof Armour) {
                armour = (Armour) e;
            }
        }
        // cannot use item armour
        String armourId = armour.getId();
        assertThrows(IllegalArgumentException.class, () ->controller.tick(armourId, Direction.RIGHT));

        // player pick up health potion and use it
        HealthPotion healthPotion = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof HealthPotion) {
                healthPotion = (HealthPotion) e;
            }
        }
        String potionId = healthPotion.getId();
        controller.tick(null, Direction.RIGHT);
        controller.tick(potionId, Direction.RIGHT);

        // player kill another enemy wearing armour
        assertThrows(IllegalArgumentException.class, () -> controller.tick(armourId, Direction.RIGHT));
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);

        // player should have greater health
        assertNotEquals(healthWithOutArmour, player.getHealth());

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.NONE);
        }
        assertFalse(player.hasArmour());

    }

    /**
     * Test the use of the one ring
     * When the player is dead (health = 0), if he has the ring
     * he will automatically have full health
     */
    @Test
    public void testUseTheOneRing() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useRing", "standard");
        Player player = DungeonManiaController.getPlayer();
        double fullHealth = player.getHealth();
        
        Ring ring = null;
        for (CarriableEntity e : DungeonManiaController.getCarriableEntities()) {
            if (e instanceof Ring) {
                ring = (Ring) e;
            }
        }
        // pick up the ring and flight with 100 enemies, and die
        controller.tick(null, Direction.RIGHT);
        for (int i = 0; i < fullHealth; i++) {
            Position enemyPosition = player.getPosition().translateBy(Direction.RIGHT);
            new Mercenary(enemyPosition);
            controller.tick(null, Direction.RIGHT);
            if (player.getHealth() <= 0) break;
        }
        assertEquals(fullHealth, player.getHealth());
        assertTrue(!player.getBag().contains(ring));
    }
    
    /**
     * Test sun stone can open door
     */
    @Test
    public void testUseSunStoneForOpenDoor() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useSunStoneDoor", "standard");
        Player player = DungeonManiaController.getPlayer();
        Position startPosition = player.getPosition();
        controller.tick(null, Direction.RIGHT);
        assertEquals(startPosition, player.getPosition());
        // pick up sunstone
        controller.tick(null, Direction.DOWN);
        controller.tick(null, Direction.UP);
        controller.tick(null, Direction.RIGHT);

        assertEquals(startPosition.translateBy(Direction.RIGHT), player.getPosition());
        // retains the stone after usage. 
        assertFalse(player.getBag().isEmpty());
        
    }
    
    /**
     * Test use sun stone to bribed a mecenary
     */
    @Test
    public void testUseSunStoneForBribed() { 
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useSunStoneBribe", "standard");
        Player player = DungeonManiaController.getPlayer();
        MovingEntity mercenary = new Mercenary(new Position(6, 1));
        DungeonManiaController.getPlayer().attach((Observer) mercenary);
        DungeonManiaController.getMovingEntities().add(mercenary);

        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        String mercenaryId = mercenary.getId();
        
        assertEquals(mercenary.getPosition(), new Position(4, 1, 1));
        controller.interact(mercenaryId);
        controller.tick(null, Direction.LEFT);
        assertEquals(mercenary.getPosition(), new Position(3, 1, 1));
        // the sun stone stays in bag
        assertFalse(player.getBag().isEmpty());

    }
    
    /**
     * Test use the ring bribed assassin.
     */
    @Test
    public void testUseRingBribed() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useRingBribe", "standard");
        MovingEntity assassin = new Assassin(new Position(8, 1));
        DungeonManiaController.getPlayer().attach((Observer) assassin);
        DungeonManiaController.getMovingEntities().add(assassin);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals(assassin.getPosition(), new Position(6, 1, 1));
        // player not have enough thing to bribe
        assertThrows(InvalidActionException.class, () -> controller.interact(assassin.getId()));
       
        // now player have sunstone now
        controller.tick(null, Direction.RIGHT);
        controller.interact(assassin.getId());
        controller.tick(null, Direction.LEFT);
        assertEquals(assassin.getPosition(), new Position(4, 1, 1));
    }

    @Test
    public void testUseAnduril() {
        // A very high damage sword which causes triple damage against bosses. 
        // When battling Hydra with this weapon, the Hydra has a 0% chance of spawning two heads when one is cut off.
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useAnduril", "standard");
        Player player = DungeonManiaController.getPlayer();
        int initialDamage = player.getAttackDamage();
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(initialDamage*3, player.getAttackDamage());
    }   
}
