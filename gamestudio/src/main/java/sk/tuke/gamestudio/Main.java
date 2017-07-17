package sk.tuke.gamestudio;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.frontend.spring.ClientProxyFactoryBeanDefinitionParser.SpringClientProxyFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sk.tuke.gamestudio.game.GameUserInterface;
import sk.tuke.gamestudio.game.Games;
import sk.tuke.gamestudio.game.GamestudioUI;
import sk.tuke.gamestudio.game.kamene.consoleui.ConsoleUIKamene;
import sk.tuke.gamestudio.game.minesweeper.consoleui.ConsoleUIMinesweeper;
import sk.tuke.gamestudio.game.minesweeper.consoleui.ReplayConsoleUI;
import sk.tuke.gamestudio.game.minesweeper.service.GameplayServiceJPA;
import sk.tuke.gamestudio.server.service.CommentService;
import sk.tuke.gamestudio.server.service.RatingService;
import sk.tuke.gamestudio.server.service.ScoreService;
import sk.tuke.gamestudio.server.service.JPA.CommentServiceJPA;
import sk.tuke.gamestudio.server.service.JPA.RatingServiceJPA;
import sk.tuke.gamestudio.server.service.REST.CommentServiceRESTClient;
import sk.tuke.gamestudio.server.service.REST.RatingServiceRESTClient;
import sk.tuke.gamestudio.server.service.REST.ScoreServiceRESTClient;

@Configuration
@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(Main.class, args);
		new SpringApplicationBuilder(Main.class).web(false).run(args);
	}

	@Bean
	public CommandLineRunner runner(GamestudioUI ui) {
		return args -> ui.start();
	}

	@Bean
	public GamestudioUI gamestudioUI() {
		return new GamestudioUI();
	}

	@Bean
	public ConsoleUIKamene consoleUIKamene() {
		return new ConsoleUIKamene();
	}

	@Bean
	public ConsoleUIMinesweeper consoleUIMines() {
		return new ConsoleUIMinesweeper();
	}

	@Bean
	public Map<Games, GameUserInterface> games() {
		Map<Games, GameUserInterface> games = new HashMap<>();
		games.put(Games.STONES, consoleUIKamene());
		games.put(Games.MINESWEEPER, consoleUIMines());
		return games;
	}

	@Bean
	public ScoreService scoreService() {
		return new ScoreServiceRESTClient();
	}

	@Bean
	public RatingService ratingService() {
		return new RatingServiceRESTClient();
		//return new RatingServiceJPA();
	}

	@Bean
	public CommentService commentService() {
		//return new CommentServiceJPA();
		return new CommentServiceRESTClient();
	}

	@Bean
	public GameplayServiceJPA gameplayServiceJPA() {
		return new GameplayServiceJPA();
	}

	@Bean
	public ReplayConsoleUI replayConsole() {
		return new ReplayConsoleUI();
	}
}