package sk.tuke.gamestudio.game.kamene;

/**
 * Settings class
 * 
 * @author P3502714
 *
 */
public class Settings {

	/** Row count */
	private final int rowCount;
	/** Column count */
	private final int columnCount;
	/** Default field setting */
	public static final Settings DEFAULT = new Settings(4, 4);

	/** Constructor */
	public Settings(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

}
