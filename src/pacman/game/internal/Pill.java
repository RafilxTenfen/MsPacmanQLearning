package pacman.game.internal;

import pacman.game.Game;

public class Pill {

	private int id;
	private int index;
	private boolean powerPill;
	private int distance;



	public Pill(int id, int index, boolean powerPill) {
		super();
		this.id = id;
		this.index = index;
		this.powerPill = powerPill;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean isPowerPill() {
		return powerPill;
	}
	
	public void setPowerPill(boolean powerPill) {
		this.powerPill = powerPill;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public void calculateDistance(int myPosition, Game game) {
		setDistance(game.getShortestPathDistance(myPosition, getIndex()));
	}

	@Override
	public String toString() {
		return "Agent [id=" + id + ", index=" + index + ", powerPill=" + powerPill + ", distance=" + distance + "]";
	}
	
}