package model;

import javafx.scene.Group;
import javafx.scene.Node;
import view.VisualsManager;

public class Field {
	
	private Group sprite;
	private int hitbox_x, hitbox_y;
	public static final int FIELD_SIZE = 32;	// size in px of one field
	public static final int FIELD_B_WIDTH = 2;	// width in px of field borders

	public static enum FieldType {
		NORMAL,
		TRIPLE_WS,
		DOUBLE_WS,
		TRIPLE_LS,
		DOUBLE_LS
	}
	
	private FieldType type;
	private Tile tile;
	
	public Field(FieldType t) {
		this.type = t;
		this.sprite = VisualsManager.getField(t);
	}
	
	public void setTile(Tile t) {
		if (t != null) {
			sprite.getChildren().add(t.getGraphics());
		}else {
			sprite.getChildren().remove(this.tile.getGraphics());
		}
		this.tile = t;
	}
	
	public Field deepCopy() {
		Field f = new Field(this.getFieldType());
		f.setTile(tile);
		return f;
	}
	
	public boolean wasClicked(int mouse_x, int mouse_y) {
		if (this.hitbox_x < mouse_x && this.hitbox_x + FIELD_SIZE > mouse_x) {
			if (this.hitbox_y < mouse_y && this.hitbox_y + FIELD_SIZE > mouse_y) {
				
				System.out.println("Field was clicked!");
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTile() {
		if (tile != null) {
			return true;
		}
		return false;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public FieldType getFieldType() {
		return type;
	}
	
	public void setFieldType(FieldType t) {
		type = t;
	}
	
	public Node updateGraphics(int x, int y, int root_zero_x, int root_zero_y){
	    sprite.setTranslateX(x); hitbox_x = root_zero_x + x;
	    sprite.setTranslateY(y); hitbox_y = root_zero_y + y;
	    System.out.println("Hitbox_x: "+hitbox_x+" Hitbox_y: "+hitbox_y);
	    
	    return sprite;
	}
}
