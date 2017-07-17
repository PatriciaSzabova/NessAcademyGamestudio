package sk.tuke.gamestudio.server.controller;

import java.util.Date;
import java.util.Formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.game.GameState;
import sk.tuke.gamestudio.game.kamene.core.Field;
import sk.tuke.gamestudio.game.kamene.core.InvalidMoveException;
import sk.tuke.gamestudio.game.kamene.core.Tile;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class StonesController extends GameController {

	private Field field = new Field(4, 4);
	private String message = "";

	@RequestMapping("/stones")
	public String Stones(@RequestParam(name = "command", required = false) String command,
			@RequestParam(name = "row", required = false) String row,
			@RequestParam(name = "column", required = false) String column, Model model) {
		message = "";
		if (command != null) {
			if ("new".equals(command)) {
				field = new Field(4, 4);
			}
		} else {
			try {
				int rowInt = Integer.parseInt(row);
				int colInt = Integer.parseInt(column);
				field.moveDefaultTile(rowInt, colInt);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				//this is expected error
			} catch (InvalidMoveException e) {
				e.printStackTrace();
			}
			if (field.getGameState() == GameState.SOLVED) {
				message = "Congratulations. You have won! :)";
				if (this.getUserController().isLogged()) {
					try {
						this.getScoreService()
								.addScore(new Score(this.getUserController().getLoggedUser().getUsername(), "STONES",
										field.getScore(), new Date()));
					} catch (ScoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		this.setDataToModel("STONES", model);
		model.addAttribute("game", "STONES");
		return "game";
	}

	public String getMessage() {
		return message;
	}

	public String renderField() {
		Formatter formatter = new Formatter();
		formatter.format("<table>");
		for (int row = 0; row < field.getRowCount(); row++) {
			formatter.format("<tr>");
			for (int col = 0; col < field.getColumnCount(); col++) {
				String image = getImageName(field.getTile(row, col));
				formatter.format("<td>");
				formatter.format("<a href='/stones?row=%d&column=%d'>", row, col);
				formatter.format("<img src='/images/stones/%s.png'>", image);
				formatter.format("</a>");
				formatter.format("</td>");
			}
			formatter.format("\n", null);
			formatter.format("<tr>");
		}
		formatter.format("</table>");
		return formatter.toString();
	}

	private String getImageName(Tile tile) {
		int number = tile.getNumber();
		if (tile.getNumber() == -1) {
			return "default";
		}
		return Integer.toString(number);
	}

}
