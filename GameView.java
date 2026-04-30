package view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameView extends JFrame {
    JFrame frame;
    GridPanel gridPanel;
    HintPanel hintPanel;
    JButton[][] buttons;

    public void render(){
        setTitle("MVC Counter");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        buttons

        add(label);
        add(button);
    }

    public void updateCell(int x, int y){

    }

    public void showResult(boolean result){

    }
}
