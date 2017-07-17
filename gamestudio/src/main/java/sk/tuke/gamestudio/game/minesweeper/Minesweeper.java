package sk.tuke.gamestudio.game.minesweeper;

import sk.tuke.gamestudio.game.GameUserInterface;

public class Minesweeper {
	private long startMillis;

	private static Minesweeper instance;

	private Settings settings;

	/**
	 * Constructor.
	 */
	private Minesweeper() {
		instance = this;
		// settings = Settings.load();
		startMillis = System.currentTimeMillis();

	}

	public static Minesweeper getInstance() {
		if (instance == null) {
			return new Minesweeper();
		}
		return instance;
	}

	public int getPlayingSeconds() {
		return (int) ((System.currentTimeMillis() - startMillis) / 1000);
	}

	public Settings getSettings() {
		return settings;
	}

	// public void setSettings(Settings settings) {
	// settings.save();
	// }

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            arguments
	 */
	// public static void main(String[] args) {
	// new Minesweeper();
	// getInstance();
	// getInstance().play();

	// }

}
