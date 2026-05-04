package Nonogram.model;

import java.util.ArrayList;

public class PuzzleList {
    private static DAO dao = new DAO();

    private ArrayList<Puzzle> puzzleList;

    public PuzzleList(){
        this.puzzleList = new ArrayList<Puzzle>();
    }

    public void init(){
        this.puzzleList = dao.getPuzzleAll();
    }

    public ArrayList<Puzzle> getPuzzleList() {return puzzleList;}
}
