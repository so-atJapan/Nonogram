package Nonogram.model;

public class Grid {
    private int sizeX;
    private int sizeY;
    private Cell[][] cells;

    public Grid(int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cells = new Cell[sizeX][sizeY];
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

    public void setCellAt(int x, int y, CellState cellState){
        this.cells[x][y] = new Cell(cellState);
    }

}
