package sk.tuke.gamestudio.game.kamene.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import sk.tuke.gamestudio.game.GameState;
import sk.tuke.gamestudio.game.kamene.Settings;

/**
 * Field represents playing field and game logic.
 */
public class Field implements Serializable {

	/**
	 * Playing field tiles.
	 */
	private final Tile[][] tiles;

	/**
	 * Field row count. Rows are indexed from 0 to (rowCount - 1).
	 */
	private final int rowCount;

	/**
	 * Column count. Columns are indexed from 0 to (columnCount - 1).
	 */
	private final int columnCount;

	/**
	 * Game state.
	 */
	private GameState gameState = GameState.PLAYING;

	/**
	 * Row number of the default tile.
	 */
	private int defaultRow;

	/**
	 * Column number of the default tile.
	 */
	private int defaultCol;
	private long startMillis;

	/**
	 * Save file.
	 */
	private static final String LAST_GAME_STATUS_FILE = System.getProperty("user.home")
			+ System.getProperty("file.separator") + "kamene.settings";

	/**
	 * Constructor.
	 * 
	 * @param rowCount
	 *            row count
	 * @param columnCount
	 *            column count
	 */
	public Field(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		tiles = new Tile[rowCount][columnCount];
		startMillis = System.currentTimeMillis();
		generate();
		getDefaultTilePosition();
	}

	/**
	 * Constructor
	 * 
	 * @param settings
	 *            field settings
	 */
	public Field(Settings settings) {
		this.rowCount = settings.getRowCount();
		this.columnCount = settings.getColumnCount();
		tiles = new Tile[rowCount][columnCount];

		generate();
		getDefaultTilePosition();
	}

	public void setDefaultRow(int defaultRow) {
		this.defaultRow = defaultRow;
	}

	public void setDefaultCol(int defaultCol) {
		this.defaultCol = defaultCol;
	}

	public int getDefaultRow() {
		return defaultRow;
	}

	public int getDefaultCol() {
		return defaultCol;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Tile getTile(int row, int column) {
		return tiles[row][column];
	}

	/**
	 * Generates the playing field.
	 */
	private void generate() {
		Random r = new Random();
		int rowOfLast = -1, colOfLast = -1;
		int randomIndex = 0;
		List<Integer> numbers = new ArrayList<>();
		for (int i = 0; i <= (rowCount * columnCount) - 1; i++) {
			numbers.add(i + 1);
		}
		int maxValue = numbers.size();
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				tiles[row][col] = new Tile();
				if (numbers.size() > 0) {
					randomIndex = r.nextInt(numbers.size());
				}
				tiles[row][col].setNumber(numbers.get(randomIndex));
				numbers.remove(randomIndex);
				if (tiles[row][col].getNumber() == maxValue) {
					rowOfLast = row;
					colOfLast = col;
				}
			}
		}
		tiles[rowOfLast][colOfLast].setNumber(-1);
	}

	// private void generate() {
	// int counter = 1;
	// for (int row = 0; row < rowCount; row++) {
	// for (int col = 0; col < columnCount; col++) {
	// tiles[row][col] = new Tile();
	// tiles[row][col].setNumber(counter);
	// counter++;
	// }
	// }
	// tiles[rowCount - 1][columnCount - 1].setNumber(-1);
	// getDefaultTilePosition();
	// shuffle();
	// }
	//
	// private void shuffle(){
	// int row = defaultRow;
	// int col = defaultCol;
	// for(int i=0; i<100; i++){
	// try {
	// moveDefaultTile(row, col);
	// } catch (InvalidMoveException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * Moves the tile to the specified row and column
	 * 
	 * @param row
	 *            row
	 * @param col
	 *            column
	 * @throws InvalidMoveException
	 *             if row and column are out of field's bounds
	 */
	public void moveDefaultTile(int row, int col) throws InvalidMoveException {
		getDefaultTilePosition();

		if (row < 0 || col < 0 || row > rowCount - 1 || col > columnCount - 1) {
			throw new InvalidMoveException("Cannot move there!");
		} else {
			if (checkMoveDistance(row, col, defaultRow, defaultCol)) {
				int temp = tiles[row][col].getNumber();
				tiles[row][col].setNumber(tiles[defaultRow][defaultCol].getNumber());
				tiles[defaultRow][defaultCol].setNumber(temp);
			}
		}
		if (isSolved()) {
			setGameState(GameState.SOLVED);
		}
	}

	/**
	 * Tracks the position of the default tile.
	 */
	public void getDefaultTilePosition() {
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				if (tiles[row][col].getNumber() == -1) {
					defaultRow = row;
					defaultCol = col;
					return;
				}
			}
		}
	}

	public boolean checkMoveDistance(int row, int col, int defRow, int defCol) throws InvalidMoveException {
		if ((Math.abs(row - defRow) == 1 && col == defCol) || (Math.abs(col - defCol) == 1 && row == defRow)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if game is solved, false otherwise.
	 *
	 * @return true if game is solved, false otherwise
	 */
	private boolean isSolved() {
		if (tiles[rowCount - 1][columnCount - 1].getNumber() != -1) {
			return false;
		}
		List<Integer> fieldValues = new ArrayList<>();

		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				fieldValues.add(tiles[row][col].getNumber());
			}
		}
		fieldValues.remove(fieldValues.size() - 1);
		List<Integer> sortedFieldValues = fieldValues.stream().sorted().collect(Collectors.toList());
		return sortedFieldValues.equals(fieldValues);
	}

	/**
	 * Saves current field status.
	 */
	public void save() {
		try (FileOutputStream out = new FileOutputStream(LAST_GAME_STATUS_FILE);
				ObjectOutputStream s = new ObjectOutputStream(out)) {
			s.writeObject(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads last saved field status.
	 * 
	 * @return field
	 */
	public static Field load() {
		Field field = null;
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(LAST_GAME_STATUS_FILE))) {
			field = (Field) input.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return field;
	}

	@Override
	public String toString() {
		Formatter f = new Formatter();
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < columnCount; col++) {
				f.format("%3s", tiles[row][col].toString());
			}
			f.format("\n", null);
		}
		return f.toString();
	}

	public int getPlayingSeconds() {
		return (int) ((System.currentTimeMillis() - startMillis) / 1000);
	}

	public int getScore() {
		int score = rowCount * columnCount * 10 - getPlayingSeconds();
		return score < 0 ? 0 : score;
	}

}
