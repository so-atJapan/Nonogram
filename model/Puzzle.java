package Nonogram.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//内田
public class Puzzle {

   //属性
   private int puzzleId;
   private String title;
   private int gridSizeX;
   private int gridSizeY;
   private Difficulty difficulty;
   private boolean isPublic;
   private LocalDateTime createdAt;
   private String createdBy; //Playerクラス追加予定
   private Grid solution;
   private Clue clue;


   //ゲッター
   public int getPuzzleId() {return puzzleId;}
   public String getTitle() {return title;}
   public int getGridSizeX() {return gridSizeX;}
   public int getGridSizeY() {return gridSizeY;}
   public Difficulty getDifficulty() {return difficulty;}
   public boolean getIsPublic(){return isPublic;}
   public LocalDateTime getCreatedAt() {return createdAt;}
   public String getCreatedBy() {return createdBy;}
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
   public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}
   public void setSolution(Grid solution) {this.solution = solution;}
   public void setClue(Clue clue) {this.clue = clue;}
   
   
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
      String[] temp1 = solution.split(" ");
      String[][] temp2 = new String[temp1.length][0];
      for (int i = 0; i < temp1.length; i++) {
         temp2[i] = temp1[i].split(",");
      }
      this.solution = new Grid(temp2.length, temp2[0].length, temp2);
   }

}
