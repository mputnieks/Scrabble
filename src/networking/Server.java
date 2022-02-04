package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Main.Game;
import model.Player;
import model.Tile;

/**
 * Server.
 * @author  Mikelis Putnieks
 * @version 2022.02.03
 */
public class Server extends Thread {

    public static void createServer(int port) {
        Server server = new Server(port);
        server.start();
    }

    private Game currentgame;
    private List<ClientHandler> threads;
    private List<Player> players;
    private ServerSocket ss;
    private boolean exit = false;
    /** Constructs a new Server object */
    public Server(int portArg) {
    	this.threads = new ArrayList<ClientHandler>();
    	this.players = new ArrayList<Player>();
        try {
			ss = new ServerSocket(portArg);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void move(String name, String c, ClientHandler ch) {
    	String[] coord = c.split(Protocol.AS);
		String[] tileNames = new String[coord.length];
		int[] pos = new int[coord.length];
		for(int i = 0; i<tileNames.length; i++) {
			tileNames[i] = String.valueOf(coord[i].charAt(0));
			if(tileNames[i].equals("-")) {
				tileNames[i] += String.valueOf(coord[i].charAt(1));
				pos[i] = Integer.parseInt(coord[i].strip().substring(2));
			}else {
				pos[i] = Integer.parseInt(coord[i].strip().substring(1));
			}
		}
		
		if(hasTiles(currentgame.getCurrentPlayer(), tileNames)) {	// If player has these tiles
			// removes tiles from player and puts on board, then checks if placement is valid
			if (currentgame.getBoard().placeTiles(pos, tileNames, currentgame.getCurrentPlayer()) && currentgame.getBoard().hasValidPlacement()) {
    			finishMove(ch, c);
    		}else {
    			System.out.println("Invalid placement!");
    			currentgame.getCurrentPlayer().getTray().addTiles(currentgame.getBoard().pickupNonFixed()); // removes non-fixed from board and adds back to player
    			ch.sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.INVALID_MOVE);
	    		skip(ch);
    		}
		}else {
    		ch.sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.INVALID_MOVE);
    		skip(ch);
		}
    }
    
    public void finishMove(ClientHandler ch, String coordinates) {
		if(currentgame.getBoard().containsValidWords()) {
			int score = currentgame.getBoard().executeMove();
			currentgame.getCurrentPlayer().addToScore(score);
			currentgame.getCurrentPlayer().getTray().fill(currentgame.getTileBag());
			
			broadcast(Protocol.MOVE + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName() + Protocol.SEPARATOR + coordinates + Protocol.SEPARATOR + String.valueOf(score));
			ch.sendMessage(Protocol.TILES + Protocol.SEPARATOR + tilesToString(currentgame.getCurrentPlayer().getTray().getTiles()));
			
			currentgame.nextPlayer();
			broadcast(Protocol.TURN + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
		}else {
			System.out.println("Invalid word!");
			currentgame.getCurrentPlayer().getTray().addTiles(currentgame.getBoard().pickupNonFixed()); // removes non-fixed from board and adds back to player
			ch.sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.INVALID_MOVE);
			skip(ch);
		}
	}
    
