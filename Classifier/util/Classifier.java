package util;

import java.io.IOException;

public interface Classifier<T> {
	public void trainClassifier() throws IOException;

	public void testClassifier() throws IOException;

	public Class classify(T[] contents);

	public void train(DataTable<T> d);

	public void printClassifier();
}
