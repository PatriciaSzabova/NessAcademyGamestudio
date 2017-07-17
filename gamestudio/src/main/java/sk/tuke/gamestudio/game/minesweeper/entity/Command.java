package sk.tuke.gamestudio.game.minesweeper.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Command {

	@Id
	@GeneratedValue
	private int id;

	private CommandType type;
	private int row;
	private int col;

	public Command() {

	}

	public Command(CommandType type, int row, int column) {
		super();
		this.type = type;
		this.row = row;
		this.col = column;
	}

	public CommandType getType() {
		return type;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}

	@Override
	public String toString() {
		return "Command [type=" + type + ", row=" + row + ", column=" + col + "]";
	}

}
