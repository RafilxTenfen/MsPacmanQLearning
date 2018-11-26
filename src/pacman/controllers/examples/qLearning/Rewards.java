package pacman.controllers.examples.qLearning;

import pacman.game.Constants;

public class Rewards {
    public static int DIE;
    public static int EAT_PILL;
    public static int EAT_POWER_PILL;
    public static int EAT_GHOST;

    public Rewards() {
        DIE            = -1000;
        EAT_PILL       = Constants.PILL;
        EAT_POWER_PILL = Constants.POWER_PILL;
        EAT_GHOST      = Constants.GHOST_EAT_SCORE;
    }

    public Rewards(int DIE, int EAT_PILL, int EAT_POWER_PILL, int EAT_GHOST) {
        Rewards.DIE = DIE;
        Rewards.EAT_PILL = EAT_PILL;
        Rewards.EAT_POWER_PILL = EAT_POWER_PILL;
        Rewards.EAT_GHOST = EAT_GHOST;
    }

}