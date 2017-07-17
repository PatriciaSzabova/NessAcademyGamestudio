package sk.tuke.gamestudio.server.service.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.DatabaseSettings;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

public class ScoreServiceJDBC implements ScoreService {

	private static final String INSERT_QUERY = "INSERT INTO score (player, game, points, playedon) VALUES (?,?,?,?)";
	private static final String SELECT_QUERY = "SELECT player, game, points, playedon FROM score WHERE game = ";

	private List<Score> scores = new ArrayList<Score>();

	@Override
	public void addScore(Score score) throws ScoreException {
		// insertToDb(score);
	}

	@Override
	public List<Score> getBestScores(String game) throws ScoreException {
		selectFromDB(game);
		Collections.sort(scores, (Score s1, Score s2) -> s1.getPoints() - s2.getPoints());
		Collections.reverse(scores);
		return scores;
	}

	// private void insertToDb(Score score) {
	// try (Connection connection =
	// DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
	// DatabaseSettings.PASSWORD); PreparedStatement pstm =
	// connection.prepareStatement(INSERT_QUERY)) {
	// pstm.setString(1, score.getPlayer());
	// pstm.setString(2, score.getGame());
	// pstm.setInt(3, score.getPoints());
	// pstm.setDate(4, score.getPlayedon());
	// pstm.execute();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }

	private void selectFromDB(String game) {
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD);
				Statement stm = connection.createStatement();
				ResultSet rs = stm.executeQuery(SELECT_QUERY + "'" + game + "'")) {
			while (rs.next()) {
				Score sc = new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDate(4));
				scores.add(sc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
