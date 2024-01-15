import java.util.ArrayList;
import java.util.Comparator;

public class GameLogic implements PlayableLogic {
    ArrayList<ConcretePiece> pawns= new ArrayList<>();

    private static final int BOARD_SIZE = 11;
    private static final String KING_SYMBOL = "♔";
    public   static  boolean  Winner; // 1 or 2
    private ConcretePlayer PlayerOne = new ConcretePlayer(1);
    private ConcretePlayer PlayerTwo = new ConcretePlayer(2);
    private Position King_position;
    private int countMove; //2 first

    private ConcretePiece[][] board = new ConcretePiece[11][11];
    public GameLogic() {
        Initgame();
        PlayerOne = new ConcretePlayer(1);
        PlayerTwo = new ConcretePlayer(2);
        King_position = new Position(5, 5);
        countMove = 0;
        Winner=false;
    }

    /**
     * Moves a piece from position a to position b.
     * This method checks if the move is valid, updates the positions,
     * and handles killing pieces if necessary.
     *
     * @param a Starting position of the piece
     * @param b Destination position for the piece
     * @return True if the move is successful, otherwise false
     */
    public boolean move(Position a, Position b) {
        // Check if the move is valid
        if (isValidMove(a, b)) {
            // Update the position of the king if it's the one moving
            if (a.isEqual(King_position)) {
                UpdateKingPosition(b);
            }
            // Move the piece to the new position
            makeMove(a, b);

            // Check for possible kills after the move, if the the one moving isn't the king
            if (!getPieceAtPosition(b).getType().equals(KING_SYMBOL)) {
                checkForPossibleKills(b);
            }
            // update the number of steps the pawn made
            updateSteps(a,b);

            // check for possiblie win of one of the sides after every valid move
              checkWinningTerms();

            // The move was successful
            return true;
            }
        // The move was not valid
        return false;
    }
    private void checkWinningTerms(){
        //if the king reached a corner, player one wins
        if (isKingAtCorner()) {
            PlayerOne.Win();
            prints(1);
            Winner=true;
        }
        // If the king is surrounded by the pawns of the second player, the second player wins
        if (isKingSurrouned()) {
            PlayerTwo.Win();
             prints(2);
            Winner=true;
        }
    }

    private void updateSteps(Position current, Position destination ){
        int current_x=current.getRow();
        int current_y=current.getColum();
        int destination_x=destination.getRow();
        int destination_y=destination.getColum();
        int ans= Math.abs((current_x-destination_x)+(current_y-destination_y));

        board[destination_x][destination_y].updateStepsNum(ans);
    }


    /**
     * Retrieves the piece at a given position on the board.
     *
     * @param position The position to check for a piece
     * @return The piece at the specified position
     */

    public Piece getPieceAtPosition(Position position) {
        return board[position.getRow()][position.getColum()];

    }

    /**
     * Retrieves the first player.
     *
     * @return The first player
     */
    public Player getFirstPlayer() {
        return PlayerOne;
    }

    /**
     * Retrieves the second player.
     *
     * @return The second player
     */
    public Player getSecondPlayer() {
        return PlayerTwo;
    }

    /**
     * Checks if the game has finished.
     *
     * @return True if the game is finished, otherwise false
     */
    public boolean isGameFinished() {

        return Winner;
    }

    /**
     * Checks if it's the second player's turn.
     * Since the second player starts the game, if the move counter is even,
     * it's the second player's turn.
     *
     * @return True if it's the second player's turn, otherwise false
     */
    public boolean isSecondPlayerTurn() {
        return countMove % 2 == 0;

    }

