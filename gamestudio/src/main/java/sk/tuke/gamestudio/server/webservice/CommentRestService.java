package sk.tuke.gamestudio.server.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;


@Path("/comment")
public class CommentRestService {
	@Autowired
	private CommentService commentService;

	@POST
	@Consumes("application/json")
	public Response addComment(Comment comment) {
		try {
			commentService.addComment(comment);
		} catch (CommentException e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@Path("/{game}")
	@Produces("application/json")
	public List<Comment> getComments(@PathParam("game") String game) {
		try {
			return commentService.getComments(game.toUpperCase());
		} catch (CommentException e) {
			e.printStackTrace();
		}
		return null;
	}

}
