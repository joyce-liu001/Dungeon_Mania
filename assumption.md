**CollectableEntities**

-*Armour*’s durability is 2 battles， it will decrease the owner damage by half during a battle

-*Bomb*’s  blastRadius is 1 and can destroy everything around bomb, except the player.

-*Bow*’s durability is 1 for each attack, it will increase the owner damage by twice during a battle

-*InvincibilityPotion* has time limit is 10 tick; The player cannot drink *InvincibilityPotion* at the same time. *Player* can't drink *InvincibilityPotion* in invincible state.

-Each game can only have one *Ring*. 

-*Shield*’s durability is 1 battle and it can decrease half of the effect of enemy attacks; To build *Shield*, we firstly use *Treasure*, if no *Treasure* in bag, then use *Key*. 

-*Sword* can destroy the Zombie Toast Spawner and its durability is 1, but in each battle with enemy, the durability is still decreasing. 
It can be used to destory the spawner,but It can *not* increase the owner attack damage during a battle

-Use *InvisiblePotion* can through all entity include enemies.

-*Anduril* causes triple damage against every enemy, not only bosses.

-Build *sceptre* firstly use two arrows and one treasure, then use one wood and one key.

-*MidnightArmour* causes damage addn 20 to against enemy.It has the same effect of armour

-Once the player picks up the TimeTurner, it can be used without limited times


-*SunStone* not takes priority over treasure. It be use. Which in front of the backpack, it be first used.

-*SunStone* can be retains after build *Sceptre*.

**StaticEntities**
-Nothing occurs when zombies or mercenaries step on an exit.

-Mercenaries and zombies cannot push boulders.

-Only boulders can trigger a switch. No other entity is able to trigger a switch.

-Spiders cannot spawn on top of boulders.

-Boulders will move first before any other entity.

-Portals will always have space on the other side for characters. If there's a
boulder there, then it get moved like normal.

-Portals will never be placed such that it causes an infinite loop.

-Spiders will continue their movement centering around a tile even after going
through a portal.

-If a switch is currently triggered, placing a bomb next to it the switch
will cause it to detonate.

-Boulders can/can't be pushed onto tiles with entities

-Assume that all tiles have a movement factor of 1, so entities can move off 
the swamp tile on the n_th tick where n is the movement factor of the tile.

-*SwitchDoor* can opened by a key or switch.

-*Switch* do not have logic, so it cannot be active through *Wire*, it only be active through a *Boulder*.

-*Wires* are connected to a *Switch* and any entity that interacts via switches and not include another *Wire*.


**MovingEntities**

-Player will have 100 health and 5 attackdamage in standard mode, 50 health and 5 attackdamage in hard mode.
	
-Spider will have 20 health and 10 attackdamage in standard mode or hard mode, 0 attackdamage in peaceful mode.
		
-Zombie will have 60 health and 40 attackdamage in standard mode or hard mode.

-Mercenary will have 40 health and 20 attackdamage in standard mode or hard mode.	
	
-Mercenary requires 1 treasure to be brided.

-Mercenaries after being bribed will still be slowed by the swamp tile. 

-After a mercenary is randomly spawned, it will also move within the same tick.

-Mercenary will have 30% to gain an amour after being spawned

-Player can not gain any extra items from killing an OlderPlayer

-Player can only carry 20 items, no more item can be picked up if the bag is full

-there can be at most 5 spiders in the map in the same time

-Spider spawns in every 10 tick