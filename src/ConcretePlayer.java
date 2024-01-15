public class ConcretePlayer implements Player {
    private final int PlayerNum; // 1 is the defender.
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

    public void Win() {
        this.WinsNum++;
    }



}
