package sk.tuke.gamestudio.server.service;

import java.util.List;

import sk.tuke.gamestudio.server.entity.Score;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;

    List<Score> getBestScores(String game) throws ScoreException;
}
