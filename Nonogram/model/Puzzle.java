package Nonogram.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

//内田
public class Puzzle {

   //属性
   private int puzzleId = -1;
   private String title = "No Title";
   private int gridSizeX = 10;
   private int gridSizeY = 10;
   private Difficulty difficulty = Difficulty.NORMAL;
   private boolean isPublic = true;
   private LocalDateTime createdAt;
   private Player createdBy = new GuestPlayer();
   private Grid solution = new Grid(10, 10);
   private Clue clue = new Clue("0 0 0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0 0 0");


   //ゲッター
   public int getPuzzleId() {return puzzleId;}
   public String getTitle() {return title;}
   public int getGridSizeX() {return gridSizeX;}
   public int getGridSizeY() {return gridSizeY;}
   public Difficulty getDifficulty() {return difficulty;}
   public boolean getIsPublic(){return isPublic;}
   public LocalDateTime getCreatedAt() {return createdAt;}
   public Player getCreatedBy() {return createdBy;}
   public Grid getSolution() {return solution;}
   public Clue getClue() {return clue;}

   //セッター
   public void setPuzzleId(int puzzleId) {this.puzzleId = puzzleId;}
   public void setTitle(String title) {this.title = title;}
   public void setGridSizeX(int gridSizeX) {this.gridSizeX = gridSizeX;}
   public void setGridSizeY(int gridSizeY) {this.gridSizeY = gridSizeY;}
   public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}
   public void setIsPublic(boolean isPublic){this.isPublic = isPublic;}
   public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
   public void setCreatedBy(Player createdBy) {this.createdBy = createdBy;}
   public void setSolution(Grid solution) {this.solution = solution;}
   public void setClue(Clue clue) {this.clue = clue;}

   public void setClue(Grid grid){
      ArrayList<ArrayList<Integer>> aRowClues = new ArrayList<>();
      ArrayList<ArrayList<Integer>> aColClues = new ArrayList<>();

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
         aRowClues.add(clue);
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
         aColClues.add(clue);
      }

      this.clue = new Clue(aRowClues, aColClues);
   }
   
   
   /**
    * グリッドサイズから難易度を自動判定して設定する。
    * max(gridSizeX, gridSizeY) を基準に判定:
    *   1〜5  → EASY
    *   6〜10 → NORMAL
    *  11〜15 → HARD
    *  16以上 → EXPERT
    */
   public void setDifficultyBySize() {
      int totalSize = this.gridSizeX + this.gridSizeY;
      if (totalSize <= 20) {
         this.difficulty = Difficulty.EASY;
      } else if (totalSize <= 40) {
         this.difficulty = Difficulty.NORMAL;
      } else if (totalSize <= 100) {
         this.difficulty = Difficulty.HARD;
      } else {
         this.difficulty = Difficulty.EXPERT;
      }
   }

   public void setDifficulty(String difficulty) {
      switch (difficulty) {
         case "EASY":
               this.difficulty = Difficulty.EASY;
            break;
         case "NORMAL":
               this.difficulty = Difficulty.NORMAL;
            break;
         case "HARD":
               this.difficulty = Difficulty.HARD;
            break;
         case "EXPERT":
               this.difficulty = Difficulty.EXPERT;
            break;
         default:
            System.out.println("エラー"); //エクセプション処理予定
            break;
      }
   }
   
   public void setSolution(String solution) {
      String[] rowText = solution.split(" ");
      String[][] solutionTextGrid = new String[rowText.length][0];
      for (int i = 0; i < rowText.length; i++) {
         solutionTextGrid[i] = rowText[i].split(",");
      }
      this.solution = new Grid(solutionTextGrid.length, solutionTextGrid[0].length, solutionTextGrid);
   }

}