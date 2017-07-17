package sk.tuke.gamestudio.server.service.JPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;

@Transactional
public class RatingServiceJPA implements RatingService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void setRating(Rating rating) throws RatingException {
		Rating r = null;
		try {
			r = (Rating) entityManager.createQuery("select r from Rating r where r.game = :game and r.player = :player")
					.setParameter("game", rating.getGame()).setParameter("player", rating.getPlayer()).getSingleResult();
			r.setRating(rating.getRating());
		} catch (NoResultException e) {
			// e.printStackTrace();
			entityManager.persist(rating);
		}

	}

	@Override
	public double getAverageRating(String game) throws RatingException {
		Double averageRating = -1.00;
		try {
			averageRating = (Double) entityManager
					.createQuery("select avg(r.rating) from Rating r where r.game = :game").setParameter("game", game)
					.getSingleResult();
		} catch (NoResultException e) {
			// e.printStackTrace();
			e.getMessage();
		}

		return averageRating;
	}

	@Override
	public int getRating(String game, String player) throws RatingException {
		Integer selectedRating = -1;
		try {
			selectedRating = (Integer) entityManager
					.createQuery("select r.rating from Rating r where r.game = :game and r.player = :player")
					.setParameter("game", game).setParameter("player", player).getSingleResult();
		} catch (NoResultException e) {
			// e.printStackTrace();
			e.getMessage();
		}

		return selectedRating;

	}

}
