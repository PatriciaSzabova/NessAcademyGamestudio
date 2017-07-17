package sk.tuke.gamestudio.server.service.SORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.DatabaseSettings;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

public class ScoreServiceSORM implements ScoreService {

	SORM sorm = new SORM();

	@Override
	public void addScore(Score score) throws ScoreException {
		try {
			sorm.insert(score);
		} catch (SORMException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Score> getBestScores(String game) throws ScoreException {
		try {
			return sorm.select(Score.class, "WHERE game = '" + game + "'");
		} catch (SORMException e) {
			e.printStackTrace();
		}
		return null;
	}

}
