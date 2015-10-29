import java.net.*;
import java.util.Map;
import java.io.*;

public class P2PServer implements Runnable {
	DatagramSocket listener;
	Communication channel;
	String myUnikey;

	public P2PServer(String myUnikey, Communication channel, DatagramSocket listener) {
		this.listener = listener;
		this.channel = channel;
		this.myUnikey = myUnikey;
	}

	public String[] unpackData(byte[] data) throws UnsupportedEncodingException { 
		String tmp = new String(data, "ISO-8859-1");

		return new String[] { tmp.split(":")[0].replaceAll("\\:", ":"), 
				tmp.split(":")[1]
		};
	}

	@Override
	public void run() {
		// Ready for receiving packets from other peers
		String message[];

		try {
			while (true) {
				byte[] buffer = new byte[512];
				DatagramPacket pack = new DatagramPacket(buffer, 512);
				listener.receive(pack);
				message = unpackData(pack.getData());
						     // unikey + status
				channel.updateStatus(message[0], message[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
