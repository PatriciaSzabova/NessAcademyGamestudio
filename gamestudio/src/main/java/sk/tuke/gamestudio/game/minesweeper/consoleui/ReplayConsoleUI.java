package sk.tuke.gamestudio.game.minesweeper.consoleui;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio.game.minesweeper.core.Field;
import sk.tuke.gamestudio.game.minesweeper.entity.Command;
import sk.tuke.gamestudio.game.minesweeper.entity.Gameplay;
import sk.tuke.gamestudio.game.minesweeper.service.GameplayServiceJPA;

public class ReplayConsoleUI {

	@Autowired
	private GameplayServiceJPA gameplayService;

	public void play(int gameplayId) {
		Gameplay gameplay = gameplayService.load(gameplayId);

		Field field = new Field(gameplay.getRowCount(), gameplay.getColumnCount(), gameplay.getMineCoordinates());
		System.out.println(field);
		for (Command cm : gameplay.getCommands()) {
			switch (cm.getType()) {
			case OPEN:
				field.openTile(cm.getRow(), cm.getColumn());
				break;
			case MARK:
				field.openTile(cm.getRow(), cm.getColumn());
				break;
			default:
				throw new IllegalArgumentException("Not supported command type" + cm.getType());
			}
			System.out.println(field);
		}
	}

}
