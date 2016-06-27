package util;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;
/**
 * @author YuanZhong
 * The training data uses selected attributes:
 */

public class DecisionTreeClassifier implements Classifier {

	public static Class yes = new Class("yes");
	public static Class no = new Class("no");
	
	Tree<Object> dt; 
	List<Example> examples;
	String testFile;
	
	// A map of <attribute_id, attribute_values>;
	Map<Integer, Set<String>> attrMap;
	// if true, the ith attribute hasn't been added in a branch.;
	Boolean[] freeAttr;
	// The tree is generated using only selected attributes: 1, 4, 5, 6, 7 (initial index = 0)
	Integer[] offset = {1, 3, 3, 3, 3};
	
	public DecisionTreeClassifier() {
		this.examples = new ArrayList<Example>();
		this.attrMap = new HashMap<Integer, Set<String>>();
	}
	
	public DecisionTreeClassifier(String trainingFile, String testFile) throws IOException {
		this.examples = new ArrayList<Example>();
		this.attrMap = new HashMap<Integer, Set<String>>();
		this.testFile = testFile;
		
		readExamples(trainingFile);
	}
	
	/********* CONFIG REGION ***********/
	// each example is comma delimited. 
	public void readExamples(String trainingFile) throws IOException {
		// Read file, add each example to examples
		BufferedReader reader = new BufferedReader(new FileReader(new File(trainingFile)));
		String line = null;
		String[] components = null;
		while ((line = reader.readLine()) != null) {
			components = line.split(",");
			int numAttr = components.length - 1;
			examples.add(new Example(Arrays.copyOf(components, numAttr), new Class(components[numAttr])));
			
			// Update the attribute->possibleValues map.
			for (int i = 0; i < numAttr; ++i) {
				if (attrMap.containsKey(i))
					attrMap.get(i).add(components[i]);
				else
					attrMap.put(i, new HashSet<String>(Arrays.asList(components[i])));
			}
		}
		reader.close();
		
		// Initialize the attribute indicator.
		freeAttr = new Boolean[components.length - 1];
		Arrays.fill(freeAttr, true);
		
	}
	
	/********* ATTRIBUTE SELECTION *********/
	public double getEntropy(List<Example> examples) {
		double entropy = 0.0;
		Map<String, Integer> classCounter = new HashMap<String, Integer>();
		
		// Get the number of occurrences of each class inside this example
		for (Example e : examples) {
			String e_class = e.eClass.name;
			if (!classCounter.containsKey(e_class)) {
				classCounter.put(e_class, new Integer(1));
			} else {
				classCounter.put(e_class, classCounter.get(e_class) + 1);
			}
		}
		
		// Calculate entropy 
		for (Iterator<Integer> itr = classCounter.values().iterator(); itr.hasNext();) {
			double p = (double) itr.next() / examples.size(); 
			entropy += p * log(p, 2);
		}
		
		return -entropy;
	}
	
	// Get the id of the best attribute that minimize the result of H(S) - H(S|a)
	public int getBestAttribute(List<Example> examples, Boolean[] freeAttr) {
		int bestAttr = 0;
		int size = examples.size();
		double T1 = getEntropy(examples); 
		double maxInfoGain = -1.0; // just a number to temporarily denote maximum
		
		for (int i = 0; i < freeAttr.length; ++i) {
			double T2 = 0;
			
			// true means this attribute hasn't been added to the branch
			if(freeAttr[i] == true) {

				for (String attrValue : attrMap.get(i)) {
					double counter = 0.0;
					double p = 0.0;
					List<Example> subset = new ArrayList<Example>();
					for (Example e : examples) {
						if (e.attrValues[i].equals(attrValue)) {
							++counter;
							subset.add(e);
						}
					}
					p = counter / size;
					T2 += p * getEntropy(subset);
				}
				
				double G = T1 - T2;
				if(G > maxInfoGain) {
					maxInfoGain = G;
					bestAttr = i;
				}
			}
		}
		
		return bestAttr;
	}
	
	/******  ID3 ALGORITHM  ******/
	@SuppressWarnings("unchecked")
	public Tree<Object> trainClassifier(List<Example> examples, Boolean[] freeAttr, String defaultClass) {
		/* Case 1: no examples with the attribute value for the branch, 
		 *         make a leaf node and label it with the majority class of this subset's parent
		 */
		if (examples.isEmpty()) return new DT_Tree(new DT_Leaf(defaultClass));
		
		/* Case 2: all examples have the same class,
		 * 		   make a leaf node corresponding to this class
		 */
		boolean sameClass = true;
		String c = examples.get(0).eClass.name;
		for (Example e : examples) {
			if (!e.eClass.equals(c)) {
				sameClass = false;
				break;
			}
		}
		if (sameClass) return new DT_Tree(new DT_Leaf(c));
		
		/* Case 3: cannot split further, all examples have the same attribute value but different classes,
		 * 		   make a leaf node and label it with the majority class of this subset
		 */
		if (containsSameElem(freeAttr, false))
			return new DT_Tree(new DT_Leaf(getMajorClass(examples)));

		// Case 4: stop criteria not met.
		int bestAttr = getBestAttribute(examples, freeAttr);
		freeAttr[bestAttr] = false;
		defaultClass = getMajorClass(examples);
		Node root = new DT_Node(bestAttr);
		Tree<Object> tree = new DT_Tree(root); 
		
		// Classify the examples using the attribute with the highest information gain
		for(String value : attrMap.get(bestAttr)) {
			Boolean[] arr_copy = freeAttr.clone();
			List<Example> subset = new ArrayList<Example>();
			
			for (Example e : examples) {
				if(e.attrValues[bestAttr].equals(value))
					subset.add(e);
			}
			
			Tree<Object> subtree = trainClassifier(subset, arr_copy, defaultClass);
			tree.insert(root, subtree.getRoot());
			// update the <attrValue, associateNextNode> map for the root.
			((Map<String,Node>) root.getData()).put(value, subtree.getRoot());
		}
		
		return tree;
	}
	
