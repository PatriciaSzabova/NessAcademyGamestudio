package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

abstract class GameController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private UserController userController;

	public UserController getUserController() {
		return userController;
	}

	public CommentService getCommentService() {
		return commentService;
	}

	public ScoreService getScoreService() {
		return scoreService;
	}

	public RatingService getRatingService() {
		return ratingService;
	}

	public void setDataToModel(String game, Model model) {
		try {
			model.addAttribute("scores", scoreService.getBestScores(game));
		} catch (ScoreException e) {
			e.printStackTrace();
		}
		try {
			model.addAttribute("comments", commentService.getComments(game));
		} catch (CommentException e) {
			e.printStackTrace();
		}
		try {
			model.addAttribute("rating", ratingService.getAverageRating(game));
		} catch (RatingException e) {
			e.printStackTrace();
		}catch(NullPointerException e){
			model.addAttribute("rating", 0);
		}
	}

}
