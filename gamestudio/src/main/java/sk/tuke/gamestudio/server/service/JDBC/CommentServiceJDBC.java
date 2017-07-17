package sk.tuke.gamestudio.server.service.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.DatabaseSettings;

public class CommentServiceJDBC implements CommentService {

	private static final String INSERT_QUERY = "INSERT INTO comment (player, game, comment, commentedon) VALUES (?,?,?,?)";
	private static final String SELECT_QUERY = "SELECT player, game, comment, commentedon FROM comment WHERE game = ";

	List<Comment> comments = new ArrayList<>();

	@Override
	public void addComment(Comment comment) throws CommentException {
		//insertToDb(comment);
	}

	@Override
	public List<Comment> getComments(String game) throws CommentException {
		selectFromDB(game);
		return comments;
	}

//	private void insertToDb(Comment comment) {
//		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
//				DatabaseSettings.PASSWORD); PreparedStatement pstm = connection.prepareStatement(INSERT_QUERY)) {
//			pstm.setString(1, comment.getPlayer());
//			pstm.setString(2, comment.getGame());
//			pstm.setString(3, comment.getComment());
//			pstm.setDate(4, comment.getCommentedon());
//			pstm.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	private void selectFromDB(String game) {
		try (Connection connection = DriverManager.getConnection(DatabaseSettings.URL, DatabaseSettings.USER,
				DatabaseSettings.PASSWORD);
				Statement stm = connection.createStatement();
				ResultSet rs = stm.executeQuery(SELECT_QUERY + "'" + game + "'")) {

			while (rs.next()) {
				Comment cmt = new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4));
				comments.add(cmt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
