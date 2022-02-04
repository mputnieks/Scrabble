package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import Main.Controller;

/**
 * Client class for client-server communications
 * @author  Mikelis Putnieks
 * @version 2022.02.03
 */
public class Client extends Thread {

	/** Constructs and returns a Client. */
	public static Client createClient(String name, String address, int port, Controller c) {
		
		InetAddress host = null;

		try {
			host = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			print("ERROR: no valid hostname!");
			System.exit(0);
		}

		try {
			Client client = new Client(name, host, port, c);
			client.sendMessage(Protocol.HELLO+Protocol.SEPARATOR+name+Protocol.SEPARATOR+"P"+Protocol.AS+"T");
			client.start();
			return client;
		} catch (IOException e) {
			print("ERROR: couldn't construct a client object!");
			System.exit(0);
		}
		return null;
	}
	
	private Controller c;
	private String clientName;
	private Socket sock;
	private BufferedReader input;
	private PrintWriter output;
	private boolean exit = false;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port, Controller c) throws IOException {
		this.clientName = name;
		this.c = c;
		// try to open a Socket to the server, which will then assign a ClientHandler
        try {
            this.sock = new Socket(host, port);
            this.output = new PrintWriter(sock.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
        	throw new IOException("ERROR: could not create a socket on " + host + " and port " + port + "or retreive its reader/writer");
        }
	}

	/**
	 * Reads the messages in the socket connection.
	 */
	public void run() {
		do {
        	String incoming = "";
    		try {
    			incoming += input.readLine();
    			if (incoming.equals("")) {
    				System.out.println("no response...");
    			}else {
    				System.out.println("> "+incoming);
    				decodeServerMsg(incoming);
    			}
    		} catch (IOException e1) {
    			System.out.println("Error: Did not manage to receive the message!");
    		}
    	}while(exit == false);
	}
	
	public void decodeServerMsg(String msg) {
    	String[] data = msg.split(Protocol.SEPARATOR);
    	String command="";
    	String arg1="";
    	String arg2="";
    	String arg3="";
    	if (data.length > 0) {
    		command = data[0];
    	}
    	if (data.length > 1) {
    		arg1 = data[1];
    	}
    	if (data.length > 2) {
    		arg2 = data[2];
    	}
    	if (data.length > 3) {
    		arg3 = data[3];
    	}
    	
    	switch(command) {
	      case Protocol.HELLO:
	    	  // nothing to do
	        break;
	      case Protocol.WELCOME:
	    	  // nothing to do
	        break;
	      case Protocol.SERVERREADY:
	    	  // nothing to do
	        break;
	      case Protocol.START:
	    	  c.startGame();
	        break;
	      case Protocol.ABORT:
	    	  // nothing to do
		    break;
	      case Protocol.TILES:
	    	  c.pushTilesToAdd(arg1.split(Protocol.AS), true);
			break;
	      case Protocol.TURN:
	    	  // maybe should display current player ??????????
	    	  if (arg1.equals(clientName)) {
	    		  c.setPaused(false);
	    	  }
			break;
	      case Protocol.MOVE:
	    	  // maybe should display player scores ????????????
	    	  if(arg1.equals(clientName)) {
	    		  c.pushBoardUpdate(arg2, true);
	    	  }else {
	    		  c.pushBoardUpdate(arg2, false);
	    	  }
	    	break;
	      case Protocol.PASS:
	    	  if (arg2.equals("")) {
	    		  // nothing to do, a successful pass has occured
	    	  }else {
	    		  c.pushTilesToAdd(arg2.split(Protocol.AS), false);
	    	  }
			break;
	      case Protocol.GAMEOVER:
	    	  
			break;
	      case Protocol.ERROR:
	    	  c.pushTilePickup();
	    	  print(arg1);
				break;
	      default:
	    	  
	    	break;
    	}
    }

	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
		output.println(msg);
		output.flush();
	}
	
	public void ready() {
		sendMessage(Protocol.CLIENTREADY+Protocol.SEPARATOR+clientName);
	}

	/** close the socket connection. */
	public void shutdown() {
		sendMessage(Protocol.ABORT+Protocol.SEPARATOR+clientName);
		print("Closing socket connection...");
		try {
			exit = true;
			sock.close();
		} catch (IOException e) {
			print("Error: Unsuccessful shutdown!");
			e.printStackTrace();
		}
	}

	/** returns the client name */
	public String getClientName() {
		return clientName;
	}
	
	private static void print(String message){
		System.out.println(message);
	}
}
