package Nonogram.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Clue {
    private final ArrayList<ArrayList<Integer>> ROW_CLUES;
    private final ArrayList<ArrayList<Integer>> COL_CLUES;

    public Clue(ArrayList<ArrayList<Integer>> rowClues, ArrayList<ArrayList<Integer>> colClues){
        this.ROW_CLUES = rowClues;
        this.COL_CLUES = colClues;
    }

    public Clue(String rowClues, String colClues){
        this(beArrayList(rowClues), beArrayList(colClues));
    }

    // ゲッター
    public ArrayList<ArrayList<Integer>> getROW_CLUES() { return ROW_CLUES; }
    public ArrayList<ArrayList<Integer>> getCOL_CLUES() { return COL_CLUES; }

    private static ArrayList<ArrayList<Integer>> beArrayList(String clues){
        ArrayList<String> clueLineText = new ArrayList<String>(Arrays.asList(clues.split(" ")));
        ArrayList<ArrayList<Integer>> parsedClues = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < clueLineText.size(); i++) {
            ArrayList<Integer> clueNumbers = new ArrayList<Integer>();
            for (String s : clueLineText.get(i).split(",")) {
                clueNumbers.add(Integer.parseInt(s));
            }
            parsedClues.add(clueNumbers);
        }
        return parsedClues;
    }

    public String rowToString(){
        StringBuilder clueTextBuilder = new StringBuilder();
        for (int x = 0; x < ROW_CLUES.size(); x++) {
            for (int y = 0; y < ROW_CLUES.get(x).size(); y++) {
                clueTextBuilder.append(ROW_CLUES.get(x).get(y));
                if (y != ROW_CLUES.get(x).size() - 1) {
                    clueTextBuilder.append(",");
                }
            }
            if (x != ROW_CLUES.size() - 1) {
                clueTextBuilder.append(" ");
            }
        }
        return clueTextBuilder.toString();
    }

    public String colToString(){
        StringBuilder clueTextBuilder = new StringBuilder();
        for (int x = 0; x < COL_CLUES.size(); x++) {
            for (int y = 0; y < COL_CLUES.get(x).size(); y++) {
                clueTextBuilder.append(COL_CLUES.get(x).get(y));
                if (y != COL_CLUES.get(x).size() - 1) {
                    clueTextBuilder.append(",");
                }
            }
            if (x != COL_CLUES.size() - 1) {
                clueTextBuilder.append(" ");
            }
        }
        return clueTextBuilder.toString();
    }
}
