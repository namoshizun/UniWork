import java.util.*;
import java.net.*;
import java.io.*;

public class Communication {

	private Map<String, Profile> contacts;
	boolean hasNewMsg;

	public Communication(String myUnikey) {
		this.contacts = new HashMap<String, Profile>();
		Properties participants = new Properties();
		String[] peers;
		int numPeers = 0;
		long timeNow = System.currentTimeMillis();
		String unikey, ip, pseudo;

		try {
			/*
			 * Load receiver details from participants.properties file Identify
			 * the local user by setting isMe to true
			 */
			participants.load(new FileInputStream("./participants.properties"));
			peers = participants.getProperty("participants").split(",");
			numPeers = peers.length;

			for (int i = 0; i < numPeers; ++i) {
				boolean isMe = false;
				ip = participants.getProperty(peers[i] + ".ip");
				pseudo = participants.getProperty(peers[i] + ".pseudo");
				unikey = participants.getProperty(peers[i] + ".unikey");
				if (unikey.equals(myUnikey)) isMe = true;

				Profile profile = new Profile(unikey, pseudo, ip, isMe, timeNow);
				contacts.put(unikey, profile);
			}

		} catch (IOException e) {
			// Dislike bugs :(
		}
	}

	public Map<String, Profile> getContacts() {
		return contacts;
	}

	public synchronized void updateStatus(String unikey, String status) {
		/*
		 * Search through the contact list, reset the peer's status whose unikey
		 * is "unikey", and mark the moment it is done.
		 */
		Profile peer = contacts.get(unikey);
		peer.setStatus(status);
		peer.setLastActive(System.currentTimeMillis());
	}

	public void printTweets() {
		System.out.println("### P2P tweets ###");
		for (Profile peer : contacts.values()) {
			double now = System.currentTimeMillis() / 1000;
			double lastActive = peer.getLastActive() / 1000;

			if (now - lastActive < 10)
				if (peer.isMyself())
					// Oh just found myself...
					System.out.println("# " + peer.getPseudo() + " (myself) : " + peer.getStatus());
				else if (peer.getStatus() == null)
					// Found someone else, but he didn't talk anything
					System.out.println("# [" + peer.getPseudo() + " (" + peer.getUnikey() + ") : not yet initialized]");
				else
					// Yes! this guy talked in the past 10 seconds!
					System.out.println("# " + peer.getPseudo() + " (" + peer.getUnikey() + ") : " + peer.getStatus());

			else if (now - lastActive < 20)
				// This guy has been silent for 10~20 seconds, so he is idle now
				System.out.println("# [" + peer.getPseudo() + " (" + peer.getUnikey() + ") : idle]");
			// Not going to print peers being silent > 20 seconds!

		}
		System.out.println("#### End tweets ###");
	}
}
