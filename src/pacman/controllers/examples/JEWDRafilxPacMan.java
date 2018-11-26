package pacman.controllers.examples;

import java.util.ArrayList;
import java.util.Comparator;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.game.internal.Pill;

import static pacman.game.Constants.*;

public class JEWDRafilxPacMan extends Controller<MOVE>
{	
	//CONSTANTES
	private static final int SAFE_DISTANCE 		= 10;
	private static final int AGRESSIVE_DISTANCE = 100;
	private static final int RADAR				= 150;
	private static final int SAFE_EDIBLE_TIME	= 20; 
	private static final int RADAR_PILLS	    = 5;
	
	private static ArrayList<Pill> pills;
	private static ArrayList<Pill> powerPills;

	public MOVE getMove(Game game,long timeDue) {
		int myPosition	 = game.getPacmanCurrentNodeIndex();
		this.populatePills(game);
		this.sortPowerPillsByDistance(myPosition, game);
		this.sortPillsByDistance(myPosition, game);
		//1 - Corre de ghosts não comestíveis
		int minDistance1 	= Integer.MAX_VALUE;
		GHOST nearestGhost1 	= null;
		for(GHOST ghost : GHOST.values()) {
			if(!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0) {
				if((game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost)) < SAFE_DISTANCE) && game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost)) < minDistance1){
					minDistance1  = game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost));
					nearestGhost1 = ghost;
				}
			}
		}
		if(nearestGhost1 != null) {
//			for(GHOST ghost : GHOST.values()) {
//				if(!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0) {
//					int ghostDistance  = game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost));
//					int[] ghostPath    = game.getShortestPath(myPosition, game.getGhostCurrentNodeIndex(ghost));
//					for (int j : ghostPath) {
//						for(Agent p : this.powerPills) {
//							if(p.getIndex() == j) {
//								if((ghostDistance - p.getDistance()) > p.getDistance()) {
////									System.out.println("Strategy 0");
//									return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
//								}
//							}
//						}
//					}
//				}
//			}
//			
			for (Pill p : this.pills) {
				if(p.getDistance() < RADAR_PILLS) {
					int [] path = game.getShortestPath(myPosition, p.getId());
					if(isPathSafe(path, game)) {
						if(isRadiusSafe(p, game)) {
//							System.out.println("Strategy 1.1");
							return game.getNextMoveTowardsTarget(myPosition, p.getIndex(), DM.PATH);
						}
					}
				}
			}
//			System.out.println("Strategy 1");
			return game.getNextMoveAwayFromTarget(myPosition,game.getGhostCurrentNodeIndex(nearestGhost1),DM.PATH);
		}
		//2 - Corre até o ghost no radar, se conseguir pegar uma PowerPill antes
		
		
		
		for(GHOST ghost : GHOST.values()) {
			if(!game.isGhostEdible(ghost) && game.getGhostLairTime(ghost) == 0) {
				if(game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost)) < RADAR){
					int ghostDistance  = game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost));
					int[] ghostPath    = game.getShortestPath(myPosition, game.getGhostCurrentNodeIndex(ghost));
					for (int j : ghostPath) {
						for(Pill p : this.powerPills) {
							if(p.getIndex() == j) {
								if((ghostDistance - p.getDistance()) > p.getDistance()) {
//									System.out.println("Strategy 2");
									return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
								}
							}
						}
					}
				}
			}
		}
		
		//3 - Se não tiver nenhum ghost azul, Busca as power pills
		if(!isAnyGhostEdible(game) && !haveGhostInLair(game)) {
			for (Pill p : this.powerPills) {
				int [] path = game.getShortestPath(myPosition, p.getId());
				if(isPathSafe(path, game)) {
					if(isRadiusSafe(p, game)) {
//						System.out.println("Strategy 3");
						return game.getNextMoveTowardsTarget(myPosition, p.getIndex(), DM.PATH);
					}
				}
			}
		} else {
			//4 - Se tiver verifica se está perto de algum ghost azul e tenta comer ele
			int minDistance 	= Integer.MAX_VALUE;
			GHOST nearestGhost 	= null;
			for(GHOST ghost : GHOST.values()) {
				game.getGhostLastMoveMade(ghost);
				if((game.getGhostEdibleTime(ghost) > SAFE_EDIBLE_TIME) && game.getGhostLairTime(ghost) == 0) {
					if((game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost)) < AGRESSIVE_DISTANCE) && game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost)) < minDistance){
						minDistance  = game.getShortestPathDistance(myPosition, game.getGhostCurrentNodeIndex(ghost));
						nearestGhost = ghost;
					}
				}
			}
			if(nearestGhost != null) {
//				System.out.println("Strategy 4");
				return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(nearestGhost),DM.PATH);
			}
		}

		//5 - Come as pílulas comuns por um caminho safe
		this.sortPillsByDistance(myPosition, game);
		
		for (Pill p : this.pills) {
			int [] path = game.getShortestPath(myPosition, p.getId());
			if(isPathSafe(path, game)) {
				if(isRadiusSafe(p, game)) {
//					System.out.println("Strategy 5");
					return game.getNextMoveTowardsTarget(myPosition, p.getIndex(), DM.PATH);
				}
			}
		}
		
		if(!this.powerPills.isEmpty() && !isAnyGhostEdible(game) && !haveGhostInLair(game)) {
			//6 - Come a PowerPill mais próxima, só pro boneco não travar
//			System.out.println("Strategy 6");
			return game.getNextMoveTowardsTarget(myPosition, this.powerPills.get(0).getIndex(), DM.PATH);
		}
		//7 - Come a pílula mais próxima, só pro boneco não travar
