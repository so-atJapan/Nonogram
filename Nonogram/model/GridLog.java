package Nonogram.model;

import java.util.LinkedList;

public class GridLog {
    private final LinkedList<Grid> GRID_ROGS;
    private int pointer;

    //コンストラクタ
    public GridLog(){
        this.GRID_ROGS = new LinkedList<Grid>();
        this.pointer = -1;
    }
    public GridLog(Grid grid){
        this();
        this.push(grid.copy());
    }

    // 新しい操作を記録
    public void push(Grid grid) {
        // pointer より先を削除
        while (GRID_ROGS.size() > pointer + 1) {
            GRID_ROGS.removeLast();
        }
        GRID_ROGS.addLast(grid.copy());
        pointer++;
        System.out.println(pointer);
    }

    public void undo() {
        if (pointer > 0) pointer--;
        System.out.println(pointer);
    }

    public void redo() {
        if (pointer < GRID_ROGS.size() - 1) pointer++;
        System.out.println(pointer);
    }

    // 任意ステップ参照
    public Grid get(int step) {
        return GRID_ROGS.get(step).copy();
    }

    public Grid get() {
        return GRID_ROGS.get(pointer).copy();
    }

    public boolean canUndo() { return pointer > 0; }
    public boolean canRedo() { return pointer < GRID_ROGS.size() - 1; }
}