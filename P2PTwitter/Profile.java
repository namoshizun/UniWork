import java.util.*;
import java.io.*;

public class Profile {
	private String unikey;
	private String pseudo;
	private String ip;
	private String status;
	private boolean isMe;
	private long lastActive;

	public Profile(String unikey, String pseudo, String ip, boolean isMe, long time) {
		this.unikey = unikey;
		this.pseudo = pseudo;
		this.ip = ip;
		this.isMe = isMe;
		this.lastActive = time;
	}

	public String getIP() {
		return ip;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getUnikey() {
		return unikey;
	}

	public String getStatus() {
		return status;
	}

	public long getLastActive() {
		return lastActive;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setLastActive(long time) {
		this.lastActive = time;
	}
	
	public boolean isMyself(){
		return isMe;
	}
}
