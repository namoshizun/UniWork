import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import util.Classifier;
import util.DataTable;
import util.DecisionTreeClassifier;
import util.DecisionTreeClassifier.DTTrainDataTable;
import util.NaiveBayesClassifier;
import util.NaiveBayesClassifier.NBTrainDataTable;
import util.Validator;

public class Main {
	
	public static void writeFolds() throws IOException {
		DataTable<Double> d = new NBTrainDataTable();
		d.parseFile("pima.csv");
		d.writeFolds("pima-folds.csv", 10);
	}
	
	public static void validate(String classifierName) throws IOException {
		if (classifierName.equals("NB")) {
			Validator<Double> v = new Validator<Double>();
			DataTable<Double> d = new NBTrainDataTable();
			d.parseFile("pima-CFS.csv");
			ArrayList<Classifier<Double>> c = new ArrayList<Classifier<Double>>();
			
			for (int i = 0; i < 10; i ++) {
				c.add(new NaiveBayesClassifier());
			}
			System.out.println(classifierName + " accuracy is" + v.validate(c, d));
			
		} else {
			Validator<String> v = new Validator<String>();
			DataTable<String> d = new DTTrainDataTable();
			
			//d.parseFile("pima-discretised-CFS.csv");
			d.parseFile("pima-indians-diabetes-discrete.csv");
			ArrayList<Classifier<String>> c = new ArrayList<Classifier<String>>();
			
			for (int i = 0; i < 10; i ++) {
				c.add(new DecisionTreeClassifier());
			}
			System.out.println(classifierName + " accuracy is" + v.validate(c, d));
		}
	}

	public static void main(String[] args) {
		String trainingFile = args[0];
		String testFile = args[1];
		String classifierName = args[2];
	
		try {
			Classifier c = null;
			if (classifierName.equals("DT")) c =  new DecisionTreeClassifier(trainingFile, testFile);
			else if (classifierName.equals("NB")) c = new NaiveBayesClassifier(trainingFile, testFile);
			
			c.trainClassifier();
			c.testClassifier();
			
			Scanner sc = new Scanner(System.in);
			/*
			 * Perform other tasks:
			 * 1. print: output the structure of decision tree
			 * 2. validate: using 10-fold stratified cross validation to test the classifier;
			 */
			while (sc.hasNextLine()) {
				String cmd = sc.nextLine();
				if (cmd.equals("print"))
					c.printClassifier();
				else if (cmd.equals("validate"))
					validate(classifierName);
				else
					break;
			}
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
