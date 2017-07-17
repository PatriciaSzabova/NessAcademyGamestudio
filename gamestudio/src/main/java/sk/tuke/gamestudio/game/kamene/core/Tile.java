package sk.tuke.gamestudio.game.kamene.core;

import java.io.Serializable;

/**
 * Tile of a field.
 */
public class Tile implements Serializable {
	
	/** Number representing tile. */
	private int number;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return this.getNumber() == -1 ? "-" : Integer.toString(number);
	}

}
