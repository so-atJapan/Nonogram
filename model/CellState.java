package Model;
    // enumについて調べよ
public enum CellState {
    EMPTY,FILLED,MARKED;

    /**
     * 状態を返す
     * EMPTY → FILLED → MARKED → EMPTY
     */
    public CellState next() {
        switch (this) {
            case EMPTY:
                return FILLED;
            case FILLED:
                return MARKED;
            case MARKED:
                return EMPTY;
            default:
                throw new IllegalStateException("Unknown state: " + this);
        }
    }
}
