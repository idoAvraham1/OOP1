public class King extends ConcretePiece {

    public King(Player owner, String name) {
        super((ConcretePlayer) owner, name);
    }

    @Override
    public String getType() {
        return "\u2654";

    }
}
