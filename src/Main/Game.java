package Main;

import java.io.IOException;
import java.util.List;

import model.Board;
import model.Player;
import model.TileBag;

public class Game {

	private static final String PATH = "./files/"; // Path to the test folder with special tile mappings
	private List<Player> players;
	private int current_player_index = 0;
	private Board board;
	private TileBag tile_bag;
	
	public Game(List<Player> players) {
		this.players = players;
		try {
			this.board = Board.createBoard(PATH+"special_fields.txt");
			this.tile_bag = TileBag.createTileBag(PATH+"complete_tile_list.txt");
		} catch (IOException e) {
			System.out.println("Error: files special_fields.txt or complete_tile_list.txt failed loading!");
			e.printStackTrace();
		}
	}
	
	public Player nextPlayer() {
		if (current_player_index == players.size()-1) {
			current_player_index = 0;
		}else {
			current_player_index++;
		}
		return getCurrentPlayer();
	}
	
	public Player getCurrentPlayer() {
		return players.get(current_player_index);
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public TileBag getTileBag() {
		return tile_bag;
	}
	
	public void start() {
		for (int i = 0; i<players.size(); i++) {
			players.get(i).FillTray(tile_bag);
		}
	}
}
