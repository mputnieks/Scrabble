package model;

import Main.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import view.VisualsManager;

public class Swapper {

	private Group sprite;
	private Button btn;
	private TileTray tray = new TileTray(1, -Field.FIELD_SIZE*8, Field.FIELD_SIZE*9);
	
	public Swapper(Controller c) {
		this.sprite = VisualsManager.getSwapper();
		
		this.btn = new Button("SWAP");
        btn.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	if (tray.getTiles().size() > 0) {
            		c.swap(tray.tilesToString());
            	}
            }
        } );
        btn.setTranslateX(Field.FIELD_SIZE/4);
        btn.setTranslateY(Field.FIELD_SIZE/2);
        sprite.getChildren().add(btn);
	}
	
	public TileTray getTray() {
		return tray;
	}
	
	public void addTile(Tile t) {
		tray.addTile(t);
	}
	
	public void removeTile(Tile t) {
		tray.removeTile(t);
	}
	
	public Field hasClickedField(int mouse_x, int mouse_y) {
		return tray.hasClickedField(mouse_x, mouse_y);
	}
	
	public void emptySwapper(TileBag bag) {
		bag.addTiles(tray.getTiles());
		while(tray.getTiles().size()>0){
			tray.removeTile(tray.getTiles().get(0));
		}
	}
	
	public void initGraphics() {
	    Node tiletray = tray.getGraphics();
	    Controller.addToRoot(sprite);
        Controller.addToRoot(tiletray);
	}
}