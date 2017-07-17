package sk.tuke.gamestudio.server.service.SORM;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;

public class CommentServiceSORM implements CommentService {

	private SORM sorm = new SORM();

	@Override
	public void addComment(Comment comment) throws CommentException {
		try {
			sorm.insert(comment);
		} catch (SORMException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Comment> getComments(String game) throws CommentException {
		try {
			return sorm.select(Comment.class, "WHERE game = '" + game + "'");
		} catch (SORMException e) {			
			e.printStackTrace();
		}
		return null;
	}

}
