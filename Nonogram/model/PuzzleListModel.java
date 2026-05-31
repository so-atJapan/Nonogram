package Nonogram.model;

import java.util.ArrayList;

public class PuzzleListModel {
    private static final DAO DAO = new DAO();

    private ArrayList<Puzzle> puzzleList;

    public PuzzleListModel(){
        this.puzzleList = new ArrayList<Puzzle>();
    }

    public void initialize(){
        this.puzzleList = DAO.getPuzzleAll();
    }

    public ArrayList<Puzzle> getPuzzleList() { return puzzleList; }
}
