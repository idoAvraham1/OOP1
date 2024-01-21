import java.util.Comparator;

/**
 * This class implements a Comparator for TreeMap sorting based on a 2D array representing positions.
 * The comparator sorts the TreeMap based on the number of different pawns that stepped on each position.
 */
public class TreeMapcomp implements Comparator<Position> {
    private int[][] likePosition; // 2D array representing positions
    public TreeMapcomp(int[][] likePosition){
        this.likePosition=likePosition;
    }

    /**
     * Compares two Position objects based on the sorting criteria.
     *
     * The comparison is primarily based on the number of different pawns that stepped on each position,
     * followed by the row (x-coordinate) and column (y-coordinate) of the positions.
     *
     * @param pos1 The first Position to be compared.
     * @param pos2 The second Position to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is
     * less than, equal to, or greater than the second, according to the sorting criteria.
     */
    @Override
    public int compare(Position pos1, Position pos2) {
        // Compare by value in descending order
        int valueComparison = Integer.compare(getValue(pos2), getValue(pos1));

        if (valueComparison != 0) {
            // If values are not equal, return the value comparison
            return valueComparison;
        } else {
            // If values are equal, compare by x in ascending order
            int xComparison = Integer.compare(pos1.getRow() , pos2.getRow());

            if (xComparison != 0) {
                // If x values are not equal, return the x comparison
                return xComparison;
            } else {
                // If x values are equal, compare by y in ascending order
                return Integer.compare(pos1.getColum() , pos2.getColum());
            }
        }
    }

    /**
     * Gets the value from the likePosition array at the specified position.
     *
     * @param a The Position object representing the position.
     * @return The value from the likePosition array at the specified position.
     */
    public int getValue(Position a){

        return likePosition[a.getRow()][a.getColum()];
    }

}
