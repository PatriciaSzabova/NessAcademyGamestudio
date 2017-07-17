package sk.tuke.gamestudio.game.minesweeper.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

@Entity
public class Gameplay {

	@Id
	@GeneratedValue
	private int id;

	private int rowCount;
	private int columnCount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "gameplay_id")
	private Set<MineCoordinate> mineCoordinates;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "gameplay_id")
	@OrderColumn
	private List<Command> commands;

	public Gameplay() {

	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public void setMineCoordinates(Set<MineCoordinate> mineCoordinates) {
		this.mineCoordinates = mineCoordinates;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
	
	public void addCommand(Command command){
		if(commands == null){
			commands = new ArrayList<>();
		}
		commands.add(command);
	}

	public Set<MineCoordinate> getMineCoordinates() {
		return mineCoordinates;
	}

	public List<Command> getCommands() {
		return commands;
	}

}
