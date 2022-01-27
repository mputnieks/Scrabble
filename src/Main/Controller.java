package Main;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import model.Field;
import model.LocalHumanPlayer;
import model.NextMoveButton;
import model.Player;
import model.Swapper;
import model.TileTray;
import view.MousepadListener;
import view.VisualsManager;

public class Controller {
	
	private MousepadListener mouse;
	private Game currentgame;
	private Swapper swapper;
	private NextMoveButton button;
	
	public Controller() {
		// VIEW ELEMENTS:
		this.mouse = new MousepadListener(this);
		this.swapper = new Swapper(this);
		this.button = new NextMoveButton(this);
		// GAME
		List<Player> players = new ArrayList<Player>();
		players.add(new LocalHumanPlayer("Player_1", new TileTray()));
		currentgame = new Game(players);
		currentgame.start();
	}
	
	public void swap() {
		swapper.swap(currentgame.getTileBag());
		swapper.setDisabled(true);
	}
	
	public void skipMove() {	//should be a separate button I suppose...
		currentgame.getCurrentPlayer().getTray().addTiles(currentgame.getBoard().pickupNonFixed()); // there will never be any non-fixed tiles...
		currentgame.nextPlayer();
		swapper.setDisabled(false);
	}
	
	public void doMove() {
		if (currentgame.getBoard().hasValidPlacement()) {
			if(currentgame.getBoard().containsValidWords()) {
				currentgame.getCurrentPlayer().addToScore(currentgame.getBoard().executeMove());
				currentgame.getCurrentPlayer().getTray().fill(currentgame.getTileBag());
				currentgame.nextPlayer();
				button.update(currentgame.getBoard());
				swapper.setDisabled(false);
				
				System.out.println("score: " + currentgame.getCurrentPlayer().getScore());
			}else {
				skipMove();
			}
		}else {
			System.out.println("non-valid placement");
		}
	}
	
	public void initGraphics() {
		currentgame.getBoard().initGraphics();
		currentgame.getCurrentPlayer().getTray().initGraphics();
		swapper.initGraphics();
		button.initGraphics();
		
		mouse.initGraphics(); // Always last!
		
		Scrabble.root.setBackground(VisualsManager.getGameBackground());
	}
	
	public static void addToRoot(Node n) {
		Scrabble.root.getChildren().add(n);
	}
	
	public void update() {
		mouse.update();
	}
	
	public void onMouseClick(int mouseX, int mouseY) {
		System.out.println("click!");
        System.out.println("mouse_x: " + mouseX + " mouse_y: " + mouseY);
        
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
            		button.update(currentgame.getBoard()); // updates the mode of the next-move button
            	}
        	}
        }
	}
	
	public Field hasClickedField(int mouse_x, int mouse_y) {
    	Field f = currentgame.getCurrentPlayer().getTray().hasClickedField(mouse_x, mouse_y);
    	if (f == null) {
    		f = currentgame.getBoard().hasClickedField(mouse_x, mouse_y);
    		if (f == null) {
        		f = swapper.hasClickedField(mouse_x, mouse_y);
        	}
    	}
    	return f;
    }
}
