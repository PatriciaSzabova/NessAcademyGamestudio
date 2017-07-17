package sk.tuke.gamestudio.game.minesweeper.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MineCoordinate {
	
	@Id
	@GeneratedValue
	private int id;

	private int row;
	private int col;

	public MineCoordinate() {

	}

	public MineCoordinate(int row, int column) {
		super();
		this.row = row;
		this.col = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}

	@Override
	public String toString() {
		return "MineCoordinate [row=" + row + ", column=" + col + "]";
	}

}
