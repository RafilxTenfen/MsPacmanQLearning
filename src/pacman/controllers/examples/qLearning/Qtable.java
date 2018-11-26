package pacman.controllers.examples.qLearning;

import java.io.Serializable;
import java.util.HashMap;

public class Qtable implements Serializable {

    private HashMap<State, HashMap<Action.ACTION, Integer>> table = new HashMap<>();

    public HashMap<State, HashMap<Action.ACTION, Integer>> getTable() {
        return table;
    }

    public void addStateItem(State state, Action.ACTION action, int reward) {
        if (containsState(state)) {
            for (State stateTable: table.keySet()) {
                if (stateEqual(stateTable, state)) {
                    HashMap<Action.ACTION, Integer> hmItem = table.get(stateTable);
                    if (hmItem != null) {
                        if (!containsAction(hmItem, action)) {
                            table.get(stateTable).put(action, reward);
                        } else {
                            for (Action.ACTION act: hmItem.keySet()) {
                                if (actionEquals(act, action) && hmItem.get(act) == 0) {
                                    table.get(stateTable).put(act, reward);
                                }
                            }
                        }
                    } else {
                        HashMap<Action.ACTION, Integer> hmItemNew = new HashMap<>();
                        hmItemNew.put(action, reward);
                        table.put(stateTable, hmItemNew);
                    }
                }
            }
        } else {
            HashMap<Action.ACTION, Integer> hmItem = new HashMap<>();
            hmItem.put(action, reward);
            table.put(state, hmItem);
        }
    }

    private boolean containsState(State oState) {
        for (State state: table.keySet()) {
            if (stateEqual(state, oState)) {
                return true;
            }
        }
        return false;
    }

    private boolean stateEqual(State s1, State s2) {
        if (s1.isGhosts() == s2.isGhosts() && s1.isGhostsEdible() == s2.isGhostsEdible() &&
            s1.isPills() == s2.isPills() && s1.isPowerPills() == s2.isPowerPills()) {
            return true;
        }

        return false;
    }

    private boolean containsAction(HashMap<Action.ACTION, Integer> hmItem, Action.ACTION oAction) {
        for (Action.ACTION action: hmItem.keySet()) {
            if (actionEquals(action, oAction)) {
                return true;
            }
        }
        return false;
    }

    private boolean actionEquals(Action.ACTION a1, Action.ACTION a2) {
        if (a1.compareTo(a2) == 0) {
            return true;
        }
        return false;
    }

    public boolean isReady(){
        if (this.table.size() == 12){
            for (HashMap<Action.ACTION, Integer> hmItem: this.table.values()) {
                if (hmItem.size() != 4) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Action.ACTION getActionMaxReward(State state) {
        Action.ACTION myAct = Action.ACTION.RUN; //default
        int maxRewardItem = Integer.MIN_VALUE;

        for (State tableState: this.table.keySet()) {
            if (stateEqual(tableState, state)) {
                HashMap<Action.ACTION, Integer> hmItem = table.get(tableState);
                if (hmItem != null) {
                    for (Action.ACTION action: hmItem.keySet()) {
                        if (hmItem.get(action) > maxRewardItem) {
                            maxRewardItem = hmItem.get(action);
                            myAct = action;
                        }
                    }
                }
            }
        }

        return myAct;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tabela Q \n g = Ghost, ge = GhostEdible , p = Pill, pp = PowerPill \n");
        for (State state: this.table.keySet()) {
            stringBuilder.append(state.toString());
            HashMap<Action.ACTION, Integer> hmItem = this.table.get(state);
            stringBuilder.append(" = [");
            for (Action.ACTION action: hmItem.keySet()) {
                stringBuilder.append("{" + action + ": " + hmItem.get(action) + " }");
            }
            stringBuilder.append("] \n");
        }

        return stringBuilder.toString();
    }
}
