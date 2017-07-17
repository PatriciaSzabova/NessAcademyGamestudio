package sk.tuke.gamestudio.server.service.JPA;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Transactional
public class ScoreServiceJPA implements ScoreService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void addScore(Score score) throws ScoreException {
		entityManager.persist(score);

	}

	@Override
	public List<Score> getBestScores(String game) throws ScoreException {
		return entityManager.createQuery("select s from Score s where s.game = :game").setParameter("game", game)
				.getResultList();
	}

}
