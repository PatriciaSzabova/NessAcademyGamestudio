package sk.tuke.gamestudio.server.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;

@Controller
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class CommentController extends GameController {

	@Autowired
	MinesController minesController;
	@Autowired
	StonesController stonesController;
	@Autowired
	PexesoController pexesoController;

	@RequestMapping("/comment")
	public String comment(Comment comment, Model model) {
		String currentGame = comment.getGame();

		comment.setPlayer(this.getUserController().getLoggedUser().getUsername());
		comment.setCommentedon(new Date());

		try {
			this.getCommentService().addComment(comment);
		} catch (CommentException e) {
			e.printStackTrace();
		}
		this.setDataToModel(currentGame, model);
		model.addAttribute("game", currentGame.toUpperCase());
		return "game";
	}

}
