package sk.tuke.gamestudio.server.service.SORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.DatabaseSettings;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.JDBC.RatingServiceJDBC;

public class RatingServiceSORM implements RatingService {
	private SORM sorm = new SORM();

	@Override
	public void setRating(Rating rating) throws RatingException {
		List<Rating> ratings = new ArrayList<>();
		try {
			ratings = sorm.select(Rating.class,
					"WHERE game = '" + rating.getGame() + "' AND player = '" + rating.getPlayer() + "'");
		} catch (SORMException e) {
			e.printStackTrace();
		}
		if (ratings.isEmpty()) {
			try {
				sorm.insert(rating);
			} catch (SORMException e) {
				e.printStackTrace();
			}
		} else {
			if (ratings.size() == 1) {
				rating.setId(ratings.get(0).getId());
				try {
					sorm.update(rating);
				} catch (SORMException e) {
					e.printStackTrace();
				}
			} else
				throw new RatingException("Cannot set rating");
		}

	}

	@Override
	public double getAverageRating(String game) throws RatingException {
		return new RatingServiceJDBC().getAverageRating(game);
	}

	@Override
	public int getRating(String game, String player) throws RatingException {
		return new RatingServiceJDBC().getRating(game, player);
	}

}
