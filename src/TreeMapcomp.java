import java.util.Comparator;

public class TreeMapcomp implements Comparator<Position> {
    private int[][] likePosition;
    public TreeMapcomp(int[][] likePosition){
        this.likePosition=likePosition;
    }
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

    public int getValue(Position a){

        return likePosition[a.getRow()][a.getColum()];
    }

}
