package pacman.controllers.examples.qLearning;

import java.io.Serializable;

public class State implements Serializable {

    private boolean ghosts;

    private boolean ghostsEdible;

    private boolean pills;

    private boolean powerPills;

    public State(boolean ghosts, boolean ghostsEdible, boolean pills, boolean powerPills) {
        this.ghosts = ghosts;
        this.ghostsEdible = ghostsEdible;
        this.pills = pills;
        this.powerPills = powerPills;
    }

    public boolean isGhosts() {
        return ghosts;
    }

    public void setGhosts(boolean ghosts) {
        this.ghosts = ghosts;
    }

    public boolean isGhostsEdible() {
        return ghostsEdible;
    }

    public void setGhostsEdible(boolean ghostsEdible) {
        this.ghostsEdible = ghostsEdible;
    }

    public boolean isPills() {
        return pills;
    }

    public void setPills(boolean pills) {
        this.pills = pills;
    }

    public boolean isPowerPills() {
        return powerPills;
    }

    public void setPowerPills(boolean powerPills) {
        this.powerPills = powerPills;
    }

    @Override
    public String toString() {
        return "{ g: " + (this.ghosts) + ", ge: " + (this.ghostsEdible) + ", p: " + (this.pills) + ", pp: " + (this.powerPills) + " }";
    }
}