    public void swap(String str, ClientHandler ch) {
    	String[] tileNames = str.split(" ");
    	List<Tile> tilesToSwap = new ArrayList<Tile>();
    	List<Tile> newTiles = new ArrayList<Tile>();
    	List<Tile> tiles = currentgame.getCurrentPlayer().getTray().getTiles();
    	for (int i = 0; i<tileNames.length; i++) {
    		for (int j = 0; j < tiles.size(); j++) {
        		if (tiles.get(j).getName().equals(tileNames[i]) && !tilesToSwap.contains(tiles.get(j))) {
        			tilesToSwap.add(tiles.get(j));
        		}
        	}
    	}
    	
    	if (hasTiles(currentgame.getCurrentPlayer(), tileNames)) {	// Player had all tiles, let's do a swap
    		if (!currentgame.getTileBag().isEmpty()) { 
    			List<Tile> replaced_tiles = new ArrayList<Tile>();
    			for(int i = 0; i < tilesToSwap.size(); i++) {
    				currentgame.getCurrentPlayer().getTray().removeTile(tilesToSwap.get(i));
    				replaced_tiles.add(tilesToSwap.get(i));
    				newTiles.add(currentgame.getTileBag().getRandomTile());
    				if(currentgame.getTileBag().isEmpty()) {
    					break;
    				}
    			}
    			currentgame.getCurrentPlayer().getTray().addTiles(newTiles);
    			currentgame.getTileBag().addTiles(replaced_tiles);
    			
    			ch.sendMessage(Protocol.PASS + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName() + Protocol.SEPARATOR + tilesToString(newTiles));
    			broadcast(Protocol.PASS + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
    			currentgame.nextPlayer();
    			broadcast(Protocol.TURN + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
    		}
    	}else {
    		System.out.println("Player-tiles: "+tilesToString(tiles));
    		ch.sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.INVALID_MOVE);
    		skip(ch);
    	}
    }
    
    public void skip(ClientHandler ch) {
		broadcast(Protocol.PASS + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
		currentgame.nextPlayer();
		broadcast(Protocol.TURN + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
    }
    
    public boolean isTurn(ClientHandler ch) {
    	if(currentgame.getCurrentPlayer().getName().equals(ch.getPlayer().getName())) {
    		return true;
    	}else {
    		ch.sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.OUT_OF_TURN);
    		return false;
    	}
    }
    
    public boolean hasTiles(Player p, String[] tileNames) {
    	List<Tile> tilesPresent = new ArrayList<Tile>();
    	List<Tile> tiles = p.getTray().getTiles();
    	for (int i = 0; i<tileNames.length; i++) {
    		for (int j = 0; j < tiles.size(); j++) {
    			if(tileNames[i].contains("-")) {
    				if (tiles.get(j).isBlank() && !tilesPresent.contains(tiles.get(j))) {
            			tilesPresent.add(tiles.get(j));
            		}
    			}else {
    				if (tiles.get(j).getName().equals(tileNames[i]) && !tilesPresent.contains(tiles.get(j))) {
            			tilesPresent.add(tiles.get(j));
            		}
    			}
        	}
    	}
    	if (tileNames.length == tilesPresent.size()) {
    		return true;
    	}
    	return false;
    }
    
    public void run() {
		while(!exit) {
			try {
				Socket connection = ss.accept();
				if (connection!=null) {
					System.out.println("new client attempting to connect");
					ClientHandler t = new ClientHandler(this, connection);
					addHandler(t);
					t.announce();
					t.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    public void print(String message){
        System.out.println(message);
    }
    
    public void broadcast(String msg) {
    	print(msg);
        for (int i = 0; i<threads.size(); i++) {
        	threads.get(i).sendMessage(msg);
        }
    }
    
    public void addHandler(ClientHandler handler) {
        threads.add(handler);
    }
    
    public void addPlayer(Player p) {
        players.add(p);
    }
    
    public void setReady(Player p){
    	p.setReady();
    	broadcast(Protocol.SERVERREADY + Protocol.SEPARATOR + getReadyPlayerString());
    	if (arePlayersReady()) {
    		broadcast(Protocol.START + Protocol.SEPARATOR + getReadyPlayerString());
    		exit = true;	// from this point on, no more connections are accepted
    		currentgame = new Game(players);
    		currentgame.start();
    		sendTiles(); // broadcasting players their tiles n stuff
    		broadcast(Protocol.TURN + Protocol.SEPARATOR + currentgame.getCurrentPlayer().getName());
    	}
    }
    
    private void sendTiles() {
    	for(int i = 0; i < threads.size(); i++) {
    		threads.get(i).sendMessage(Protocol.TILES + Protocol.SEPARATOR + tilesToString(threads.get(i).getPlayer().getTray().getTiles()));
    	}
    }
    
    private String tilesToString(List<Tile> tiles) {
    	String str = "";
    	for (int i = 0; i < tiles.size(); i++) {
    		str += tiles.get(i).getName() + Protocol.AS;
    	}
    	return str;
    }
    
    public boolean arePlayersReady() {
    	for (int i = 0; i<players.size(); i++) {
    		if(!players.get(i).isReady()) {
    			return false;
    		}
        }
    	return true;
    }
    
    public String getPlayerString() {
    	String result = players.get(0).getName();
    	for(int i = 1; i < players.size(); i++) {
    		result += " " + players.get(i).getName();
    	}
    	return result;
    }
    
    public String getReadyPlayerString() {
    	String result = "";
    	for(int i = 0; i < players.size(); i++) {
    		if(players.get(i).isReady()) {
    			result += players.get(i).getName()+" ";
    		}
    	}
    	return result;
	}
    
    public void removeHandler(ClientHandler handler) {
        threads.remove(handler);
    }
    
    public void removePlayer(Player p) {
        players.remove(p);
    }
}
