package dungeonmania.util;

import dungeonmania.Entity;
import dungeonmania.MovingEntites.MovingEntity;
import dungeonmania.MovingEntites.Player;
import dungeonmania.util.Position;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public final class GameSave implements Serializable {

	/**
	 * This class is for saving game, all data will be saved in binary file
	 */

	private final HashMap<String, ArrayList<? extends Entity>> map;
	private final int tickNum;
	private ArrayList<HashMap<String, ArrayList<? extends Entity>>> history = new ArrayList<>();

	public ArrayList<HashMap<String, ArrayList<? extends Entity>>> getHistory() {
		return history;
	}

	public GameSave(
		HashMap<String, ArrayList<? extends Entity>> map, int tickNum, ArrayList<HashMap<String, ArrayList<? extends Entity>>> history) {
		this.map = map;
		this.tickNum = tickNum;
		this.history = history;
	}

	public HashMap<String, ArrayList<? extends Entity>> getMap() {
		return map;
	}

	public int getTickNum() {
		return tickNum;
	}
}
