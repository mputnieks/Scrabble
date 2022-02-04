package model;

import java.util.ArrayList;
import java.util.List;

import Main.Controller;
import javafx.scene.Group;
import javafx.scene.Node;
import networking.Protocol;
import view.VisualsManager;

/**
 * Class <code>TileTray</code> for storing and manipulating a line of tiles.
 * @invariant MAX_TILE_COUNT = 7 (how many tiles the <code>TileTray</code> can accomodate.)
 */
public class TileTray {

	private Group sprite;
	private int FIELD_COUNT;		// how many empty fields does the tray have?
	private static final int MAX_TILE_COUNT = 7;	// how many tiles can the tray host?
	private Field[] fields;
	private int tx, ty;
	
	
	/**
    * Creates a <code>TileTray</code> with the given <code>field_count</code> as well as its TranslateX <code>tx</code> and TranslateY <code>ty</code> relative to root.
    * @param field_count the amount of fields this tray will have.
    * @param tx the TranslateX of this trays GUI representation.
    * @param ty the TranslateY of this trays GUI representation.
    * @requires field_count > 6
    */
	public TileTray(int field_count, int tx, int ty) {
		this.tx = tx; this.ty = ty;
		this.FIELD_COUNT = field_count;
		this.fields = new Field[FIELD_COUNT];
		for(int i = 0; i < FIELD_COUNT; i++) {
			fields[i] = new Field(Field.FieldType.NORMAL);
		}
		this.sprite = VisualsManager.getTileTray(FIELD_COUNT, tx, ty);
	}
	
	/**
     * Returns a list of type <code>Tile</code> containing all tiles placed in this tray.
     * @return a <code>List</code> containing the tiles that the <code>TileTray</code> currently hosts.
     */
	public List<Tile> getTiles() {
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i<fields.length; i++) {
			if (fields[i].hasTile()) {
				tiles.add(fields[i].getTile());
			}
		}
		return tiles;
	}
	
	/**
     * Adds this <code>List</code> of type <code>Tile</code> to the <code>TileTray<Tile>.
     * @param t <code>List</code> of <code>Tile</code> objects to add.
     * @ensures every <code>Tile</code> of this <code>List</code> will be added to the <code>TileTray</code> until the tray is full (<code>MAX_TILE_COUNT</code> is reached).
     */
	public void addTiles(List<Tile> t) {
		for (int i = 0; i < t.size(); i++) {
			addTile(t.get(i));
		}
	}
	
	/**
     * Adds this <code>Tile</code> to the <code>TileTray<Tile>.
     * @param t <code>Tile</code> to add.
     * @ensures <code>Tile</code> will be added to the <code>TileTray</code> if the <code>MAX_TILE_COUNT</code> is not reached.
     */
	public void addTile(Tile t) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].getTile() == null) {
				addTile(t, i);
				break;
			}
		}
	}
	
	/**
     * Adds this <code>Tile</code> to the <code>TileTray<Tile> at the given position.
     * @param t <code>Tile</code> to add.
     * @param i position indicating the <code>Field</code> to add the <code>Tile</code> at.
     * @ensures <code>Tile</code> will be added to the <code>TileTray</code> if the <code>MAX_TILE_COUNT</code> is not reached.
     * @requires i smaller than <code>FIELD_COUNT</code>
     */
	public void addTile(Tile t, int i) {
		fields[i].setTile(t);
	}
	
	/**
     * Produces a textual representation of the <code>Tile</code> objects in this <code>TileTray<Tile>.
     * @return a <code>String</code> representation of the present <code>Tile</code> objects compliant to the network protocol.
     */
	public String tilesToString() {
		List<Tile> tiles = getTiles();
    	String str = "";
    	for (int i = 0; i < tiles.size(); i++) {
    		str += tiles.get(i).getName() + Protocol.AS;
    	}
    	return str;
    }
	
	/**
     * Fills this <code>TileTray</code> taking tiles from the <code>TileBag</code>.
     * @ensures This <code>TileTray<Tile> will be filled until the <code>MAX_TILE_COUNT</code> is reached. Every <code>Tile</code> added will be removed from the given <code>TileBag</code>.
     * @param bag The <code>TileBag</code> to take <code>Tile</code> objects from.
     */
	public void fill(TileBag bag) {
		while(getTiles().size() < MAX_TILE_COUNT && !bag.isEmpty()) {
			System.out.println("filling tray!");
			addTile(bag.getRandomTile());
		}
	}
	
	/**
     * Removes this <code>Tile</code> from this <code>TileTray</code>.
     * @ensures This <code>Tile<Tile> will be removed from the <code>TileTray</code>.
     * @param t The <code>Tile</code> to remove.
     */
	public void removeTile(Tile t) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].hasTile() && fields[i].getTile().equals(t)) {
				fields[i].setTile(null);
			}
		}
	}
	
	/**
     * Returns the <code>Field</code> that is part of this <code>TileTray</code> if it was clicked.
     * @param mouse_x The x position where the click occurred.
     * @param mouse_y The y position where the click occurred.
     * @return <code>Field</code> if a <code>Field</code> of this <code>TileTray</code> was clicked
     * or <code>null</code> if no field was clicked.
     */
	public Field hasClickedField(int mouse_x, int mouse_y) {
		for(int i = 0; i < fields.length; i++) {
			if (fields[i].wasClicked(mouse_x, mouse_y)) {
				return fields[i];
			}
		}
		return null;
	}
	
	/**
     * Adds this the graphical representation of this <code>TileTray<Tile> to the main <code>Panel</code> of the application.
     * @ensures The graphical representation of this <code>TileTray<Tile> will be added to the main <code>Panel</code> of the application.
     */
	public void initGraphics() {
		Controller.addToRoot(getGraphics());
	}
	
	/**
     * creates/updates and returns the GUI representation of this <code>TileTray<Tile>.
     * @return The GUI representation of this <code>TileTray<Tile>.
     */
	public Node getGraphics() {
	    for(int x = 0; x < FIELD_COUNT; x++) {
	    	sprite.getChildren().add(fields[x].updateGraphics((int)((x+0.5)*Field.FIELD_SIZE), Field.FIELD_SIZE/2, tx-(FIELD_COUNT+1)*Field.FIELD_SIZE/2, ty-1*Field.FIELD_SIZE));
		}
	    return sprite;
	}

	/**
     * Picks up the <code>Tile<Tile> objects of this <code>TileTray<Tile> removing them from it.
     * @return The <code>Tile<Tile> objects of this <code>TileTray<Tile>.
     */
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
