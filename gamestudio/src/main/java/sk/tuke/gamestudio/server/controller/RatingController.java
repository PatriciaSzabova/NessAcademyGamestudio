package sk.tuke.gamestudio.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingException;

@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class RatingController extends GameController {

	@Autowired
	MinesController minesController;
	@Autowired
	StonesController stonesController;
	@Autowired
	PexesoController pexesoController;

	@RequestMapping("/rating")
	public String rating(String rating, String game, Model model) {
		Rating r = new Rating();
		String currentGame = game;
		r.setGame(currentGame);
		r.setPlayer(this.getUserController().getLoggedUser().getUsername());
		r.setRatedon(new Date());
		try {
			int gameRating = Integer.parseInt(rating);
			r.setRating(gameRating);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		try {
			this.getRatingService().setRating(r);
		} catch (RatingException e) {
			e.printStackTrace();
		}
		this.setDataToModel(currentGame, model);
		model.addAttribute("game", currentGame.toUpperCase());

		return "game";
	}

}