	/********* TEST REGION *********/
	@SuppressWarnings("unchecked")
	public Node classify(Node root, String[] attrValues) {
		int attrID = root.getID();
		// Base case: if reaches leaf node
		if(attrID == -1) {return root;}
		// Recursion case: pass on to the next corresponding attribute tester.
		else {
			int attrToTest = attrID;// + offset[attrID];
			return classify(((Map<String,Node>) root.getData()).get(attrValues[attrToTest]), attrValues);
		}
	}
	
	public void testClassifier(String testFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(new File(testFile)));
		String line = null;
		StringBuilder result = new StringBuilder();


		while ((line = reader.readLine()) != null) {
			String[] attrValues = line.split(",");
			Class _class = classify(attrValues);
			result.append(_class.name);
			result.append('\n');
		}
		reader.close();
		System.out.print(result.toString());
	}

	/*
	 * A branch line is for example if attrNum = 2, depth = 3, value = high:
	 * "| | | 2 = high"
	 */
	@SuppressWarnings("unchecked")
	public String brachLine(Node n, String value, int depth) {
		int attrNum = n.getID() + offset[n.getID()] + 1;
		StringBuilder sb = new StringBuilder();
		// a string of depth times repetitive "| "
		sb.append(new String(new char[depth]).replace("\0", "|    "));

		if (((Map<String, Node>) n.getData()).get(value).getID() == -1)
			// don't need the new line character if the child node will be a leaf
			sb.append(attrNum + " = " + value);
		else
			sb.append(attrNum + " = " + value + '\n');

		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public String getTreeLayout(Node root, int depth) {
		if (root.getID() == -1)
			// the leaf
			return ": " + root.getData() + "\n";

		StringBuilder sb = new StringBuilder();
		// map<value, corresponding next node>
		Map<String, Node> v_map = (Map<String, Node>) root.getData();

		for (Iterator<Entry<String, Node>> it = v_map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, Node> pair = (Entry<String, Node>) it.next();

			sb.append(brachLine(root, pair.getKey(), depth));
			sb.append(getTreeLayout(pair.getValue(), depth + 1));
		}

		return sb.toString();
	}

	/** Client code interfaces **/
	public void trainClassifier() {
		this.dt = trainClassifier(this.examples, this.freeAttr, getMajorClass(this.examples));
	}

	public void testClassifier() throws IOException {
		testClassifier(this.testFile);
	}

	public void printClassifier() {
		Node root = this.dt.getRoot();
		String classifierTree = getTreeLayout(root, 0);
		System.out.print(classifierTree);
	}

	public Class classify(Object[] contents) {
		Node node = classify(dt.getRoot(), (String[]) contents);
		if (node.getData().equals("yes"))
			return yes;
		else
			return no;
	}

	/********* HELPER METHODS *********/
	public double log(double x, int base) {
		return Math.log(x) / Math.log(base);
	}

	public boolean containsSameElem(Boolean[] freeAttr, boolean elem) {
		boolean isSame = true;
		for (int i = 0; i < freeAttr.length; ++i) {
			if (!(freeAttr[i] == elem)) {
				isSame = false;
				return isSame;
			}
		}
		return isSame;
	}
	
	// Read through all examples and find the major class
	public String getMajorClass(List<Example> examples) {
		Map<String, Integer> classCounter = new HashMap<String, Integer>();
		
		for(Example e : examples) {
			
			if (!classCounter.containsKey(e.eClass.name)) {
				// A new class, put it to the map and assign a counter.
				classCounter.put(e.eClass.name, new Integer(1));
			} else {
				// Existing class, update its counter by 1.
				classCounter.put(e.eClass.name, classCounter.get(e.eClass.name) + 1);
			}
		}
		
		int max = Integer.MIN_VALUE;
		boolean hasTie = false;
		String majorClass = null;
		for(String c : classCounter.keySet()) {
			Integer num = classCounter.get(c);
			if (num == max) hasTie = true;
			else if (num > max) {
				max = num;
				majorClass = c;
			}
		}
		
		// If there's a tie, return "yes" class as required.
		return hasTie ? "yes" : majorClass; 
	}

	@Override
	public void train(DataTable d) {
		this.examples.clear();
		this.attrMap.clear();
		
		int len = 0;
		for (Iterator<DataEntry> it = d.iterator(); it.hasNext();) {
			DataEntry entry = it.next();
			len = entry.content.length;
			int numAttr = len - 1;
			this.examples.add(new Example((String[])entry.content, entry.cls));
			
			for (int i = 0; i < numAttr; ++i) {
				if (attrMap.containsKey(i))
					attrMap.get(i).add((String)entry.content[i]);
				else
					attrMap.put(i, new HashSet<String>(Arrays.asList((String)entry.content[i])));
			}
		}
		// Initialize the attribute indicator.
		freeAttr = new Boolean[len - 1];
		Arrays.fill(freeAttr, true);
		this.dt = trainClassifier(this.examples, this.freeAttr, getMajorClass(this.examples));
		int i = 0;
	}
	
	// TABLES
	public static class DTTrainDataTable extends DataTable<String> {

		@Override
		public DataEntry<String> parseLine(String input) {
			String[] components = input.split(",");
			String[] values = new String[components.length - 1];
			for (int i = 0; i < values.length; ++i)
				values[i] = components[i];
			return new DataEntry<String>(values, components[components.length - 1].equals("yes") ? yes : no);
		}
	}
}
