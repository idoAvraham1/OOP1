import java.util.LinkedList;
/**
 * An abstract class representing a piece on the board, which can be either a king or a pawn.
 */
public abstract class ConcretePiece implements Piece {
    private ConcretePlayer owner;          // Owner of the piece
    private LinkedList<String> steps;       // Linked list to save the history of each position the piece has visited
    private final String name;                    // Name of the piece according to the game rules
    private int numOfSquares;               // The total number of squares the piece has moved to

    /**
     * Constructor to initialize a ConcretePiece with an owner and a name.
     *
     * @param owner The player who owns the piece.
     * @param name  The name of the piece according to the game rules.
     */
    public ConcretePiece(ConcretePlayer owner, String name) {
        this.owner = owner;
        steps = new LinkedList<>();
        this.name = name;
        numOfSquares =0;
    }

    public Player getOwner() {

        return this.owner;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Gets the list of positions the piece has visited.
     *
     * @return A linked list containing the history of positions the piece has visited.
     */
    public LinkedList<String> getSteps() {
        return steps;
    }

    public int getNumOfSquares(){ return this.numOfSquares;}

    /**
     * Updates the total number of squares the piece has moved by a specified amount.
     *
     * @param i The amount by which to update the number of squares moved.
     */
    public void updateSquaresNum(int i){
        this.numOfSquares =this.numOfSquares +i;
    }

    /**
     * Returns a formatted string of all positions the piece has visited.
     *
     * @return A string in the format: name : [ position , position.. ]
     */
    public String stepsList(){
        String ans= getName() + ": [";
        // Iterate over the linked list using a regular for loop
        for (int i = 0; i < steps.size(); i++) {
            ans += steps.get(i);

            // Add a comma and space if it's not the last element
            if (i < steps.size() - 1) {
                ans += ", ";
            }
        }

        ans += "]";
        return ans;
    }

    /**
     * Adds a new position to the step list of the piece.
     *
     * @param step The position to be added to the step list.
     */
    public void addStep(String step) {
        steps.add(step);
    }



}




