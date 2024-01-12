import java.io.PipedOutputStream;
import java.util.LinkedList;

public abstract class ConcretePiece implements Piece {
    private ConcretePlayer owner;
    private LinkedList<String> Steps;
    private String name;

    public ConcretePiece(ConcretePlayer owner, String name) {
        this.owner = owner;
        Steps = new LinkedList<>();
        this.name = name;

    }

    public Player getOwner() {

        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    public LinkedList<String> getSteps() {
        return Steps;
    }

    public void addStep(String step) {
        Steps.add(step);
    }


}




