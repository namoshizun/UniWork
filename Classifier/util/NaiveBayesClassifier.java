package util;


import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NaiveBayesClassifier implements Classifier<Double> {

	public static NBClass yes = NBClass.getYes();
	public static NBClass no = NBClass.getNo();
	String trainingFile;
	String testFile;
	
	public NaiveBayesClassifier() {
		
	}

	public NaiveBayesClassifier(String trainingFile, String testFile) {
		this.trainingFile = trainingFile;
		this.testFile = testFile;
	}

	public static double pdf(double x, double mean, double sd) {
		return 1 / (sd * Math.sqrt(2 * Math.PI)) * Math.pow(Math.E, -Math.pow((x - mean), 2) / 2 / sd / sd);
	}

	public static double ave(List<Double> l) {
		double sum = 0;
		int count = 0;
		for (Double d : l) {
			sum += d;
			count++;
		}
		return sum / count;
	}

	public static double sd(List<Double> l, double mean) {
		double sum = 0;
		int count = -1;
		for (Double d : l) {
			sum += Math.pow((d - mean), 2);
			count++;
		}
		return Math.sqrt(sum / count);
	}

	private Set<Integer> features = new HashSet<Integer>();

	public void setFesture(List<Integer> l) {
		features.addAll(l);
	}

	public Set<Integer> getFeatures() {
		if (features.isEmpty()) {
			Set<Integer> s = new HashSet<Integer>();
			for (int i = 0; i < yes.mean.length; i++)
				s.add(i);
			return s;
		} else {
			return features;
		}
	}

	@Override
	public void train(DataTable<Double> data) {
		for (NBClass c : new NBClass[] { yes, no }) {
			DataTable<Double> current = data.subTableOfClass(c);
			c.mean = new double[data.width()];
			c.sd = new double[data.width()];
			c.p = current.size() / (double) data.size();
			for (int column = 0; column < data.width(); column++) {
				c.mean[column] = ave(current.column(column).elements);
				c.sd[column] = sd(current.column(column).elements, c.mean[column]);
			}
		}
	}

	/** CLIENT CODE INTERFACE **/
	@Override
	public void trainClassifier() throws IOException {
		DataTable<Double> table = new NBTrainDataTable();
		table.parseFile(this.trainingFile);
		train(table);
	}

	@Override
	public void testClassifier() throws IOException {
		DataTable<Double> table = new NBTestDataTable();
		table.parseFile(testFile);
		for (DataEntry<Double> e : table)
			System.out.println(classify(e.content) == yes ? "yes" : "no");
	}

	@Override
	public Class classify(Double[] a) {
		double Pyes = yes.p;
		double Pno = no.p;
		for (Integer i : getFeatures()) {
			Pyes *= pdf((Double) a[i], yes.mean[i], yes.sd[i]);
			Pno *= pdf((Double) a[i], no.mean[i], no.sd[i]);
		}
		return Pyes >= Pno ? yes : no;
	}

	/** CLIENT CODE INTERFACE **/
	/** NB-TABLE **/
	public static class NBTrainDataTable extends DataTable<Double> {

		@Override
		public DataEntry<Double> parseLine(String input) {
			String[] components = input.split(",");
			Double[] values = new Double[components.length - 1];
			for (int i = 0; i < values.length; i++)
				values[i] = Double.valueOf(components[i]);
			return new DataEntry<Double>(values, components[components.length - 1].equals("yes") ? yes : no);
		}
	}

	public static class NBTestDataTable extends DataTable<Double> {

		@Override
		public DataEntry<Double> parseLine(String input) {
			String[] components = input.split(",");
			Double[] values = new Double[components.length];
			for (int i = 0; i < values.length; i++)
				values[i] = Double.valueOf(components[i]);
			return new DataEntry<Double>(values, null);
		}
	}

	public static class NBClass extends Class {

		private static NBClass yesClass = null;
		private static NBClass noClass = null;

		public double[] mean, sd;
		public double p;

		private NBClass(String name) {
			super(name);
		}

		public static NBClass getYes() {
			if (yesClass == null)
				yesClass = new NBClass("yes");
			return yesClass;
		}

		public static NBClass getNo() {
			if (noClass == null)
				noClass = new NBClass("no");
			return noClass;
		}
	}

	/** NB-TABLE **/

	@Override
	public void printClassifier() {
	}
}
