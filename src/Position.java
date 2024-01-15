public class Position {
    private int row;
    private int colum;

    public Position(int row, int colum) {
        this.row = row;
        this.colum = colum;

    }

    public Position(Position position) {
        this.row = position.getRow();
        ;
        this.colum = position.getColum();
    }

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

    public boolean isDiagonal(Position p) {
        return p.getRow() != this.getRow() && p.getColum() != this.getColum();
    }

    public boolean isEqual(Position p) {
        return p.getRow() == this.getRow() && p.getColum() == this.getColum();
    }

    public boolean isCorner() {
        return (this.row == 0 && this.colum == 0) || (this.row == 0 && this.colum == 10)
                || (this.row == 10 && this.colum == 0) || (this.row == 10 && this.colum == 10);
    }

    public boolean isEdge() {
        return this.row == 1 || this.colum == 1 || this.row == 9 || this.colum == 9;
    }

    public boolean isUniquePlace() {

        return (this.row == 2 && this.colum == 0) || (this.row == 8 && this.colum == 0) || (this.row == 0 && this.colum == 2) ||
                (this.row == 10 && this.colum == 2) || (this.row == 10 && this.colum == 8) || (this.row == 8 && this.colum == 10)
                || (this.row == 2 && this.colum == 10) || (this.row == 0 && this.colum == 8);
    }


}


