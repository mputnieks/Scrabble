package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import model.Board;
import model.Field;
import model.LocalHumanPlayer;
import model.NextMoveButton;
import model.Player;
import model.Swapper;
import model.Tile;
import model.TileBag;
import networking.Client;
import networking.Protocol;
import networking.Server;
import view.BlankTilePopUp;
import view.MousepadListener;
import view.StartupPopUp;

public class Controller {
	
	private static final String PATH = "./files/"; // Path to the test folder with special tile mappings
	
	private TileBag bag;
	private Board board;
	private Swapper swapper;
	private StartupPopUp ospu;
	private MousepadListener mouse;
	private NextMoveButton button;
	private static final int PORT = 2728;
	private Player player;
	private Client local_client;
	private boolean paused = true;
	private boolean start_game = false;
	private boolean tile_addition_required = false;
	private boolean board_update_available = false;
	private boolean board_update_for_this_player;
	private boolean rewrite_old_tiles;
	private boolean pickup_tiles;
	private Server server;
	private String[] tiles_to_add;
	private String coordinates;
	
	public Controller() {
		// VIEW ELEMENTS:
		this.mouse = new MousepadListener(this);
		this.swapper = new Swapper(this);
		this.button = new NextMoveButton(this);
		
		// LOAD LOCAL BOARD TO DISPLAY AND TILE BAG TO USE
		try {
			this.board = Board.createBoard(PATH+"special_fields.txt");
			this.bag = TileBag.createTileBag(PATH+"complete_tile_list.txt");
		} catch (IOException e) {
			System.out.println("Error: files special_fields.txt or complete_tile_list.txt failed loading!");
		}
		
		// START GAME MENU
		this.ospu = new StartupPopUp(this);
	}
	
	public void createGame(String name) {
		server = Server.createServer(PORT);
		joinGame(name);
	}

	public void joinGame(String input) {
		local_client = Client.createClient(input, "localhost", PORT, this);
		player = new LocalHumanPlayer(input);
	}
	
	public void notifyReady() {
		local_client.ready();
	}
	
	public void startGame() {
		start_game = true;
	}
	
	public void swap(String tileNames) {
		System.out.println("swapper clicked!");
		local_client.sendMessage(Protocol.PASS + Protocol.SEPARATOR + tileNames);
		pushTilePickup();
		setPaused(true);
	}
	
	public void skipMove() {
		System.out.println("skip clicked!");
		local_client.sendMessage(Protocol.PASS);
		pushTilePickup();
		setPaused(true);
	}
	
	public void doMove() {
		if (board.hasValidPlacement()) {	// placement is valid
			if (board.hasBlankTiles()) {	// choose letters for blank tiles
				List<Tile> bt = board.getBlankTiles();
				for(int i = 0; i<bt.size(); i++) {
					new BlankTilePopUp(bt.get(i), this);
				}
			}else {											// no blank tiles present
				finishMove();
			}
		}else {
			System.out.println("non-valid placement");
		}
	}
	
	public void finishMove() {
		// EXTRACT MOVE AND SEND TO SERVER!
		local_client.sendMessage(Protocol.MOVE + Protocol.SEPARATOR + player.getName() + Protocol.SEPARATOR + board.extractMove());		
		// probably have to remove the tiles now, and wait for the server response.
		setPaused(true);
	}
	
	// -- PUSH COMMANDS AND THEIR EXECUTOR METHODS --
	
	public void pushTilesToAdd(String[] tileNames, boolean rewriteTiles) {
		rewrite_old_tiles = rewriteTiles;
		tiles_to_add = tileNames;
		tile_addition_required = true;
	}
	
	public void pushBoardUpdate(String c, boolean thisPlayer) {
		coordinates = c;
		board_update_for_this_player = thisPlayer;
		board_update_available = true;
	}
	
	public void pushTilePickup() {
		pickup_tiles = true;
	}
	
	private void addTiles(String[] tileNames, boolean rewriteTiles) {
		if(rewriteTiles) {
			while(player.getTray().getTiles().size() > 0){
				bag.addTile(player.getTray().getTiles().get(0));
				player.getTray().removeTile(player.getTray().getTiles().get(0));
			}
		}
		swapper.emptySwapper(bag);
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < tileNames.length; i++) {
			tiles.add(bag.getTileByName(tileNames[i]));
		}
		player.getTray().addTiles(tiles);
	}
	
	public void updateBoard(String c, boolean thisPlayer) {
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
		if(!thisPlayer) {
			board.placeTiles(pos, tileNames, bag);
		}
		board.executeMove();
	}
	
	// -- OTHER COMMANDS --
	
	public static void addToRoot(Node n) {
		Scrabble.root.getChildren().add(n);
	}
	
	public void setPaused(boolean p) {
		paused = p;
	}
	
	public void update() {
		if (!paused) {
			mouse.update();
		}
		if (start_game) {
			setPaused(true);
			board.initGraphics();
			player.getTray().initGraphics();
			
			ospu.remove();
			swapper.initGraphics();
			button.initGraphics();
			mouse.initGraphics(); // Always last!
			
			start_game = false;
		}
		if(pickup_tiles) {
			player.getTray().addTiles(board.pickupNonFixed());
			pickup_tiles = false;
		}
		if (tile_addition_required) {
			addTiles(tiles_to_add, rewrite_old_tiles);
			tile_addition_required = false;
		}
		if (board_update_available) {
			updateBoard(coordinates, board_update_for_this_player);
			board_update_available = false;
		}
	}
	
	public void onMouseClick(int mouseX, int mouseY) {
		if(!paused) {
			Field f = hasClickedField((int)(mouseX-Scrabble.root.getWidth()/2), (int)(mouseY-Scrabble.root.getHeight()/2));
	        if (f != null) {	// IF A TILE WAS CLICKED
	        	
	        	if (mouse.getTile() == null) {		// no tile picked up yet
	            	if (f.hasTile() && !f.getTile().isFixed()) {
	            		mouse.setTile(f.getTile());
	            		f.setTile(null);
	            	}
	        	}
	        	else {								// tile being carried already
	        		if (!f.hasTile()) {
	            		f.setTile(mouse.getTile());
	            		mouse.setTile(null);
	            		button.update(board); // updates the mode of the next-move button
	            	}
	        	}
	        }
		}
	}
	
	public Field hasClickedField(int mouse_x, int mouse_y) {
    	Field f = player.getTray().hasClickedField(mouse_x, mouse_y);
    	if (f == null) {
    		f = board.hasClickedField(mouse_x, mouse_y);
    		if (f == null) {
        		f = swapper.hasClickedField(mouse_x, mouse_y);
        	}
    	}
    	return f;
    }

	public void exit() {
		if (local_client != null) {local_client.shutdown();}
		if (server != null) {server.shutdown();}
	}
	
}
