package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import Main.Scrabble;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Field;
import model.Field.FieldType;


// A more compact construction of a rectangle:
// Rectangle(double x, double y, double width, double height);

public class VisualsManager {
	
	private static final String PATH = "./files/"; // Path to the test folder with special tile mappings

	public static Node getTile(String name) {
	    Rectangle shape = new Rectangle();
	    shape.setWidth(Field.FIELD_SIZE-6);
	    shape.setHeight(Field.FIELD_SIZE-6);
	    shape.setFill(ColorManager.getColor("tiles"));
	    shape.setArcHeight(10);
	    shape.setArcWidth(10);
	    Group group = new Group(shape);
	    
	    Label label = new Label(name);
	    label.setTextFill(Color.BLACK);
		label.setTranslateX(9);
	    label.setTranslateY(5);
	    
	    group.getChildren().add(label);
	    group.setTranslateX(2);
	    group.setTranslateY(2);
	    return group;
	}

	public static Group getField(FieldType t) {
	    Rectangle shape = new Rectangle();
	    shape.setWidth(Field.FIELD_SIZE-Field.FIELD_B_WIDTH);
	    shape.setHeight(Field.FIELD_SIZE-Field.FIELD_B_WIDTH);
	    shape.setFill(ColorManager.getFillColor(t));
	    shape.setStrokeWidth(Field.FIELD_B_WIDTH);
	    shape.setStroke(Color.web("988255",1.0));
	    Group group = new Group(shape);
	    
	    Label label = new Label(" ");
	    switch(t) {
	      case DOUBLE_LS:
	    	  label = new Label("2xLS");
	        break;
	      case TRIPLE_WS:
	    	  label = new Label("3xWS");
	        break;
	      case DOUBLE_WS:
	    	  label = new Label("2xWS");
	        break;
	      case TRIPLE_LS:
	    	  label = new Label("3xLS");
	        break;
	    }
	    label.setTextFill(Color.WHITE);
	    label.setFont(Font.font("Verdana", FontWeight.BOLD, 8));
	    label.setTranslateX(2);
	    label.setTranslateY(8);
	    group.getChildren().add(label);
		
		return group;
	}
	
	public static Group getTileTray(int FIELD_COUNT) {
		Rectangle shape = new Rectangle();
	    shape.setWidth((FIELD_COUNT+1)*Field.FIELD_SIZE);
	    shape.setHeight(2*Field.FIELD_SIZE);
	    shape.setFill(ColorManager.getColor("board_borders"));
	    Group group = new Group(shape);
	    group.setTranslateY(Field.FIELD_SIZE*9);
	    
		return group;
	}
	
	public static Group getBoard(int border_size) {
		//Drawing a Rectangle
	    Rectangle shape = new Rectangle();
	    //Setting the properties of the rectangle
	    shape.setWidth(17*Field.FIELD_SIZE);
	    shape.setHeight(17*Field.FIELD_SIZE);
	    //Setting other properties
	    shape.setFill(Color.web("091732",1.0));
	    shape.setStrokeWidth(border_size);
	    shape.setStroke(Color.web("2C100C",1.0));
	    //Setting the Scene
	    Group group = new Group(shape);
	    group.setTranslateY(-Field.FIELD_SIZE*1.5);
	    
	    return group;
	}

	public static Group getSwapper() {
		Rectangle shape = new Rectangle();
	    shape.setWidth(3.5*Field.FIELD_SIZE);
	    shape.setHeight(2*Field.FIELD_SIZE);
	    shape.setFill(ColorManager.getColor("board_borders"));
	    Group group = new Group(shape);
	    group.setTranslateX(-Field.FIELD_SIZE*8);
	    group.setTranslateY(Field.FIELD_SIZE*9);
	    
		return group;
	}

	public static Group getNextMoveButton() {
		Rectangle shape = new Rectangle();
	    shape.setWidth(2.5*Field.FIELD_SIZE);
	    shape.setHeight(2*Field.FIELD_SIZE);
	    shape.setFill(ColorManager.getColor("board_borders"));
	    Group group = new Group(shape);
	    group.setTranslateX(+Field.FIELD_SIZE*8);
	    group.setTranslateY(Field.FIELD_SIZE*9);
	    
		return group;
	}
	
	public static Background getGameBackground() {
		try {
			FileInputStream inp = new FileInputStream(PATH+"wood_floor_1.jpg");
			//image creation
			Image im = new Image(inp);
			// create a background image
			BackgroundImage bi = new BackgroundImage(im,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER,
			new BackgroundSize(1200,700,true,true,true,true));
			// Background creation
			Background bg = new Background(bi);
			// set background
			return bg;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}