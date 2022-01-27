package model;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import Main.Controller;
import checker.InMemoryScrabbleWordChecker;
import checker.ScrabbleWordChecker;
import javafx.scene.Group;
import model.Field.FieldType;
import view.VisualsManager;

public class Board {
	private Field[][] fields;
	private static final int SIZE = 15;			// size in fields of the board
	
	public static Board createBoard(String filename) throws IOException {
		Field[][] f = new Field[SIZE][SIZE];
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				f[x][y] = new Field(Field.FieldType.NORMAL);
			}
		}
		
		Path filePath = new File(filename).toPath();
		Charset charset = Charset.defaultCharset();
		List<String> stringList = Files.readAllLines(filePath, charset);
		for (int i = 0; i<stringList.size(); i++) {
			String[] data = stringList.get(i).split(";"); //every data should have 4 members
			int x = Integer.parseInt(data[0])-1;
			int y = Integer.parseInt(data[1])-1;
			if (data[2].equals("W")) {
				if (data[3].equals("2")) {
					f[x][y] = new Field(Field.FieldType.DOUBLE_WS);
				}else {
					f[x][y] = new Field(Field.FieldType.TRIPLE_WS);
				}
			}else{
				if(data[3].equals("2")) {
					f[x][y] = new Field(Field.FieldType.DOUBLE_LS);
				}else {
					f[x][y] = new Field(Field.FieldType.TRIPLE_LS);
				}
			}
		}
		return new Board(f);
	}
	
	public Board(Field f[][]) { this.fields = f; }
	
	public List<Field> getFieldsWithTiles(){
		List<Field> f = new ArrayList<Field>();
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				if (fields[x][y].hasTile()) {
					f.add(fields[x][y]);
				}
			}
		}
		return f;
	}
	
	public List<Field> getFieldsNonFixed(){
		List<Field> with_tiles = getFieldsWithTiles();
		List<Field> with_fixed_tiles = new ArrayList<Field>();
		for(int i = 0; i < with_tiles.size(); i++) {
			if (!with_tiles.get(i).getTile().isFixed()) {
				with_fixed_tiles.add(with_tiles.get(i));
			}
		}
		return with_fixed_tiles;
	}
	
	public List<Tile> pickupNonFixed() {
		List<Field> f = getFieldsNonFixed();
		List<Tile> tiles = new ArrayList<Tile>();
		for(int i = 0; i< f.size(); i++) {
			tiles.add(f.get(i).getTile());
			f.get(i).setTile(null);
		}
		return tiles;
	}
	
	public boolean hasNonFixed() {
		if (getFieldsNonFixed().size() > 0) { return true; }
		return false;
	}
	
	public boolean placeTile(int x, int y, Tile tile) {
		if (!isValidField(x,y)) { return false; }
		fields[x][y].setTile(tile);
		return true;
	}
	
	public boolean isValidField(int x, int y) {
		if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
			return false;
		}
		return true;
	}
	
	public boolean isEmptyField(int x, int y) {
		if (isValidField(x,y) && fields[x][y].hasTile()) {
			return true;
		}
		return false;
	}
	
	public Field getField(int x, int y) {
		if (!isValidField(x,y)) { return null; }
		return fields[x][y];
	}
	
	public boolean hasValidPlacement() {
		
		// all placed tiles must be on one vertical or one horizontal, they can be interrupted by old placed tiles.
		// exception cases:
		// * there is only one placed tile
		// * there are no previously placed tiles on the board
		// * corner and side tiles!!
		// * on first move only placement that includes the center field is valid
		
		int total_non_fixed = getFieldsNonFixed().size();
		
		if (getFieldsWithTiles().size() - total_non_fixed == 0) {		// NO PREVIOUSLY PLACED TILES ON THE BOARD
			if(total_non_fixed == 1) {				// there is only one tile on the whole board
				return false;
			}
			else{									// there are only non-fixed tiles on the board
				if(fields[7][7].hasTile()) {
					int[] xy = getFirst();
					return continuityCheck(xy[0], xy[1], total_non_fixed, true); // activates the continuity checker
				}
				return false;
			}
		}else if (total_non_fixed == 1){			// there is only one non-fixed tile on the whole board
			int[] xy = getFirst();
			return hasFixedNearby(xy[0], xy[1]);	// check if connects to fixed tiles
		}
		else {
			int[] xy = getFirst();
			return continuityCheck(xy[0], xy[1], total_non_fixed, false);	// activates the continuity checker
		}
	}
	
	private int[] getFirst() {	// ADD NO NON-FIXED TILES EXCEPTION(?)
		int[] xy = new int[2];
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				if (fields[x][y].hasTile() && !fields[x][y].getTile().isFixed()) { // found the first non-fixed tile (most in the upper-left corner)
					xy[0] = x; xy[1] = y;
					return xy;
				}
			}
		}
		// throw exception!
		return xy;
	}
	
	private boolean hasFixedNearby(int x,int y) {
		if (isValidField(x+1,y) && getField(x+1,y).hasTile() && getField(x+1,y).getTile().isFixed()) {
			return true;
		}else if(isValidField(x-1,y) && getField(x-1,y).hasTile() && getField(x-1,y).getTile().isFixed()){
			return true;
		}else if(isValidField(x,y+1) && getField(x,y+1).hasTile() && getField(x,y+1).getTile().isFixed()) {
			return true;
		}else if(isValidField(x,y-1) && getField(x,y-1).hasTile() && getField(x,y-1).getTile().isFixed()) {
			return true;
		}
		return false;
	}
	
	private String getDirection(int x_f, int y_f) {
		System.out.println("x: " + x_f + " y: " + y_f);
		for(int x = x_f+1; x < SIZE; x++) {
			if (fields[x][y_f].hasTile() && !fields[x][y_f].getTile().isFixed()) {
				return "HOR";
			}
		}
		for(int y = y_f+1; y < SIZE; y++) {
			if (fields[x_f][y].hasTile() && !fields[x_f][y].getTile().isFixed()) {
				return "VER";
			}
		}
		for(int x = x_f-1; x < SIZE  && x>0; x++) {
			if (fields[x][y_f].hasTile()) {
				return "HOR";
			}
		}
		for(int y = y_f-1; y < SIZE && y>0; y++) {
			if (fields[x_f][y].hasTile()) {
				return "VER";
			}
		}
		return null;	// should be an impossible case
	}
	
	private int scanWord(int x_f, int y_f, String dir, boolean recursive) {
		boolean continues = true;
		int x = x_f; int y = y_f;
		do {							// goes to the start of the word
			if (dir.equals("VER")) {
				y--;
			}else {
				x--;
			}
			if (y!=-1 && x!=-1) {
				if(fields[x][y].hasTile()) {
					x_f = x; y_f = y;
				}else {
					continues = false;
				}
			}else {
				continues = false;
			}
		}while(continues);
		
		int score = 0;
		int other_word_scores = 0;
		int word_multiplier = 1;
		x = x_f; y = y_f;
		continues = true;
		do {							// goes through the word counting score
			if (y != SIZE && x != SIZE) {
				if(fields[x][y].hasTile()) {
					
					if (fields[x][y].getTile().isFixed()) {
						score += fields[x][y].getTile().getValue();
					}else if(fields[x][y].getFieldType() == FieldType.NORMAL) {
						score += fields[x][y].getTile().getValue();
					}else if (fields[x][y].getFieldType() == FieldType.DOUBLE_LS) {
						score += 2*fields[x][y].getTile().getValue();
					}else if (fields[x][y].getFieldType() == FieldType.TRIPLE_LS) {
						score += 3*fields[x][y].getTile().getValue();
					}else if(fields[x][y].getFieldType() == FieldType.DOUBLE_WS) {
						System.out.println("double word");
						word_multiplier = word_multiplier * 2;
						score += fields[x][y].getTile().getValue();
					}else if (fields[x][y].getFieldType() == FieldType.TRIPLE_WS) {
						System.out.println("triple word");
						word_multiplier = word_multiplier * 3;
						score += fields[x][y].getTile().getValue();
					}
					
					if (dir.equals("VER")) {
						
						if(isValidField(x-1, y) && !fields[x][y].getTile().isFixed() && recursive && (fields[x-1][y].hasTile() || fields[x+1][y].hasTile() )) {
							other_word_scores += scanWord(x, y, "HOR", false);
						}
						
						y++;
					}else {
						
						if(isValidField(x, y-1) && !fields[x][y].getTile().isFixed() && recursive && (fields[x][y-1].hasTile() || fields[x][y+1].hasTile() )) {
							other_word_scores += scanWord(x, y, "VER", false);
						}
						
						x++;
					}
					
				}else {
					continues = false;
				}
			}else {
				continues = false;
			}
		}while(continues);
		score = score*word_multiplier + other_word_scores;
		return score;
	}
	
	
	private boolean continuityCheck(int x, int y, int total, boolean isFirstMove) {
		
		String dir = getDirection(x, y);
		if(dir == null) {
			return false;
		}
		System.out.println(dir);
		boolean continuous = true;
		boolean touching = false;
		int i_x = x; int i_y = y;
		
		if(hasFixedNearby(i_x, i_y) || isFirstMove) {
			touching = true;
		}
		
		boolean exit = false;
		i_x = x; i_y = y;
		int non_fixed_scanned = 1;
		do {	// scanning forward (hor or ver depending on dir) (both fixed and non-fixed tiles can be here)
			if (dir.equals("VER")) {
				i_y++;
			}else {
				i_x++;
			}
			
			System.out.println("i_x: " + i_x + " i_y: " + i_y);
			if(!getField(i_x, i_y).hasTile()) {
				System.out.println("continuity broken");
				continuous = false;
				exit = true;
			}else if (!getField(i_x, i_y).getTile().isFixed()){
				non_fixed_scanned++;
				System.out.println("n-f-scanned: " + non_fixed_scanned);
				if(hasFixedNearby(i_x, i_y)) {
					touching = true;
				}
			}else {
				touching = true;
			}
			
			if (non_fixed_scanned == total) {
				exit = true;
			}
			
		}while(!exit);
		
		System.out.println("cont: "+continuous+" touch: "+touching);
		if (continuous && touching) {
			return true;
		}
		return false;
	}
	
	public boolean containsValidWords() {
		int xy[] = getFirst();
		List<String> words = getPresentWords(xy[0], xy[1], getDirection(xy[0], xy[1]), true);
		ScrabbleWordChecker inMemoryChecker = new InMemoryScrabbleWordChecker();
		
		for (int i = 0; i < words.size(); i++) {
			ScrabbleWordChecker.WordResponse response = inMemoryChecker.isValidWord(words.get(i));
	        if(response == null) {
	            System.out.println("The word \"" + words.get(i) + "\" is not known in the dictionary!");
	            return false;
	        }
	        else {
	            System.out.println(response);
	        }
		}
		
		return true;
	}
	
	public List<String> getPresentWords(int x_f, int y_f, String dir, boolean recursive) {
		boolean continues = true;
		int x = x_f; int y = y_f;
		do {							// goes to the start of the word
			if (dir.equals("VER")) {
				y--;
			}else {
				x--;
			}
			if (y!=-1 && x!=-1) {
				if(fields[x][y].hasTile()) {
					x_f = x; y_f = y;
				}else {
					continues = false;
				}
			}else {
				continues = false;
			}
		}while(continues);
		
		String word = "";
		List<String> words = new ArrayList<String>();
		x = x_f; y = y_f;
		continues = true;
		do {							// goes through the word collecting it
			if (y != SIZE && x != SIZE) {
				if(fields[x][y].hasTile()) {
					word += fields[x][y].getTile().getName();
					if (dir.equals("VER")) {
						if(isValidField(x-1, y) && !fields[x][y].getTile().isFixed() && recursive && (fields[x-1][y].hasTile() || fields[x+1][y].hasTile() )) {
							words.add(getPresentWords(x, y, "HOR", false).get(0));
						}
						
						y++;
					}else {
						if(isValidField(x, y-1) && !fields[x][y].getTile().isFixed() && recursive && (fields[x][y-1].hasTile() || fields[x][y+1].hasTile() )) {
							words.add(getPresentWords(x, y, "VER", false).get(0));
						}
						
						x++;
					}
					
				}else {
					continues = false;
				}
			}else {
				continues = false;
			}
		}while(continues);
		words.add(word);
		return words;
	}
	
	public int executeMove() {
		int xy[] = getFirst();
		int score = scanWord(xy[0], xy[1], getDirection(xy[0], xy[1]), true);
		List<Field> f = getFieldsNonFixed();
		if (f.size() == 7) {score+=50;}
		for (int i = 0; i<f.size(); i++) {
			f.get(i).getTile().fixTile();
		}
		return score;
	}
	
	public Board deepCopy() {
		Field[][] fields_copy = new Field[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				fields_copy[i][j] = fields[i][j].deepCopy();
			}
		}
		return new Board(fields_copy);
	}
	
	public void initGraphics() {
		int border_size = Field.FIELD_SIZE;
		Group group = VisualsManager.getBoard(border_size);
	    
	    for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				group.getChildren().add(fields[x][y].updateGraphics(x*Field.FIELD_SIZE+border_size, y*Field.FIELD_SIZE+border_size, (int)(-8.5*Field.FIELD_SIZE), -10*Field.FIELD_SIZE));
			}
		}
	    
		Controller.addToRoot(group);
	}

	public Field hasClickedField(int mouse_x, int mouse_y) {
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				if (fields[x][y].wasClicked(mouse_x, mouse_y)) {
					return fields[x][y];
				}
			}
		}
		return null;
	}
}