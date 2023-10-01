package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dungeonmania.CollectableEntity.*;
import dungeonmania.MovingEntites.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;

public class BuildableEntityTest {   
    /**
     * Test to build bow, shield, sceptre and Midnight Armour
     */
    @Test
    public void testBuildBuildableEntity() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("build", "standard");
        Player player = DungeonManiaController.getPlayer();
        assertThrows(InvalidActionException.class, () -> controller.build("bow"));
        assertThrows(InvalidActionException.class, () -> controller.build("shield"));
        assertThrows(InvalidActionException.class, () -> controller.build("sceptre"));
        assertThrows(InvalidActionException.class, () -> controller.build("midnight_armour"));

        // pick up all the collectable entites for building bow and shield
        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.RIGHT);
        }
     
        assertThrows(IllegalArgumentException.class, () -> controller.build("notBowOrShield"));
        // build the buildable entites
        controller.build("bow");
        // two shield that can be both build by treasure and key
        controller.build("shield");
        // player still has the key
        assertTrue(player.hasKey());
        controller.build("shield");
       
        // the buildable enties should be in the player's bag
        // after the entities got build
        int numBow = 0;
        int numShield = 0;
        for(CarriableEntity e: player.getBag()) {
            if (e instanceof Bow) {
                numBow++;
            } else if (e instanceof Shield) {
                numShield++;
            }
        }
        assertEquals(1, numBow);
        assertEquals(2, numShield);
        assertEquals(3, player.getBag().size());

        // test build Midnight Armour
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.build("midnight_armour");
        assertEquals(5, player.getBag().size());
        // check entities should in bag after build midnight armour
        int numSunStone = 0;
        int numMidnightArmour = 0;
        for(CarriableEntity e: player.getBag()) {
            if (e instanceof SunStone) {
                numSunStone++;
            } else if (e instanceof MidnightArmour) {
                numMidnightArmour++;
            }
        }
        assertEquals(1, numSunStone); // The player retains the stone after usage.
        assertEquals(1, numMidnightArmour);


        // test build Sceptre
        // pick up all the collectable entites for building
        for (int i = 0; i < 6; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        // assum firstly use two arrows and one treasure
        controller.build("sceptre");
        assertTrue(player.hasKey());
        // then use one wood and one key
        controller.build("sceptre");
        int numSceptre = 0;
        for(CarriableEntity e: player.getBag()) {
            if (e instanceof Sceptre) {
                numSceptre++;
            } 
        }
        assertEquals(2, numSceptre); 
        assertEquals(7, player.getBag().size());
    }


    /**
     * Test the use of bow that will cause double damage
     */
    @Test
    public void testUseBow() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useBow", "standard");
        Player player = DungeonManiaController.getPlayer();
        double fullHealth = player.getHealth();
        assertEquals(100.00, fullHealth);
        // attack enemy without bow
        controller.tick(null, Direction.RIGHT);
        assertNotEquals(100.00, player.getHealth());
        // player pick up health potion and use it
        // this is for comparing the damage using and without
        // using bow
        controller.tick(null, Direction.RIGHT);
        String potionId = null;
        for (CarriableEntity e : player.getBag()) {
            if (e instanceof HealthPotion) {
                potionId = e.getId();
            }
        }
        // use the potion
        controller.tick(potionId, Direction.RIGHT);
        assertEquals(100.00, player.getHealth());
        // pick up all the collectable entites and build bow
        for (int i = 0; i < 4; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        controller.build("bow");
        assertTrue(!player.getBag().isEmpty());
        // attack the enemy
        controller.tick(null, Direction.RIGHT);
        // as the player have double damage, he will recevive 
        // half the damage from the enemy
        // bow has no durability(assume bow's durability = 1)
        assertTrue(player.getBag().isEmpty());
    }

    /**
     * Test the use of shield that will receive half damage from enemy
     */
    @Test
    public void testUseShield() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useShield", "standard");
        Player player = DungeonManiaController.getPlayer();
        // player killed the enemy
        controller.tick(null, Direction.RIGHT);

        double healthWithOutShield = player.getHealth();
        assertNotEquals(100.00, healthWithOutShield);
        // player pick up health potion and use it
        controller.tick(null, Direction.RIGHT);
        String potionId = null;
        for (CarriableEntity e : player.getBag()) {
            if (e instanceof HealthPotion) {
                potionId = e.getId();
            }
        }
    
        controller.tick(potionId, Direction.RIGHT);
        assertEquals(100.00, player.getHealth());
        // pick up all the collectable entites and build shield
        for (int i = 0; i < 3; i++) {
            controller.tick(null, Direction.RIGHT);
        }
        controller.build("shield");
        Mercenary newMercenary = new Mercenary(new Position(8, 0));
        DungeonManiaController.getMovingEntities().add(newMercenary);
        
        // player killed the enemy
        controller.tick(null, Direction.RIGHT);

        // player should have greater health
        assertNotEquals(healthWithOutShield, player.getHealth());
    }
    
    /**
     * Test use sceptre to control mercenary and assassin's mind 
     * The effects only last for 10 ticks.
     */
    @Test
    public void testUseSceptre() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useSceptre", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.build("sceptre");
      
        Mercenary mercenary = new Mercenary(new Position(8, 1, 1));
        Assassin assassin = new Assassin(new Position(6, 3, 1));
        DungeonManiaController.getPlayer().attach((Observer) mercenary);
        DungeonManiaController.getMovingEntities().add(mercenary);
        DungeonManiaController.getPlayer().attach((Observer) assassin);
        DungeonManiaController.getMovingEntities().add(assassin);
        controller.tick(null, Direction.NONE);
        controller.interact(mercenary.getId());
        controller.interact(assassin.getId());
        assertTrue(mercenary.isAlly());
        assertTrue(assassin.isAlly());

        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.NONE);
        }
        assertFalse(mercenary.isAlly());
        assertFalse(assassin.isAlly());
    }
    
    /**
     * Test use Midnight Armour
     */
    @Test
    public void testUseMidnightArmour () {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("useMidnightArmour", "standard");
        Player player = DungeonManiaController.getPlayer();
        double fullHealth = player.getHealth();
        // player killed the enemy
        controller.tick(null, Direction.RIGHT);
        double healthWithOutArmour = player.getHealth();
        assertNotEquals(fullHealth, healthWithOutArmour);
        // player pick up and build midnight armour
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        controller.build("midnight_armour");
        
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
        controller.tick(null, Direction.RIGHT);

        // player should have greater health
        assertNotEquals(healthWithOutArmour, player.getHealth());
    }
}