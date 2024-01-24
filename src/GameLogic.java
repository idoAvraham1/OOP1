import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
/**
 * The `GameLogic` class implements the `PlayableLogic` interface and serves as the core logic for a two-player board game.
 * It manages the game state, player turns, and the interaction between players and the game board.
 * Properties:
 * - `pawns`: An `ArrayList` containing instances of `ConcretePiece` representing the pawns on the game board.
 * - `likePosition`: A 2D array representing potential positions on the board for game pieces.
 * - `BOARD_SIZE`: A constant integer defining the size of the game board.
 * - `KING_SYMBOL`: A constant string representing the symbol for the king piece on the board.
 * - `Winner`: A boolean indicating whether one of the players has won the game.
 * - `PlayerOne`: An instance of `ConcretePlayer` representing Player One.
 * - `PlayerTwo`: An instance of `ConcretePlayer` representing Player Two.
 * - `King_position`: A `Position` object representing the current position of the king on the board.
 * - `countMove`: An integer counter to keep track of the number of moves and determine player turns.
 * - `board`: A 2D array of `ConcretePiece` representing the game board and its properties.
 * Note: The game logic assumes a board size of 11x11 and uses a custom symbol for the king piece.
 * Players take turns making moves, and the game continues until one player is declared the winner.
 */
public class GameLogic implements PlayableLogic {
    private final ArrayList<ConcretePiece> pawns= new ArrayList<>();
    private final int[][] likePosition=new int[11][11];
    private static final int BOARD_SIZE = 11;
    private static final String KING_SYMBOL = "♔";
    private static boolean  Winner;
    private final ConcretePlayer PlayerOne = new ConcretePlayer(1);
    private final ConcretePlayer PlayerTwo = new ConcretePlayer(2);
    private Position King_position;
    private int countMove;

    private final ConcretePiece[][] board = new ConcretePiece[11][11];



    private String[][] boardLayout = {{"x",".",".","A1","A2","A3","A4","A5",".",".","x"},
                                        {".",".",".",".",".","A6",".",".",".",".","."},
                                        {".",".",".",".",".",".",".",".",".",".","."},
                                        {"A7",".",".",".",".","D1",".",".",".",".","A8"},
                                        {"A9",".",".",".","D2","D3","D4",".",".",".","A10"},
                                        {"A11","A12",".","D5","D6","K7","D8","D9",".","A13","A14"},
                                        {"A15",".",".",".","D10","D11","D12",".",".",".","A16"},
                                        {"A17",".",".",".",".","D13",".",".",".",".","A18"},
                                        {".",".",".",".",".",".",".",".",".",".","."},
                                        {".",".",".",".",".","A19",".",".",".",".","."},
                                        {"x",".",".","A20","A21","A22","A23","A24",".",".","x"}};
    public GameLogic() {
        Initgame();

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

                kill(b);
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
    /**
     * check if one of the condition to end the game is statisfited,
     * the king is surrunded or that the king escapred.
     * if it does then it will change our winne flage to true.
     */
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

    /**
     * update the number of squares a pawn has take during he's current move
     * use a simple furmula of distance calculation and upated the amount of square
     *
     * @param current Starting position of the piece
     * @param destination  destination position for the piece
     */
    private void updateSteps(Position current, Position destination ){
        int current_x=current.getRow();
        int current_y=current.getColum();
        int destination_x=destination.getRow();
        int destination_y=destination.getColum();
        int ans= Math.abs((current_x-destination_x)+(current_y-destination_y));

        board[destination_x][destination_y].updateSquaresNum(ans);
    }

    /**
     * update for the destination position the number of pawn's steped on it
     * the method will update the number only if its the first time that the pawn
     * has steped that position.  by checking the history of steps of the pawn.
     *
     * @param  destination  destination position for the piece
     */
    private void updateLikePosition(Position destination){

        String pawnSteps= ((ConcretePiece)getPieceAtPosition(destination)).stepsList();
        if (pawnSteps.contains(destination.toString())) {
            return;   // not a new pawn that steped.
        }
        likePosition[destination.getRow()][destination.getColum()]++;
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
        King_position = new Position(5, 5);
        countMove = 0;
        Winner=false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(boardLayout[i][j].contains("A")){
                    board[j][i] = new Pawn(PlayerTwo, boardLayout[i][j]);
                    board[j][i].addStep(new Position(j,i).toString());
                }
                if(boardLayout[i][j].contains("D")){
                    board[j][i] = new Pawn(PlayerOne, boardLayout[i][j]);
                    board[j][i].addStep(new Position(j,i).toString());
                }
                if(boardLayout[i][j].contains("K")){
                    board[j][i] = new King(PlayerOne, boardLayout[i][j]);
                    board[j][i].addStep(new Position(j,i).toString());
                }
            }
        }

