import java.util.Comparator;

public class ConcretePieceComp implements Comparator<ConcretePiece> {

    private final String sort_kind;
    private final int winner_num;

    public ConcretePieceComp(String sort_kind, int winner_num) {
        this.sort_kind = sort_kind;
        this.winner_num = winner_num;
    }

    /**
     * Compares two ConcretePiece objects based on the specified sorting criteria.
     *
     * @param o1 The first ConcretePiece to be compared.
     * @param o2 The second ConcretePiece to be compared.
     * @return A negative integer, zero, or a positive integer as the first argument is
     * less than, equal to, or greater than the second, according to the sorting
     * criteria.
     */
    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        // get the pieces names
        String o1_name1 = o1.getName();
        String o2_name2 = o2.getName();
        // Extract numerical part from the  names
        int o1_number = Integer.parseInt(o1_name1.substring(1));
        int o2_number = Integer.parseInt(o2_name2.substring(1));
        // Get the owner information
        boolean o1_owner = o1.getOwner().isPlayerOne();
        boolean o2_owner = o2.getOwner().isPlayerOne();

            if (sort_kind.equals("Sort by steps")) {
                return compareBySteps(o1, o2, o1_owner, o2_owner, o1_number, o2_number);
            }
        if (sort_kind.equals("Sort by kills")) {
            return compareByKills(o1, o2, o1_owner, o2_owner, o1_number, o2_number);
        }

        if (sort_kind.equals("Sort by stepsAmount")) {
            return compareByStepsAmount(o1, o2, o1_owner, o2_owner, o1_number, o2_number);
        }

        return 0; // Default case
        }
    /**
     * Compares two ConcretePiece objects based on the "Sort by steps" criteria.
     *
     * This method is used to determine the ordering of ConcretePiece objects when sorting
     * a collection. The comparison is primarily based on the number of steps each piece
     * visited and the winning side of the game. If two pieces have the same number of steps, their
     * order is  determined by their number .
     *
     * @param o1 The first ConcretePiece to be compared.
     * @param o2 The second ConcretePiece to be compared.
     * @param o1_owner The ownership status of the first ConcretePiece.
     * @param o2_owner The ownership status of the second ConcretePiece.
     * @param o1_number The specific number assigned to the first ConcretePiece.
     * @param o2_number The specific number assigned to the second ConcretePiece.
     * @return A negative integer if the first piece is less than the second, zero if they
     *         are equal, and a positive integer if the first piece is greater than the second.
     */

        private int compareBySteps (ConcretePiece o1, ConcretePiece o2,boolean o1_owner,
        boolean o2_owner, int o1_number, int o2_number){
            int size1 = o1.getSteps().size();
            int size2 = o2.getSteps().size();

            if (o1_owner == o2_owner) {
                if (size1 > size2) {
                    return 1;
                } else if (size1 < size2) {
                    return -1;
                } else {
                    return Integer.compare(o1_number, o2_number);
                }
            } else {
                if (o1_owner && winner_num == 1) {
                    return -1;
                } else if (!o1_owner && winner_num == 2) {
                    return -1;
                }
                return 1;
            }
        }
    /**
     * Compares two ConcretePiece objects based on the "Sort by kills" criteria.
     * The comparison is primarily determined by the number of kills associated with each piece,
     * with special consideration for the King ('♔') type, which can't make any kills.
     *
     * @param o1           the first ConcretePiece object to be compared
     * @param o2           the second ConcretePiece object to be compared
     * @param o1_owner     a boolean indicating whether the owner of the first piece is the current player
     * @param o2_owner     a boolean indicating whether the owner of the second piece is the current player
     * @param o1_number    an integer representing the position or order of the first piece
     * @param o2_number    an integer representing the position or order of the second piece
     * @return             A negative integer, zero, or a positive integer as the first argument is
     *                     less than, equal to, or greater than the second.
     */
    private int compareByKills(ConcretePiece o1, ConcretePiece o2, boolean o1_owner,
                               boolean o2_owner, int o1_number, int o2_number) {
        // king handling
        if ("♔".equals(o1.getType())) return 1;
        if ("♔".equals(o2.getType())) return -1;

        // Extract the number of kills associated with each Pawn
        int killsO1 = ((Pawn) o1).getNumOfkills();
        int killsO2 = ((Pawn) o2).getNumOfkills();

        // Compare based on the number of kills
        if (killsO1 > killsO2) {
            return -1;
        } else if (killsO1 < killsO2) {
            return 1;
        } else {
            return compareTie(o1_owner, o2_owner, winner_num,o1_number,o2_number);
        }
    }
    /**
     * Compares two ConcretePiece objects based on the "Sort by stepsAmount" criteria.
     *
     * This method is used for sorting ConcretePiece objects in descending order based on the
     * number of steps they have taken. The comparison considers the number of steps each piece
     * has, with the piece having a higher number of steps considered "less than" the one with
     * a lower number of steps. If the stepsAmount values are equal, a tie-breaking mechanism
     * is invoked by the compareTie method, taking into account additional parameters:
     * ownership status (o1_owner and o2_owner) and piece numbers (o1_number and o2_number).
     *
     * @param o1 The first ConcretePiece object to compare.
     * @param o2 The second ConcretePiece object to compare.
     * @param o1_owner The ownership status of the first piece.
     * @param o2_owner The ownership status of the second piece.
     * @param o1_number The piece number of the first piece.
     * @param o2_number The piece number of the second piece.
     * @return A negative integer, zero, or a positive integer as the first argument is
     *         less than, equal to, or greater than the second.
     */
    private int compareByStepsAmount(ConcretePiece o1, ConcretePiece o2, boolean o1_owner,
                                     boolean o2_owner, int o1_number, int o2_number) {
        int stepsAmountO1 = o1.getNumOfSteps();
        int stepsAmountO2 = o2.getNumOfSteps();

        if (stepsAmountO1 > stepsAmountO2) {
            return -1;
        } else if (stepsAmountO1 < stepsAmountO2) {
            return 1;
        } else {
            return compareTie(o1_owner, o2_owner, winner_num,o1_number,o2_number);
        }
    }


    /**
     * Compares two ConcretePiece objects based on tie-breaking logic when they have
     * different owners and the same numerical value.
     *
     * This method compares the ownership and a winner number to determine the order
     * of the objects. If the numbers are different, the comparison is based on the
     * numerical values. If the numbers are the same, the tie-breaking logic considers
     * the winner number and the ownership of the objects.
     *
     * @param o1_owner The ownership status of the first ConcretePiece.
     * @param o2_owner The ownership status of the second ConcretePiece.
     * @param winner_num The winner number (1 or 2) indicating which owner is the winner.
     * @param o1_number The numerical value of the first ConcretePiece.
     * @param o2_number The numerical value of the second ConcretePiece.
     * @return A negative integer if the first ConcretePiece should come before the second,
     *         zero if they are considered equal, and a positive integer if the second
     *         ConcretePiece should come before the first, based on the tie-breaking logic.
     */
        private int compareTie(boolean o1_owner, boolean o2_owner, int winner_num,int o1_number,int o2_number){
            // Check if the numerical values are different
            if (o1_number > o2_number)
                return 1;
            if (o1_number < o2_number)
                return -1;

            // Determine the order based on ownership and winner number
           else {

                if (o1_owner && winner_num == 1) {
                    return -1;
                } else if (!o1_owner && winner_num == 2) {
                    return -1;
                }
                return 1;
            }
        }

    }



