package model;

import Main.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import model.Field.FieldType;
import view.VisualsManager;

public class Swapper {

	private Group sprite;
	private Button btn;
	private Field field = new Field(FieldType.NORMAL);
	
	public Swapper(Controller c) {
		this.sprite = VisualsManager.getSwapper();
		
		this.btn = new Button("SWAP");
        btn.setOnAction(new EventHandler<ActionEvent>() {   
            @Override  
            public void handle(ActionEvent arg0) {
            	if (field.hasTile()) {
            		c.swap();
            	}
            }
        } );
        btn.setTranslateX(Field.FIELD_SIZE/4);
        btn.setTranslateY(Field.FIELD_SIZE/2);
        sprite.getChildren().add(btn);
	}
	
	public void addTile(Tile t) {
		field.setTile(t);
	}
	
	public void removeTile() {
		field.setTile(null);
	}
	
	public Field hasClickedField(int mouse_x, int mouse_y) {
		if (field.wasClicked(mouse_x, mouse_y)) {
			return field;
		}
		return null;
	}
	
	public void swap(TileBag bag) {
		if (!bag.isEmpty() && field.hasTile()) {
			Tile new_tile = bag.exchangeTile(field.getTile());
			field.setTile(null);
			field.setTile(new_tile);
		}
	}
	
	public void initGraphics() {
        sprite.getChildren().add(field.updateGraphics((int)(2*Field.FIELD_SIZE), Field.FIELD_SIZE/2, (int)(-9.5*Field.FIELD_SIZE), Field.FIELD_SIZE*8));
	    
		Controller.addToRoot(sprite);
	}

	public void setDisabled(boolean active) {
		btn.setDisable(active);
	}	
}
