package networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import model.LocalHumanPlayer;
import model.Player;

/**
 * ClientHandler.
 * @author  Mikelis Putnieks
 * @version 2022.02.03
 */
public class ClientHandler extends Thread {
	
	private Player p;
    private Server server;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    private String features;
    private boolean exit = false;

    /**
     * Constructs a ClientHandler object
     * Initialises both Data streams.
     */
    //@ requires serverArg != null && sockArg != null;
    public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
    	this.server = serverArg;
    	this.out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(sockArg.getInputStream()));
    }

    /**
     * Reads the name of a Client from the input stream and sends 
     * a broadcast message to the Server to signal that the Client
     * is participating in the chat. Notice that this method should 
     * be called immediately after the ClientHandler has been constructed.
     */
    public void announce() throws IOException {
    	String[] first_msg = in.readLine().split(Protocol.SEPARATOR);
    	features = first_msg[2];
        clientName = first_msg[1];
        p = new LocalHumanPlayer(clientName);
        server.addPlayer(p);
        sendMessage(Protocol.HELLO + Protocol.SEPARATOR + server.getPlayerString() + Protocol.SEPARATOR + features);
        server.broadcast(Protocol.WELCOME + Protocol.SEPARATOR + clientName + Protocol.SEPARATOR + features);
        server.broadcast(Protocol.SERVERREADY);
    }

    /**
     * This method takes care of sending messages from the Client.
     * Every message that is received, is preprended with the name
     * of the Client, and the new message is offered to the Server
     * for broadcasting. If an IOException is thrown while reading
     * the message, the method concludes that the socket connection is
     * broken and shutdown() will be called. 
     */
    public void run() {
    	do {
	    	try {
	    		try {
				String msg = in.readLine();
				decodeClientMsg(msg);
	    		}catch(SocketException e2) {
	    			shutdown();
	    		}
			} catch (IOException e) {
				shutdown();
			}
    	}while(!exit);
    }
    
    public void decodeClientMsg(String msg) {
    	String[] data = msg.split(Protocol.SEPARATOR);
    	String command="";
    	String arg1="";
    	String arg2="";
    	if (data.length > 0) {
    		command = data[0];
    	}
    	if (data.length > 1) {
    		arg1 = data[1];
    	}
    	if (data.length > 2) {
    		arg2 = data[2];
    	}
    	
    	switch(command) {
	      case Protocol.HELLO:
	    	  // already handled by announce()
	        break;
	      case Protocol.CLIENTREADY:
	    	  server.setReady(p);
	        break;
	      case Protocol.ABORT:
	    	  shutdown();
	        break;
	      case Protocol.MOVE:
	    	  if(server.isTurn(this)) {
	    		  server.move(arg1, arg2, this);
	    	  }
	        break;
	      case Protocol.PASS:
	    	  if(server.isTurn(this)) {
		    	  if (arg1.equals("")) {
		    		  server.skip(this);
		    	  }else {
		    		  server.swap(arg1, this);
		    	  }
	    	  }
		    break;
	      default:
	    	  sendMessage(Protocol.ERROR + Protocol.SEPARATOR + Protocol.UNRECOGNIZED);
	    	break;
		}
    	
    }
    
    public Player getPlayer() {
    	return p;
    }

    /**
     * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     */
    public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			shutdown();
			e.printStackTrace();
		}
    }

    /**
     * This ClientHandler signs off from the Server and subsequently
     * sends a last broadcast to the Server to inform that the Client
     * is no longer participating in the chat. 
     */
    private void shutdown() {
    	exit = true;
        server.removeHandler(this);
        //server.removePlayer(p); this might remove the player from the game too, as the list of server is referenced in game.
        server.broadcast(Protocol.ABORT+Protocol.SEPARATOR+clientName);
    }
}
