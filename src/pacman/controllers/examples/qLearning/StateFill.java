package pacman.controllers.examples.qLearning;

public class StateFill {

    private boolean pillProxima;
    private boolean powerPillProxima;
    private int fromJunction;
    private int toJunction;

    public StateFill(boolean pillProxima, boolean powerPillProxima, int fromJunction, int toJunction) {
        this.pillProxima = pillProxima;
        this.powerPillProxima = powerPillProxima;
        this.fromJunction = fromJunction;
        this.toJunction = toJunction;
    }

    public boolean isPillProxima() {
        return pillProxima;
    }

    public void setPillProxima(boolean pillProxima) {
        this.pillProxima = pillProxima;
    }

    public boolean isPowerPillProxima() {
        return powerPillProxima;
    }

    public void setPowerPillProxima(boolean powerPillProxima) {
        this.powerPillProxima = powerPillProxima;
    }

    public int getFromJunction() {
        return fromJunction;
    }

    public void setFromJunction(int fromJunction) {
        this.fromJunction = fromJunction;
    }

    public int getToJunction() {
        return toJunction;
    }

    public void setToJunction(int toJunction) {
        this.toJunction = toJunction;
    }

}