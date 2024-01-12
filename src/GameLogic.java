
public class GameLogic implements PlayableLogic {
    public static final int Sort_By_Steps = 1; // for the second part of the ass
    private static final int BOARD_SIZE = 11;
    private static final String KING_SYMBOL = "♔";
    private ConcretePlayer PlayerOne = new ConcretePlayer(1);
    ;
    private ConcretePlayer PlayerTwo = new ConcretePlayer(2);
    ;
    private Position King_position;
    private int countMove; //2 first

    private ConcretePiece[][] pawns = new ConcretePiece[11][11];
    public GameLogic() {
        Initgame();
        PlayerOne = new ConcretePlayer(1);
        PlayerTwo = new ConcretePlayer(2);
        King_position = new Position(5, 5);
        countMove = 0;
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

            // The move was successful
            return true;
        }

        // The move was not valid
        return false;
    }


    /**
     * Retrieves the piece at a given position on the board.
     *
     * @param position The position to check for a piece
     * @return The piece at the specified position
     */

    public Piece getPieceAtPosition(Position position) {
        return pawns[position.getRow()][position.getColum()];

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
        //if the king reached a corner, player one wins
        if (isKingAtCorner()) {
            PlayerOne.Win();
            return true;
        }
        // If the king is surrounded by the pawns of the second player, the second player wins
        if (isKingSurrouned()) {
            PlayerTwo.Win();
            return true;
        }
        return false;
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
        for (int i = 0; i < pawns.length; i++) {
            for (int j = 0; j < pawns.length; j++)
                pawns[i][j] = null;
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
    public void UpdateKingPosition(Position b) {
        this.King_position.setRow(b.getRow());
        this.King_position.setColum(b.getColum());
    }

    /**
     * Initializes the game board with pieces in their starting positions.
     */
    public void Initgame() {
        pawns[3][5] = new Pawn(PlayerOne, "");
        pawns[5][5] = new King(PlayerOne, "");
        pawns[7][5] = new Pawn(PlayerOne, "");

        for (int i = 4; i < 7; i++) {
            pawns[4][i] = new Pawn(PlayerOne, "");
        }
        for (int i = 3; i < 8; i++) {
            if (i == 5) {
                continue;
            }
            pawns[5][i] = new Pawn(PlayerOne, "");
        }

        for (int i = 4; i < 7; i++) {
            pawns[6][i] = new Pawn(PlayerOne, "");
        }


        pawns[1][5] = new Pawn(PlayerTwo, "");
        for (int i = 3; i < 8; i++) {
            pawns[0][i] = new Pawn(PlayerTwo, "");
        }
        pawns[5][1] = new Pawn(PlayerTwo, "");
        for (int i = 3; i < 8; i++) {
            pawns[i][0] = new Pawn(PlayerTwo, "");
        }

        pawns[9][5] = new Pawn(PlayerTwo, "");
        for (int i = 3; i < 8; i++) {
            pawns[10][i] = new Pawn(PlayerTwo, "");
        }
        pawns[5][9] = new Pawn(PlayerTwo, "");
        for (int i = 3; i < 8; i++) {
            pawns[i][10] = new Pawn(PlayerTwo, "");
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
    public boolean isValidMove(Position a, Position b) {
        // Check for self-move, diagonal move, or moving from an empty square
        if (a.isEqual(b) || a.isDiagonal(b) || pawns[a.getRow()][a.getColum()] == null
                || pawns[b.getRow()][b.getColum()] != null) {
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
    public boolean isValidPath(Position a, Position b) {
        // Check if the movement is along the same column
        if (a.getColum() == b.getColum()) {
            // Check upward movement
            if (a.getRow() < b.getRow()) {
                for (int i = a.getRow() + 1; i < b.getRow(); i++) {
                    if (pawns[i][a.getColum()] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
            // Check downward movement
            else if (a.getRow() > b.getRow()) {
                for (int i = b.getRow() + 1; i < a.getRow(); i++) {
                    if (pawns[i][a.getColum()] != null) {
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
                    if (pawns[a.getRow()][i] != null) {
                        // Path is blocked
                        return false;
                    }
                }
            }
            // Check rightward movement
            else if (a.getColum() > b.getColum()) {
                for (int i = b.getColum() + 1; i < a.getColum(); i++) {
                    if (pawns[a.getRow()][i] != null) {
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
    public void makeMove(Position startPosition, Position destinationPosition) {
        pawns[destinationPosition.getRow()][destinationPosition.getColum()] = pawns[startPosition.getRow()][startPosition.getColum()];
        pawns[destinationPosition.getRow()][destinationPosition.getColum()].addStep(destinationPosition.toString());
        pawns[startPosition.getRow()][startPosition.getColum()] = null;
        countMove++;
    }

    /**
     * Checks for possible kills after moving a pawn to the destination position.
     * Determines the type of position and checks if a kill can happen at that position after the move.
     *
     * @param destinationPosition The destination position after the move
     */
    public void checkForPossibleKills(Position destinationPosition) {
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
    public void checkForCornerKill(Position destinationPosition) {
        int row = destinationPosition.getRow();
        int column = destinationPosition.getColum();
        boolean owner = getPieceAtPosition(destinationPosition).getOwner().isPlayerOne();

        // check for a kill in the first row.
        if (row == 1 && pawns[0][column] != null && pawns[0][column].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = pawns[0][column];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                pawns[0][column] = null;
                System.out.println("Corner kill at position (0, " + column + ")");
            }
        }
        // check for a kill in the first column
        if (column == 1 && pawns[row][0] != null && pawns[row][0].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = pawns[row][0];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                pawns[row][0] = null;
                System.out.println("Corner kill at position (" + row + ", 0)");

            }
        }
        // check for a kill in the last row
        if (row == 9 && pawns[10][column] != null && pawns[10][column].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = pawns[10][column];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                pawns[10][column] = null;
                System.out.println("Corner kill at position (10, " + column + ")");

            }
        }
        // check for a kill in the last column
        if (column == 9 && pawns[row][10] != null && pawns[row][10].getOwner().isPlayerOne() != owner) {
            ConcretePiece potentialVictim = pawns[row][10];
            if (!potentialVictim.getType().equals(KING_SYMBOL)) {
                pawns[row][10] = null;
                System.out.println("Corner kill at position (" + row + ", 10)");
            }
        }
    }

    /**
     * Checks if a directional kill can occur after moving a pawn to the destination position.
     * Examines different directional movements to detect potential kills after the pawn's move.
     *
     * @param destinationPosition The destination position after the pawn's move.
     */
    public void checkForDirectionalKill(Position destinationPosition) {
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
            ConcretePiece currentPiece = pawns[currentRow][currentColumn];
            ConcretePiece middlePiece = pawns[middleRow][middleColumn];
            ConcretePiece closingPiece = pawns[closingRow][closingColumn];

            // Check if all pieces involved in the kill are non-null
            if (currentPiece != null && middlePiece != null && closingPiece != null && !King_position.isEqual(new Position(middleRow, middleColumn))) {
                // Check if the conditions for a kill are met
                if (middlePiece.getOwner().isPlayerOne() != owner && closingPiece.getOwner().isPlayerOne() == owner) {
                    // Check if the king is involved in the kill
                    if (currentRow == King_position.getRow() && currentColumn == King_position.getColum())
                        return;
                    // Perform the kill
                    pawns[middleRow][middleColumn] = null;
                    System.out.println("Directional kill");
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
     * @return True if a unique kill occurs, otherwise false.
     */

    public void checkForUniqueKill(Position destinationPosition) {
        int i = destinationPosition.getRow();
        int j = destinationPosition.getColum();
        boolean owner = getPieceAtPosition(destinationPosition).getOwner().isPlayerOne();
        // Create a string representation of unique kill positions
        String uniqueKillPositions = "(2,0)(2,10)(0,2)(10,2)(8,0)(8,10)(0,8)(10,8)";

        // Check if the destination position is in the unique kill positions
        if (uniqueKillPositions.contains(destinationPosition.toString())) {
            // Check and perform unique kills in different directions
            if (performUniqueKill(i - 1, j, owner))
                return;
            if (performUniqueKill(i, j - 1, owner))
                return;
            if (performUniqueKill(i + 1, j, owner))
                return;
            performUniqueKill(i, j + 1, owner);
        }
    }

    /**
     * Checks and performs a unique kill if the conditions are met.
     * Checks if a pawn can perform a unique kill in a specific direction after moving.
     *
     * @param targetRow    The row where the target is located.
     * @param targetColumn The column where the target is located.
     * @param owner        The owner of the moving pawn.
     */
    private boolean performUniqueKill(int targetRow, int targetColumn, boolean owner) {
        // Check if the indices are valid
        if (isValidIndex(targetRow, targetColumn)) {
            ConcretePiece targetPiece = pawns[targetRow][targetColumn];

            // Check if the conditions for a unique kill are met
            if (targetPiece != null && targetPiece.getOwner().isPlayerOne() != owner && !targetPiece.getType().equals(KING_SYMBOL)) {
                // Perform the unique kill
                pawns[targetRow][targetColumn] = null;
                System.out.println("Unique kill");
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
    public boolean isKingAtCorner() {
        int i = King_position.getRow();
        int j = King_position.getColum();
        return (i == 0 && j == 0) || (i == 10 && j == 0) || (i == 10 && j == 10) || (i == 0 && j == 10);
    }

    /**
     * Checks if the king is completely surrounded by player one's pawns.
     *
     * @return True if the king is surrounded by player one's pawns, otherwise false.
     */
    public boolean isKingSurrouned() {
        if (King_position.getRow() == 0 || King_position.getRow() == 10 || King_position.getColum() == 10 || King_position.getColum() == 0)
            return isKing3Surrounded();

        int i = King_position.getRow();
        int j = King_position.getColum();

        if (pawns[i - 1][j] != null && pawns[i + 1][j] != null && pawns[i][j - 1] != null && pawns[i][j + 1] != null) {
            return !pawns[i - 1][j].getOwner().isPlayerOne() && !pawns[i + 1][j].getOwner().isPlayerOne() && !pawns[i][j - 1].getOwner().isPlayerOne() &&
                    !pawns[i][j + 1].getOwner().isPlayerOne();
        }
        return false;

    }

    /**
     * Checks if the king is surrounded by three player one's pawns and a board edge.
     *
     * @return True if the king is surrounded by three player one's pawns and an edge, otherwise false.
     */
    public boolean isKing3Surrounded() {
        int i = King_position.getRow();
        int j = King_position.getColum();

        if ((j == 0) && (pawns[i - 1][j] != null) && (pawns[i + 1][j] != null) && (pawns[i][j + 1] != null)) {
            return !pawns[i - 1][j].getOwner().isPlayerOne() && !pawns[i + 1][j].getOwner().isPlayerOne() && !pawns[i][j + 1].getOwner().isPlayerOne();
        }
        if (j == 10 && pawns[i - 1][j] != null && pawns[i + 1][j] != null && pawns[i][j - 1] != null) {
            return !pawns[i - 1][j].getOwner().isPlayerOne() && !pawns[i + 1][j].getOwner().isPlayerOne() && !pawns[i][j - 1].getOwner().isPlayerOne();
        }

        if ((i == 0) && (pawns[i][j - 1] != null) && (pawns[i + 1][j] != null) && (pawns[i][j + 1] != null)) {
            return !pawns[i][j - 1].getOwner().isPlayerOne() && !pawns[i + 1][j].getOwner().isPlayerOne() && !pawns[i][j + 1].getOwner().isPlayerOne();
        }
        if (i == 10 && pawns[i - 1][j] != null && pawns[i][j - 1] != null && pawns[i][j + 1] != null) {
            return !pawns[i - 1][j].getOwner().isPlayerOne() && !pawns[i][j - 1].getOwner().isPlayerOne() && !pawns[i][j + 1].getOwner().isPlayerOne();
        }
        return false;
    }


}



