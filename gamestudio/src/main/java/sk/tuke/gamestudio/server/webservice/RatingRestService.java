package sk.tuke.gamestudio.server.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;

@Path("/rating")
public class RatingRestService {
	@Autowired
	private RatingService ratingService;

	@POST
	@Consumes("application/json")
	public void setRating(Rating rating) {
		try {
			ratingService.setRating(rating);
		} catch (RatingException e) {
			e.printStackTrace();
		}

	}

	@GET
	@Path("/{game}")
	@Produces("application/json")
	public double getAvgRating(@PathParam("game") String game) throws RatingException {
		return ratingService.getAverageRating(game.toUpperCase());
	}

	@GET
	@Path("/{game}/{player}")
	@Produces("application/json")
	public int getRating(@PathParam("game") String game, @PathParam("player") String player) throws RatingException {
		return ratingService.getRating(game.toUpperCase(), player);
	}

}
