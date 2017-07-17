package sk.tuke.gamestudio.server.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import sk.tuke.gamestudio.server.service.annotation.Column;
import sk.tuke.gamestudio.server.service.annotation.Table;

@Entity
// @Table(name = "rating", createDrop = false)
public class Rating {

	@Id
	@GeneratedValue
	// @Column(name = "id", primaryKey = true)
	private int id;
	// @Column(name = "player")
	private String player;
	// @Column(name = "game")
	private String game;
	// @Column(name = "rating")
	private int rating;
	// @Column(name = "ratedon")
	private Date ratedon;

	public Rating() {

	}

	public Rating(String player, String game, int rating, Date ratedon) {
		super();
		this.player = player;
		this.game = game;
		this.rating = rating;
		this.ratedon = ratedon;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getRatedon() {
		return ratedon;
	}

	public void setRatedon(Date ratedon) {
		this.ratedon = ratedon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "(" + player + ", " + game + ", " + rating + ", " + ratedon + ")";
	}

}
