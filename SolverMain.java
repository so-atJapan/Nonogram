package Nonogram;

import Nonogram.controller.AppController;
import Nonogram.controller.SolverController;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleList;
import Nonogram.model.SolverModel;
import Nonogram.view.SolverView;
import javafx.application.Application;
import javafx.stage.Stage;

public class SolverMain extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        
        PuzzleList puzzleList = new PuzzleList();
        puzzleList.initialize();
        Puzzle puzzle = puzzleList.getPuzzleList().get(9);

        SolverModel solverModel = new SolverModel(puzzle);
        SolverView solverView = new SolverView(stage);
        SolverController solverController  = new SolverController(solverModel, solverView, null);
        solverController.initialize();
        
    }
    
    public static void main(String[] args) {
        launch(args);  
    }
}