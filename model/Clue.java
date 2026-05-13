package Nonogram.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Clue {
    private ArrayList<ArrayList<Integer>> rowClues;
    private ArrayList<ArrayList<Integer>> colClues;

    public Clue(ArrayList<ArrayList<Integer>> rowClues, ArrayList<ArrayList<Integer>> colClues){
        this.rowClues = rowClues;
        this.colClues = colClues;
    }

    public Clue(String rowClues, String colClues){
        this(beArrayList(rowClues), beArrayList(colClues));
    }

    //ゲッター
    public ArrayList<ArrayList<Integer>> getRowClues() {return rowClues;}
    public ArrayList<ArrayList<Integer>> getColClues() {return colClues;}

    private static ArrayList<ArrayList<Integer>> beArrayList(String clues){
        ArrayList<String> temp1 = new ArrayList<String>(Arrays.asList(clues.split(" ")));
        ArrayList<ArrayList<Integer>> temp2 = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < temp1.size(); i++) {
            ArrayList<Integer> temp3 = new ArrayList<Integer>();
            for (String s : temp1.get(i).split(",")) {
                temp3.add(Integer.parseInt(s));
            }

            temp2.add(temp3);
        }

        return temp2;
    }

    public static Clue fromGrid(Grid grid) {
    ArrayList<ArrayList<Integer>> rowClues = new ArrayList<>();
    ArrayList<ArrayList<Integer>> colClues = new ArrayList<>();

    // 行クルー（x行ごとに連続するFILLEDの数を数える）
    for (int x = 0; x < grid.getSizeX(); x++) {
        ArrayList<Integer> clue = new ArrayList<>();
        int count = 0;
        for (int y = 0; y < grid.getSizeY(); y++) {
            if (grid.getCellAt(x, y).isFilled()) {
                count++;
            } else if (count > 0) {
                clue.add(count);
                count = 0;
            }
        }
        if (count > 0) clue.add(count);
        if (clue.isEmpty()) clue.add(0); // 全空の行は[0]
        rowClues.add(clue);
    }

    // 列クルー（y列ごとに連続するFILLEDの数を数える）
    for (int y = 0; y < grid.getSizeY(); y++) {
        ArrayList<Integer> clue = new ArrayList<>();
        int count = 0;
        for (int x = 0; x < grid.getSizeX(); x++) {
            if (grid.getCellAt(x, y).isFilled()) {
                count++;
            } else if (count > 0) {
                clue.add(count);
                count = 0;
            }
        }
        if (count > 0) clue.add(count);
        if (clue.isEmpty()) clue.add(0);
        colClues.add(clue);
    }

    Clue result = new Clue(rowClues, colClues);
    result.rowClues = rowClues;
    result.colClues = colClues;
    return result;
    }

    public String rowToString(){

        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < rowClues.size(); x++) {
            for (int y = 0; y < rowClues.get(x).size(); y++) {

                sb.append(rowClues.get(x).get(y));

                if (y != rowClues.get(x).size() - 1) {
                    sb.append(",");
                }
            }

            if (x != rowClues.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();

    }

    public String colToString(){

        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < colClues.size(); x++) {
            for (int y = 0; y < colClues.get(x).size(); y++) {

                sb.append(colClues.get(x).get(y));

                if (y != colClues.get(x).size() - 1) {
                    sb.append(",");
                }
            }

            if (x != colClues.size() - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();

    }


}
