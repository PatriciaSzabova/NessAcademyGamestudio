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

import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;

@Path("/score")
public class ScoreRestService {
	@Autowired
	private ScoreService scoreService;
	
	@GET
	@Path("/{game}")
	@Produces("application/json")
	public List<Score> getScores(@PathParam("game") String game) {
		try {
			return scoreService.getBestScores(game.toUpperCase());
		} catch (ScoreException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Consumes("application/json")
	public Response addScore(Score score){
		try {
			scoreService.addScore(score);
		} catch (ScoreException e) {			
			e.printStackTrace();
		}
		return Response.ok().build();
	}

}
