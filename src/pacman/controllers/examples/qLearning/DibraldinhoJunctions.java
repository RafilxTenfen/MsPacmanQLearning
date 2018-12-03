package pacman.controllers.examples.qLearning;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.Random;

public class DibraldinhoJunctions extends Controller<Constants.MOVE> {

    private GameUtils gameUtils;
    private Game game;
    private Params params;
    private Agent fromJunction;
    private Agent toJunction;
    private Action action;
    private boolean pacmanRichedFromJunction;
    private boolean pacmanRichedToJunction;

    public DibraldinhoJunctions(Params params) {
        this.params = params;
        this.fromJunction = null;
        this.toJunction = null;
        this.pacmanRichedFromJunction = false;
        this.pacmanRichedToJunction = false;
    }

    /**
     * Com 10% de chance ele não pega uma junction da tabela que maximiza
     * @return boolean
     */
    public boolean getMinMaxRandomMove() {
        Random random = new Random();

        return random.nextInt(10) > 8;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.game = game;
        this.gameUtils = new GameUtils(game, this.params);
        this.action = new Action(game, gameUtils);

        if (fromJunction == null) {
            fromJunction = gameUtils.getGameJunctions().get(0);
        }

        if (fromJunction.getIndex() == gameUtils.getPacmanNodeIndex()) {
            this.pacmanRichedFromJunction = true;
        }

        if (!pacmanRichedFromJunction) {
            return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), fromJunction.getIndex(), game.getPacmanLastMoveMade(), Constants.DM.PATH);
        }

        if (this.toJunction == null) {
            if (getMinMaxRandomMove()) {
                this.toJunction = gameUtils.getJunctionMove(action.getRandomAction());
            } else {
                this.toJunction = gameUtils.getJunctionMove(action.getRandomAction());
            }
        } else {
            if (this.toJunction.getIndex() != game.getPacmanCurrentNodeIndex() && !this.pacmanRichedToJunction) {
                return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), toJunction.getIndex(), game.getPacmanLastMoveMade(), Constants.DM.PATH);
            } else {
                this.pacmanRichedToJunction = true;

            }
        }





        return null;
    }

    public Agent getJunctionMove(Constants.MOVE move) {
        for (Agent junction: gameUtils.getGameJunctions()) {
            if (game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), junction.getIndex(), game.getPacmanLastMoveMade(), Constants.DM.PATH) == move) {
                return  junction;
            }
        }
        return gameUtils.getGameJunctions().get(1);
    }



}
