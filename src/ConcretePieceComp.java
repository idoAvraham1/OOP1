import java.util.Comparator;

public class ConcretePieceComp implements Comparator<ConcretePiece> {
    public static final Comparator<ConcretePiece> SortBySteps = new ConcretePieceComp(GameLogic.Sort_By_Steps);
    private int _flag;

    public ConcretePieceComp(int flag) {
        _flag = flag;
    }

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        int ans = 0;
        if (_flag == 1) {
            if (o1.getSteps().size() > o2.getSteps().size())
                ans = 1;
            else {
                ans = -1;
            }
        }
        return ans;
    }
}
