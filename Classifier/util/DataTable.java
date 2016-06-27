package util;

import java.io.*;
import java.util.*;

public class DataTable<T> implements Iterable<DataEntry<T>> {

	// A list of <DataEntry -> <Contents, Class>>
	ArrayList<DataEntry<T>> data;

	public DataTable() {
		data = new ArrayList<DataEntry<T>>();
	}

	public DataTable(ArrayList<DataEntry<T>> data) {
		this.data = data;
	}

	public DataEntry<T> parseLine(String input) {
		return null;
	}

	// return a table of all data entries whose class = c
	public DataTable<T> subTableOfClass(Class c) {
		DataTable<T> d = new DataTable<T>();
		for (DataEntry<T> e : data)
			if (e.cls == c)
				d.data.add(e);
		return d;
	}

	// return the subset of data table[start, end]
	public DataTable<T> subTable(int start, int end) {
		return new DataTable<T>(new ArrayList<DataEntry<T>>(data.subList(start, end)));
	}

	public ArrayList<DataTable<T>> partition(int folds) {
		ArrayList<DataTable<T>> d = new ArrayList<DataTable<T>>();
		int len = data.size();
		for (int i = 0; i < folds; i++) {
			d.add(subTable(len * i / folds, len * (i + 1) / folds));
		}
		return d;
	}

	private List<Class> containedClasses() {
		List<Class> c = new ArrayList<Class>();
		for (DataEntry<T> e : data) {
			if (!c.contains(e.cls))
				c.add(e.cls);
		}
		return c;
	}

	public ArrayList<DataTable<T>> stratifiedPartition(int folds) {
		List<Class> allclasses = containedClasses();
		ArrayList<DataTable<T>> partsByClass = new ArrayList<DataTable<T>>();
		for (Class c : allclasses) {
			partsByClass.add(subTableOfClass(c));
		}
		DataTable<T> sorted = new DataTable<T>();
		sorted.joinAll(partsByClass);

		ArrayList<DataTable<T>> parts = new ArrayList<DataTable<T>>();
		for (int i = 0; i < folds; i++) {
			parts.add(new DataTable<T>());
		}

		int len = data.size();
		for (int i = 0; i < len; i++) {
			parts.get(i % folds).data.add(sorted.row(i));
		}
		return parts;
	}

	public void writeFolds(String filename, int folds) throws IOException {
		PrintWriter writer = new PrintWriter(filename);

		List<DataTable<T>> d = stratifiedPartition(folds);
		for (int i = 0; i < folds; i++) {
			writer.println("fold" + (i + 1));
			writer.println(d.get(i));
		}
		writer.close();
	}

	public void join(DataTable<T> d) {
		data.addAll(d.data);
	}

	public void joinAll(Collection<DataTable<T>> c) {
		for (DataTable<T> t : c) {
			this.join(t);
		}
	}

	public int size() {
		return data.size();
	}

	public int width() {
		return data.isEmpty() ? 0 : data.get(0).content.length;
	}

	public void parseFile(String filename) throws IOException {
		data.clear();
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));) {
			for (String line; (line = reader.readLine()) != null;) {
				data.add(parseLine(line));
			}
		}
	}

	public DataEntry<T> row(int index) {
		return data.get(index);
	}

	public Column column(int index) {
		return new Column(index);
	}

	public String toString() {
		String out = "";
		for (DataEntry<T> e : data) {
			out += e + "\n";
		}
		return out;
	}

	@Override
	public Iterator<DataEntry<T>> iterator() {
		return data.iterator();
	}

	public class Column implements Iterable<T> {

		public ArrayList<T> elements;
		int column;

		public Column(int column) {
			elements = new ArrayList<T>();
			for (DataEntry<T> d : data) {
				elements.add(d.content[column]);
			}
		}

		public T get(int index) {
			return elements.get(index);
		}

		public void set(int index, T value) {
			elements.set(index, value);
			data.get(index).content[column] = value;
		}

		@Override
		public Iterator<T> iterator() {
			return elements.iterator();
		}
	}
}
