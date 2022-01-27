package view;

import javafx.scene.paint.Color;
import model.Field.FieldType;

/* ------ COLOR SCHEME -------
 * GUI ELEMENT		- HEX CODE
 * 
 * board_borders	- 2C100C
 * board_bg			- 091732
 * field background - EAE6E3
 * field borders	- 988255
 * tiles			- B69B85 (+ wood pattern?)
 * 2x letter score	- 7885B1
 * 3x letter score	- 282B61
 * 2x word score	- 911E2D
 * 3x word score	- 5E1025
 * 
 * + wooden table background picture
 * 
 */

public class ColorManager {
	
	private static Color board_borders = Color.web("2C100C",1.0);
	private static Color board_bg = Color.web("091732",1.0);
	private static Color tiles = Color.web("B69B85",1.0);
	private static Color field_borders = Color.web("988255",1.0);

	public static Color getColor(String name){
		Color c;
		switch(name) {
	      case "board_borders":
	    	  c = board_borders;
	        break;
	      case "board_bg":
	    	  c = board_bg;
	        break;
	      case "tiles":
	    	  c = tiles;
	        break;
	      case "field_borders":
	    	  c = field_borders;
	        break;
	      default:
	    	  c = Color.WHITE;
	    	break;
		}
		return c;
	}
	
	private static Color triple_ls = Color.web("232A5E",1.0);
	private static Color double_ls = Color.web("6E7BA6",1.0);
	private static Color triple_ws = Color.web("5E1025",1.0);
	private static Color double_ws = Color.web("911E2D",1.0);
	private static Color field_normal = Color.web("EAE6E3",1.0);
	
	public static Color getFillColor(FieldType type){
		
		Color c;
		switch(type) {
	      case DOUBLE_LS:
	    	  c = double_ls;
	        break;
	      case TRIPLE_WS:
	    	  c =  triple_ws;
	        break;
	      case DOUBLE_WS:
	    	  c = double_ws;
	        break;
	      case TRIPLE_LS:
	    	  c = triple_ls;
	        break;
	      default:	//NORMAL
	    	  c = field_normal;
	    	break;
		}
		return c;
	}
	
}