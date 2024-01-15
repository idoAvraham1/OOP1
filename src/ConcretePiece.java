import java.util.LinkedList;

public abstract class ConcretePiece implements Piece {
    private ConcretePlayer owner;
    private LinkedList<String> Steps;
    private String name;

    private int numOfSteps;

    public ConcretePiece(ConcretePlayer owner, String name) {
        this.owner = owner;
        Steps = new LinkedList<>();
        this.name = name;
        numOfSteps=0;
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

    public int getNumOfSteps(){ return this.numOfSteps;}

    public void updateStepsNum(int i){
        this.numOfSteps=this.numOfSteps+i;
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




