package Nonogram.model;
import java.sql.*;
import java.util.ArrayList;


public class DAO {
    
    private final String DB_PATH = "jdbc:sqlite:src\\Nonogram\\model\\Nonogram.DB";

    private final String SELECT_ALL_PUZZLES =
        "SELECT\r\n" + //
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

    private final String UPDATE_PUZZLE =
        "UPDATE puzzles " +
        "SET " +
        "    title = ?, " +
        "    grid_size_x = ?, " +
        "    grid_size_y = ?, " +
        "    difficulty_id = ( " +
        "        SELECT difficulty_id " +
        "        FROM difficulty " +
        "        WHERE difficulty_name = ? " +
        "    ), " +
        "    is_public = ?, " +
        "    created_at = ?, " +
        "    created_by = ?, " +
        "    solution = ?, " +
        "    clue_row = ?, " +
        "    clue_col = ? " +
        "WHERE puzzle_id = ?";

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
                    puzzle.setCreatedAt(rs.getTimestamp(7).toLocalDateTime());
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

    public void updatePuzzle(Puzzle puzzle){

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(UPDATE_PUZZLE);
        ) {

            ps.setString(1, puzzle.getTitle());
            ps.setInt(2, puzzle.getGridSizeX());
            ps.setInt(3, puzzle.getGridSizeY());
            ps.setString(4, puzzle.getDifficulty().toString());
            ps.setBoolean(5, puzzle.getIsPublic());
            ps.setTimestamp(6, Timestamp.valueOf(puzzle.getCreatedAt()));
            ps.setString(7, puzzle.getCreatedBy());
            ps.setString(8, puzzle.getSolution().toString());
            ps.setString(9, puzzle.getClue().rowToString());
            ps.setString(10, puzzle.getClue().colToString());
            ps.setInt(11, puzzle.getPuzzleId());

            int result = ps.executeUpdate();

            System.out.println(result + "件更新しました");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
