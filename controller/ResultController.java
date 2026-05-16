package Nonogram.controller;

import Nonogram.model.ResultModel;
import Nonogram.view.ResultView;

public class ResultController {

    private ResultModel model;
    private ResultView view;
    private AppController appController;
    // private PlayRecord record; // TODO: PlayRecordクラス実装後にResultModelの代わりに利用予定

    public ResultController(ResultModel model, ResultView view, AppController appController) {
        this.model = model;
        this.view = view;
        this.appController = appController;
    }

    public void initialize() {
        view.initialize(model);
        view.render();
        showResult();
        view.showHomeButton().setOnAction(e -> onPuzzleList());
        // view.showRetryButton().setOnAction(e -> onRetry()); // TODO: リトライ機能追加時に有効化
    }

    public void showResult() {
        view.displayResult(model);
    }

    public void onPuzzleList() {
        appController.navigateTo("list");
    }

    // public void onRetry() {
    //     appController.navigateTo("game");
    // }
}
