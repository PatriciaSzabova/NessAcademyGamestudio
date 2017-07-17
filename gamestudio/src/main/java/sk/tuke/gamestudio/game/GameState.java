package sk.tuke.gamestudio.game;

import java.io.Serializable;

/**
 * Game state.
 */
public enum GameState implements Serializable {
	/** Playing game. */
	PLAYING,
	/** Game solved. */
	SOLVED,
	
	FAILED,
	
	EXIT

}
