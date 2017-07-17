package sk.tuke.gamestudio.server.service.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.DatabaseSettings;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;

public class RatingServiceJDBC implements RatingService {

	private int avgRating;
	private int selectedRating;

	private static final String INSERT_QUERY = "INSERT INTO rating (player, game, rating, ratedon) VALUES (?,?,?,?)";
	private static final String SELECT_RATING_QUERY = "SELECT rating FROM rating WHERE player = ? AND game = ?";
	private static final String SELECT_EXISTS_RATING_QUERY = "SELECT player FROM rating WHERE player = ? AND game = ?";
	private static final String SELECT_AVG_RATING_QUERY = "SELECT AVG(rating) FROM rating WHERE game = ?";
	private static final String UPDATE_RATING = "UPDATE rating SET rating = ? WHERE player = ? AND game = ?";
	

	@Override
	public void setRating(Rating rating) throws RatingException {
		if (existsInDB(rating.getGame(), rating.getPlayer())) {
			updateRating(rating.getRating(), rating.getPlayer(), rating.getGame());
		} else {
			//insertToDb(rating);
		}
	}

	@Override
	public double getAverageRating(String game) throws RatingException {
		selectAvgRatingDB(game);
		return avgRating;
	}

	@Override
	public int getRating(String game, String player) throws RatingException {
		selectFromDB(game, player);
		return selectedRating;
	}

//	private void insertToDb(Rating rating) {
//		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
//				DatabaseSettings.PASSWORD); PreparedStatement pstm = connection.prepareStatement(INSERT_QUERY)) {
//			pstm.setString(1, rating.getPlayer());
//			pstm.setString(2, rating.getGame());
//			pstm.setInt(3, rating.getRating());
//			pstm.setDate(4, rating.getRatedon());
//			pstm.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	private void selectFromDB(String game, String player) {
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD); PreparedStatement pstm = connection.prepareStatement(SELECT_RATING_QUERY)) {
			pstm.setString(1, player);
			pstm.setString(2, game);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				selectedRating = rs.getInt(1);
			}
			System.out.println(selectedRating);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean existsInDB(String game, String player) {
		boolean result = false;
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD);
				PreparedStatement pstm = connection.prepareStatement(SELECT_EXISTS_RATING_QUERY)) {
			pstm.setString(1, player);
			pstm.setString(2, game);
			ResultSet rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	private void selectAvgRatingDB(String game) {
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD);
				PreparedStatement pstm = connection.prepareStatement(SELECT_AVG_RATING_QUERY)) {
			pstm.setString(1, game);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				avgRating = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateRating(int rating, String player, String game) {
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD); PreparedStatement pstm = connection.prepareStatement(UPDATE_RATING)) {
			pstm.setInt(1, rating);
			pstm.setString(2, player);
			pstm.setString(3, game);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
