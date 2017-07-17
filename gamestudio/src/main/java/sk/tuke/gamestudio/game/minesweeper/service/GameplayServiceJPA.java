package sk.tuke.gamestudio.game.minesweeper.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import sk.tuke.gamestudio.game.minesweeper.entity.Gameplay;
import sk.tuke.gamestudio.server.service.GameplayService;

@Transactional
public class GameplayServiceJPA implements GameplayService {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void save(Gameplay gamePlay) {
		entityManager.persist(gamePlay);
	}

	@Override
	public Gameplay load(int id) {
		return entityManager.find(Gameplay.class, id);
	}

}