//		System.out.println("Strategy 7");
		return game.getNextMoveTowardsTarget(myPosition, this.pills.get(0).getIndex(), DM.PATH);
	}
	
	public void sortPowerPillsByDistance(int myPosition, Game game) {
		for (Pill pill : powerPills) {
			pill.calculateDistance(myPosition, game);
		}
		
		this.powerPills.sort(new Comparator<Pill>() {
			public int compare(Pill p1, Pill p2) {
				return p1.getDistance() - p2.getDistance();
			}
		});
	}
	
	public void sortPillsByDistance(int myPosition, Game game) {
		for (Pill pill : pills) {
			pill.calculateDistance(myPosition, game);
		}
		
		this.pills.sort(new Comparator<Pill>() {
			public int compare(Pill p1, Pill p2) {
				return p1.getDistance() - p2.getDistance();
			}
		});
	}
	
	public boolean isPathSafe(int [] path, Game game) {
		for (int j : path) {
			for(GHOST ghost : GHOST.values()) {
				if(game.getGhostCurrentNodeIndex(ghost) == j) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isRadiusSafe(Pill p, Game game) {
		for(GHOST ghost : GHOST.values()) {
			if(game.getShortestPathDistance(p.getIndex(), game.getGhostCurrentNodeIndex(ghost)) < p.getDistance()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isAnyGhostEdible(Game game){
		for(GHOST ghost : GHOST.values()) {
			if(game.isGhostEdible(ghost)) {
				return true;
			}
		}
		return false;
	}
	
	public void populatePills(Game game) {
		this.pills = new ArrayList<Pill>();
		for (int i : game.getActivePillsIndices()) {
			this.pills.add(new Pill(game.getPillIndex(i), i, false));
		}
		this.powerPills = new ArrayList<Pill>();
		for (int i : game.getActivePowerPillsIndices()) {
			this.powerPills.add(new Pill(game.getPowerPillIndex(i), i, true));
		}
//		for (int i : game.getPillIndices()) {
//			System.out.println("Node: (X|Y)" + game.getNodeXCood(i) + "|" + game.getNodeYCood(i));
//		}
	}
	
	public boolean haveGhostInLair(Game game) {
		for(GHOST ghost : GHOST.values()) {
			if(game.getGhostLairTime(ghost) != 0) {
				return true;
			}
		}
		return false;
	}
}