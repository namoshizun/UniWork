package util;
import java.util.List;

public class Validator<T> {

	public double validate(List<Classifier<T>> c, DataTable<T> t) {
		if (c.size() < 10)
			throw new RuntimeException("Must pass 10 classifier instance");

		List<DataTable<T>> folds = t.partition(10);
		double sumAccuracy = 0;
		for (int i = 0; i < 10; i++) {
			DataTable<T> testFold = folds.remove(0);
			DataTable<T> trainFold = new DataTable<T>();
			trainFold.joinAll(folds);
			
			c.get(i).train(trainFold);
			
			sumAccuracy += test(c.get(i), testFold);
			folds.add(testFold);
		}
		return sumAccuracy / 10;
	}

	private double test(Classifier<T> c, DataTable<T> t) {
		int hit = 0, total = t.size();
		for (DataEntry<T> e : t) {
			if (c.classify(e.content) == e.cls)
				hit += 1;
		}
		return hit / (double) total;
	}
}
