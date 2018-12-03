package pacman.controllers.examples.qLearning;

public class Params {

    private int range;
    private int nivelArvore;

    public Params(int range) {
        this.range = range;
    }

    public Params(int range, int nivelArvore) {
        this.range = range;
        this.nivelArvore = nivelArvore;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getNivelArvore() {
        return nivelArvore;
    }

    public void setNivelArvore(int nivelArvore) {
        this.nivelArvore = nivelArvore;
    }
}