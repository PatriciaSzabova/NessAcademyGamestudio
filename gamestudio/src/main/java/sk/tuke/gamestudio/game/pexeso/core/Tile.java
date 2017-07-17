package sk.tuke.gamestudio.game.pexeso.core;

public class Tile {

	public enum State {
		OPEN,
		CLOSED,
		SOLVED
	}
	
	private State state = State.CLOSED;
	private int value;		

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