    /**
     * Resets the game to its initial state.
     */
    public void reset() {

        countMove = 0;
        UpdateKingPosition(new Position(5, 5));
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++)
                board[i][j] = null;
        }
        Initgame();
    }


    public void undoLastMove() {

    }


    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Updates the king's position on the board.
     *
     * @param b The new position for the king
     */
    private void UpdateKingPosition(Position b) {
        this.King_position.setRow(b.getRow());
        this.King_position.setColum(b.getColum());
    }

    /**
     * Initializes the game board with pieces in their starting positions.
     */
    private void Initgame() {
        board[5][3] = new Pawn(PlayerOne, "D1");
        board[5][3].addStep(new Position(5,3).toString());

        board[5][5] = new King(PlayerOne, "K7");
        board[5][5].addStep(new Position(5,5).toString());

        board[5][7] = new Pawn(PlayerOne, "D13");
        board[5][7].addStep(new Position(5,7).toString());

        for (int i = 4; i < 7; i++) {
            board[i][4] = new Pawn(PlayerOne, "D"+(i-2));
            board[i][4].addStep(new Position(i,4).toString());
        }
        for (int i = 3; i < 8; i++) {
            if (i == 5) {
                continue;
            }
            board[i][5] = new Pawn(PlayerOne, "D"+(i+2));
            board[i][5].addStep(new Position(i,5).toString());
        }

        for (int i = 4; i < 7; i++) {
            board[i][6] = new Pawn(PlayerOne, "D"+(i+6));
            board[i][6].addStep(new Position(i,6).toString());
        }


        board[5][1] = new Pawn(PlayerTwo, "A6");
        board[5][1].addStep(new Position(5,1).toString());
        for (int i = 3; i < 8; i++) {
            board[i][0] = new Pawn(PlayerTwo, "A"+(i-2));
            board[i][0].addStep(new Position(i,0).toString());
        }
        board[1][5] = new Pawn(PlayerTwo, "A12");
        board[1][5].addStep(new Position(1,5).toString());
        int j=7;
        for (int i = 3; i < 8; i++) {
            if (i==6){
                board[0][i] = new Pawn(PlayerTwo, "A15");
                board[0][i].addStep(new Position(0,i).toString());
                j=17;
                continue;
            }
            board[0][i] = new Pawn(PlayerTwo, "A"+(j));
            board[0][i].addStep(new Position(0,i).toString());
            j=j+2;
        }
        j=20;
        board[5][9] = new Pawn(PlayerTwo, "A19");
        board[5][9].addStep(new Position(5,9).toString());
        for (int i = 3; i < 8; i++) {
            board[i][10] = new Pawn(PlayerTwo, "A"+j);
            board[i][10].addStep(new Position(i,10).toString());
            j++;
        }
        board[9][5] = new Pawn(PlayerTwo, "A13");
        board[9][5].addStep(new Position(9,5).toString());
        j=8;
        for (int i = 3; i < 8; i++) {
            if (i==5){
                board[10][i] = new Pawn(PlayerTwo, "A14");
                board[10][i].addStep(new Position(10,i).toString());
                j=16;
                continue;
            }
            board[10][i] = new Pawn(PlayerTwo, "A"+j);
            board[10][i].addStep(new Position(10,i).toString());
            j=j+2;
        }
        // init the arraylist with all the pieces
          initArrayLists();

    }

    private void initArrayLists() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    pawns.add(board[i][j]);
                }
            }
        }
    }

    /**
     * Checks if a move from position a to position b is valid.
     * Validates the move based on the piece type, owner's turn, and path validity.
     *
     * @param a Starting position of the piece
     * @param b Destination position for the piece
     * @return True if the move is valid, otherwise false
     */
    private boolean isValidMove(Position a, Position b) {
        // Check for self-move, diagonal move, or moving from an empty square
        if (a.isEqual(b) || a.isDiagonal(b) || board[a.getRow()][a.getColum()] == null
                || board[b.getRow()][b.getColum()] != null) {
            return false;
        }

        // Check if the piece is not a king and moving to a corner
        if (!getPieceAtPosition(a).getType().equals(KING_SYMBOL) && b.isCorner()) {
            return false;
        }
        // Check if it's the second player's turn and the piece belongs to the first player
        if (isSecondPlayerTurn() && getPieceAtPosition(a).getOwner().isPlayerOne()
                || !isSecondPlayerTurn() && !getPieceAtPosition(a).getOwner().isPlayerOne()) {
            return false;
        }
        // Check if the path from a to b is valid
        return isValidPath(a, b);
    }

    /**
     * Checks if the path from position a to position b is valid.
     * Ensures that the path between positions is clear for movement.
     *
     * @param a Starting position of the piece
     * @param b Destination position for the piece
     * @return True if the path is valid, otherwise false
     */
    private boolean isValidPath(Position a, Position b) {
        // Check if the movement is along the same column
        if (a.getColum() == b.getColum()) {
            // Check upward movement
            if (a.getRow() < b.getRow()) {
                for (int i = a.getRow() + 1; i < b.getRow(); i++) {
                    if (board[i][a.getColum()] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
            // Check downward movement
            else if (a.getRow() > b.getRow()) {
                for (int i = b.getRow() + 1; i < a.getRow(); i++) {
                    if (board[i][a.getColum()] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
        }
        // Check if the movement is along the same row
        else {
            // Check leftward movement
            if (a.getColum() < b.getColum()) {
                for (int i = a.getColum() + 1; i < b.getColum(); i++) {
                    if (board[a.getRow()][i] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
            // Check rightward movement
            else if (a.getColum() > b.getColum()) {
                for (int i = b.getColum() + 1; i < a.getColum(); i++) {
                    if (board[a.getRow()][i] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
        }

        // The path is clear
        return true;
    }

    /**
     * Makes a move from startPosition to destinationPosition.
     * Moves the piece from startPosition to destinationPosition and updates the move count.
     *
     * @param startPosition       Starting position of the piece
     * @param destinationPosition Destination position for the piece
     */
    private void makeMove(Position startPosition, Position destinationPosition) {
        board[destinationPosition.getRow()][destinationPosition.getColum()] = board[startPosition.getRow()][startPosition.getColum()];
        // add the current step to the pawn step-list
        board[destinationPosition.getRow()][destinationPosition.getColum()].addStep(destinationPosition.toString());
        board[startPosition.getRow()][startPosition.getColum()] = null;
        countMove++;
    }

    /**
     * Checks for possible kills after moving a pawn to the destination position.
     * Determines the type of position and checks if a kill can happen at that position after the move.
     *
     * @param destinationPosition The destination position after the move
     */
    private void checkForPossibleKills(Position destinationPosition) {
        if (destinationPosition.isUniquePlace()) {
            checkForUniqueKill(destinationPosition);
        }
        if (destinationPosition.isEdge()) {
            checkForCornerKill(destinationPosition);
        }
        checkForDirectionalKill(destinationPosition);
    }

    /**
     * Checks if a corner-kill can occur after moving a pawn to position b.
     * Determines if the destination position leads to a corner-kill and validates if a kill happens at that corner.
     *
     * @param destinationPosition The destination position after the move.
     * @return True if a kill can occur, otherwise false.
     */
    private void checkForCornerKill(Position destinationPosition) {
        int row = destinationPosition.getRow();
        int column = destinationPosition.getColum();
        boolean owner = getPieceAtPosition(destinationPosition).getOwner().isPlayerOne();

        // check for a kill in the first row.
        if (row == 1 && board[0][column] != null && board[0][column].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = board[0][column];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                ((Pawn) board[row][column]).Kill();
                board[0][column] = null;
            }
        }
        // check for a kill in the first column
        if (column == 1 && board[row][0] != null && board[row][0].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = board[row][0];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                ((Pawn) board[row][column]).Kill();
                board[row][0] = null;

            }
        }
        // check for a kill in the last row
        if (row == 9 && board[10][column] != null && board[10][column].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = board[10][column];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                ((Pawn) board[row][column]).Kill();
                board[10][column] = null;

            }
        }
        // check for a kill in the last column
        if (column == 9 && board[row][10] != null && board[row][10].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = board[row][10];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                ((Pawn) board[row][column]).Kill();
                board[row][10] = null;
            }
        }
    }

    /**
     * Checks if a directional kill can occur after moving a pawn to the destination position.
     * Examines different directional movements to detect potential kills after the pawn's move.
     *
     * @param destinationPosition The destination position after the pawn's move.
     */
    private void checkForDirectionalKill(Position destinationPosition) {
        int i = destinationPosition.getRow();
        int j = destinationPosition.getColum();
        boolean owner = getPieceAtPosition(destinationPosition).getOwner().isPlayerOne();

        // Check for potential kills in different directions
        performDirectionalKill(i, j, i - 2, j, i - 1, j, owner);
        performDirectionalKill(i, j, i + 2, j, i + 1, j, owner);
        performDirectionalKill(i, j, i, j - 2, i, j - 1, owner);
        performDirectionalKill(i, j, i, j + 2, i, j + 1, owner);
    }

    /**
     * Checks and performs a directional kill if the conditions are met.
     * Checks if a pawn can perform a kill in a specific direction after moving.
     *
     * @param currentRow    The current row of the pawn that moved.
     * @param currentColumn The current column of the pawn that moved.
     * @param middleRow     The  row where the target is located.
     * @param middleColumn  The  column where the target is located.
     * @param closingRow    The closing row between the row of the current mov and the target row.
     * @param closingColumn The closing column between the column of the current move and the target column.
     * @param owner         The owner of the moving pawn.
     */
    private void performDirectionalKill(int currentRow, int currentColumn, int closingRow, int closingColumn, int middleRow, int middleColumn, boolean owner) {
        // Check if indices are valid
        if (isValidIndex(closingRow, closingColumn) && isValidIndex(middleRow, middleColumn)) {
            ConcretePiece currentPiece = board[currentRow][currentColumn];
            ConcretePiece middlePiece = board[middleRow][middleColumn];
            ConcretePiece closingPiece = board[closingRow][closingColumn];

            // Check if all pieces involved in the kill are non-null
            if (currentPiece != null && middlePiece != null && closingPiece != null && !King_position.isEqual(new Position(middleRow, middleColumn))) {
                // Check if the conditions for a kill are met
                if (middlePiece.getOwner().isPlayerOne() != owner && closingPiece.getOwner().isPlayerOne() == owner) {
                    // Check if the king is involved in the kill
                    if (currentRow == King_position.getRow() && currentColumn == King_position.getColum())
                        return;
                    // update the numOfKills of the pawn
                    ((Pawn) board[currentRow][currentColumn]).Kill();
                    // Perform the kill
                    board[middleRow][middleColumn] = null;
                }
            }
        }
    }

    /**
     * Checks if the provided row and column indices are within the valid range of the board.
     *
     * @param row    The row index.
     * @param column The column index.
     * @return True if the indices are valid, otherwise false.
     */
    private boolean isValidIndex(int row, int column) {
        return row >= 0 && row < BOARD_SIZE && column >= 0 && column < BOARD_SIZE;
    }

    /**
     * Checks if a move results in a unique kill, trapping an opponent's pawn at the board corners.
     * Examines the destination position after a pawn move to detect unique kills near the board corners.
     *
     * @param destinationPosition The destination position after the pawn's move.
     */

    private void checkForUniqueKill(Position destinationPosition) {
        int i = destinationPosition.getRow();
        int j = destinationPosition.getColum();
        boolean owner = getPieceAtPosition(destinationPosition).getOwner().isPlayerOne();
        // Define unique kill positions for each corner
        String uniqueKillPositionsUp = "(0, 2)(10, 2)";
        String uniqueKillPositionsDown = "(0, 8)(10, 8)";
        String uniqueKillPositionsRight = "(8, 0)(8, 10)";
        String uniqueKillPositionsLeft = "(2, 0)(2, 10)";


        // Check if the destination position is in the unique kill positions
        if (uniqueKillPositionsUp.contains(destinationPosition.toString())) {
            if (performUniqueKill(i, j - 1, owner)) {
                ((Pawn) board[i][j]).Kill();
                return;
            }
        }
        if (uniqueKillPositionsDown.contains(destinationPosition.toString())) {
           if (performUniqueKill(i, j + 1, owner)) {
               ((Pawn) board[i][j]).Kill();
               return;
           }
        }
        if (uniqueKillPositionsLeft.contains(destinationPosition.toString())) {
            if (performUniqueKill(i - 1, j, owner)) {
                ((Pawn) board[i][j]).Kill();
                return;
            }
        }
        if (uniqueKillPositionsRight.contains(destinationPosition.toString())) {
            if (performUniqueKill(i + 1, j, owner)) {
                ((Pawn) board[i][j]).Kill();
                return;
            }
        }
    }

    /**
     * Checks and performs a unique kill if the conditions are met.
     * Checks if a pawn can perform a unique kill in a specific direction after moving.
     *
     * @param targetRow    The row where the target is located.
     * @param targetColumn The column where the target is located.
     * @param owner        The owner of the moving pawn.
     * return True if a unique kill occurs, otherwise false.
     */
    private boolean performUniqueKill(int targetRow, int targetColumn, boolean owner) {
        // Check if the indices are valid
        if (isValidIndex(targetRow, targetColumn)) {
            ConcretePiece targetPiece = board[targetRow][targetColumn];

            // Check if the conditions for a unique kill are met
            if (targetPiece != null && targetPiece.getOwner().isPlayerOne() != owner && !targetPiece.getType().equals(KING_SYMBOL)) {
                // Perform the unique kill
                board[targetRow][targetColumn] = null;
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the king is positioned at one of the corners of the board.
     *
     * @return True if the king is at a corner, otherwise false.
     */
    private boolean isKingAtCorner() {
        int i = King_position.getRow();
        int j = King_position.getColum();
        return (i == 0 && j == 0) || (i == 10 && j == 0) || (i == 10 && j == 10) || (i == 0 && j == 10);
    }

    /**
     * Checks if the king is completely surrounded by player one's pawns.
     *
     * @return True if the king is surrounded by player one's pawns, otherwise false.
     */
    private boolean isKingSurrouned() {
        if (King_position.getRow() == 0 || King_position.getRow() == 10 || King_position.getColum() == 10 || King_position.getColum() == 0)
            return isKing3Surrounded();

        int i = King_position.getRow();
        int j = King_position.getColum();

        if (board[i - 1][j] != null && board[i + 1][j] != null && board[i][j - 1] != null && board[i][j + 1] != null) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j - 1].getOwner().isPlayerOne() &&
                    !board[i][j + 1].getOwner().isPlayerOne();
        }
        return false;

    }

    /**
     * Checks if the king is surrounded by three player one's pawns and a board edge.
     *
     * @return True if the king is surrounded by three player one's pawns and an edge, otherwise false.
     */
    private boolean isKing3Surrounded() {
        int i = King_position.getRow();
        int j = King_position.getColum();

        if ((j == 0) && (board[i - 1][j] != null) && (board[i + 1][j] != null) && (board[i][j + 1] != null)) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        if (j == 10 && board[i - 1][j] != null && board[i + 1][j] != null && board[i][j - 1] != null) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j - 1].getOwner().isPlayerOne();
        }

        if ((i == 0) && (board[i][j - 1] != null) && (board[i + 1][j] != null) && (board[i][j + 1] != null)) {
            return !board[i][j - 1].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        if (i == 10 && board[i - 1][j] != null && board[i][j - 1] != null && board[i][j + 1] != null) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i][j - 1].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        return false;
    }

    public void prints(int winnerNum){
        printStepsList(winnerNum);
        printKills(winnerNum);
        printStepsAmount(winnerNum);
    }

    public void printStepsList(int winnerNum){
        Comparator<ConcretePiece> SortBySteps = new ConcretePieceComp("Sort by steps",winnerNum);
        pawns.sort(SortBySteps);

        for (int i=0; i<pawns.size();i++){
            if (pawns.get(i).getSteps().size()>1) {
                System.out.println(pawns.get(i).stepsList());
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks

    }
    public void printKills(int winnerNum){
        Comparator<ConcretePiece> SortBykills = new ConcretePieceComp("Sort by kills",winnerNum);

        pawns.sort(SortBykills);

        for (int i=0; i<pawns.size();i++){
            if (!pawns.get(i).getType().equals(KING_SYMBOL)) {
                if (((Pawn) pawns.get(i)).getNumOfkills() >0) {
                    int kills =((Pawn) pawns.get(i)).getNumOfkills();
                    String name= pawns.get(i).getName();
                    System.out.println(name+": "+kills+" kills");
                }
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks


    }

    public void printStepsAmount(int winnerNum){
        Comparator<ConcretePiece> SortByStepsAmount = new ConcretePieceComp("Sort by stepsAmount",winnerNum);

        pawns.sort(SortByStepsAmount);

        for (int i=0; i<pawns.size();i++){
            if ( pawns.get(i).getNumOfSteps() >0) {
                int stepsAmount = pawns.get(i).getNumOfSteps();
                String name= pawns.get(i).getName();
                System.out.println(name+": "+stepsAmount+" squares");
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks

    }


}



