package dungeonmania.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

import dungeonmania.StaticEntities.*;

public class Dijkstra {
    /**
     * Main dijkstra algorithm utilising a priority queue that powers the 
     * pathfinding for mercenaries and assassins. 
     * @param topLeft Most top left corner of the map. 
     * @param bottomRight Most bottom right corner of the map. 
     * @param staticList List of static entities. 
     * @param player Player's position.
     * @return HashMap that maps a position to its predecessor or the next tile 
     * you should move to, to get closer to the player. 
     */
    public static HashMap<Position, Position> getNextPosition(Position topLeft, 
        Position bottomRight, ArrayList<StaticEntity> staticList, 
        Position player) {
        HashMap<Position, Double> dist = new HashMap<>();
        HashMap<Position, Position> prev = new HashMap<>();
        
        ArrayList<Position> posList = makePosList(topLeft, bottomRight);
        
        Position start = new Position(player.getX(), player.getY());
        HashMap<Position, Double> gridDist = Dijkstra.getGridDistances(posList, 
            staticList, start, 0.0);

        Comparator<Position> posCmp = new Comparator<Position>() {
            @Override
            public int compare(Position pos1, Position pos2) {
                Double dist1 = gridDist.get(pos1);
                Double dist2 = gridDist.get(pos2);
                return dist1.compareTo(dist2);
            }
        };
        Queue<Position> posQueue = new PriorityQueue<>(posCmp);
        
        for (Position pos : posList) {
            dist.put(pos, Double.POSITIVE_INFINITY);
            prev.put(pos, null);
            if (gridDist.containsKey(pos)) {
                posQueue.add(pos);
            }
        }
        dist.replace(start, 0.0);
        
        while (!posQueue.isEmpty()) {
            Position u = posQueue.remove();
            for (Direction d : Direction.values()) {
                if (!d.equals(Direction.NONE)) {
                    Position v = u.translateBy(d);
                    if (gridDist.containsKey(v)) {
                        Double distU = dist.get(u);
                        Double distV = dist.get(v);
                        Double costUV = Math.abs(
                            gridDist.get(u) - gridDist.get(v));
                        if (distU + costUV < distV) {
                            dist.replace(v, distU + costUV);
                            prev.replace(v, u);
                        }
                    }
                }
            }
        }
        return prev;
    }

    /**
     * Function to create the costs to traverse from a starting position to 
     * any other position on the grid. 
     * @param posList Rectangular grid of positions.
     * @param staticList List of static entities that may cause bad pathfinding.
     * @param currPos Starting position.
     * @param currDist Starting distance.
     * @return HashMap that maps positions to how far away they are from the
     * starting position. 
     */
    public static HashMap<Position, Double> getGridDistances(
        ArrayList<Position> posList, ArrayList<StaticEntity> staticList, 
        Position currPos, Double currDist) {
        HashMap<Position, Double> gridDistances = new HashMap<>();
        gridDistances = setGridDistances(gridDistances, posList, staticList, 
            currPos, currDist);
        return gridDistances;
    }

    /**
     * Recursive method that traverses through the grid positions and calculates
     * the cost to reach that position based off the adjacent positions. 
     * @param gridDistances HashMap mapping positions to how far away they are
     * from the starting position.
     * @param posList Rectangular grid of positions.
     * @param staticList List of static entities.
     * @param currPos Current position. 
     * @param currDist Current distance from the starting position. 
     * @return HashMap mapping positions to how far away they are
     * from the starting position
     */
    public static HashMap<Position, Double> setGridDistances(
        HashMap<Position, Double> gridDistances, ArrayList<Position> posList, 
        ArrayList<StaticEntity> staticList, Position currPos, Double currDist) {
        if (posList.contains(currPos)) {
            if (!gridDistances.containsKey(currPos)) {
                int increment = 1;
                for (StaticEntity e : staticList) {
                    if (e.getPosition().equals(currPos)) {
                        if (e.getType().equals("wall") || 
                            e.getType().equals("boulder") || 
                            e.getType().equals("zombie_toast_spawner")) {
                            gridDistances.put(currPos, Double.POSITIVE_INFINITY);
                            return gridDistances;
                        } else if (e.getType().equals("door")) {
                            Door door = (Door)e;
                            if (door.getState() instanceof ClosedDoorState) {
                                gridDistances.put(currPos, 
                                Double.POSITIVE_INFINITY);
                                return gridDistances;
                            }
                        } else if (e.getType().equals("swamp_tile")) {
                            SwampTile swampTile = (SwampTile)e;
                            increment = swampTile.getMovementFactor();
                        }
                    }
                }
                gridDistances.put(currPos, currDist);
                currDist += increment;
            } else {
                Double posDist = gridDistances.get(currPos);
                if (!posDist.equals(Double.POSITIVE_INFINITY) && 
                    currDist < posDist) {
                    gridDistances.replace(currPos, currDist);
                    currDist++;
                } else {
                    return gridDistances;
                }
            }
            gridDistances = setGridDistances(gridDistances, posList, staticList, 
                currPos.translateBy(Direction.DOWN), currDist);
            gridDistances = setGridDistances(gridDistances, posList, staticList, 
                currPos.translateBy(Direction.RIGHT), currDist);
            gridDistances = setGridDistances(gridDistances, posList, staticList, 
                currPos.translateBy(Direction.UP), currDist);
            gridDistances = setGridDistances(gridDistances, posList, staticList, 
                currPos.translateBy(Direction.LEFT), currDist);
        }
        return gridDistances;
    }

    /**
     * Makes a rectangular grid of positions based off the top left and bottom 
     * right corners.
     * @param topLeft The top left corner.
     * @param bottomRight The bottom right corner.
     * @return An array list of positions corresponding to the rectangular grid.
     */
    public static ArrayList<Position> makePosList(Position topLeft, 
        Position bottomRight) {
        ArrayList<Position> posList = new ArrayList<>();
        for (int i = topLeft.getX() ; i <= bottomRight.getX(); i++) {
            for (int j = topLeft.getY(); j <= bottomRight.getY(); j++) {
                posList.add(new Position(i, j));
            }
        }
        return posList;
    }
}
