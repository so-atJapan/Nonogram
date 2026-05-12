package Nonogram.model;
import java.sql.*;
import java.util.ArrayList;


public class DAO {
    
    private final String DB_PATH = "jdbc:sqlite:src\\Nonogram\\model\\Nonogram.DB";

    private final String SELECT_ALL_PUZZLES = "SELECT\r\n" + //
                                            "    p.puzzle_id,\r\n" + //
                                            "    p.title,\r\n" + //
                                            "    p.grid_size_x,\r\n" + //
                                            "    p.grid_size_y,\r\n" + //
                                            "    d.difficulty_name AS difficulty,\r\n" + //
                                            "    p.is_public,\r\n" + //
                                            "    p.created_at,\r\n" + //
                                            "    p.created_by,\r\n" + //
                                            "    p.solution,\r\n" + //
                                            "    p.clue_row,\r\n" + //
                                            "    p.clue_col\r\n" + //
                                            "FROM puzzles p\r\n" + //
                                            "JOIN difficulty d\r\n" + //
                                            "    ON p.difficulty_id = d.difficulty_id;";

public  ArrayList<Puzzle> getPuzzleAll(){
    ArrayList<Puzzle> puzzleList = new ArrayList<Puzzle>();

    try (
        Connection connection = DriverManager.getConnection(DB_PATH);
        PreparedStatement ps = connection.prepareStatement(SELECT_ALL_PUZZLES);
    ){
        try(ResultSet rs = ps.executeQuery();){
            while(rs.next()){
                Puzzle puzzle = new Puzzle();

                puzzle.setPuzzleId(rs.getInt(1));
                puzzle.setTitle(rs.getString(2));
                puzzle.setGridSizeX(rs.getInt(3));
                puzzle.setGridSizeY(rs.getInt(4));
                puzzle.setDifficulty(rs.getString(5));
                puzzle.setIsPublic(rs.getBoolean(6));
                puzzle.setCreatedAt(rs.getString(7));
                puzzle.setCreatedBy(rs.getString(8));
                puzzle.setSolution(rs.getString(9));
                puzzle.setClue(new Clue(rs.getString(10), rs.getString(11)));

                puzzleList.add(puzzle);
            }
        }
    } catch (Exception e) {
        System.out.println(e);
        System.out.println("呼出失敗");
    }
    return puzzleList;
}

}
