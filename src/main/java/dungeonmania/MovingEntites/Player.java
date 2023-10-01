package dungeonmania.MovingEntites;

import dungeonmania.CollectableEntity.*;
import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Player extends MovingEntity implements Subject{

	private ArrayList<CarriableEntity> bag= new ArrayList<>();
	ArrayList<Observer> listObservers = new ArrayList<Observer>();
	private boolean Armoured;
	private boolean ComsumedHealthPotion;
	private int balance;
	private int bagSize;
	private static int id = 0;
	private ArrayList<PlayerStatus> status;
	private int invincibleTime;

	/**
	 * return the player status
	 * @return list of playerStatus
	 */
	public ArrayList<PlayerStatus> getStatus() {
		return status;
	}

	/**
	 * set the player status
	 * @param status
	 */
	public void setStatus(ArrayList<PlayerStatus> status) {
		this.status = status;
	}

	public Player(Position p) {
		String mode = DungeonManiaController.getGameMode();
		if (Objects.equals(mode, "hard")) {
			this.health = 50.0;
			this.fullHealth = 50.0;
		} else {
			this.health = 100.0;
			this.fullHealth = 100.0;
		}
		this.Armoured = false;
		if (Objects.equals(mode, "standard")) {
			this.attackDamage = 5;
		}
		if (Objects.equals(mode, "peaceful")) {
			this.attackDamage = 0;
		}
		id += 1;
		String curId = "player" + id;
		this.setId(curId);
		this.bagSize = 20;
		this.status = new ArrayList<>();
		this.invincibleTime = 0;
		this.setPosition(p);
		this.setType("player");
		this.setCanTeleport(true);
		this.setId("player");
		this.balance = 0;
	}

	/**
	 * get the left time to be invinciable
	 * @return int
	 */
	public int getInvincibleTime() {
		return invincibleTime;
	}

	/**
	 *  set left time to be invincible
	 * @param invincibleTime the time that player can be invincible
	 */
	public void setInvincibleTime(int invincibleTime) {
		this.invincibleTime = invincibleTime;
	}

	/**
	 *  set left time to be invincible
	 * @param time the time that player can be invincible
	 */
	public void setInvincible(int time) {
		if (DungeonManiaController.getGameMode().equals("hard")) {return;}
		this.getStatus().add(PlayerStatus.INVINCIBLE);
		this.setInvincibleTime(time);
	}

	@Override
	public void attach(Observer o) {
		this.listObservers.add(o);
	}

	@Override
	public void detach(Observer o) {
		this.listObservers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : listObservers) {
			observer.update(this);
		}
	}
	/**
	 * return the character's bag
	 * @return list of collectable
	 */
	public List<CarriableEntity> getBag() {
		return bag;
	}

	public void setBag(ArrayList<CarriableEntity> bag) {
		this.bag = bag;
	}

	/**
	 * check if the character is being armoured
	 * return true if it is being armoured, otherwise false
	 * @return boolean value
	 */
	public boolean hasArmour() {
		return Armoured;
	}

	/**
	 * armour the character
	 */
	public void setArmour(boolean b) {
		this.Armoured = b;
	}

	/**
	 * get the character balance
	 * @return int
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * increment the character balance
	 * @param balance int
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}

	
	/**
	 * given the CarriableEntity, add it to character's bag
	 * @param e CarriableEntity
	 */
	public void addToBag(CarriableEntity e) {
		if (bag.size() < this.bagSize) {
			bag.add(e);
		}
	}

	/**
	 * remove the CarriableEntity from character's bag
	 * @param e CarriableEntity
	 */
	public void removeFromBag(CarriableEntity e) {
		bag.remove(e);
	}

	/**
	 * set the character health back to 100
	 * @return void
	 */
	public void setFullHealth() {
		if (DungeonManiaController.getGameMode().equals("hard"))
			this.health = 50.0;
		else
			this.health = 100.0;
	}

	/**
	 * make the character invisible
	 */
	public void setToInvisible() {
		if (!this.status.contains(PlayerStatus.INVISIBLE)) {
			this.status.add(PlayerStatus.INVISIBLE);
		}
	}

	/**
	 * the player is allowed to use HealthPotion once
	 * @param b boolean
	 */
	public void setComsumedHealthPotion(boolean b) {
		this.ComsumedHealthPotion = b;
	}

	/**
	 * given the id, try to find the corresponding item in player's bag
	 * return null if not find one
	 * @param id string
	 */
	public CarriableEntity getOneFromBag(String id) {
		for (CarriableEntity c: this.bag) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	public int getBagSize() {
		return this.bagSize;
	}

	/**
	 * update player position to target Direction
	 * @param direction target Direction
	 */
	@Override
	public void move(Direction direction) {
		HashMap<String, ArrayList<? extends Entity>> map = DungeonManiaController.getMap();
		Position currPosition = this.getPosition();
		Position targetPosition = this.getPosition().translateBy(direction);
		if (!this.getStatus().contains(PlayerStatus.INVISIBLE)) {
			for (Entity entity : map.get("staticEntities")) {
				if (entity.getType().equals("wall") && targetPosition.equals( // If the target Position is wall, stop
					entity.getPosition())) {
					targetPosition = currPosition;
					break;
				}
				if (entity.getType().equals("zombie_toast_spawner") && targetPosition.equals( // If the target position is zombie toast spawner, stop
					entity.getPosition())) {
					targetPosition = currPosition;
					break;
				}
				if (entity.getType().equals("door") && targetPosition.equals( // If the target position is a door, check whether the door is open, if so , open the door, if not stop.
					entity.getPosition())) {
					Door door = (Door) entity;
					if (door.canMove(this)) {
						targetPosition = door.getPosition();
					} else if (this.hasSunStone()) { // If the door is closed, check whether we got a sun stone, if so open it
						door.useKey(this);
					} else if (this.getBag().contains((Object)door.getKey())) {// If the door is not open, check whether we got the key, if so open it, if not stop
						door.useKey(this);
					} else {
						targetPosition = this.getPosition();
					}
					//TODO: check door and key
				}
				if (entity.getType().equals("portal") && targetPosition.equals( // If the target position is a portal, update position
					entity.getPosition())) {
					Portal portal = (Portal) entity;
					targetPosition = portal.teleport(this, direction);
					currPosition = portal.getOtherPortal().getPosition();
				}
				if (entity.getType().equals("boulder") && targetPosition.equals( // If the target position is a boulder, check the position behind the boulder
					entity.getPosition())) {
					Boulder boulder = (Boulder) entity;
					Position p1 = boulder.getPosition();
					boulder.move(direction);
					Position p2 = boulder.getPosition();
					if (p1.equals(p2))
						targetPosition = currPosition;
					else
						targetPosition = currPosition.translateBy(direction);
				}
			}
		}
		this.setPosition(targetPosition);
		//Player player = DungeonManiaController.getPlayer();
		//((Subject) player).notifyObservers();// After finishing all checks update position, then we need to check whether new position has any carriable entity, if so, carry it
		for (CarriableEntity carriableEntity : DungeonManiaController.getCarriableEntities()) {
			if (carriableEntity.getPosition().equals(this.getPosition()) && !getBag().contains(carriableEntity) && !carriableEntity.getIsRemove())
				carriableEntity.pickBy(this);
				//TODO: carry the item
		} 
	}

	/**
	 * get one weapon from the bag
	 * @return CarriableEntity
	 */
	public CarriableEntity getWeapon() {
		for (CarriableEntity carriableEntity : this.getBag()) {
			if (carriableEntity.getType().equals("bow") || carriableEntity.getType().equals("sword") || carriableEntity.getType().equals("anduril"))
				return carriableEntity;
		}
		return null;
	}

	/**
	 * get the armour from the bag
	 * @return cariableEntity
	 */
	public CarriableEntity getArmour() {
		for (CarriableEntity carriableEntity : this.getBag()) {
			if (carriableEntity.getType().equals("armour") || carriableEntity.getType().equals("shield") || carriableEntity.getType().equals("midnight_armour"))
				return carriableEntity;
		}
		return null;
	}

	/**
	 * check if the player has one key
	 * @return true if the player has one
	 */
	public boolean hasKey() {
		for (Entity entity : this.getBag()) {
			if (entity.getType().equals("key"))
				return true;
		}
		return false;
	}

	/**
	 * check if the player have the Sunstone
	 * @return true if the player has one, otherwise false
	 */
	public boolean hasSunStone() {
		for (Entity entity : this.getBag()) {
			if (entity.getType().equals("sun_stone"))
				return true;
		}
		return false;
	}


	/**
	 * check if the player has anduril
	 * @return true if the player has one
	 */
	public boolean hasAnduril() {
		for (CarriableEntity carriableEntity : this.getBag())
			if (carriableEntity.getType().equals("anduril"))
				return true;
		return false;
	}

	/**
	 * check if the player has used the HealthPotion before
	 * @return true if the player used before
	 */
	public boolean isComsumedHealthPotion() {
		return ComsumedHealthPotion;
	}

	/**
	 * check the invisible time
	 */
	public void checkInvisibleTime() {
		if (this.getInvincibleTime() > 0) {
			this.setInvincibleTime(this.getInvincibleTime() - 1);
		} else {
			this.setInvincibleTime(0);
			this.getStatus().remove(PlayerStatus.INVINCIBLE);
		}
	}

	/**
	 * get oneRing from the bag
	 * @return null if there is no ring in the bag, otherwise return the object
	 */
	public CarriableEntity getTheRing() {
		for (CarriableEntity c: this.bag) {
			if (c instanceof Ring) {
				return c;
			}
		}
		return null;
	}
	/**
	 * get the sceptre from the bag
	 * @return null if there is no sceptre in the bag, otherwise return the object
	 */
	public boolean getTheSceptre() {
		for (CarriableEntity c: this.bag) {
			if (c instanceof Sceptre) {
				return true;
			}
		}
		return false;
	}
}
