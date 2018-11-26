package pacman.controllers.examples.qLearning;

import pacman.game.Constants;
import pacman.game.Game;

import java.io.Serializable;
import java.util.*;

public class Action {

    private Game game;
    private GameUtils utils;
    private Constants.DM myDM;
    private static final int SAFE_DISTANCE = 15;
    private static final int RADAR_GHOSTS  = 100;

    public Action(Game game, GameUtils utils) {
        this.game = game;
        this.utils = utils;
        this.myDM = Constants.DM.PATH;
    }

    public enum ACTION implements Serializable {
        RUN, EATPILLS, EATGHOSTS, EXPLORE;

        private static final List<ACTION> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static ACTION randomAction() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    private boolean isPathSafe(int [] path) {
        for (Agent ghost: utils.getGhosts()) {
            for (int i: path) {
                if (i != -1 && ghost.getIndex() != -1) {
                    if (game.getShortestPathDistance(i, ghost.getIndex()) < SAFE_DISTANCE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Constants.MOVE runAway() {
        //foge do ghost mais proximo
        if (utils.getGhosts().size() > 0) {
            for (Agent ghost: utils.getGhosts()) {
                if (ghost.getIndex() != -1 && utils.getPacmanNodeIndex() != -1) {
                    if (ghost.getDistance() < SAFE_DISTANCE) {
                        return game.getNextMoveAwayFromTarget(utils.getPacmanNodeIndex(), ghost.getIndex(), myDM);
                    }
                }
            }
        }

        //vai para a pilula mais proxima
        return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), utils.getPills().get(Math.round(utils.getPills().size() / 8)).getIndex(), game.getPacmanLastMoveMade(), myDM);
    }

    private Constants.MOVE eatPills() {

        //Vai para a pilula (sem GHOSTS)
        for (int i = utils.getPills().size() - 1; i >= 0; i--) {
            Agent pill = utils.getPills().get(Math.round(i / 8));
            int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), pill.getIndex(), game.getPacmanLastMoveMade());
            if (isPathSafe(path)) {
                return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), pill.getIndex(), game.getPacmanLastMoveMade(), myDM);
            }
        }

        //Vai para Come um EdibleGhost (sem GHOSTS)(
        Agent ghostEdible = isAnySafeGhostEdible();
        if (ghostEdible != null) {
            return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), ghostEdible.getIndex(), game.getPacmanLastMoveMade(), myDM);
        }
        //Vai para a PowerPill (sem GHOSTS)
        for (Agent powerPill: utils.getPowerPills()) {
            int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), powerPill.getIndex(), game.getPacmanLastMoveMade());
            if (isPathSafe(path)) {
                return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), powerPill.getIndex(), game.getPacmanLastMoveMade(), myDM);
            }
        }


        //vai para a pilula mais proxima
        return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), utils.getPills().get(Math.round(utils.getPills().size() / 8)).getIndex(), game.getPacmanLastMoveMade(), myDM);
    }

    private Constants.MOVE eatGhosts() {
        //Vai para Come um EdibleGhost (sem GHOSTS)(
        Agent ghostEdible = isAnySafeGhostEdible();
        if (ghostEdible != null) {
            return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), ghostEdible.getIndex(), game.getPacmanLastMoveMade(), myDM);
        }

        // Vai para o Edible Ghost mais próximo se estiver seguro (SEM GHOSTS)
        for (Agent edibleGhost: utils.getGhostsEdible()) {
            int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), edibleGhost.getIndex(), game.getPacmanLastMoveMade());
            if (isPathSafe(path)) {
                return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), edibleGhost.getIndex(), game.getPacmanLastMoveMade(), myDM);
            }
        }


        //vai para a pilula mais proxima
        return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), utils.getPills().get(Math.round(utils.getPills().size() / 2)).getIndex(), game.getPacmanLastMoveMade(), myDM);
    }

    private Constants.MOVE explore() {
        // vai para a power pill mais distante do pacman segura (SEM GHOSTS)
        for (int i = utils.getPowerPills().size() - 1; i >= 0; i--) {
            Agent powerPill = utils.getPowerPills().get(Math.round(i / 8));
            int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), powerPill.getIndex(), game.getPacmanLastMoveMade());
            if (isPathSafe(path)) {
                return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), powerPill.getIndex(), game.getPacmanLastMoveMade(), myDM);
            }
        }

        // vai para a pill mais distante do pacman segura (SEM GHOSTS)
        for (int i = utils.getPills().size() - 1; i >= 0; i--) {
            Agent pill = utils.getPills().get(Math.round(i / 8));
            int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), pill.getIndex(), game.getPacmanLastMoveMade());
            if (isPathSafe(path)) {
                return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), pill.getIndex(), game.getPacmanLastMoveMade(), myDM);
            }
        }


        //vai para a pilula mais proxima
        return game.getNextMoveTowardsTarget(utils.getPacmanNodeIndex(), utils.getPills().get(utils.getPills().size() - 1).getIndex(), game.getPacmanLastMoveMade(), myDM);
    }

    public Agent isAnySafeGhostEdible() {
        for (Agent ghostEdible: utils.getGhostsEdible()) {
            if (game.getShortestPathDistance(utils.getPacmanNodeIndex(), ghostEdible.getIndex(), game.getPacmanLastMoveMade()) < RADAR_GHOSTS) {
                int [] path = game.getShortestPath(utils.getPacmanNodeIndex(), ghostEdible.getIndex(), game.getPacmanLastMoveMade());
                if (isPathSafe(path)) {
                    return ghostEdible;
                }
            }
        }

        return null;
    }

    public Constants.MOVE doMove (ACTION act, Game game) {
        this.game = game;
        switch (act) {
            case EATGHOSTS:
                return this.eatGhosts();

            case EXPLORE:
                return this.explore();

            case EATPILLS:
                return this.eatPills();

            default:
                return this.runAway();
        }

    }
}
