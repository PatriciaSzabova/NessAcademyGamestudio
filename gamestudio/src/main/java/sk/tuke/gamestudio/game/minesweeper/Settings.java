package sk.tuke.gamestudio.game.minesweeper;

import java.io.Serializable;

public class Settings implements Serializable {

	private final int rowCount;
	private final int columnCount;
	private final int mineCount;

	public static final Settings BEGINNER = new Settings(9, 9, 10);
	public static final Settings INTERMEDIATE = new Settings(16, 16, 40);
	public static final Settings EXPERT = new Settings(16, 30, 99);

	// private static final String SETTING_FILE =
	// System.getProperty("user.home") + System.getProperty("file.separator")
	// + "minesweeper.settings";

	public Settings(int rowCount, int columnCount, int mineCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public int getMineCount() {
		return mineCount;
	}

	// public void save() {
	// try (FileOutputStream out = new FileOutputStream(SETTING_FILE);
	// ObjectOutputStream s = new ObjectOutputStream(out)) {
	// s.writeObject(this);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	// public static Settings load() {
	// Settings settings = null;
	// try
	// (ObjectInputStream input = new ObjectInputStream(new
	// FileInputStream(SETTING_FILE)))
	// {settings = (Settings) input.readObject();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// if (settings == null){
	// return BEGINNER;
	// }
	//
	// return settings;
	//
	// }

	@Override
	public int hashCode() {
		return rowCount * mineCount * columnCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Settings other = (Settings) obj;
		if (columnCount != other.columnCount)
			return false;
		if (mineCount != other.mineCount)
			return false;
		if (rowCount != other.rowCount)
			return false;
		return true;
	}

}
