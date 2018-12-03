package pacman.controllers.examples.qLearning;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Random;

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
    private int countTable;
    private int discount;

    public DibraldinhoQLearningFillTable(Params oParams, Qtable qtable) {
        this.params = oParams;
        this.table = qtable;
        this.rewards = new Rewards();
        this.qntPills = -1;
        this.qntPowerPills = -1;
        this.qntGhostEaten = -1;
        this.indexPacMan = 0;
        this.countTable = 0;
        this.discount = 10;
    }

    public boolean getMinMaxRandomMove() {
        Random random = new Random();
        this.discount *= 0.90;
        return random.nextInt(Math.round(this.discount + 1)) > 6;
    }


    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        this.utils = new GameUtils(game, params);
        this.action = new Action(game, utils);

        if (qntPills != -1 && qntPowerPills != -1 && qntGhostEaten != -1 && actMade != null) {
            qntPills -= game.getNumberOfActivePills();
            qntPowerPills -= game.getNumberOfActivePowerPills();
            qntGhostEaten = game.getNumGhostsEaten();

            this.table.addStateItem(state, actMade, this.getReward(game.wasPacManEaten(), game.getShortestPathDistance(indexPacMan, game.getPacmanCurrentNodeIndex())));
        }

        qntPills = game.getNumberOfActivePills();
        qntPowerPills = game.getNumberOfActivePowerPills();
        qntGhostEaten = game.getNumGhostsEaten();
        indexPacMan = game.getPacmanCurrentNodeIndex();

        this.state = new State(utils.isAgentsInRange(this.utils.getGhosts()), utils.isAgentsInRange(this.utils.getGhostsEdible()),
                               utils.isAgentsInRange(this.utils.getPills()), utils.isAgentsInRange(this.utils.getPowerPills()));


        //makeMove
        if (!this.getMinMaxRandomMove()) {
            Constants.MOVE moveToBeMade = action.doMove(table.getActionMaxReward(this.state), game);
            int index = game.getNeighbour(game.getPacmanCurrentNodeIndex(), moveToBeMade);

            if (index != -1) {
                 this.state = new State(utils.isAgentsInRangeIndex(this.utils.getGhosts(), index), utils.isAgentsInRangeIndex(this.utils.getGhostsEdible(), index),
                                            utils.isAgentsInRangeIndex(this.utils.getPills(), index), utils.isAgentsInRangeIndex(this.utils.getPowerPills(), index));

                actMade = table.getActionMaxReward(this.state);
            }
        } else {
            actMade = Action.ACTION.randomAction();
        }

        if (actMade == null){
            actMade = Action.ACTION.randomAction();
        }

        return action.doMove(actMade, game);
    }

    private int getReward (boolean pacManEaten, int pacmanDistance) {
        if (pacManEaten){
            this.discount = 10;
            return Rewards.DIE;
        }

        return ((qntPills * Rewards.EAT_PILL) + (qntPowerPills * Rewards.EAT_POWER_PILL) +
                (qntGhostEaten * Rewards.EAT_GHOST));
    }

}