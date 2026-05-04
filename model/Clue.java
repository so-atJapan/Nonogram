package Nonogram.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Clue {
    private ArrayList<ArrayList<Integer>> rowClues;
    private ArrayList<ArrayList<Integer>> colClues;


    public Clue(String rowClues, String colClues){
        this.rowClues = beArrayList(rowClues);
        this.colClues = beArrayList(colClues);
    }

    //ゲッター
    public ArrayList<ArrayList<Integer>> getRowClues() {return rowClues;}
    public ArrayList<ArrayList<Integer>> getColClues() {return colClues;}

    private ArrayList<ArrayList<Integer>> beArrayList(String clues){
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

}
