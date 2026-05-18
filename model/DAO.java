package Nonogram.model;
import java.sql.*;
import java.util.ArrayList;


public class DAO {
    
    private final String DB_PATH = "jdbc:sqlite:src\\Nonogram\\model\\Nonogram.DB";

    private final String SELECT_ALL_PUZZLES =
        "SELECT" +
        "    p.puzzle_id," +
        "    p.title," +
        "    p.grid_size_x," +
        "    p.grid_size_y," +
        "    d.difficulty_name AS difficulty," +
        "    p.is_public," +
        "    p.created_at," +
        "    p.created_by," +
        "    p.solution," +
        "    p.clue_row," +
        "    p.clue_col" +
        "FROM puzzles p" +
        "JOIN difficulty d" +
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

    private final String SELECT_PASSWORD_HASH =
    "SELECT password_hash FROM players WHERE e_mail = ?";

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

            ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPasswordHash(String email) {

        try (
            Connection connection = DriverManager.getConnection(DB_PATH);
            PreparedStatement ps = connection.prepareStatement(SELECT_PASSWORD_HASH);
        ) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
