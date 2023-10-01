package dungeonmania.MovingEntites;

import dungeonmania.CollectableEntity.Armour;
import dungeonmania.CollectableEntity.CarriableEntity;
import dungeonmania.CollectableEntity.MidnightArmour;
import dungeonmania.CollectableEntity.Shield;
import dungeonmania.DungeonManiaController;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import java.util.ArrayList;

public abstract class Enemy extends MovingEntity{
    private boolean armoured = false;
    private boolean canMove = true;
    
    public boolean isCanMove() {
        return canMove;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
    /**
     * given current Position, source Position, if after moving current Position in target Direction, the distance
     * between source and cur is closer return true, else return false
     * @param cur Position
     * @param source Position
     * @param target Direction
     * @return boolean
     **/
    public boolean isCloseDirection(Position cur, Position source, Direction target) {
        Position curD = Position.calculatePositionBetween(cur, source);
        int oldD = curD.getX() * curD.getX() + curD.getY() *curD.getY();
        Position newP = cur.translateBy(target);
        Position newD1 = Position.calculatePositionBetween(newP, source);
        int newD = newD1.getX() * newD1.getX() + newD1.getY() *newD1.getY();
        if (newD < oldD) {
            return true;
        }
        return false;
    }
    /**
     * if the movingEntity is on the same position with player, there should be a battle
     * @boolean isAlly, it is boolean value to see if the enemy is hostile
     * @return MovingEntity, it is to identify the dead MovingEntity during the battle, if there is no battle, return null
     **/
    public MovingEntity battle(boolean isAlly) {
        Player player = DungeonManiaController.getPlayer();
        if(!isAlly) {
            if (player.getPosition().equals(this.getPosition())) {
                if (player.getStatus().contains(PlayerStatus.INVISIBLE)) {
                    return null;
                }
                if (!player.getStatus().contains(PlayerStatus.BATTLE))
                    player.getStatus().add(PlayerStatus.BATTLE);
                if (player.getStatus().contains(PlayerStatus.INVINCIBLE) && DungeonManiaController.getGameMode() != "hard") {
                    int curT = player.getInvincibleTime();
                    if (curT - 1 > 0) {
                        player.setInvincibleTime(curT - 1);
                    } else {
                        ArrayList<PlayerStatus> curStatus = player.getStatus();
                        curStatus.remove(PlayerStatus.INVINCIBLE);
                    }
                    return this;
                }
                double playerHealth = player.getHealth();
                double enemyHealth = this.getHealth();
                CarriableEntity playerArmour = player.getArmour();
                CarriableEntity playerWeapon = player.getWeapon();
                // this is used to adjust enemyDamage
                double alpha1 = 1.0;
                // this is used to adjust playerDamage
                int originalplayerDamage = player.getAttackDamage();

                if (playerArmour != null) {
                    if (playerArmour instanceof Armour || playerArmour instanceof Shield){
                        alpha1 = 0.5;
                    }
                    playerArmour.use(player);
                }
                if (playerWeapon != null) {
                    playerWeapon.use(player);
                }
                while(playerHealth > 0.0 && enemyHealth > 0.0) {
                    int playerDamage = player.getAttackDamage();
                    int enemyDamage = this.getAttackDamage();
                    playerHealth = playerHealth - ((this.getHealth()*enemyDamage*alpha1)/10);
                    if (this.getType().equals("hydra") && !player.hasAnduril()) {
                        double prob = Hydra.r.nextDouble();
                        if (prob >= 0.5)
                            enemyHealth = enemyHealth +((player.getHealth()* playerDamage)/5);
                        else
                            enemyHealth = enemyHealth -((player.getHealth() * playerDamage)/5);
                    }
                    else
                        enemyHealth = enemyHealth -((player.getHealth() * playerDamage)/5);
                    if (playerHealth < 0.0 ) {
                        CarriableEntity ring = player.getTheRing();
                        if (ring == null) break;
                        ring.use(player);
                        playerHealth = player.getHealth();
                    }
                    player.setHealth(playerHealth);
                    this.setHealth(enemyHealth);
                }
                if (playerHealth <= 0.0) {
                    player.setHealth(playerHealth);
                    this.setHealth(enemyHealth);
                    return player;
                } else {
                    player.setHealth(playerHealth);
                    // back to original damage
                    player.setAttackDamage(originalplayerDamage);
                    if (this.armoured) {
                        //TODO: Add Armour
                        Armour newArmour = new Armour(player.getPosition());
                        player.addToBag(newArmour);
                        player.setArmour(true);
                    }
                    if (this instanceof Mercenary) {
                        Observer o = (Observer) this;
                        ((Subject) player).detach(o);
                    }
                    this.setHealth(enemyHealth);
                    return this;
                }
            }
        }
        return null;
    }

    /**
     * check if the Enemy has an armour
     * @return boolean
     */
    public boolean isArmoured() {
        return armoured;
    }

    /**
     * let the enemy be armoured
     * @param armoured
     */
    public void setArmoured(boolean armoured) {
        this.armoured = armoured;
    }
}
