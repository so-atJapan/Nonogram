package Nonogram.controller;

import Nonogram.model.DAO;
import Nonogram.model.ResultModel;
import Nonogram.view.ResultView;

/**
 * リザルト画面の処理を管理するコントローラクラス
 * 太田
 */
public class ResultController {

    private final ResultModel RESULT_MODEL;
    private final ResultView RESULT_VIEW;
    private final AppController APP_CONTROLLER;

    public ResultController(ResultModel resultModel, ResultView resultView, AppController appController) {
        this.RESULT_MODEL = resultModel;
        this.RESULT_VIEW = resultView;
        this.APP_CONTROLLER = appController;
    }

    public void initialize() {
        RESULT_VIEW.initialize(RESULT_MODEL);
        RESULT_VIEW.render();
        showResult();
        savePuzzleRecord();
        RESULT_VIEW.showHomeButton().setOnAction(e -> onPuzzleList());
    }

    private void savePuzzleRecord() {
        if (APP_CONTROLLER.isLoggedIn()) {
            DAO dao = new DAO();
            dao.insertPuzzleRecord(APP_CONTROLLER.getCurrentPlayer(), RESULT_MODEL.getPUZZLE());
        }
    }

    public void showResult() {
        RESULT_VIEW.displayResult(RESULT_MODEL);
    }

    public void onPuzzleList() {
        APP_CONTROLLER.navigateTo("list");
    }
}
