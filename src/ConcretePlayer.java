/**
 * This class represents a concrete player with a player number and a count of wins.
 * The player number can be 1 for the defender or 2 for the attacker.
 */

public class ConcretePlayer implements Player {
    private final int PlayerNum; // Player number (1 for defender, 2 for attacker)
    private int WinsNum;

    public ConcretePlayer(int PlayerNum) {
        this.PlayerNum = PlayerNum;
        this.WinsNum = 0;

    }

    public boolean isPlayerOne() {
        return PlayerNum == 1;
    }

    public int getPlayerNum() {return PlayerNum;}

    public int getWins() {
        return WinsNum;

    }

    /**
     * Increments the count of wins for the player.
     */
    public void Win() {
        this.WinsNum++;
    }



}
