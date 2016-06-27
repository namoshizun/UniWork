import java.util.*;

public class Snapshot {
	private String timestamp;
	private List<Entry> entryList;
	
	public Snapshot(String timestamp, List<Entry> entryList){
		this.timestamp = timestamp;
		this.entryList = entryList;
	}
	
	public List<Entry> getEntrylist(){
		return entryList;
	}
	
	public String getTimestamp(){
		return timestamp;
	}
}
