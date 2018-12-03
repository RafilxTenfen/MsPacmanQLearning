package pacman.controllers.examples.qLearning;

import pacman.game.Constants;
import pacman.game.Game;
import java.util.ArrayList;
import java.util.Comparator;

public class GameUtils {

    private Game game;

    private Params params;

    private int pacmanNodeIndex;

    private ArrayList<Agent> pills;

    private ArrayList<Agent> powerPills;

    private ArrayList<Agent> ghosts;

    private ArrayList<Agent> ghostsEdible;

    private ArrayList<Agent> gameJunctions;

    public GameUtils(Game game, Params params) {
        this.game = game;
        this.params = params;
        this.pacmanNodeIndex = game.getPacmanCurrentNodeIndex();
        this.pills = new ArrayList<>();
        this.powerPills = new ArrayList<>();
        this.ghosts = new ArrayList<>();
        this.ghostsEdible = new ArrayList<>();
        this.gameJunctions = new ArrayList<>();
        this.populatePills();
        this.populateJunctions();
        this.populateGhosts();
        this.orderAllByDistance();
    }

    private void populateJunctions() {
        for (int i: game.getJunctionIndices()) {
            this.gameJunctions.add(new Agent(i, i, game.getShortestPathDistance(pacmanNodeIndex, i)));
        }
    }

    private void populatePills() {
        for (int indice: game.getActivePowerPillsIndices()) {
            int index = game.getPowerPillIndex(indice);
            powerPills.add(new Agent(indice, index, game.getShortestPathDistance(pacmanNodeIndex, index)));
        }

        for (int indice: game.getActivePillsIndices()) {
            int index = game.getPillIndex(indice);
            pills.add(new Agent(indice, index, game.getShortestPathDistance(pacmanNodeIndex, index)));
        }
    }

    public boolean isAgentsInRange(ArrayList<Agent> list) {
        for (Agent agent: list) {
            if (agent.getDistance() != -1 && agent.getDistance() <= this.params.getRange()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAgentsInRangeIndex(ArrayList<Agent> list, int index) {
        for (Agent agent: list) {
            if (game.getShortestPathDistance(index, agent.getIndex()) <= this.params.getRange()) {
                return true;
            }
        }
        return false;
    }

    private void populateGhosts() {
        int indice = 0;
        for (Constants.GHOST ghostType: Constants.GHOST.values()) {
            int index = game.getGhostCurrentNodeIndex(ghostType);
            if (game.isGhostEdible(ghostType)) {
                ghostsEdible.add(new Agent(indice, index, (int) game.getShortestPathDistance(pacmanNodeIndex, index)));
            } else {
                ghosts.add(new Agent(indice, index, (int) game.getShortestPathDistance(pacmanNodeIndex, index)));
            }
            indice++;
        }
    }

    private void orderAllByDistance() {
        Comparator <Agent> oComparatorDistance = (o1, o2) -> {
            if (o1.getDistance() < o2.getDistance()) {
                return -1;
            }
            if (o1.getDistance() > o2.getDistance()) {
                return 1;
            }
            return 0;
        };
        this.pills.sort(oComparatorDistance);
        this.powerPills.sort(oComparatorDistance);
        this.gameJunctions.sort(oComparatorDistance);
        this.ghosts.sort(oComparatorDistance);
        this.ghostsEdible.sort(oComparatorDistance);
    }

    public Agent getJunctionMove(Constants.MOVE move) {
        for (Agent junction: this.getGameJunctions()) {
            if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), junction.getIndex(), game.getPacmanLastMoveMade(), Constants.DM.PATH) == move) {
                return  junction;
            }
        }
        return this.getGameJunctions().get(1);
    }

    public ArrayList<Agent> getPills() {
        return pills;
    }

    public ArrayList<Agent> getPowerPills() {
        return powerPills;
    }

    public ArrayList<Agent> getGhosts() {
        return ghosts;
    }

    public ArrayList<Agent> getGhostsEdible() {
        return ghostsEdible;
    }

    public ArrayList<Agent> getGameJunctions() {
        return gameJunctions;
    }

    public int getPacmanNodeIndex() {
        return pacmanNodeIndex;
    }
}
