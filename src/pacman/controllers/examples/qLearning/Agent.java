package pacman.controllers.examples.qLearning;

import java.io.Serializable;

public class Agent implements Serializable {

    private int indice;

    private int index;

    private int distance;

    public Agent(int indice, int index, int distanceToPacman) {
        this.indice = indice;
        this.index = index;
        this.distance = distanceToPacman;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

}