package sk.tuke.gamestudio.game.kamene;

import sk.tuke.gamestudio.game.GameUserInterface;
import sk.tuke.gamestudio.game.WrongFormatException;

/**
 * Main application class
 * 
 * @author P3502714
 *
 */
public class Stones {
	/** User interface */
//	private GameUserInterface userInterface;

	private static Stones instance;

	/** Starting time of the game */
	private long startMillis;

	/** settings */
	private Settings settings;

	/**
	 * Constructor
	 */
	public Stones() {
		instance = this;
		// GameUserInterface = new ConsoleUI();
		// try {
		// userInterface.loadLastField();
		// } catch (WrongFormatException e) {
		// e.getMessage();
		// }
		startMillis = System.currentTimeMillis();
	}

	/**
	 * Starts a new game.
	 */
	public void startNewGame() {
		settings = this.getSettings();
	}

	public static Stones getInstance() {
		if (instance == null) {
			return new Stones();
		}
		return instance;

	}

	public int getPlayingSeconds() {
		return (int) ((System.currentTimeMillis() - startMillis) / 1000);
	}

	public void setStartMillis(long startMillis) {
		this.startMillis = startMillis;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
