package Nonogram;

import Nonogram.model.GameModel;
import Nonogram.model.PuzzleList;
import Nonogram.view.GameView;


import Nonogram.controller.GameController;

public class Main {
    public static void main(String[] args) {

        int puzzle_id = 1;


        PuzzleList puzzleList = new PuzzleList();
        puzzleList.init();
        GameModel gameModel = new GameModel(puzzleList.getPuzzleList().get(puzzle_id));
        GameView gameView = new GameView();
        GameController gameController = new GameController(gameModel, gameView);

        gameController.init();

        
    }
}
