package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.kamene.core.Field;
import sk.tuke.gamestudio.server.entity.Score;

public interface GameUserInterface {

	/**
	 * Starts the game.
	 * 
	 * @param field
	 *            field of mines and clues
	 */
	Score newGameStarted();

	/**
	 * Updates user interface - prints the field.
	 */
	void update();

	/**
	 * Selects the size of the field.
	 * 
	 * @throws WrongFormatException
	 */
	void chooseFieldSize() throws WrongFormatException;
	
//	void loadLastField() throws WrongFormatException;

}
