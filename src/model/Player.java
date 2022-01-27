package model;

public abstract class Player {
	
	    // -- Instance variables -----------------------------------------

		private TileTray tray = new TileTray();
	    private String name;
	    private int score;

	    // -- Constructors -----------------------------------------------

	    public Player(String name, TileTray tray) {
	        this.name = name;
	        this.tray = tray;
	        this.score = 0;
	    }

	    // -- Queries ----------------------------------------------------
	    
	    public String getName() { return name; }

	    public TileTray getTray() { return tray; }
	    
	    public int getScore() { return score; }
	    
	    public void addToScore(int n) { score += n; }
	    
	    public void FillTray(TileBag bag) {tray.fill(bag);}

	    public abstract String determineMove(Board board);
	}
