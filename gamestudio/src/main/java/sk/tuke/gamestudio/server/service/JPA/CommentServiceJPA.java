package sk.tuke.gamestudio.server.service.JPA;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;

@Transactional
public class CommentServiceJPA implements CommentService{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addComment(Comment comment) throws CommentException {
		entityManager.persist(comment);
		
	}

	@Override
	public List<Comment> getComments(String game) throws CommentException {
		return entityManager.createQuery("select c from Comment c where c.game = :game").setParameter("game", game)
				.getResultList();
	}

}
