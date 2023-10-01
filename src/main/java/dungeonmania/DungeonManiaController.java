package dungeonmania;

import dungeonmania.CollectableEntity.*;
import dungeonmania.Goals.GoalCheck;
import dungeonmania.MovingEntites.*;
import dungeonmania.MovingEntites.Observer;
import dungeonmania.StaticEntities.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.*;
import dungeonmania.util.*;

import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class DungeonManiaController {
    private String dungeonName;
    private JSONObject goalJson;
    private String goalString;
    private List<EntityResponse> currEntityResponses = new ArrayList<>();
    private List<ItemResponse> currItemResponses = new ArrayList<>();
    private List<String> currBuildables = new ArrayList<>();
    private Position topLeft = null;
    private Position bottomRight = null;
    private Position start = null;

    private final static Position pickupPosition = new Position(-1, -1, 5);
    static private Player player;
    static private HashMap<Position, Position> dijkstraPrev = new HashMap<>();
    static private HashMap<String, ArrayList<? extends Entity>> map = new HashMap<>();
    static private String gameMode;
    private int count = 0;
    private static int tickNum = 1;
    public static Random r = new Random(888);
    private static ArrayList<HashMap<String, ArrayList<? extends Entity>>> history = new ArrayList<>();

    public DungeonManiaController() {
        goalJson = null;
        goalString = null;
        ArrayList<CarriableEntity> carriableEntities = new ArrayList<>();
        ArrayList<StaticEntity> staticEntities = new ArrayList<>();
        ArrayList<MovingEntity> movingEntities = new ArrayList<>();

        map.put("carriableEntities", carriableEntities);
        map.put("staticEntities", staticEntities);
        map.put("movingEntities", movingEntities);
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
    }

    /**
     * /dungeons

     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        r = new Random(885);
        Zombie.r = new Random(885);
        Mercenary.r = new Random(885);
        Hydra.r = new Random(885);
        tickNum = 1;
        List<String> staticEntityType = new ArrayList<>();
        List<String> MovingEntityType = new ArrayList<>();
        List<String> CollectableEntityType = new ArrayList<>();

        // add staticEntityType
        staticEntityType.add("boulder");
        staticEntityType.add("door");
        staticEntityType.add("exit");
        staticEntityType.add("switch");
        staticEntityType.add("portal");
        staticEntityType.add("wall");
        staticEntityType.add("zombie_toast_spawner");
        staticEntityType.add("swamp_tile");
        staticEntityType.add("time_travelling_portal");
        staticEntityType.add("switch_door");
        staticEntityType.add("light_bulb_on");
        staticEntityType.add("light_bulb_off");
        staticEntityType.add("wire");

        //add MovingEntityType
        MovingEntityType.add("mercenary");
        MovingEntityType.add("player");
        MovingEntityType.add("spider");
        MovingEntityType.add("zombie");
        MovingEntityType.add("hydra");
        MovingEntityType.add("assassin");
        MovingEntityType.add("older_player");
        //add collecttableEntity
        CollectableEntityType.add("armour");
        CollectableEntityType.add("arrow");
        CollectableEntityType.add("bomb");
        CollectableEntityType.add("bow");
        CollectableEntityType.add("health_potion");
        CollectableEntityType.add("invincibility_potion");
        CollectableEntityType.add("invisibility_potion");
        CollectableEntityType.add("key");
        CollectableEntityType.add("one_ring");
        CollectableEntityType.add("shield");
        CollectableEntityType.add("sword");
        CollectableEntityType.add("treasure");
        CollectableEntityType.add("wood");
        CollectableEntityType.add("sun_stone");
        CollectableEntityType.add("anduril");
        CollectableEntityType.add("sceptre");
        CollectableEntityType.add("midnight_armour");
        CollectableEntityType.add("time_turner");

        try {
            clearAll();
            this.dungeonName = dungeonName;
            if (getGameModes().contains(gameMode)) {
                setGameMode(gameMode);
            } else {
                throw new IllegalArgumentException();
            }
            String loadString = FileLoader.loadResourceFile("/dungeons/" +
                dungeonName + ".json");
            JSONObject loadJson = new JSONObject(loadString);
            JSONArray entityArray = loadJson.getJSONArray("entities");
            goalJson = loadJson.getJSONObject("goal-condition");
            for (int i = 0; i < entityArray.length(); i++) {
                JSONObject currEntity = entityArray.getJSONObject(i);
                String entityType = currEntity.getString("type");
                if (staticEntityType.contains(entityType)) {
                    addStaticEntityFromJson(currEntity, i);
                }
                if (MovingEntityType.contains(entityType)) {
                    addMovingEntityFromJson(currEntity);
                }
                if (CollectableEntityType.contains(entityType)) {
                    addCollectableEntityFromJson(currEntity);
                }
            }

            for (StaticEntity staticEntity : getStaticEntities()) {
                if (staticEntity.getType().equals("portal")) {
                    Portal portal = (Portal) staticEntity;
                    for (StaticEntity staticEntity1 : getStaticEntities())
                        if (staticEntity1.getType().equals("portal")) {
                            Portal portal1 = (Portal) staticEntity1;
                            if (!portal.equals(portal1)) {
                                if (portal.getColour().equals(portal1.getColour()))
                                    portal.setOtherPortal(portal1);
                            }
                        }

                }
                if (staticEntity.getType().equals("door") || staticEntity.getType().equals("switch_door")) {
                    Door door = (Door) staticEntity;
                    for (CarriableEntity carriableEntity : getCarriableEntities()) {
                        if (carriableEntity.getType().equals("key")) {
                            Key key = (Key) carriableEntity;
                            door.setKey(key);
                        }
                    }
                }
            }
            List<EntityResponse> entityResponseList = new ArrayList<>();

            for (StaticEntity e : getStaticEntities()) {
                entityResponseList.add(new EntityResponse(e.getId(), e.getType()
                    , e.getPosition(), false));
            }
            for (MovingEntity m: getMovingEntities()) {
                entityResponseList.add(new EntityResponse(m.getId(), m.getType()
                    , m.getPosition(), m.getIsInteractable()));
            }
            for (CarriableEntity c: getCarriableEntities()) {
                entityResponseList.add(new EntityResponse(c.getId(), c.getType()
                    , c.getPosition(), c.getIsInteractable()));
            }
            // TODO: Do the same for other lists
            currEntityResponses = entityResponseList;
            // TODO: Need unique dungeon id
            goalString = GoalCheck.goalString(goalJson);
            this.count += 1;
            List<AnimationQueue> animations = new ArrayList<>();
            double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
            animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
                "healthbar set " + healthPer, "healthbar tint 0xff0000"
            ), false, -1));

            return new DungeonResponse("test"+ this.count, dungeonName + ".json",
                currEntityResponses, currItemResponses, currBuildables, goalString, animations);
        } catch (IOException | NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }

    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        String fileName = name;
        GameSave save = new GameSave(map, tickNum, history);
        try {
            File folder = new File("./saves");
            if (!folder.exists() && !folder.isDirectory())
                folder.mkdir();
            FileOutputStream fos = new FileOutputStream("./saves/"+fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(save);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.count +=  1;
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));
        return new DungeonResponse("test"+this.count, dungeonName,
            currEntityResponses, currItemResponses, currBuildables, goalString, animations);
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        clearAll();
        try {
            name = "./saves/" + name;
            FileInputStream fis = new FileInputStream(name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GameSave gs = (GameSave) ois.readObject();
            map = gs.getMap();
            tickNum = gs.getTickNum();
            history = gs.getHistory();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (MovingEntity movingEntity : getMovingEntities()) {
            if (movingEntity.getType().equals("player"))
                setPlayer((Player) movingEntity);
            currEntityResponses.add(
                new EntityResponse(movingEntity.getId(), movingEntity.getType(),
                    movingEntity.getPosition(), movingEntity.getIsInteractable()));
        }
        for (StaticEntity staticEntity : getStaticEntities()) {
            currEntityResponses.add(
                new EntityResponse(staticEntity.getId(), staticEntity.getType(),
                    staticEntity.getPosition(), staticEntity.getIsInteractable()));
        }
        for (CarriableEntity carriableEntity : getCarriableEntities()) {
            if (!carriableEntity.getIsRemove()) {
                if (getPlayer().getBag().contains(carriableEntity))
                    currItemResponses.add(
                        new ItemResponse(carriableEntity.getId(), carriableEntity.getType()));
                else
                    currEntityResponses.add(
                        new EntityResponse(carriableEntity.getId(), carriableEntity.getType(),
                            carriableEntity.getPosition(), carriableEntity.getIsInteractable()));
            }
        }
        this.count += 1;
        this.currBuildables = HelperBuild.checkCanBuild(getPlayer());
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));
        return new DungeonResponse("test" + count, dungeonName,
            currEntityResponses, currItemResponses, currBuildables, goalString, animations);
    }

    public List<String> allGames() {
        ArrayList<String> fileNames = new ArrayList<>();
        String pathName = "./saves";
        File file = new File(pathName);
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile())
                fileNames.add(fileList[i].toString().substring(pathName.length() + 1));
        }
        return fileNames;
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {

        tickNum++;
        HashMap<String, ArrayList<? extends Entity>> oldMap;
        oldMap = clone(map);
        history.add(oldMap);
        updateMindControlTime();
        currEntityResponses = new ArrayList<>();
        currItemResponses = new ArrayList<>();
        currBuildables = new ArrayList<>();
        ArrayList<MovingEntity> movingEntities = getMovingEntities();
        ArrayList<StaticEntity> staticEntities = getStaticEntities();
        ArrayList<CarriableEntity> carriableEntities = getCarriableEntities();
        ArrayList<Entity> updateList = new ArrayList<>();
        updateList.addAll(movingEntities);
        updateList.addAll(staticEntities);
        updateList.addAll(carriableEntities);
        ArrayList<SwampTile> swampTiles = getSwampTiles(staticEntities);

        getPlayer().checkInvisibleTime();
        int spiderNum = 0;
        for (MovingEntity movingEntity : getMovingEntities()) {
            if (movingEntity.getType().equals("spider"))
                spiderNum++;
            if (movingEntity instanceof Enemy) {
                Enemy enemy = (Enemy)movingEntity;
                Position currPosition = movingEntity.getPosition();
                for (SwampTile swampTile : swampTiles) {
                    if (swampTile.getPosition().equals(currPosition)) {
                        swampTile.addEnemy(enemy);
                    }
                }
            }
        }
        if (spiderNum < 5 && tickNum%10 == 0){
            int r1 = r.nextInt(10);
            int r2 = r.nextInt(10);
            r1 -= 5;
            r2 -= 5;
            if (r1 >= 0)
                r1 += 2;
            else
                r1 -= 2;
            if (r2 >= 0)
                r2 += 2;
            else
                r2 -= 2;
            int x = getPlayer().getPosition().getX();
            int y = getPlayer().getPosition().getY();
            Spider spider = new Spider(new Position(x + r1,  y + r2, 1));
            getMovingEntities().add(spider);
        }
        if (tickNum % 50 == 0){
            int r1 = r.nextInt(10);
            int r2 = r.nextInt(10);
            r1 -= 5;
            r2 -= 5;
            int x = getPlayer().getPosition().getX();
            int y = getPlayer().getPosition().getY();
            Hydra hydra = new Hydra(new Position(x + r1,  y + r2, 1));
            getMovingEntities().add(hydra);
        }
        if (tickNum % 15 == 0 && getMovingEntities().size() > 1) {
            Mercenary mercenary = new Mercenary(start);
            // 30% change to Assassin
            if (r.nextInt(15) > 10) {
                mercenary = new Assassin(start);
            }
            getMovingEntities().add(mercenary);
            ((Subject) getPlayer()).attach((Observer) mercenary);
        }

        updateCorners(updateList);
        HashMap<Position, Position> updatedPrev = Dijkstra.getNextPosition(topLeft, bottomRight, getStaticEntities(), getPlayer().getPosition());
        setDijkstra(updatedPrev);

        if (itemUsed == null) {
            getPlayer().getStatus().remove(PlayerStatus.BATTLE);
            getPlayer().move(movementDirection);
            ArrayList<MovingEntity> removeList = new ArrayList<>();
            for (MovingEntity movingEntity : movingEntities) {
                if (!movingEntity.getType().equals("player")) {
                    Enemy enemy = (Enemy) movingEntity;
                    if (!movingEntity.getType().equals("mercenary") && !movingEntity.getType().equals("older_player")) {
                        MovingEntity deadOne = enemy.battle(false);
                        if (deadOne != null)
                            removeList.add(deadOne);
                    } else {
                        if (movingEntity.getType().equals("mercenary")) {
                            Mercenary mercenary = (Mercenary) enemy;
                            MovingEntity deadOne = enemy.battle(mercenary.isAlly());
                            if (deadOne != null)
                                removeList.add(deadOne);
                        }
                        else {
                            OlderPlayer olderPlayer = (OlderPlayer) enemy;
                            MovingEntity deadOne = enemy.battle(olderPlayer.isAlly());
                            if (deadOne != null)
                                removeList.add(deadOne);
                        }

                    }
                }
            }
            getMovingEntities().removeAll(removeList);
            removeList = new ArrayList<>();
            ((Subject) getPlayer()).notifyObservers();
            for (MovingEntity movingEntity : movingEntities) {
                if (movingEntity.getType().equals("zombie") || movingEntity.getType()
                    .equals("spider") || movingEntity.getType().equals("older_player")) 
                    movingEntity.move(null);
                if (movingEntity.getType().equals("older_player")) {
                    OlderPlayer olderPlayer = (OlderPlayer) movingEntity;
                    if (olderPlayer.isDisappear())
                        removeList.add(movingEntity);
                }
            }
            getMovingEntities().removeAll(removeList);
            for (MovingEntity movingEntity : movingEntities) {
                if (!movingEntity.getType().equals("player")) {
                    Enemy enemy = (Enemy) movingEntity;
                    if (!movingEntity.getType().equals("mercenary"))
                        removeList.add(enemy.battle(false));
                    else {
                        Mercenary mercenary = (Mercenary) enemy;
                        removeList.add(enemy.battle(mercenary.isAlly()));
                    }
                }
            }
            getMovingEntities().removeAll(removeList);
        } else {
            if (itemUsed.contains("bomb") || itemUsed.contains("invisibility_potion")
                ||itemUsed.contains("health_potion") || itemUsed.contains("invincibility_potion")) {
                CarriableEntity item = null;
                for (CarriableEntity e: getPlayer().getBag()) {
                    if (e.getId().equals(itemUsed))
                        item = e;
                }
                // use item
                if (item == null && itemUsed != null) {
                    throw new InvalidActionException("itemUsed is not in the player's inventory");
                } else {
                    if (item instanceof InvincibilityPotion) {
                        item.use(getPlayer());
                    } else if (item instanceof Bomb || item instanceof HealthPotion) {
                        item.use(getPlayer());
                    } else if (item instanceof InvisibilityPotion) {
                        getPlayer().setToInvisible();
                        item.use(getPlayer());
                    }
                }
            } else {
                throw new IllegalArgumentException("itemUsed is not a bomb, health_potion, invincibility_potion, or an invisibility_potion, or null");
            }
            for (MovingEntity movingEntity : movingEntities) {
                if (!movingEntity.getType().equals("player") && !movingEntity.getType().equals("mercenary"))
                    movingEntity.move(movementDirection);
            }
            ((Subject) getPlayer()).notifyObservers();

            ArrayList<Entity> staticRemoveList = new ArrayList<>();
            for (CarriableEntity e : carriableEntities) {
                if (e instanceof Bomb) {
                    Bomb bomb = (Bomb)e;
                    staticRemoveList = bomb.updateBlast();
                }
            }
            if (staticRemoveList != null) {
                ArrayList<StaticEntity> staticList = getStaticEntities();
                staticList.removeAll(staticRemoveList);
                DungeonManiaController.setStaticEntities(staticList);
            }
        }

        // update switch
        for (StaticEntity staticEntity : staticEntities) {
            if (staticEntity.getType().equals("zombie_toast_spawner")) {
                ZombieToastSpawner spawner = (ZombieToastSpawner)staticEntity;
                spawner.spawn();
            } else if (staticEntity.getType().equals("switch")) {
                ArrayList<Entity> entityList = new ArrayList<>();
                entityList.addAll(staticEntities);
                entityList.addAll(movingEntities);
                FloorSwitch floorSwitch = (FloorSwitch)staticEntity;
                floorSwitch.setOnTop(entityList);
                floorSwitch.addEntities(carriableEntities);
                floorSwitch.cycle();
            }
        }

        for (StaticEntity e : staticEntities) {
            if (e instanceof Wire) {
                ((Wire) e).updateSwitch();
            }
        }

        for (StaticEntity e : staticEntities) {
            if (e instanceof SwitchDoor) {
                if (HelperLogic.logic(e, ((SwitchDoor) e).getLogic())) {
                    ((SwitchDoor) e).setState(true);
                } else {
                    ((SwitchDoor) e).setState(false);
                }
            } else if (e instanceof LightBulb) {
                if (HelperLogic.logic(e, ((LightBulb) e).getLogic())) {
                    ((LightBulb) e).setState(true);
                } else {
                    ((LightBulb) e).setState(false);
                }
            }
        }

        List<Entity> removeList = null;
        for (CarriableEntity e : carriableEntities) {
            if (e instanceof Bomb) {
                Bomb bomb = (Bomb) e;
                removeList = bomb.updateBlast();
            }
        }
        if (removeList != null) {
            List<CarriableEntity> removeC = new ArrayList<>();
            List<MovingEntity> removeM = new ArrayList<>();
            List<StaticEntity> removeS = new ArrayList<>();

            for (Entity e : removeList) {
                if (e instanceof CarriableEntity) {
                    removeC.add((CarriableEntity) e);
                } else if (e instanceof MovingEntity) {
                    removeM.add((MovingEntity) e);
                } else if (e instanceof StaticEntity) {
                    removeS.add((StaticEntity) e);
                }
            }
            carriableEntities.removeAll(removeC);
            movingEntities.removeAll(removeM);
            staticEntities.removeAll(removeS);
        }

        for (StaticEntity staticEntity : getStaticEntities()) {
            if (staticEntity.getType().equals("time_travelling_portal") && staticEntity.getPosition().equals(getPlayer().getPosition())) {
                switch (movementDirection) {
                    case UP:
                        getPlayer().move(Direction.DOWN);
                        break;
                    case DOWN:
                        getPlayer().move(Direction.UP);
                        break;
                    case LEFT:
                        getPlayer().move(Direction.RIGHT);
                        break;
                    case RIGHT:
                        getPlayer().move(Direction.LEFT);
                        break;
                }
                return rewind(30);
            }
        }
        // create response
        for (MovingEntity movingEntity : movingEntities) {
            currEntityResponses.add(
                new EntityResponse(movingEntity.getId(), movingEntity.getType(),
                    movingEntity.getPosition(), movingEntity.getIsInteractable()));
        }
        for (StaticEntity staticEntity : staticEntities) {
            currEntityResponses.add(
                new EntityResponse(staticEntity.getId(), staticEntity.getType(),
                    staticEntity.getPosition(), staticEntity.getIsInteractable()));
        }
        for (CarriableEntity carriableEntity : carriableEntities) {
            if (!carriableEntity.getIsRemove()) {
                if (getPlayer().getBag().contains(carriableEntity))
                    currItemResponses.add(new ItemResponse(carriableEntity.getId(), carriableEntity.getType()));
                else
                    currEntityResponses.add(
                        new EntityResponse(carriableEntity.getId(), carriableEntity.getType(),
                            carriableEntity.getPosition(), carriableEntity.getIsInteractable()));
            }
        }
        this.count += 1;
        this.currBuildables = HelperBuild.checkCanBuild(getPlayer());
        if (GoalCheck.evaluate(goalJson)) {
            goalString = "";
        }
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));
        return new DungeonResponse("test" + this.count, dungeonName,
            currEntityResponses, currItemResponses, currBuildables, goalString, animations);
    }


    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        currEntityResponses = new ArrayList<>();
        currItemResponses = new ArrayList<>();
        currBuildables = new ArrayList<>();
        ArrayList<MovingEntity> movingEntities = getMovingEntities();
        ArrayList<StaticEntity> staticEntities = getStaticEntities();
        Mercenary mercenary = null;
        ZombieToastSpawner zombieToastSpawner = null;
        for(MovingEntity m: movingEntities) {
            if (Objects.equals(m.getId(), entityId) && m instanceof Mercenary) {
                mercenary = (Mercenary) m;
            }
        }
        for (StaticEntity s:staticEntities) {
            if (Objects.equals(s.getId(), entityId) && s instanceof ZombieToastSpawner) {
                zombieToastSpawner = (ZombieToastSpawner) s;
            }
        }
        if (mercenary == null && zombieToastSpawner == null) {
            throw new IllegalArgumentException();
        }
        if (mercenary != null) {
            mercenary.bride(getPlayer());
        }
        if (zombieToastSpawner != null) {
            Player player = getPlayer();
            Position zP = zombieToastSpawner.getPosition();
            Position p = player.getPosition();
            if (Position.isAdjacent(zP,p)) {
                if (player.getWeapon() != null) {
                    getStaticEntities().remove(zombieToastSpawner);
                    if (GoalCheck.evaluate(goalJson)) {
                        goalString = "";
                    }
                } else {
                    throw new InvalidActionException("you dont have weapon");
                }
            } else {
                throw new InvalidActionException("you are far away from the spawner");
            }
        }
        for (MovingEntity movingEntity : getMovingEntities()) {
            currEntityResponses.add(
                new EntityResponse(movingEntity.getId(), movingEntity.getType(),
                    movingEntity.getPosition(), movingEntity.getIsInteractable()));
        }
        for (StaticEntity staticEntity : getStaticEntities()) {
            currEntityResponses.add(
                new EntityResponse(staticEntity.getId(), staticEntity.getType(),
                    staticEntity.getPosition(), staticEntity.getIsInteractable()));
        }
        for (CarriableEntity carriableEntity : getCarriableEntities()) {
            if (!carriableEntity.getIsRemove()) {
            if (getPlayer().getBag().contains(carriableEntity))
                    currItemResponses.add(
                        new ItemResponse(carriableEntity.getId(), carriableEntity.getType()));
                else
                    currEntityResponses.add(
                        new EntityResponse(carriableEntity.getId(), carriableEntity.getType(),
                            carriableEntity.getPosition(), carriableEntity.getIsInteractable()));
            }
        }
        this.count += 1;
        this.currBuildables = HelperBuild.checkCanBuild(getPlayer());
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));
        return new DungeonResponse("test" + this.count, dungeonName,
            currEntityResponses, currItemResponses, currBuildables, goalString, animations);
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        currItemResponses = new ArrayList<>();
        if (buildable.equals("bow")) {
            if (!HelperBuild.checkCanBuild(getPlayer()).contains(buildable)){
                throw new InvalidActionException("the player does not have sufficient items to craft the buildable");
            }
            Bow newBow = new Bow();
            newBow.build(getPlayer());
            getCarriableEntities().add(newBow);
            // currItemResponses.add(new ItemResponse(newBow.getId(), newBow.getType()));
        } else if (buildable.equals("shield")) {
            if (!HelperBuild.checkCanBuild(getPlayer()).contains(buildable)){
                throw new InvalidActionException("the player does not have sufficient items to craft the buildable");
            }
            Shield newShield = new Shield();
            newShield.build(getPlayer());
            getCarriableEntities().add(newShield);
            // currItemResponses.add(new ItemResponse(newShield.getId(), newShield.getType()));
        } else if (buildable.equals("sceptre")) {
            if (!HelperBuild.checkCanBuild(getPlayer()).contains(buildable)){
                throw new InvalidActionException("the player does not have sufficient items to craft the buildable");
            }
            Sceptre newSceptre = new Sceptre();
            newSceptre.build(getPlayer());
            getCarriableEntities().add(newSceptre);
            // currItemResponses.add(new ItemResponse(newSceptre.getId(), newSceptre.getType()));
        } else if (buildable.equals("midnight_armour")) {
            if (!HelperBuild.checkCanBuild(getPlayer()).contains(buildable)){
                throw new InvalidActionException("the player does not have sufficient items to craft the buildable");
            }
            MidnightArmour newMidnightArmour = new MidnightArmour();
            newMidnightArmour.build(getPlayer());
            getCarriableEntities().add(newMidnightArmour);
            // currItemResponses.add(new ItemResponse(newMidnightArmour.getId(), newMidnightArmour.getType()));
        } else {
            throw new IllegalArgumentException("buildable is not one of bow, shield");
        }
        // update current buildable
        currBuildables = HelperBuild.checkCanBuild(getPlayer());
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));

        for (CarriableEntity carriableEntity :  getCarriableEntities()) {
            if (!carriableEntity.getIsRemove()) {
                if (getPlayer().getBag().contains(carriableEntity))
                    currItemResponses.add(new ItemResponse(carriableEntity.getId(), carriableEntity.getType()));
            }
        }

        return new DungeonResponse("test", dungeonName + ".json",
            currEntityResponses, currItemResponses, currBuildables, goalString);
    }

    /**
     * Function that does boulder goal checking
     * @return True if requirement is satisfied, False otherwise
     */
    public static Boolean boulderCheck() {
        for (StaticEntity e : getStaticEntities()) {
            if (e.getType().equals("switch")) {
                FloorSwitch floorSwitch = (FloorSwitch)e;
                if (!floorSwitch.getSwitchedOn()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Function that does exit goal checking
     * @return True if requirement is satisfied, False otherwise
     */
    public static Boolean playerOnExit() {
        Player player = DungeonManiaController.getPlayer();
        ArrayList<StaticEntity> staticList =
            DungeonManiaController.getStaticEntities();
        for (StaticEntity e : staticList) {
            if (e.getType().equals("exit")) {
                if (e.getPosition().equals(player.getPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Function that does enemy goal checking
     * @return True if requirement is satisfied, False otherwise
     */
    public static Boolean zeroEnemies() {
        ArrayList<MovingEntity> moveList =
            DungeonManiaController.getMovingEntities();
        ArrayList<StaticEntity> staticList =
            DungeonManiaController.getStaticEntities();

        for (StaticEntity e : staticList) {
            if (e.getType().equals("zombie_toast_spawner")) {
                return false;
            }
        }
        return moveList.size() == 1;
    }

    /**
     * Function that does treasure goal checking
     * @return True if requirement is satisfied, False otherwise
     */
    public static Boolean treasureGoal() {
        ArrayList<CarriableEntity> carryList =
            DungeonManiaController.getCarriableEntities();
        Player player = DungeonManiaController.getPlayer();
        int totalTreasure = 0;
        for (CarriableEntity e : carryList) {
            if (e.getType().equals("treasure")) {
                totalTreasure += 1;
            }
        }
        int ownedTreasure = 0;
        for (CarriableEntity e : player.getBag()) {
            if (e.getType().equals("treasure")) {
                ownedTreasure += 1;
            }
        }

        return ownedTreasure == totalTreasure;
    }

    public static Player getPlayer() {
        return player;
    }

    public static HashMap<Position, Position> getDijkstra() {
        return dijkstraPrev;
    }

    public static void setDijkstra(HashMap<Position, Position> newPrev) {
        dijkstraPrev = newPrev;
    }

    public String getGoalString() {
        return goalString;
    }

    public ArrayList<SwampTile> getSwampTiles(ArrayList<StaticEntity> staticEntities) {
        ArrayList<SwampTile> swampTiles = new ArrayList<>();
        for (StaticEntity staticEntity : staticEntities) {
            if (staticEntity.getType().equals("swamp_tile")) {
                swampTiles.add((SwampTile)staticEntity);
            }
        }
        return swampTiles;
    }

    public static void setPlayer(Player player) {
        DungeonManiaController.player = player;
    }

    static public ArrayList<CarriableEntity> getCarriableEntities() {
        return (ArrayList<CarriableEntity>)map.get("carriableEntities");
    }

    static public void setCarriableEntities(ArrayList<CarriableEntity> carriableEntities) {
        map.replace("carriableEntities",  carriableEntities);
    }

    static public ArrayList<MovingEntity> getMovingEntities() {
        return (ArrayList<MovingEntity>) map.get("movingEntities");
    }

    public static void setMovingEntities(ArrayList<MovingEntity> movingEntities) {
        map.replace("movingEntities", movingEntities);
    }
    static public ArrayList<StaticEntity> getStaticEntities() {
        return (ArrayList<StaticEntity>)map.get("staticEntities");
    }

    public static void setStaticEntities(ArrayList<StaticEntity> staticEntities) {
        map.replace("staticEntities", staticEntities);
    }

    static public HashMap<String, ArrayList<? extends Entity>> getMap() {
        return map;
    }

    public void setMap(HashMap<String, ArrayList<? extends Entity>> map) {
        this.map = map;
    }

    static public String getGameMode() {
        return gameMode;
    }

    static public void setGameMode(String gameMode2) {
        gameMode = gameMode2;
    }

    public void addStaticEntityFromJson(JSONObject currEntity, int counter) {
        String entityType = currEntity.getString("type");
        Integer entityX = currEntity.getInt("x");
        Integer entityY = currEntity.getInt("y");
        updateCorners(entityX, entityY);
        // TODO: Put entities at the right layer
        switch (entityType) {
            case "boulder":
                getStaticEntities().add(new Boulder(new Position(entityX, entityY),
                        "boulder" + Integer.toString(counter)));
                break;
            case "door":
                getStaticEntities().add(new Door(new Position(entityX, entityY),
                        currEntity.getInt("key"), "door" + Integer.toString(counter)));
                break;
            case "switch_door":
                if (currEntity.has("logic")) {
                    getStaticEntities().add(new SwitchDoor(new Position(entityX, entityY),
                        currEntity.getInt("key"), "door" + Integer.toString(counter), currEntity.getString("logic")));
                } else {
                    getStaticEntities().add(new SwitchDoor(new Position(entityX, entityY),
                        currEntity.getInt("key"), "door" + Integer.toString(counter)));
                }
                break;
            case "exit":
                getStaticEntities().add(new Exit(new Position(entityX, entityY),
                        "switch" + Integer.toString(counter)));
                break;
            case "switch":
                getStaticEntities().add(new FloorSwitch(new Position(entityX,
                        entityY), "switch" + Integer.toString(counter)));
                break;
            case "portal":
                getStaticEntities().add(new Portal(new Position(entityX, entityY),
                        currEntity.getString("colour"),
                        "portal" + Integer.toString(counter)));
                break;
            case "wall":
                getStaticEntities().add(new Wall(new Position(entityX, entityY),
                        "wall" + Integer.toString(counter)));
                break;
            case "zombie_toast_spawner":
                getStaticEntities().add(new ZombieToastSpawner(new Position(entityX,
                        entityY), gameMode, "spawner" + Integer.toString(counter)));
                break;
            case "wire":
                getStaticEntities().add(new Wire(new Position(entityX,
                        entityY), "wire" + Integer.toString(counter)));
                break;
            case "light_bulb_off":
                if (currEntity.has("logic")) {
                    getStaticEntities().add(new LightBulb(new Position(entityX, entityY),
                        "light" + Integer.toString(counter), currEntity.getString("logic")));
                } else {
                    getStaticEntities().add(new LightBulb(new Position(entityX, entityY),
                        "light" + Integer.toString(counter)));
                }
                break;
            case "swamp_tile":
                getStaticEntities().add(new SwampTile(new Position(entityX, 
                        entityY), "swampTile" + Integer.toString(counter), 
                        currEntity.getInt("movement_factor")));
                break;
            case "time_travelling_portal":
                getStaticEntities().add(new TimeTravelPortal(new Position(entityX, entityY), "time_travelling_portal" + Integer.toString(counter)));
        }
    }

    public void addMovingEntityFromJson(JSONObject currEntity) {
        String entityType = currEntity.getString("type");
        Integer entityX = currEntity.getInt("x");
        Integer entityY = currEntity.getInt("y");
        updateCorners(entityX, entityY);
        switch (entityType) {
            case "player": {
                start = new Position(entityX, entityY);
                Player player = new Player(new Position(entityX, entityY, 1));
                getMovingEntities().add(player);
                setPlayer(player);
                for (MovingEntity m : DungeonManiaController.getMovingEntities()) {
                    if (m instanceof Mercenary) {
                        Observer Om = (Observer) m;
                        Subject Sp = player;
                        Sp.detach(Om);
                        Sp.attach(Om);
                    }
                }
                break;
            }
            case "mercenary": {
                Mercenary mercenary = new Mercenary(new Position(entityX, entityY, 1));
                // 30% change to Assassin
                if (r.nextInt(15) > 10) {
                    mercenary = new Assassin(new Position(entityX, entityY));
                }
                getMovingEntities().add(mercenary);
                Player player = getPlayer();
                if (player != null) {
                    ((Subject) player).attach((Observer) mercenary);
                }
                break;
            }
            case "spider":
                getMovingEntities().add(new Spider(new Position(entityX, entityY, 1)));
                break;
            case "zombie":
                getMovingEntities().add(new Zombie(new Position(entityX, entityY, 1)));
                break;
            case "assassin": {
                Assassin assassin = new Assassin((new Position(entityX, entityY, 1)));
                getMovingEntities().add(assassin);
                Player player = getPlayer();
                if (player != null) {
                    ((Subject) player).attach(assassin);
                }
                break;
            }
        }
    }
    public void addCollectableEntityFromJson(JSONObject currEntity) {
        String entityType = currEntity.getString("type");
        Position position = new Position(currEntity.getInt("x"), currEntity.getInt("y"));
        updateCorners(currEntity.getInt("x"), currEntity.getInt("y"));
        switch (entityType) {
            case "armour":
                getCarriableEntities().add(new Armour(position));
                break;
            case "arrow":
                getCarriableEntities().add(new Arrow(position));
                break;
            case "bomb":
                if (currEntity.has("logic")) {
                    getCarriableEntities().add(new Bomb(position, currEntity.getString("logic")));
                } else {
                    getCarriableEntities().add(new Bomb(position));
                }
                break;
            case "health_potion":
                getCarriableEntities().add(new HealthPotion(position));
                break;
            case "invincibility_potion":
                getCarriableEntities().add(new InvincibilityPotion(position));
                break;
            case "invisibility_potion":
                getCarriableEntities().add(new InvisibilityPotion(position));
                break;
            case "key":
                int key = currEntity.getInt("key");
                getCarriableEntities().add(new Key(position, key));
                break;
            case "one_ring":
                getCarriableEntities().add(new Ring(position));
                break;
            case "sword":
                getCarriableEntities().add(new Sword(position));
                break;
            case "treasure":
                getCarriableEntities().add(new Treasure(position));
                break;
            case "wood":
                getCarriableEntities().add(new Wood(position));
                break;
            case "sun_stone":
                getCarriableEntities().add(new SunStone(position));
                break;
            case "anduril":
                getCarriableEntities().add(new Anduril(position));
                break;
            case "time_turner":
                getCarriableEntities().add(new TimeTurner(position));
                break;
        }
    }

    public void updateCorners(Integer x, Integer y) {
        if (bottomRight == null) {
            bottomRight = new Position(x, y);
        } else {
            if (x > bottomRight.getX()) {
                bottomRight = new Position(x, bottomRight.getY());
            }
            if (y > bottomRight.getY()) {
                bottomRight = new Position(bottomRight.getX(), y);
            }
        }

        if (topLeft == null) {
            topLeft = new Position(x, y);
        } else {
            if (x < topLeft.getX()) {
                topLeft = new Position(x, topLeft.getY());
            }
            if (y < topLeft.getY()) {
                topLeft = new Position(topLeft.getX(), y);
            }
        }
    }

    public void updateCorners(ArrayList<Entity> entityList) {
        for (Entity e : entityList) {
            updateCorners(e.getPosition().getX(), e.getPosition().getY());
        }
    }

    public void setTopLeft(Position topLeft) {
        this.topLeft = topLeft;
    }

    public void setBottomRight(Position bottomRight) {
        this.bottomRight = bottomRight;
    }

    public void clearAll() {
        dungeonName = null;
        gameMode = null;
        topLeft = null;
        bottomRight = null;
        currEntityResponses.clear();
        currItemResponses.clear();
        currBuildables.clear();
        dijkstraPrev.clear();
        HashMap<String, ArrayList<? extends Entity>> map = new HashMap<>();
        ArrayList<CarriableEntity> carriableEntities = new ArrayList<>();
        ArrayList<StaticEntity> staticEntities = new ArrayList<>();
        ArrayList<MovingEntity> movingEntities = new ArrayList<>();
        map.put("carriableEntities", carriableEntities);
        map.put("staticEntities", staticEntities);
        map.put("movingEntities", movingEntities);
        setMap(map);
    }

    public void updateMindControlTime() {
        for (MovingEntity m: getMovingEntities()) {
            if (m instanceof Mercenary) {
                if (((Mercenary) m).getMindContorlTime() > 0) {
                    int curTime = ((Mercenary) m).getMindContorlTime();
                    ((Mercenary) m).setMindContorlTime(curTime - 1);
                    if (curTime - 1 == 0) {
                        ((Mercenary) m).setAlly(false);
                        m.setIsInteractable(false);
                    }
                }
            }
        }
    }

    public DungeonResponse rewind(int ticks)
        throws IllegalArgumentException {
        OlderPlayer curOlderPlayer = null;
        for (MovingEntity m:getMovingEntities()) {
            if (m instanceof OlderPlayer){
                curOlderPlayer = (OlderPlayer) m;
                break;
            }
        }
        clearAll();
        if (ticks > history.size())
            throw new IllegalArgumentException("Not enough history to rewind!");
        if (ticks < 0)
            throw new IllegalArgumentException("Tick cannot be negative!");
        ArrayList<HashMap<String, ArrayList<? extends Entity>>> removeMap = new ArrayList<>();
        for (int i = history.size() - ticks; i < history.size(); i++) {
            removeMap.add(history.get(i));
        }
        history.removeAll(removeMap);
        Player curPlayer = getPlayer();
        Player rewindPlayer = null;
        for (Entity entity : removeMap.get(0).get("movingEntities")) {
            if (entity.getType().equals("player")) {
                rewindPlayer = (Player) entity;
            }
        }
        assert rewindPlayer != null;
        double health = rewindPlayer.getHealth();
        int attackDamage = rewindPlayer.getAttackDamage();
        boolean isAlly = false;
        for (CarriableEntity carriableEntity : rewindPlayer.getBag()) {
            if (carriableEntity.getType().equals("sun_stone") || carriableEntity.getType().equals("midnight_armour"))
                isAlly = true;
        }
        if (rewindPlayer.hasArmour())
            health *= 2;
        if (rewindPlayer.getWeapon() != null)
            attackDamage *= 2;
        ArrayList<Position> fp = new ArrayList<>();
        Player temp;
        for (HashMap<String, ArrayList<? extends Entity>> stringArrayListHashMap : removeMap) {
            for (Entity entity : stringArrayListHashMap.get("movingEntities")) {
                if (entity.getType().equals("player")) {
                    temp = (Player) entity;
                    fp.add(temp.getPosition());
                }
            }
        }
        if (curOlderPlayer != null) {
            fp.addAll(curOlderPlayer.getFuturePosition());
            getMovingEntities().remove(curOlderPlayer);
        }
        curOlderPlayer = new OlderPlayer(health, attackDamage, isAlly, fp);
        setMap(removeMap.get(0));
//        for (HashMap<String, ArrayList<? extends Entity>> stringArrayListHashMap : removeMap) {
//            for (Entity entity : stringArrayListHashMap.get("movingEntities")) {
//                if (entity.getType().equals("player")) {
//                    removePlayer = (Player) entity;
//                    break;
//                }
//            }
//            stringArrayListHashMap.get("movingEntities").remove(removePlayer);
//        }
//        Player removePlayer = null;
        HashMap<String, ArrayList<? extends Entity>> stringArrayListHashMap = removeMap.get(0);
//        for (Entity entity : stringArrayListHashMap.get("movingEntities")) {
//            if (entity.getType().equals("player")) {
//                removePlayer = (Player) entity;
//                break;
//            }
//        }
        stringArrayListHashMap.get("movingEntities").remove(rewindPlayer);
        getMovingEntities().add(curPlayer);
        getMovingEntities().add(curOlderPlayer);
        setPlayer(curPlayer);
        List<CarriableEntity> removeItems = curPlayer.getBag();
        curPlayer.setBag(new ArrayList<CarriableEntity>());
        for (CarriableEntity c1:removeItems) {
            for (CarriableEntity c2 :getCarriableEntities()) {
                if (c1.getId().equals(c2.getId())) {
                    curPlayer.addToBag(c2);
                }
            }
        }
        for (MovingEntity movingEntity : getMovingEntities()) {
            currEntityResponses.add(
                new EntityResponse(movingEntity.getId(), movingEntity.getType(),
                    movingEntity.getPosition(), movingEntity.getIsInteractable()));
        }
        for (StaticEntity staticEntity : getStaticEntities()) {
            currEntityResponses.add(
                new EntityResponse(staticEntity.getId(), staticEntity.getType(),
                    staticEntity.getPosition(), staticEntity.getIsInteractable()));
        }
        for (CarriableEntity carriableEntity : getCarriableEntities()) {
            if (!carriableEntity.getIsRemove()) {
                if (getPlayer().getBag().contains(carriableEntity))
                    currItemResponses.add(new ItemResponse(carriableEntity.getId(), carriableEntity.getType()));
                else
                    currEntityResponses.add(
                        new EntityResponse(carriableEntity.getId(), carriableEntity.getType(),
                            carriableEntity.getPosition(), carriableEntity.getIsInteractable()));
            }
        }
        this.count += 1;
        this.currBuildables = HelperBuild.checkCanBuild(getPlayer());
        if (GoalCheck.evaluate(goalJson)) {
            goalString = "";
        }
        List<AnimationQueue> animations = new ArrayList<>();
        double healthPer = getPlayer().getHealth()/getPlayer().getFullHealth();
        animations.add(new AnimationQueue("PostTick", getPlayer().getId(), Arrays.asList(
            "healthbar set " + healthPer, "healthbar tint 0xff0000"
        ), false, -1));
        return new DungeonResponse("test" + this.count, dungeonName,
            currEntityResponses, currItemResponses, currBuildables, goalString, animations);
    }

    /**
     * Code from https://www.jianshu.com/p/eb2113e12ecb, deep copy an object
     *
     * @param obj object
     * @param <T> Its type
     * @return Clone of the object
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T obj) {
        T clonedObj = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clonedObj = (T) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clonedObj;
    }
}
