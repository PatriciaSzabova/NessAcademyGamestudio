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
import sk.tuke.gamestudio.game.pexeso.core.Field;
import sk.tuke.gamestudio.game.pexeso.core.Tile;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.entity.User;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PexesoController extends GameController {

	private Field field = new Field(3, 4);
	private String message = "";

	@RequestMapping("/pexeso")
	public String pexeso(@RequestParam(name = "command", required = false) String command,
			@RequestParam(name = "row", required = false) String row,
			@RequestParam(name = "column", required = false) String column, Model model) {
		message = "";
		if (command != null) {
			if ("new".equals(command)) {
				field = new Field(3, 4);
			}
		} else {
			try {
				int rowInt = Integer.parseInt(row);
				int colInt = Integer.parseInt(column);
				field.openTile(rowInt, colInt);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				//expected error
			}
			if (field.getGameState() == GameState.SOLVED) {
				message = "Congratulations. You have won! :)";
				if (this.getUserController().isLogged()) {
					try {
						this.getScoreService()
								.addScore(new Score(this.getUserController().getLoggedUser().getUsername(), "PEXESO",
										field.getScore(), new Date()));

					} catch (ScoreException e) {
						e.printStackTrace();
					}
				}

			}
		}
		this.setDataToModel("PEXESO", model);
		model.addAttribute("game", "PEXESO");
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
				formatter.format("<a href='/pexeso?row=%d&column=%d'>", row, col);
				formatter.format("<img src='/images/pexeso/%s.png'>", image);
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
		switch (tile.getState()) {
		case CLOSED:
			return "default";
		case OPEN:
			return Integer.toString(tile.getValue());
		case SOLVED:
			return Integer.toString(tile.getValue());
		default:
			throw new IllegalArgumentException("Unexpected tile state");
		}

	}

}
