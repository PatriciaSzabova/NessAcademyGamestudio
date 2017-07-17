package sk.tuke.gamestudio.game.kamene.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import sk.tuke.gamestudio.game.GameState;
import sk.tuke.gamestudio.game.GameUserInterface;
import sk.tuke.gamestudio.game.WrongFormatException;
import sk.tuke.gamestudio.game.kamene.Stones;
import sk.tuke.gamestudio.game.kamene.Settings;
import sk.tuke.gamestudio.game.kamene.core.Field;
import sk.tuke.gamestudio.game.kamene.core.InvalidMoveException;
import sk.tuke.gamestudio.server.entity.Score;

/**
 * Console user interface.
 */
public class ConsoleUIKamene implements GameUserInterface {

	/** Playing field. */
	private Field field;
	private Settings settings;
	private Score score;

	/** Current date */

	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	int moveCounter = 1;

	/** Input reader. */
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Reads line of text from the reader.
	 * 
	 * @return line as a string
	 */
	private String readLine() {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Creates a new playing field.
	 */
	@Override
	public Score newGameStarted() {
		StringBuilder sb = new StringBuilder();
		sb.append("Let's play a game,").append(System.getProperty("user.name")).append("\n");
		System.out.println(sb.toString());
		chooseFieldSize();
		settings = Stones.getInstance().getSettings();
		Field field = new Field(settings.getRowCount(), settings.getColumnCount());

		this.field = field;

		do {
			update();
			processInput();

		} while (field.getGameState() == GameState.PLAYING);
		if (field.getGameState() == GameState.SOLVED) {
			System.out.println("Congratulations! You have won :)");
			update();

			score = new Score(System.getProperty("user.name"), "STONES",
					1000 - (Stones.getInstance().getPlayingSeconds() + moveCounter), getSQLCurrentDate());
		} else if (field.getGameState() == GameState.EXIT) {
			System.out.println("You have exited the game");
		}
		return score;

	}

	/**
	 * Updates the field during play.
	 */
	@Override
	public void update() {
		Formatter f = new Formatter();
		f.format("Playing time: %s%n", Stones.getInstance().getPlayingSeconds());
		System.out.println(f.toString());
		System.out.println(field);

	}

	/**
	 * Processes players input from console.
	 */
	private void processInput() {
		StringBuilder sb = new StringBuilder();
		sb.append("Please enter your selection:\n").append(
				"<NEW> for New Game, <EXIT> to exit game, <W / UP> , <S / DOWN>, <A / LEFT>, <D / RIGHT> to move the default tile");
		System.out.println(sb.toString());
		String input = readLine().toUpperCase();
		try {
			handleInput(input);
		} catch (WrongFormatException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Handles players input from console.
	 * 
	 * @param input
	 *            Players input from console.
	 * @throws WrongFormatException
	 */
	private void handleInput(String input) throws WrongFormatException {

		if (input.equals("EXIT")) {
			field.save();
			field.setGameState(GameState.EXIT);
		} else if (input.equals("NEW")) {
			Stones.getInstance().startNewGame();
		} else if (input.equals("W") || input.equals("UP")) {
			try {
				field.moveDefaultTile(field.getDefaultRow() - 1, field.getDefaultCol());
				field.getDefaultTilePosition();
				moveCounter++;
			} catch (InvalidMoveException e) {
				e.getMessage();
			}
		} else if (input.equals("S") || input.equals("DOWN")) {
			try {
				field.moveDefaultTile(field.getDefaultRow() + 1, field.getDefaultCol());
				field.getDefaultTilePosition();
				moveCounter++;
			} catch (InvalidMoveException e) {
				e.getMessage();
			}
		} else if (input.equals("A") || input.equals("LEFT")) {
			try {
				field.moveDefaultTile(field.getDefaultRow(), field.getDefaultCol() - 1);
				field.getDefaultTilePosition();
				moveCounter++;
			} catch (InvalidMoveException e) {
				e.getMessage();
			}
		} else if (input.equals("D") || input.equals("RIGHT")) {
			try {
				field.moveDefaultTile(field.getDefaultRow(), field.getDefaultCol() + 1);
				field.getDefaultTilePosition();
				moveCounter++;
			} catch (InvalidMoveException e) {
				e.getMessage();
			}

		} else {
			throw new WrongFormatException("Wrong input! Enter again");
		}
	}

	/**
	 * Allows player to chose the playing field size. Default or custom.
	 */
	@Override
	public void chooseFieldSize() {
		StringBuilder sb = new StringBuilder();
		sb.append("Choose your Field:\n").append("1. DEFAULT\n").append("2. CUSTOM");
		System.out.println(sb.toString());
		String choice = readLine();
		int selectionNumber = Integer.parseInt(choice);
		if (selectionNumber == 0) {
			System.out.println("Wrong Input! Try again.");
		}
		switch (selectionNumber) {
		case 1:
			Stones.getInstance().setSettings(Settings.DEFAULT);
			break;
		case 2:
			System.out.println("Enter number of rows:");
			String rowNumber = readLine();
			int rowCount = Integer.parseInt(rowNumber);
			if (rowCount == 0 || rowCount < 2) {
				System.out.println("Chose a bigger field!");
			}
			System.out.println("Enter number of columns:");
			String colNumber = readLine();
			int columnCount = Integer.parseInt(colNumber);
			if (columnCount == 0 || columnCount < 2) {
				System.out.println("Chose a bigger field!");
			}
			Stones.getInstance().setSettings(new Settings(rowCount, columnCount));
			break;
		default:
			System.out.println("Wrong Input! Try again.");
			break;
		}
	}

	// @Override
	// public void loadLastField() throws WrongFormatException {
	// System.out.println("Do you wish to continue previous game? Y/N");
	// String choice = readLine();
	// if (choice.toUpperCase().equals("Y")) {
	// field = field.load();
	// newGameStarted();
	// } else if (choice.toUpperCase().equals("N")) {
	// Kamene.getInstance().startNewGame();
	// } else {
	// throw new WrongFormatException("Wrong Input!");
	// }
	// }
	//
	private java.sql.Date getSQLCurrentDate() {
		return new java.sql.Date(new Date().getTime());
	}

}
