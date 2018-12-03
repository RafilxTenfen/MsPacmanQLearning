package pacman.controllers.examples.qLearning;


import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.List;

public class DibraldinhoFillTable extends Controller<Constants.MOVE> {

    private Agent ChoicedJunction;
    private Game game;
    private Params params;
    private GameUtils gameUtils;
    private Rewards rewards;
    private Agent choicedJunction;
    private Agent lastJunction;
    private Tree arvore;

    public DibraldinhoFillTable(Params params) {
        this.rewards = new Rewards();
        this.params = params;
        choicedJunction = null;
    }

    private boolean isPacmanInChoicedJunction() {
        return this.choicedJunction.getIndex() == gameUtils.getPacmanNodeIndex();
    }

    public Agent getJunctionPill() {
        for (Agent junction: gameUtils.getGameJunctions()) {

            if (game.getShortestPathDistance(junction.getIndex(), gameUtils.getPills().get(0).getIndex()) < params.getRange()) {
                return junction;
            }
        }
        return null;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.game = game;
        this.gameUtils = new GameUtils(game, params);
        int indexToGo = -1;

        if (lastJunction == null) {
            lastJunction = gameUtils.getGameJunctions().get(0);
        }

        if (choicedJunction != null) {
            indexToGo = choicedJunction.getIndex();
            if (game.getPacmanCurrentNodeIndex() == choicedJunction.getIndex()) {
                lastJunction = null;
                choicedJunction = null;
            }

            if (game.getPacmanCurrentNodeIndex() != indexToGo) {
                return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), indexToGo, game.getPacmanLastMoveMade(), Constants.DM.PATH);
            }
        }

        return null;
    }
}
