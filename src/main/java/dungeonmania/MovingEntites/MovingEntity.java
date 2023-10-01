package dungeonmania.MovingEntites;
/*
assumption: as long as MovingEntities are not hostile to each other, they can all stay in one cell
*/
import dungeonmania.Entity;
import dungeonmania.util.Position;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class MovingEntity extends Entity implements Movable {
    protected double health;
    protected double fullHealth;

    public double getFullHealth() {
        return fullHealth;
    }

    protected int attackDamage;

    public double getHealth() {
        return this.health;
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setAttackDamage(int attackDamage) {this.attackDamage = attackDamage;}
}
