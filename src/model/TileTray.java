package model;

import java.util.ArrayList;
import java.util.List;

import Main.Controller;
import javafx.scene.Group;
import view.VisualsManager;

public class TileTray {

	private Group sprite;
	private static final int FIELD_COUNT = 10;		// how many empty fields does the tray have?
	private static final int MAX_TILE_COUNT = 7;	// how many tiles can the tray host?
	private Field[] fields = new Field[10];
	
	public TileTray() {
		for(int i = 0; i < FIELD_COUNT; i++) {
			fields[i] = new Field(Field.FieldType.NORMAL);
		}
		this.sprite = VisualsManager.getTileTray(FIELD_COUNT);
	}
	
	public List<Tile> getTiles() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i<fields.length; i++) {
			if (fields[i].hasTile()) {
				tiles.add(fields[i].getTile());
			}
		}
		return tiles;
	}
	
	public void addTiles(List<Tile> t) {
		for (int i = 0; i < t.size(); i++) {
			addTile(t.get(i));
		}
	}
	
	public void addTile(Tile t) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].getTile() == null) {
				addTile(t, i);
				break;
			}
		}
	}
	
	public void addTile(Tile t, int i) {
		fields[i].setTile(t);
	}
	
	public void fill(TileBag bag) {
		while(getTiles().size() < MAX_TILE_COUNT && !bag.isEmpty()) {
			System.out.println("filling tray!");
			addTile(bag.getRandomTile());
		}
	}
	
	public void removeTile(Tile t) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].getTile().equals(t)) {
				fields[i].setTile(null);
			}
		}
	}
	
	public Field hasClickedField(int mouse_x, int mouse_y) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].wasClicked(mouse_x, mouse_y)) {
				return fields[i];
			}
		}
		return null;
	}
	
	public void initGraphics() {
	    
	    for(int x = 0; x < FIELD_COUNT; x++) {
	    	sprite.getChildren().add(fields[x].updateGraphics((int)((x+0.5)*Field.FIELD_SIZE), Field.FIELD_SIZE/2, (FIELD_COUNT+1)*Field.FIELD_SIZE/-2, 8*Field.FIELD_SIZE));
		}
	    
		Controller.addToRoot(sprite);
	}
	
}
