package model;

public class LocalHumanPlayer extends Player {

    public LocalHumanPlayer(String name, TileTray tray) {
    	super(name, tray);
    }
    
    @Override
    public String determineMove(Board board) {
//	    printHelpMenu();
//	    System.out.println("> " + getName() + ", what is your move? ");
//	    
//	    boolean valid = false;
//	    while (!valid) {
//	    	valid = playerTUI(board);
//  		if(!valid) {	// if not skipping
//				if(board.isValidMove() && board.containsValidWord()){ // if well placed word present
//					valid = true;
//				}
//				else {
//					System.out.println("Sorry, yet this move is not valid!");
//					getTray().addTiles(board.pickupNonFixed());
//				}
//    		}
//	    }
	    // return the full move information, make move, fix tiles, next player!
	    
	    // Client suggests a move, position is the first letter’s position, along with the entire word, give a direction (HOR or VER),
	    // MOVE;<position>;<letters>;<direction>!
	    // Example message string: "MOVE;H8;UNIVERSE;HOR!"
	    return "MOVE;H8;UNIVERSE;HOR!";
    }
}