package pacman.controllers.examples.qLearning;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

public class DibraldinhoQLearning extends Controller<Constants.MOVE> {

    private Params params;
    private GameUtils utils;
    private State state;
    private Qtable table;
    private Action action;

    public DibraldinhoQLearning(Params params, Qtable table) {
        this.params = params;
        this.table = table;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.utils = new GameUtils(game, params);
        this.action = new Action(game, utils);

        this.state = new State(utils.isAgentsInRange(this.utils.getGhosts()), utils.isAgentsInRange(this.utils.getGhostsEdible()),
                               utils.isAgentsInRange(this.utils.getPills()), utils.isAgentsInRange(this.utils.getPowerPills()));

        return action.doMove(table.getActionMaxReward(state), game);
    }
}
