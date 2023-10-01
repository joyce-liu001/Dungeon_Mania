package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.CollectableEntity.Armour;
import dungeonmania.CollectableEntity.Arrow;
import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.CollectableEntity.SunStone;
import dungeonmania.CollectableEntity.Treasure;
import dungeonmania.CollectableEntity.Wood;
import dungeonmania.MovingEntites.MovingEntity;
import dungeonmania.MovingEntites.Player;
import dungeonmania.MovingEntites.Zombie;


public class HelperBuild {
    
    /**
     * check the entity in the player bag, return buildable entities can build.
     * @param player the game player
     * @return list of entity can build
     */
    public static List<String> checkCanBuild(Player player) {
        List<String> buildable = new ArrayList<>();
        int numWood = 0;
        int numArrow = 0;
        int numSunStone = 0;
        int numTreasure = 0;
        int numArmour = 0;
        Boolean hasKey = player.hasKey();
        for (CarriableEntity e: player.getBag()) {
            if (e instanceof Wood) {
                numWood++;
            } else if (e instanceof Arrow) {
                numArrow++;
            } else if (e instanceof Treasure) {
                numTreasure++;
            } else if (e instanceof SunStone) {
                numSunStone++;
            } else if (e instanceof Armour) {
                numArmour++;
            }
        }
        if (numArrow >= 3 && numWood >= 1) {
            buildable.add("bow");
        }
        if (numWood >= 2) {
            if (hasKey || numTreasure >= 1) {
                buildable.add("shield");
            }
        }
        if (numSunStone >= 1) {
            if ((numWood >= 1 || numArrow >= 2) && (hasKey || numTreasure >= 1)){
                buildable.add("sceptre");
            }
        }
        if (numSunStone >= 1 && numArmour >= 1) {
            // check can build Midnight Armour
            Boolean hasZombie = false;
            for (MovingEntity e : DungeonManiaController.getMovingEntities()) {
                if (e instanceof Zombie) {
                    hasZombie = true;
                }
            }
            if (!hasZombie) {
                buildable.add("midnight_armour");
            }
        }
        return buildable;
    }
}
