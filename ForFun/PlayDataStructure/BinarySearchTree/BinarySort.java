import java.util.ArrayList;
import java.util.List;

public class BinarySort {

	static BinaryTreeNode ROOT;

	/**
	 * Returns the given numbers in ascending sorted order
	 */

	public static List<Integer> sort(List<Integer> numbers) {

		List<Integer> sortedList = new ArrayList<Integer>();
		BinaryTree sorter = new BinaryTree();
		sorter.insertSeries(numbers);

		while (sorter.root != null) {
			sortedList.add(sorter.removeMin().value);
		}
		
		return sortedList;
	}

	public static void main(String[] args) {

		BinarySort test = new BinarySort();
		List<Integer> numbers = new ArrayList<Integer>();
		
		for(int i = 1; i < 10000; ++i){
			numbers.add((int) (Math.random() * 30000));
		}
		System.out.println(numbers.size());
		numbers.add(1);
		numbers.add(61);
		numbers.add(2);
		numbers.add(4);
		numbers.add(6);
		numbers.add(9);
		numbers.add(123);
		numbers.add(0);
		numbers.add(34);
		numbers.add(9);
		numbers.add(1);
		numbers.add(-2);
		numbers.add(450);
		numbers.add(2);
		numbers.add(6);
		numbers.add(13); 
		System.out.println(test.sort(numbers));
	}
}
