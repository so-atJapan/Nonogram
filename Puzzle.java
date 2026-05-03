package Model;
import javax.swing.JFrame;

public class Puzzle {
   public int gridSizeX = 10;
   public int gridSizeY = 10;
   public boolean[][] solution = {{false,false,true,true,false,false,false,true,true,false},
                                   {false,true,false,false,true,false,true,false,false,true},
                                   {false,true,false,true,true,true,true,true,false,true},
                                   {false,false,true,true,false,true,false,true,true,false},
                                   {false,false,false,true,true,true,true,true,false,false},
                                   {false,false,true,true,true,false,true,true,true,false},
                                   {true,false,true,true,true,true,true,true,true,true},
                                   {true,false,true,true,true,true,true,true,true,true},
                                   {true,false,true,true,true,true,true,true,true,true},
                                   {false,true,true,true,true,true,true,true,true,false}};
   public int[][] rowHints = {{3},{2,1},{1,1,5},{1,8},{2,6},{3,4},{2,6},{1,8},{1,1,5},{2,3}};
   public int[][] colHints = {{2,2},{1,1,1,1},{1,5,1},{2,1,2},{5},{3,3},{1,8},{1,8},{1,8},{8}};
}