package Nonogram.controller;

import Nonogram.model.GameModel;
import Nonogram.model.Grid;
import Nonogram.model.GuestPlayer;
import Nonogram.model.LoginModel;
import Nonogram.model.Player;
import Nonogram.model.Puzzle;
import Nonogram.model.PuzzleEditorModel;
import Nonogram.model.PuzzleListModel;
import Nonogram.model.ResultModel;
import Nonogram.model.SignupModel;
import Nonogram.model.SolverModel;

import Nonogram.view.GameView;
import Nonogram.view.LoginView;
import Nonogram.view.HomeView;
import Nonogram.view.PuzzleEditorView;
import Nonogram.view.PuzzleListView;
import Nonogram.view.ResultView;
import Nonogram.view.SignupView;
import Nonogram.view.SolverView;

import javafx.stage.Stage;

/**
 * 全体の画面遷移を管理するコントローラクラス
 * 太田
 */
public class AppController {

    private final Stage STAGE;
    private Puzzle pendingPuzzle;
    private PuzzleListController puzzleListController;
    private GameController gameController;
    private PuzzleEditorController puzzleEditorController;
    private ResultController resultController;
    private Grid clearedGrid;
    private int clearTimeSeconds;
    private LoginController loginController;
    private SignupController signupController;
    private Player currentPlayer = new GuestPlayer();

    /** adminアカウントのDB上のplayer_id（DBで自動採番された固定値） */
    private static final int ADMIN_PLAYER_ID = 1;
    /** adminアカウントのメールアドレス */
    private static final String ADMIN_EMAIL = "admin@email.com";

    /**
     * コンストラクタ
     *
     * @param stage 画面表示に使用するStage
     */
    public AppController(Stage stage) {
        this.STAGE = stage;
    }

    public void initialize() {
        navigateTo("home");
    }

    public void navigateTo(String destination) {
        switch (destination) {
            case "home":    showHome();           break;
            case "login":   showLogin();          break;
            case "signup":  showSignup();         break;
            case "list":    showPuzzleList();     break;
            case "game":    showGame();           break;
            case "create":  showCreate();         break;
            case "editor":  showEditor();         break;
            case "result":  showResult();         break;
            case "solver":  showSolver();         break;
            case "homeSolver": showSolverFromHome(); break;
            default:        showPuzzleList();     break;
        }
    }

    /**
     * 現在のプレイヤーが管理者かどうかを返す
     */
    public boolean isAdmin() {
        if (!this.isLoggedIn()) return false;
        return this.currentPlayer.getPlayerId() == ADMIN_PLAYER_ID
            && ADMIN_EMAIL.equals(this.currentPlayer.getEMail());
    }

    public boolean isLoggedIn() {
        if (this.currentPlayer == null) return false;
        return this.currentPlayer.getPlayerId() != 0;
    }

    public void showHome() {
        HomeView homeView = new HomeView(STAGE);
        HomeController homeController = new HomeController(homeView, this);
        homeController.initialize();
    }

    public void showPuzzleList() {
        PuzzleListModel puzzleList = new PuzzleListModel();
        puzzleList.initialize();
        PuzzleListView listView = new PuzzleListView(STAGE);
        puzzleListController = new PuzzleListController(listView, puzzleList, this);
        puzzleListController.initialize();
    }

    public void showGame() {
        GameModel gameModel = new GameModel(pendingPuzzle);
        GameView gameView = new GameView(STAGE);
        gameController = new GameController(gameModel, gameView, this);
        gameController.initialize();
    }

    public void showCreate() {
        PuzzleEditorModel puzzleEditorModel = new PuzzleEditorModel(currentPlayer);
        PuzzleEditorView puzzleEditorView = new PuzzleEditorView(STAGE);
        puzzleEditorController = new PuzzleEditorController(puzzleEditorModel, puzzleEditorView, this);
        puzzleEditorController.initialize();
    }

    public void showEditor() {
        Puzzle targetPuzzle = this.pendingPuzzle;
        PuzzleEditorModel puzzleEditorModel = new PuzzleEditorModel(targetPuzzle);
        PuzzleEditorView puzzleEditorView = new PuzzleEditorView(STAGE);
        puzzleEditorController = new PuzzleEditorController(puzzleEditorModel, puzzleEditorView, this);
        puzzleEditorController.initialize();
    }

    public void showResult() {
        ResultModel resultModel = new ResultModel(pendingPuzzle, clearedGrid, clearTimeSeconds);
        ResultView resultView = new ResultView(STAGE);
        resultController = new ResultController(resultModel, resultView, this);
        resultController.initialize();
    }

    public void showSolverFromHome() {
        SolverModel solverModel = new SolverModel(new Puzzle());
        SolverView solverView = new SolverView(STAGE);
        SolverController solverController = new SolverController(solverModel, solverView, this);
        solverController.initialize();
    }

    public void showSolver() {
        SolverModel solverModel = new SolverModel(pendingPuzzle);
        SolverView solverView = new SolverView(STAGE);
        SolverController solverController = new SolverController(solverModel, solverView, this);
        solverController.initialize();
    }

    public void showLogin() {
        if (this.isLoggedIn()) { showHome(); return; }
        LoginModel loginModel = new LoginModel();
        LoginView loginView = new LoginView(STAGE);
        loginController = new LoginController(loginModel, loginView, this);
        loginController.initialize();
    }

    public void showSignup() {
        if (this.isLoggedIn()) { showHome(); return; }
        SignupModel signupModel = new SignupModel();
        SignupView signupView = new SignupView(STAGE);
        signupController = new SignupController(signupModel, signupView, this);
        signupController.initialize();
    }

    public void exitGame(){
        this.STAGE.close();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPendingPuzzle(Puzzle puzzle) {
        this.pendingPuzzle = puzzle;
    }

    public void setResultData(Puzzle puzzle, Grid completedGrid, int tickSeconds) {
        this.pendingPuzzle = puzzle;
        this.clearedGrid = completedGrid;
        this.clearTimeSeconds = tickSeconds;
    }

    public GameController getGameController() { return this.gameController; }

    public Player getCurrentPlayer()          { return this.currentPlayer; }
}
