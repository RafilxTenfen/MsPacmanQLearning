package pacman.controllers.examples.qLearning;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;

public class DibraldinhoQLearningFillTable extends Controller<Constants.MOVE> {

    private Params params;
    private GameUtils utils;
    private Rewards rewards;
    private Action action;
    private Qtable table;

    //REWARDS COUNT, ACTION AND STATE
    private State state;
    private int qntPills;
    private int qntPowerPills;
    private int qntGhostEaten;
    private int indexPacMan;
    private Action.ACTION actMade;

    public DibraldinhoQLearningFillTable(Params oParams, Qtable qtable) {
        this.params = oParams;
        this.table = qtable;
        this.rewards = new Rewards();
        this.qntPills = -1;
        this.qntPowerPills = -1;
        this.qntGhostEaten = -1;
        this.indexPacMan = 0;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.utils = new GameUtils(game, params);
        this.action = new Action(game, utils);

        if (qntPills != -1 && qntPowerPills != -1 && qntGhostEaten != -1 && actMade != null) {
            qntPills -= game.getNumberOfActivePills();
            qntPowerPills -= game.getNumberOfActivePowerPills();
            qntGhostEaten = game.getNumGhostsEaten();

            this.table.addStateItem(state, actMade, this.getReward(game.wasPacManEaten(), (int) game.getEuclideanDistance(indexPacMan, game.getPacmanCurrentNodeIndex())));
        }

        qntPills = game.getNumberOfActivePills();
        qntPowerPills = game.getNumberOfActivePowerPills();
        qntGhostEaten = game.getNumGhostsEaten();
        indexPacMan = game.getPacmanCurrentNodeIndex();

        this.state = new State(utils.isAgentsInRange(this.utils.getGhosts()), utils.isAgentsInRange(this.utils.getGhostsEdible()),
                               utils.isAgentsInRange(this.utils.getPills()), utils.isAgentsInRange(this.utils.getPowerPills()));

        //makeMove
        actMade = Action.ACTION.randomAction();
        //actMade = Action.ACTION.RUN;
        return action.doMove(actMade, game);
    }

    private int getReward (boolean pacManEaten, int pacmanDistance) {
        if (pacManEaten){
            return Rewards.DIE;
        }

        return ((qntPills * Rewards.EAT_PILL) + (qntPowerPills * Rewards.EAT_POWER_PILL) +
                (qntGhostEaten * Rewards.EAT_GHOST) + pacmanDistance);
    }



}