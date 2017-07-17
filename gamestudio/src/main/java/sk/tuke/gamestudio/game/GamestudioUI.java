package sk.tuke.gamestudio.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.server.entity.Comment;
import sk.tuke.gamestudio.server.entity.Rating;
import sk.tuke.gamestudio.server.entity.Score;
import sk.tuke.gamestudio.server.service.CommentException;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingException;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreException;
import sk.tuke.gamestudio.server.service.ScoreService;
import sk.tuke.gamestudio.server.service.JPA.CommentServiceJPA;
import sk.tuke.gamestudio.server.service.JPA.RatingServiceJPA;
import sk.tuke.gamestudio.server.service.JPA.ScoreServiceJPA;


public class GamestudioUI {

	@Autowired
	private Map<Games, GameUserInterface> allGames;	
	@Autowired
	private ScoreService scoreService;
	@Autowired
	private RatingService ratingService;
	@Autowired
	private CommentService commentService;

	private String currentGamePlayed;

	private Score gameScore;

	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	private String readLine() {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public void start() {
		do {
			processInput();

			if (gameScore != null) {
				System.out.println("Your score for this game is: " + gameScore);
				try {
					scoreService.addScore(gameScore);
				} catch (ScoreException e) {
					e.getMessage();
				}
			}

			try {
				System.out.println(scoreService.getBestScores(currentGamePlayed).toString());
			} catch (ScoreException e) {
				e.getMessage();
			}
			try {
				commentOption();
			} catch (WrongFormatException e) {
				e.getMessage();
			}
			System.out.println("Other comments: ");
			try {
				System.out.println(commentService.getComments(currentGamePlayed).toString());
			} catch (CommentException e1) {
				e1.getMessage();
			}
			try {
				ratingOption();
			} catch (WrongFormatException e) {
				e.getMessage();
			}
			System.out.println("Average rating:");
			try {
				System.out.println(ratingService.getAverageRating(currentGamePlayed));
			} catch (RatingException e) {
				e.printStackTrace();
			}
			System.out.println("Your Rating:");
			try {
				System.out.println(ratingService.getRating(currentGamePlayed, System.getProperty("user.name")));
			} catch (RatingException e) {
				e.printStackTrace();
			}
			currentGamePlayed = "";
		} while (true);
	}

	private void processInput() {
		int counter = 1;
		StringBuilder sb = new StringBuilder();
		sb.append("Pick your game:\n");
		for (Games g : Games.values()) {
			sb.append(counter).append(". ").append(g).append("\n");
			counter++;
		}
		sb.append("or press <X> to exit Gamestudio");
		System.out.println(sb.toString());
		String input = readLine();
		handleInput(input.toUpperCase());
	}

	private void handleInput(String input) {
		Pattern pattern = Pattern.compile("(X)|([0-9]+)");
		Matcher matcher = pattern.matcher(input);

		if (matcher.matches()) {
			if (input.equals("X")) {
				System.out.println("You have exited Gamestudio");
				System.exit(0);
			} else if (Integer.parseInt(input) - 1 >= allGames.size() || Integer.parseInt(input) - 1 < 0) {
				System.out.println("No such game exists :(");
				processInput();
			}
			int index = Integer.parseInt(input) - 1;
			Games g = Games.values()[index];
			gameScore = allGames.get(g).newGameStarted();
			currentGamePlayed = g.name();
		} else {
			System.out.println("Wrong Input! Try again. ");
			processInput();
		}

	}

	private void commentOption() throws WrongFormatException {
		System.out.println("Would you like to leave a comment? Y/N");
		String choice = readLine();
		if (choice.toUpperCase().equals("Y")) {
			System.out.println("Enter your comment:");
			String userComment = readLine();
			Comment cmt = new Comment(System.getProperty("user.name"), currentGamePlayed, userComment,
					getSQLCurrentDate());
			try {
				commentService.addComment(cmt);
			} catch (CommentException e) {
				e.getMessage();
			}
		} else if (choice.toUpperCase().equals("N")) {
			return;
		} else {
			throw new WrongFormatException("Wrong Input!");
		}
	}

	private void ratingOption() throws WrongFormatException {
		System.out.println("Would you like to rate the game? Y/N");
		String choice = readLine();

		if (choice.toUpperCase().equals("Y")) {
			System.out.println("Enter your rating: (1 for lowest - 5 for highest");
			String userRating = readLine();

			Pattern pattern = Pattern.compile("[1-5]");
			Matcher matcher = pattern.matcher(userRating);

			if (matcher.matches()) {
				Rating rt = new Rating(System.getProperty("user.name"), currentGamePlayed, Integer.parseInt(userRating),
						getSQLCurrentDate());
				try {
					ratingService.setRating(rt);
				} catch (RatingException e) {
					e.printStackTrace();
				}
			}

		} else if (choice.toUpperCase().equals("N")) {
			return;
		} else {
			throw new WrongFormatException("Wrong Input!");
		}
	}

	private java.sql.Date getSQLCurrentDate() {
		return new java.sql.Date(new Date().getTime());
	}

}
