import java.net.*;
import java.io.*;
import java.util.*;

public class P2PTwitter {

	public static void main(String[] args) {

		String unikey = args[0];
		DatagramSocket sock = null;
		int port = 7014;

		try {
			sock = new DatagramSocket(port);
		} catch (SocketException e) {
		}

		/**
		 * Communication: stores the contact <unikey, status, ip..> details of all participants
		 * Server:  Keep receiving packets from participants, 
		 * 			upon reception,let Communication to update statues.
		 * Client:  Keep asking me to enter new status,
		 * 			send new status to everyone (including myself),
		 * 			let Communication to print out tweets of all participants.
		 * 		
		 */
		Communication channel = new Communication(unikey);
		Thread server = new Thread(new P2PServer(unikey, channel, sock));
		Thread client = new Thread(new P2PClient(unikey, sock, channel, port));
		
		server.start();
		client.start();
	}

}
