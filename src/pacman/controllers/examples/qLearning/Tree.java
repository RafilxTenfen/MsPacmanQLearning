package pacman.controllers.examples.qLearning;

import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    private List<Nodo> arvore;
    private Game game;
    private GameUtils gameUtils;
    private Params params;
    private int safeDistance = 20;

    public Tree(Game game, GameUtils gameUtils, Params params) {
        this.game = game;
        this.gameUtils = gameUtils;
        this.params = params;
        this.arvore = new ArrayList<>();
    }

    private boolean isAllNodoMaxLevel() {
        for (Nodo nodo: this.arvore) {
            if (nodo.getNivel() != params.getNivelArvore()) {
                return false;
            }
        }

        return true;
    }

    private Nodo getFirstNodoFromArvore() {
        for (int i = 0; i <= this.arvore.size(); i++) {
            if (this.arvore.get(i).getNivel() != this.params.getNivelArvore()) {
                return this.arvore.remove(i);
            }
        }
        return null;
    }

    private boolean isIndexInFatherTree(int index, Nodo nodoPai) {
        Nodo aux = nodoPai;

        while (aux.getNodoPai() != null) {
            if (aux.getIndex() == index) {
                return true;
            }
            aux = aux.getNodoPai();
        }

        return false;
    }

    private boolean isGhostInIndex(int index) {
        for (Agent ghost: gameUtils.getGhosts()) {
            if (game.getShortestPathDistance(index, ghost.getIndex()) < this.safeDistance) {
                return true;
            }
        }
        return false;
    }

    public void criaArvore() {
        Nodo msNodo = new Nodo(gameUtils.getPacmanNodeIndex(), 0);
        this.arvore.add(msNodo);
        while (!isAllNodoMaxLevel()) {
            Nodo nodoAtual = getFirstNodoFromArvore();
            if (nodoAtual != null) {
                for (int index: game.getNeighbouringNodes(nodoAtual.getIndex())) {
                    if (!isGhostInIndex(index)) {
                        if (!isIndexInFatherTree(index, nodoAtual)) {
                            this.arvore.add(new Nodo(index, nodoAtual.getNivel() + 1, nodoAtual));
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public Agent getJunctionPill() {
        for (Agent junction: gameUtils.getGameJunctions()) {
            if (game.getShortestPathDistance(junction.getIndex(), gameUtils.getPills().get(0).getIndex()) < params.getRange()) {
                return junction;
            }
        }

        return gameUtils.getGameJunctions().get(gameUtils.getGameJunctions().size() - 1);
    }




}
