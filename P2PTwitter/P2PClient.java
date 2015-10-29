import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.net.*;

public class P2PClient implements Runnable {

	DatagramSocket sender;
	String unikey;
	Communication channel;
	int port;

	public P2PClient(String unikey, DatagramSocket sender, Communication channel, int port) {
		this.sender = sender;
		this.unikey = unikey;
		this.channel = channel;
		this.port = port;
	}

	public void shareTweets(String status) {
		String message = unikey + ":" + status;
		message = message.replaceAll(":", "\\:");
		byte[] buffer = message.getBytes();
		int length = buffer.length;
		DatagramPacket pack = null;
		InetAddress address;

		try {
			for (Profile peer : channel.getContacts().values()) {
				address = InetAddress.getByName(peer.getIP());
				pack = new DatagramPacket(buffer, length, address, port);
				sender.send(pack);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * Client Thread: Keep catching status from local machine and broadcast to
	 * everybody in contacts
	 */
	public void run() {

        BufferedReader reader = new BufferedReader(new InputStreamReader (System.in));
		Random rand = new Random();
		String status = null;
		try {
			// Gets the very first input 
			System.out.print("Status: ");	
			// Loop until EOF		
			while (true) {

				// Case 1: A new status is entered from command line, delay < 1s
				if (reader.ready()) {
                    status = reader.readLine();
					if (status.length() > 140) {
						// Handle invalid status
						System.out.println("Status is too long, 140 characters max. Retry");
						continue;
					} else if (status.isEmpty()) {
						System.out.println("Status is empty. Retry");
						continue;

						/*
						 * Valid status: Broadcast to every body then ask
						 * channel to print all the current statuses
						 */
					} else {
						
						shareTweets(status);
						// Just in case the server hasn't got enough time to update status.
						Thread.sleep(20);
						channel.printTweets();
						System.out.print("Status: ");
					}
				} else {
				//	Case 2: No input, local status is broadcasted periodically. 1s < delay < 3s 
					shareTweets(status);
					Thread.sleep(rand.nextInt(2000) + 1000);
				}

			}

		} catch (NullPointerException e) {
			// Received ^D from terminal
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
