package dungeonmania.CollectableEntity;

import java.util.ArrayList;

import dungeonmania.DungeonManiaController;
import dungeonmania.Entity;
import dungeonmania.HelperLogic;
import dungeonmania.MovingEntites.Mercenary;
import dungeonmania.MovingEntites.MovingEntity;
import dungeonmania.MovingEntites.Observer;
import dungeonmania.MovingEntites.Player;
import dungeonmania.MovingEntites.Subject;
import dungeonmania.StaticEntities.*;
import dungeonmania.util.Position;

public class Bomb extends CarriableEntity {
    private static int countId = 0;
    private String logic = null;

    /**
     * Constructor for bomb
     * @param position of the bomb
     */
    public Bomb(Position position) {
        countId += 1;
        String curId = "bomb" + countId;
        setId(curId);
        setPosition(position);
        setType("bomb");
    }

    /**
     * Constructor for bomb
     * @param position of the bomb
     * @param logic of the bomb
     */
    public Bomb(Position position, String logic) {
        countId += 1;
        String curId = "bomb" + countId;
        setId(curId);
        setPosition(position);
        setType("bomb");
        this.logic = logic;
    }

    /**
     * @return the logic of this bomb
     */
    public String getLogic() {
        return logic;
    }

    @Override
    public void pickBy(Player player) {
        player.addToBag(this);
    }

    @Override
    public void use(Player player) {
        player.removeFromBag(this);
        setPosition(player.getPosition());
    }

    /**
     * Update the remove list if the bomb is going to blast
     * @return the list of entities that are blasted
     */
    public ArrayList<Entity> updateBlast() {
        // check if swich is adjacent this, and boulder is on this
        Player player = DungeonManiaController.getPlayer();
        ArrayList<Entity> removeList = new ArrayList<>();
        if (!this.getIsRemove() && HelperLogic.logic(this, this.getLogic())) {
            // destory all in range
            ArrayList<CarriableEntity> carriableEntities = DungeonManiaController.getCarriableEntities();
            for (CarriableEntity e: carriableEntities) {
                if (e.getPosition().getAdjacentPositions().contains(this.getPosition()) && !player.getBag().contains(e)) {
                    removeList.add(e);
                }
            }
            removeList.add(this);

            ArrayList<MovingEntity> movingEntities = DungeonManiaController.getMovingEntities();
            for (MovingEntity e: movingEntities) {
                if (e instanceof Player) continue;
                if (e.getPosition().getAdjacentPositions().contains(this.getPosition())) {
                    if (e instanceof Mercenary) {
                        Observer mercObserver = (Observer)e;
                        Subject playerSubject = (Subject)DungeonManiaController.getPlayer();
                        playerSubject.detach(mercObserver);
                    }
                    removeList.add(e);
                }
            }

            ArrayList<StaticEntity> staticEntities = DungeonManiaController.getStaticEntities();
            for (StaticEntity e: staticEntities) {
                if (e.getPosition().getAdjacentPositions().contains(this.getPosition())) {
                    removeList.add(e);
                }
            }
        }
        return removeList;
    }


}
