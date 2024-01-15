public class Pawn extends ConcretePiece {
    private int NumOfkills;

    public Pawn(Player owner, String name) {
        super((ConcretePlayer) owner, name);
        this.NumOfkills = 0;

    }

    @Override
    public String getType() {
        return "♙";
    }

    public void Kill() {
        this.NumOfkills++;
    }
    public int getNumOfkills(){ return this.NumOfkills;}

}
