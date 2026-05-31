package Nonogram.model;

public class Grid {
    private int sizeX;
    private int sizeY;
    private Cell[][] cells;

    public Grid(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cells = new Cell[sizeX][sizeY];
        this.setEMPTY();
    }
    public Grid(int sizeX, int sizeY, String[][] cells){
        this(sizeX, sizeY);

        this.cells = new Cell[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                switch (cells[x][y]) {
                    case "0":
                        this.cells[x][y] = new Cell(CellState.EMPTY);
                        break;
                    case "1":
                        this.cells[x][y] = new Cell(CellState.FILLED);
                        break;
                    case "2":
                        this.cells[x][y] = new Cell(CellState.MARKED);
                        break;
                }
            }
        }
    }

    //ゲッター
    public int getSizeX() {return sizeX;}
    public int getSizeY() {return sizeY;}


    public Cell getCellAt(int x, int y){
        return cells[x][y];
    }
    public Cell[][] getCellAll(){
        return cells;
    }

    public Cell[] getRow(int x){
        return cells[x];
    }

    public Cell[] getCol(int y){
        Cell[] columnCells = new Cell[sizeX];
        for (int x = 0; x < columnCells.length; x++) {
            columnCells[x] = this.cells[x][y];
        }
        return columnCells;
    }

    public void setRow(Cell[] row, int x){
        cells[x] = row;
    }

    public void setCol(Cell[] col, int y){
        for (int x = 0; x < col.length; x++) {
            cells[x][y] = col[x];
        }
    }
    
    public void setCellAt(int x, int y, CellState cellState){
        this.cells[x][y] = new Cell(cellState);
    }

    private void setEMPTY(){
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                this.setCellAt(x, y, CellState.EMPTY);
            }
        }
    }

    public Grid copy(){
        Grid copyGrid = new Grid(this.sizeX, this.sizeY);

        for (int x = 0; x < this.sizeX; x++) {
            for (int y = 0; y < this.sizeY; y++) {
                switch (this.cells[x][y].getState()) {
                    case CellState.EMPTY:
                        copyGrid.setCellAt(x, y, CellState.EMPTY);
                        break;
                    case CellState.MARKED:
                        copyGrid.setCellAt(x, y, CellState.MARKED);
                        break;
                    case CellState.FILLED:
                        copyGrid.setCellAt(x, y, CellState.FILLED);
                        break;
                }
            }
        }
        return copyGrid;
    }

    public double filledPercent(){

        final double ALL_CELL_AMOUNT       = this.sizeX * this.sizeY;
        double filledCellAmount = 0;

        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {

                if(this.cells[x][y].getState() != CellState.EMPTY){
                    filledCellAmount ++;
                }

            }
        }

        double filledPercent = filledCellAmount / ALL_CELL_AMOUNT;

        return filledPercent;
    }


    @Override
    public String toString() {
        StringBuilder gridTextBuilder = new StringBuilder();

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                if (this.cells[x][y].getState() == CellState.FILLED) {
                    gridTextBuilder.append(1);
                } else {
                    gridTextBuilder.append(0);
                }

                if (y != sizeY - 1) {
                    gridTextBuilder.append(",");
                }
            }

            if (x != sizeX - 1) {
                gridTextBuilder.append(" ");
            }
        }

        return gridTextBuilder.toString();
    }

}
