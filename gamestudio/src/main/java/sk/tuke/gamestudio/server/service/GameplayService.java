package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.game.minesweeper.entity.Gameplay;

public interface GameplayService {

	void save(Gameplay gamePlay);

	Gameplay load(int id);

}