package pacman.controllers.examples.qLearning;

public class Nodo {

    private int index;
    private Nodo nodoPai;
    private int nivel;

    public Nodo(int index, int nivel) {
        this.index = index;
        this.nivel = nivel;
    }

    public Nodo(int index, int nivel, Nodo nodoPai) {
        this.index = index;
        this.nodoPai = nodoPai;
        this.nivel = nivel;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Nodo getNodoPai() {
        return nodoPai;
    }

    public void setNodoPai(Nodo nodoPai) {
        this.nodoPai = nodoPai;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
