package model;

import java.util.ArrayList;
import java.util.List;

import Main.Controller;
import javafx.scene.Group;
import javafx.scene.Node;
import networking.Protocol;
import view.VisualsManager;

public class TileTray {

	private Group sprite;
	private int FIELD_COUNT;		// how many empty fields does the tray have?
	private static final int MAX_TILE_COUNT = 7;	// how many tiles can the tray host?
	private Field[] fields;
	private int tx, ty;
	
	public TileTray(int field_count, int tx, int ty) {
		this.tx = tx; this.ty = ty;
		this.FIELD_COUNT = field_count;
		this.fields = new Field[FIELD_COUNT];
		for(int i = 0; i < FIELD_COUNT; i++) {
			fields[i] = new Field(Field.FieldType.NORMAL);
		}
		this.sprite = VisualsManager.getTileTray(FIELD_COUNT, tx, ty);
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
	
	public String tilesToString() {
		List<Tile> tiles = getTiles();
    	String str = "";
    	for (int i = 0; i < tiles.size(); i++) {
    		str += tiles.get(i).getName() + Protocol.AS;
    	}
    	return str;
    }
	
	public void fill(TileBag bag) {
		while(getTiles().size() < MAX_TILE_COUNT && !bag.isEmpty()) {
			System.out.println("filling tray!");
			addTile(bag.getRandomTile());
		}
	}
	
	public void removeTile(Tile t) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].hasTile() && fields[i].getTile().equals(t)) {
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
		Controller.addToRoot(getGraphics());
	}
	
	public Node getGraphics() {
	    for(int x = 0; x < FIELD_COUNT; x++) {
	    	sprite.getChildren().add(fields[x].updateGraphics((int)((x+0.5)*Field.FIELD_SIZE), Field.FIELD_SIZE/2, tx-(FIELD_COUNT+1)*Field.FIELD_SIZE/2, ty-1*Field.FIELD_SIZE));
		}
	    return sprite;
	}

	public List<Tile> pickupNonFixed() {
		List<Tile> tiles = new ArrayList<Tile>();
		for(int i = 0; i< fields.length; i++) {
			if(fields[i].hasTile()) {
				tiles.add(fields[i].getTile());
				fields[i].setTile(null);
			}
		}
		return tiles;
	}
	
}
