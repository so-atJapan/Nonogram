package Nonogram.view;

import Nonogram.model.Puzzle;
import Nonogram.model.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView {

    private JFrame frame;
    private JPanel gridPanel;
    private JPanel hintPanelTop;
    private JPanel hintPanelSide;
    private JButton[][] buttons;
    private JButton checkButton;

    private int rows;
    private int cols;

    private final int cellSize = 40;

    // パズル描画
    public void render(Puzzle puzzle) {
        this.rows = puzzle.getGridSizeX();
        this.cols = puzzle.getGridSizeY();

        frame = new JFrame("Nonogram");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== 各パネル =====
        JPanel cornerPanel = new JPanel(); // 左上空白
        gridPanel = new JPanel(new GridLayout(rows, cols));
        hintPanelTop = new JPanel(new GridLayout(1, cols));
        hintPanelSide = new JPanel(new GridLayout(rows, 1));

        // ===== サイズ統一（重要）=====
        cornerPanel.setPreferredSize(new Dimension(cellSize * 2, cellSize * 2));
        hintPanelTop.setPreferredSize(new Dimension(cols * cellSize, cellSize * 2));
        hintPanelSide.setPreferredSize(new Dimension(cellSize * 2, rows * cellSize));

        // ===== グリッド =====
        buttons = new JButton[rows][cols];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(cellSize, cellSize));
                btn.setMargin(new Insets(0,0,0,0));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                btn.setBackground(Color.WHITE);

                buttons[y][x] = btn;
                gridPanel.add(btn);
            }
        }

    // ===== 行ヒント（左）=====
    ArrayList<ArrayList<Integer>> rowHints = puzzle.getClue().getRowClues();

    for (int i = 0; i < rowHints.size(); i++) {
        StringBuilder text = new StringBuilder();

        for (int num : rowHints.get(i)) {
            text.append(num).append(" ");
        }

        JLabel label = new JLabel(text.toString(), SwingConstants.RIGHT);
        label.setPreferredSize(new Dimension(cellSize * 2, cellSize));
        hintPanelSide.add(label);
    }


    // ===== 列ヒント（上）=====
    ArrayList<ArrayList<Integer>> colHints = puzzle.getClue().getColClues();

    for (int i = 0; i < colHints.size(); i++) {
        StringBuilder text = new StringBuilder("<html>");

        for (int num : colHints.get(i)) {
            text.append(num).append("<br>");
        }

        text.append("</html>");

        JLabel label = new JLabel(text.toString(), SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(cellSize, cellSize * 2));
        hintPanelTop.add(label);
    }

        // ===== チェックボタン（下）=====
        checkButton = new JButton("確認");

        // ===== レイアウト組み立て =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(cornerPanel, BorderLayout.WEST);
        topPanel.add(hintPanelTop, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(hintPanelSide, BorderLayout.WEST);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(checkButton, BorderLayout.SOUTH);

        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // セル更新
    public void updateCell(int x, int y, Cell[][] cells) {
        JButton btn = buttons[x][y];

        switch (cells[x][y].getState()) {
            case FILLED:
                btn.setBackground(Color.BLACK);
                btn.setText("");
                break;
            case MARKED:
                btn.setBackground(Color.WHITE);
                btn.setText("X");
                break;
            default:
                btn.setBackground(Color.WHITE);
                btn.setText("");
                break;
        }
    }

    // 結果表示
    public void showResult(boolean result) {
        String message = result ? "クリア！" : "不正解...";
        JOptionPane.showMessageDialog(frame, message);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public JButton getCheckButton() {
        return checkButton;
    }
}
