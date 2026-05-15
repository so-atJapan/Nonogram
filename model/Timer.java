package Nonogram.model;

public class Timer {


    private int timeCount ;

    /**
     * コンストラクタ
     */
    public Timer() {
        this.timeCount = 0;
    }

    /**
     * 一秒ごとに呼ばれる処理
     * 
     * @return 経過時間
     */
    public int tick() {
        timeCount++;
        return timeCount;
    }

    public int getElapsedSeconds() {
        return timeCount;
    }
}
