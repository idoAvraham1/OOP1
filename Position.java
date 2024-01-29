/**
 * This class represents a position on the board with row and column coordinates.
 */
public class Position {
    private int row;
    private int colum;

    public Position(int row, int colum) {
        this.row = row;
        this.colum = colum;

    }

    /**
     * Gets a string representation of the Position in the format (row, column).
     *
     * @return A string representation of the Position.
     */
    @Override
    public String toString() {
        return "(" + this.row + ", " + this.colum + ")";
    }

    public int getRow() {
        return row;
    }

    public int getColum() {
        return colum;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColum(int colum) {
        this.colum = colum;
    }

    /**
     * Checks if the current position is diagonal to another position.
     *
     * @param p Another Position object for comparison.
     * @return True if the positions are diagonal; otherwise, false.
     */
    public boolean isDiagonal(Position p) {
        return p.getRow() != this.getRow() && p.getColum() != this.getColum();
    }
    /**
     * Checks if the current position is equal to another position.
     *
     * @param p Another Position object for comparison.
     * @return True if the positions are equal; otherwise, false.
     */
    public boolean isEqual(Position p) {
        return p.getRow() == this.getRow() && p.getColum() == this.getColum();
    }

    /**
     * Checks if the current position is at one of the corners of the board.
     *
     * @return True if the position is at a corner; otherwise, false.
     */
    public boolean isCorner() {
        return (this.row == 0 && this.colum == 0) || (this.row == 0 && this.colum == 10)
                || (this.row == 10 && this.colum == 0) || (this.row == 10 && this.colum == 10);
    }

    /**
     * Checks if the current position is on one of the edges of the board.
     *
     * @return True if the position is on an edge; otherwise, false.
     */
    public boolean isEdge() {
        return this.row == 1 || this.colum == 1 || this.row == 9 || this.colum == 9;
    }

    /**
     * Checks if the current position is at a unique place on the board.
     *
     * @return True if the position is at a unique place; otherwise, false.
     */
    public boolean isUniquePlace() {

        return (this.row == 2 && this.colum == 0) || (this.row == 8 && this.colum == 0) || (this.row == 0 && this.colum == 2) ||
                (this.row == 10 && this.colum == 2) || (this.row == 10 && this.colum == 8) || (this.row == 8 && this.colum == 10)
                || (this.row == 2 && this.colum == 10) || (this.row == 0 && this.colum == 8);
    }




}


