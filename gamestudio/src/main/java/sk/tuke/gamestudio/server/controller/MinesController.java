package sk.tuke.gamestudio.server.controller;

import java.util.Date;
import java.util.Formatter;

import javax.jws.WebParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.game.GameState;
import sk.tuke.gamestudio.game.minesweeper.core.Clue;
import sk.tuke.gamestudio.game.minesweeper.core.Field;
import sk.tuke.gamestudio.game.minesweeper.core.Tile;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class MinesController extends GameController {

	private Field field = new Field(9, 9, 10);

	private boolean marking;
	private String message = "";

	@RequestMapping("/minesweeper")
	public String mines(@RequestParam(name = "command", required = false) String command,
			@RequestParam(name = "row", required = false) String row,
			@RequestParam(name = "column", required = false) String column, Model model) {
		message = "";
		if (command != null) {
			if ("mark".equals(command)) {
				marking = !marking;
			} else if ("new".equals(command)) {
				field = new Field(9, 9, 10);
			}
		} else {
			try {
				int rowInt = Integer.parseInt(row);
				int colInt = Integer.parseInt(column);
				if (marking) {
					field.markTile(rowInt, colInt);
				} else {
					field.openTile(rowInt, colInt);
				}
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				//expected error
			}

			if (field.getState() != GameState.PLAYING) {
				if (field.getState() == GameState.FAILED) {
					message = "You have failed :(";
				} else {
					message = "Congratulations. You have won! :)";
					if (this.getUserController().isLogged()) {
						try {
							this.getScoreService()
									.addScore(new Score(this.getUserController().getLoggedUser().getUsername(),
											"MINESWEEPER", field.getScore(), new Date()));
						} catch (ScoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		this.setDataToModel("MINESWEEPER", model);
		model.addAttribute("game", "MINESWEEPER");
		return "game";
	}

	public String renderField() {
		Formatter formatter = new Formatter();
		formatter.format("<table class='table-responsive table-bordered'>");
		for (int row = 0; row < field.getRowCount(); row++) {
			formatter.format("<tr>");
			for (int column = 0; column < field.getColumnCount(); column++) {
				formatter.format("<td>");
				String image = getImageName(field.getTile(row, column));
				formatter.format("<a href='/minesweeper?row=%d&column=%d'>", row, column);
				formatter.format("<img src='/images/mines/%s.png'>", image);
				formatter.format("</a>");
				formatter.format("</td>");
			}
			formatter.format("</tr>");
		}
		formatter.format("</table>");
		return formatter.toString();
	}

	public boolean isMarking() {
		return marking;
	}

	public String getMessage() {
		return message;
	}

	private String getImageName(Tile tile) {
		switch (tile.getState()) {
		case CLOSED:
			return "closed";
		case MARKED:
			return "marked";
		case OPEN:
			if (tile instanceof Clue) {
				return "open" + ((Clue) tile).getValue();
			} else {
				return "mine";
			}
		default:
			throw new IllegalArgumentException("Unexpected tile state");
		}
	}
}
