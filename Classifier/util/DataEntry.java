package util;

public class DataEntry <T> {

	public T[] content;
	public Class cls;
	
	public DataEntry(T[] data, Class c) {
		content = data;
		cls = c;
	}
	
	public String toString() {
		String out = "";
		for (T i : content) {
			out += i + ",";
		}
		return out + cls.name;
	}
	
}
