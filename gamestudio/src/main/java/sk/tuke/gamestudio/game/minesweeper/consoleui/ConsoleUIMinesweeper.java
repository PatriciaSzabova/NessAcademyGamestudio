package sk.tuke.gamestudio.game.minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio.game.minesweeper.Minesweeper;
import sk.tuke.gamestudio.game.minesweeper.Settings;
import sk.tuke.gamestudio.game.GameState;
import sk.tuke.gamestudio.game.GameUserInterface;
import sk.tuke.gamestudio.game.WrongFormatException;
import sk.tuke.gamestudio.game.minesweeper.core.Field;
import sk.tuke.gamestudio.game.minesweeper.core.Tile;
import sk.tuke.gamestudio.game.minesweeper.entity.Gameplay;
import sk.tuke.gamestudio.game.minesweeper.service.GameplayServiceJPA;
import sk.tuke.gamestudio.server.entity.Score;

/**
 * Console user interface.
 */
public class ConsoleUIMinesweeper implements GameUserInterface {
	/** Playing field. */
	private Field field;
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	private Settings settings;
	private Score score;

	/** Input reader. */
	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	private int randomRow;
	private int randomCol;
	@Autowired
	private ReplayConsoleUI replayUI;

	@Autowired
	private GameplayServiceJPA gameplayService;
	boolean isReplay;

	private RandomOpenThread thread = new RandomOpenThread();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see minesweeper.consoleui.UserInterface#newGameStarted(minesweeper.core.
	 * Field)
	 */
	@Override
	public Score newGameStarted() {
		StringBuilder sb = new StringBuilder();
		sb.append(df.format(getSQLCurrentDate())).append("\n\n").append("Let's play a game,")
				.append(System.getProperty("user.name")).append("\n");
		System.out.println(sb.toString());
		try {
			chooseFieldSize();
		} catch (WrongFormatException e) {
			e.printStackTrace();
		}
		if (!isReplay) {
			settings = Minesweeper.getInstance().getSettings();
			Field field = new Field(settings.getRowCount(), settings.getColumnCount(), settings.getMineCount());
			this.field = field;
			synchronized (this) {
				thread.start();
			}

			do {
				update();
				processInput();

			} while (field.getState() == GameState.PLAYING);

			if (field.getState() == GameState.SOLVED) {
				System.out.println("Congratulations! You have WON! :D ");
				update();
				score = new Score(System.getProperty("user.name"), "MINESWEEPER", field.getScore(),
						getSQLCurrentDate());

			} else if (field.getState() == GameState.FAILED) {
				System.out.println("You have FAILED! :(");
				update();
			} else if (field.getState() == GameState.EXIT) {
				System.out.println("You have exited the game");
			}
			Gameplay gameplay = field.getGameplay();
			if (gameplay != null) {
				gameplayService.save(gameplay);
			}
		}
		return score;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see minesweeper.consoleui.UserInterface#update()
	 */
	@Override
	public void update() {
		Formatter f = new Formatter();
		f.format("Playing time: %s%n%nRemaining mines: %s%n", Minesweeper.getInstance().getPlayingSeconds(),
				field.getRemainingMineCount());
		System.out.println(f.toString());
		System.out.println(field);
	}

	/**
	 * Processes user input. Reads line from console and does the action on a
	 * playing field according to input string.
	 */
	private void processInput() {
		String s = "Please enter your selection <X> EXIT GAME, <MA1> Mark tile (A1), <OB4> Open tile (B4): ";
		System.out.println(s);
		String input = readLine().toUpperCase();
		try {
			handleInput(input);
		} catch (WrongFormatException e) {
			System.out.println(e.getMessage());
		}

	}

	private void handleInput(String input) throws WrongFormatException {
		Pattern pattern = Pattern.compile("(O|M)([A-Z])([0-9]+)");
		Matcher matcher = pattern.matcher(input);

		if (input.equals("X")) {
			field.setState(GameState.EXIT);
		} else if (matcher.matches()) {
			int row = matcher.group(2).charAt(0) - 'A';
			int column = Integer.parseInt(matcher.group(3));
			if (row > field.getRowCount() || column > field.getColumnCount()) {
				throw new WrongFormatException("Field is not large enough. Go smaller, buddy");
			}
			if (matcher.group(1).charAt(0) == 'O') {
				field.openTile(row, column);
			} else {
				field.markTile(row, column);
			}
			thread.actionWasPerformed();
			thread = new RandomOpenThread();
			thread.start();
		} else {
			throw new WrongFormatException("Wrong Input. Enter again!");

		}

	}

	@Override
	public void chooseFieldSize() throws WrongFormatException {
		StringBuilder sb = new StringBuilder();
		sb.append("Chose your game difficulty: \n").append("1. BEGINNER \n").append("2. INTERMEDIATE \n")
				.append("3. EXPERT \n").append("4. CUSTOM \n").append("5. REPLAY");
		System.out.println(sb.toString());
		String choice = readLine();
		int selectionNumber = Integer.parseInt(choice);
		switch (selectionNumber) {
		case 1:
			Minesweeper.getInstance().setSettings(Settings.BEGINNER);
			break;
		case 2:
			Minesweeper.getInstance().setSettings(Settings.INTERMEDIATE);
			break;
		case 3:
			Minesweeper.getInstance().setSettings(Settings.EXPERT);
			break;
		case 4:
			System.out.println("Enter number of rows:");
			String rowNumber = readLine();
			int rowCount = Integer.parseInt(rowNumber);
			System.out.println("Enter number of columns:");
			String colNumber = readLine();
			int columnCount = Integer.parseInt(colNumber);
			System.out.println("Enter number of mines:");
			String mineNumber = readLine();
			int mineCount = Integer.parseInt(mineNumber);
			Minesweeper.getInstance().setSettings(new Settings(rowCount, columnCount, mineCount));
			break;
		case 5:
			replayUI.play(759);
			isReplay = true;
			break;
		default:
			System.out.println("Wrong input");
			break;
		}

	}

	private void openRandomTile(Field field) {
		boolean isValid = false;
		randomRow = 0;
		randomCol = 0;
		Random r = new Random();
		do {
			randomRow = r.nextInt(field.getRowCount());
			randomCol = r.nextInt(field.getColumnCount());
			if (field.getTile(randomRow, randomCol).getState().equals(Tile.State.CLOSED)) {
				field.openTile(randomRow, randomCol);
				isValid = true;
			}

		} while (isValid = false);
	}

	// @Override
	// public void loadLastField() throws WrongFormatException {
	// Minesweeper.getInstance().setSettings(Settings.load());
	//
	// }

	private java.sql.Date getSQLCurrentDate() {
		return new java.sql.Date(new Date().getTime());
	}

	private class RandomOpenThread extends Thread {
		private boolean actionPerformed = false;

		@Override
		public void run() {
			try {
				Thread.sleep(500000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!actionPerformed) {
				openRandomTile(field);
				update();
				thread.actionWasPerformed();
				thread = new RandomOpenThread();
				thread.start();
			}
		}

		public void actionWasPerformed() {
			this.actionPerformed = true;
		}

	}

}
