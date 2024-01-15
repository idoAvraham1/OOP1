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

    public String stepsList(){
        String ans= getName() + ": [";
        // Iterate over the linked list using a regular for loop
        for (int i = 0; i < Steps.size(); i++) {
            ans += Steps.get(i);

            // Add a comma and space if it's not the last element
            if (i < Steps.size() - 1) {
                ans += ", ";
            }
        }

        ans += "]";
        return ans;
    }
    public void addStep(String step) {
        Steps.add(step);
    }



}