        // init the arraylist with all the pieces
        initArrayLists();
        initLikePosition();

    }
    /**
     * init the collection of the pawns with the pawn on the board
     */
    private void initArrayLists() {
        pawns.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null) {
                    pawns.add(board[i][j]);
                }
            }
        }
    }
    /**
     * init the like position array with the updated number of steps after init the game
     * with the pawns on the board.
     */
    private void initLikePosition() {

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                 likePosition[i][j]=0;
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null)
                      likePosition[i][j]++;
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
        board[startPosition.getRow()][startPosition.getColum()] = null;
        // update the number of pieces that stepted on b
        updateLikePosition(destinationPosition);
        // add the current step to the pawn step-list
        board[destinationPosition.getRow()][destinationPosition.getColum()].addStep(destinationPosition.toString());

        countMove++;
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

        if ((j == 0) && (i!=10) && (i!=0) && (board[i - 1][j] != null) && (board[i + 1][j] != null) && (board[i][j + 1] != null)) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        if ((j == 10) && (i!=10) && (i!=0) && board[i - 1][j] != null && board[i + 1][j] != null && board[i][j - 1] != null) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j - 1].getOwner().isPlayerOne();
        }

        if ((i == 0) && (j!=10) &&  (j!=0) && (board[i][j - 1] != null) && (board[i + 1][j] != null) && (board[i][j + 1] != null)) {
            return !board[i][j - 1].getOwner().isPlayerOne() && !board[i + 1][j].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        if ((i == 10) && (j!=10) && (j!=0) && board[i - 1][j] != null && board[i][j - 1] != null && board[i][j + 1] != null) {
            return !board[i - 1][j].getOwner().isPlayerOne() && !board[i][j - 1].getOwner().isPlayerOne() && !board[i][j + 1].getOwner().isPlayerOne();
        }
        return false;
    }

    /**
     * This method prints the game statistics based on the specified order and criteria.
     * It includes printing the step list, number of kills, number of squares stepped on,
     * and the number of different pawns that stepped on each position.
     *
     * @param winnerNum The number representing the winning player.
     */
    public void prints(int winnerNum){
        printStepsList(winnerNum);
        printKills(winnerNum);
        printStepsAmount(winnerNum);
        printSteptedSquare();
    }

    /**
     * This method prints the step list of each pawn, sorted by the number of steps,
     * using a custom comparator.
     *
     * @param winnerNum The number representing the winning player.
     */
    private void printStepsList(int winnerNum){
        Comparator<ConcretePiece> SortBySteps = new ConcretePieceComp("Sort by steps",winnerNum);
        pawns.sort(SortBySteps);

        for (ConcretePiece pawn : pawns) {
            if (pawn.getSteps().size() > 1) {
                System.out.println(pawn.stepsList());
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks

    }

    /**
     * This method prints the number of kills for each pawn, sorted by kills,
     * using a custom comparator.
     *
     * @param winnerNum The number representing the winning player.
     */
    private void printKills(int winnerNum){
        Comparator<ConcretePiece> SortBykills = new ConcretePieceComp("Sort by kills",winnerNum);

        pawns.sort(SortBykills);

        for (ConcretePiece pawn : pawns) {
            if (!pawn.getType().equals(KING_SYMBOL)) {
                if (((Pawn) pawn).getNumOfkills() > 0) {
                    int kills = ((Pawn) pawn).getNumOfkills();
                    String name = pawn.getName();
                    System.out.println(name + ": " + kills + " kills");
                }
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks


    }

    /**
     * This  method prints the number of squares each pawn has stepped on,
     * sorted by steps amount, using a custom comparator.
     *
     * @param winnerNum The number representing the winning player.
     */
    private void printStepsAmount(int winnerNum){
        Comparator<ConcretePiece> SortByStepsAmount = new ConcretePieceComp("Sort by stepsAmount",winnerNum);

        pawns.sort(SortByStepsAmount);

        for (ConcretePiece pawn : pawns) {
            if (pawn.getNumOfSquares() > 0) {
                int stepsAmount = pawn.getNumOfSquares();
                String name = pawn.getName();
                System.out.println(name + ": " + stepsAmount + " squares");
            }
        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks

    }

    /**
     * This  method prints the number of different pawns that stepped on each position
     * by creating a sorted TreeMap with the current stats and iterating through the tree.
     */
    private void printSteptedSquare(){
        TreeMap<Position, Integer> Treemap= createTreeMap();
        int originalSize = Treemap.size();


        // iterate over the tree and print the number of stepped pawns
        for (int i=0; i<originalSize;i++){
            // print the first position each time
            Position position= Treemap.firstEntry().getKey();

            int stepted= Treemap.firstEntry().getValue();

            // remove the first position in the tree
            Treemap.pollFirstEntry();
            System.out.println(position.toString()+stepted+" pieces");

        }

        for (int i = 0; i < 75; i++) {
            System.out.print("*");
        }
        System.out.println(); // Move to the next line after printing the asterisks
    }

    /**
     * This method creates a TreeMap where the position is used as the key, and an integer represents
     * the count of different pawns that have stepped on each position. The TreeMap is sorted based on
     * the count of pawns in descending order.
     * The method iterates over a two-dimensional array representing positions and counts, and uses
     * a custom comparator to sort the TreeMap according to the desired order.
     *
     * @return TreeMap<Position, Integer> - A TreeMap sorted by the number of pawns stepped on each position.
     */
    private TreeMap<Position, Integer> createTreeMap() {
        // Instantiate a custom comparator for sorting the TreeMap
        TreeMapcomp myComp=new TreeMapcomp(likePosition);
        // Create a TreeMap with the custom comparator for sorting
        TreeMap<Position, Integer> positionTreeMap = new TreeMap<>(myComp);

        // Iterate over the two-dimensional array of positions and counts
        for (int i = 0; i < likePosition.length; i++) {
            for (int j = 0; j < likePosition[i].length; j++) {
                int cellValue = likePosition[i][j];

                // Check if the cell value is at least 2
                if (cellValue >= 2) {
                    // Create a new Position object for the current array indices
                    Position currentPosition = new Position(i, j);

                    // Add the entry to the TreeMap with the Position as the key and the cell value as the value
                    positionTreeMap.put(currentPosition, cellValue);
                }
            }

        }

        return positionTreeMap;
    }

    /**
     * @param b
     * getNeighbors creates positions which is an array of 8 positions.
     * the first of which is directly above to the position
     * where the piece moved the other 3 are clockwise, and the last four are
     * compatible but two squares away.
     * in the same direction. First, the function checks to see if there is
     * a valid position adjacent to where the player just moved his
     * piece. If there is one it adds ut to the array.
     * if not the cell stays null, so we could indicate if the piece
     * has moved to an edge location on the board
     * or if he is near an edge location by the two squares away part
     * @return positions = [up1, right1, down1, left1, up2, right2, down2, left2]

     */
    private Position[] getNeighbors(Position b){
        Position[] positions = new Position[8];
        int col = b.getColum();
        int row = b.getRow();
        //Up:
        if(col > 0 && !(col == 1 && (row == 0 || row == 10))){
            positions[0] = new Position(row , col - 1);
        }

        //Right:
        if(row < 10 && !(row == 9 && (col == 0 || col == 10))){
            positions[1] = new Position(row + 1 , col);
        }

        //Down:
        if(col < 10 && !(col == 9 && (row == 0 || row == 10))){
            positions[2] = new Position(row , col + 1);
        }

        //Left:
        if(row > 0 && !(row == 1 && (col == 0 || col == 10))){
            positions[3] = new Position(row - 1 , col);
        }

        //Up2:
        if(col > 1 && !(col == 2 && (row == 0 || row == 10))){
            positions[4] = new Position(row , col - 2);
        }

        //Right2:
        if(row < 9 && !(row == 8 && (col == 0 || col == 10))){
            positions[5] = new Position(row + 2 , col);
        }

        //Down2:
        if(col < 9 && !(col == 8 && (row == 0 || row == 10))){
            positions[6] = new Position(row , col + 2);
        }

        //Left2:
        if(row > 1 && !(row == 2 && (col == 0 || col == 10))){
            positions[7] = new Position(row - 2 , col);
        }



        return positions;
    }

    /**
     *
     * @param b
     * kill execute all the possible kills from a moved piece
     * with the help of getNeighbors it can approximate
     * if the piece has moved to an edge place on the board
     * and check for possible kills accordingly
     */
    private void kill(Position b){
        Position[] neighbors = getNeighbors(b);
        Pawn killer = (Pawn)getPieceAtPosition(b);
        boolean owner = killer.getOwner().isPlayerOne();
        ConcretePiece victim = null;
        ConcretePiece accomplice = null;

        if(neighbors[0] != null){

            victim = (ConcretePiece)getPieceAtPosition(neighbors[0]);

            if(victim != null && victim.getOwner().isPlayerOne() != owner && !victim.getType().equals(KING_SYMBOL)){
                if(neighbors[4] == null){
                    board[neighbors[0].getRow()][neighbors[0].getColum()] = null;
                    killer.Kill();
                }
                else{
                    accomplice = (ConcretePiece)getPieceAtPosition(neighbors[4]);

                    if(accomplice!= null && accomplice.getOwner().isPlayerOne() == owner && !accomplice.getType().equals(KING_SYMBOL)){
                        board[neighbors[0].getRow()][neighbors[0].getColum()] = null;
                        killer.Kill();
                    }
                }
            }
        }
        if(neighbors[1] != null){

            victim = (ConcretePiece)getPieceAtPosition(neighbors[1]);

            if(victim != null && victim.getOwner().isPlayerOne() != owner && !victim.getType().equals(KING_SYMBOL)){
                if(neighbors[5] == null){
                    board[neighbors[1].getRow()][neighbors[1].getColum()] = null;
                    killer.Kill();
                }
                else{
                    accomplice = (ConcretePiece)getPieceAtPosition(neighbors[5]);

                    if(accomplice != null && accomplice.getOwner().isPlayerOne() == owner && !accomplice.getType().equals(KING_SYMBOL)){
                        board[neighbors[1].getRow()][neighbors[1].getColum()] = null;
                        killer.Kill();
                    }
                }
            }
        }
        if(neighbors[2] != null){

            victim = (ConcretePiece)getPieceAtPosition(neighbors[2]);

            if(victim != null && victim.getOwner().isPlayerOne() != owner && !victim.getType().equals(KING_SYMBOL)){
                if(neighbors[6] == null){
                    board[neighbors[2].getRow()][neighbors[2].getColum()] = null;
                    killer.Kill();
                }
                else{
                    accomplice = (ConcretePiece)getPieceAtPosition(neighbors[6]);

                    if(accomplice != null && accomplice.getOwner().isPlayerOne() == owner && !accomplice.getType().equals(KING_SYMBOL)){
                        board[neighbors[2].getRow()][neighbors[2].getColum()] = null;
                        killer.Kill();
                    }
                }
            }
        }
        if(neighbors[3] != null){

            victim = (ConcretePiece)getPieceAtPosition(neighbors[3]);

            if(victim != null && victim.getOwner().isPlayerOne() != owner && !victim.getType().equals(KING_SYMBOL)){
                if(neighbors[7] == null){
                    board[neighbors[3].getRow()][neighbors[3].getColum()] = null;
                    killer.Kill();
                }
                else{
                    accomplice = (ConcretePiece)getPieceAtPosition(neighbors[7]);

                    if(accomplice!= null && accomplice.getOwner().isPlayerOne() == owner && !accomplice.getType().equals(KING_SYMBOL)){
                        board[neighbors[3].getRow()][neighbors[3].getColum()] = null;
                        killer.Kill();
                    }
                }
            }
        }

    }



}



