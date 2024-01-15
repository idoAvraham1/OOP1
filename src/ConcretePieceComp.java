import java.util.Comparator;

public class ConcretePieceComp implements Comparator<ConcretePiece> {
    public static final Comparator<ConcretePiece> SortBySteps = new ConcretePieceComp(GameLogic.Sort_By_Steps);
    private int _flag;

    public ConcretePieceComp(int flag) {
        _flag = flag;
    }

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if (_flag == 1) {
            int size1 = o1.getSteps().size();
            int size2 = o2.getSteps().size();

            if (size1 > size2) {
                return 1; // Return negative value to indicate o1 comes before o2
            } else if (size1 < size2) {
                return -1;  // Return positive value to indicate o1 comes after o2
            } else {
                // Handle tie, compare names
                String name1 = o1.getName();
                String name2 = o2.getName();

                // Extract numerical part from the second character
                int num1 = Character.getNumericValue(name1.charAt(1));
                int num2 = Character.getNumericValue(name2.charAt(1));

                // Compare numerical part in descending order
                int numComparison = Integer.compare(num2, num1);

                return numComparison;
            }

        }
        return 0; // Default to equal if _flag is not 1
    }
}

