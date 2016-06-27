package others;
import java.util.*;

public class ReverseWord_StoreCredit {

	final static String reserveWordDescription = "STORE CREDIT \n" +
			"You receive a credit C at a local store and would like to buy two items.\n" + 
			"You first walk through the store and create a list L of all available items.\n" +
			"From this list you would like to buy two items that add up to the entire value of the credit.\n" +
			"The solution you provide will consist of the two integers indicating the positions of the items in your list (smaller number first)\n" +
			"Input:\n" + 
			"The first line of input gives the number of cases, N. N test cases follow. For each test case there will be:\n" +
			"One line containing the value C, the amount of credit you have at the store.\n" +
			"One line containing the value I, the number of items in the store.\n" +
			"One line containing a space separated list of I integers. Each integer P indicates the price of an item in the store.\n" +
			"Each test case will have exactly one solution.\n\n" +
			"Output:\n" +
			"For each test case, output one line containing 'Case #x: ' followed by the indices of the two items whose price \n" +
			"adds up to the store credit. The lower index should be output first.\n" +
			"> ";
				 
	final static String storeCreditDescription = "REVERSE WORD\n"
			+ "Given a list of space separated words, reverse the order of the words. \n"
			+ "Each line of text contains L letters and W words. A line will only consist of letters and space characters. \n"
			+ "There will be exactly one space character between each pair of consecutive words.\n\n"
			+ "Input:\n"
			+ "The first line of input gives the number of cases, N.\n"
			+ "N test cases follow. For each test case there will a line of letters and space characters indicating \n"
			+ "a list of space separated words. Spaces will not appear at the start or end of a line.\n\n"
			+ "Output : "
			+ "For each test case, output one line containing 'Case #x: ' followed by the list of words in reverse order.\n"
			+ "> ";
			
	
	public static void storeCredit(Scanner sc) { //FIXME
		Queue<String> l = new LinkedList<String>();
		// Store all inputss
		while (sc.hasNextLine()) l.add(sc.nextLine());
		
		// Process batches
		int caseN = Integer.parseInt(l.poll());
		for (int i = 1; i <= caseN; ++i) {
			int credit = Integer.parseInt(l.poll());
			int numItems = Integer.parseInt(l.poll());
			int[] items = Arrays.stream(l.poll().split(" ")).mapToInt(Integer::parseInt).toArray();
			
			label: for (int j = 0; j < numItems; ++j) {
				for (int k = j + 1; k < numItems; ++k) {
					if (items[i] + items[j] == credit) {
						System.out.println("Case #" + i + ": " + j + " " + k);
						break label; // escape the loop once a valid pair of items is found for this case
					}
				}
			}
		}
		sc.close();
	}
	
	public static void reverseWord(Scanner sc) {
		Queue<String> l = new LinkedList<String>(); 
		// Store all inputs
		while (sc.hasNextLine()) l.add(sc.nextLine()); 
		int caseN = Integer.parseInt(l.poll()); 
		
		// print each line of words in reverse order
		for (int i = 1; i <= caseN; ++i) { 
			String[] parts = l.poll().split(" ");
			StringBuilder reversed = new StringBuilder(); 
			for (int j = parts.length - 1; j >= 0; --j) reversed.append(parts[j] + " ");
			System.out.println("Case #" + i + ": " + reversed.toString());  
		}  
		sc.close();
	}
	
	public static void main(String[] args) {
		System.out.print("Have Fun: \n" +
				"> ReverseWord -r, to review the document for ReverseWord puzzle \n" +
				"> StoreCredit -s, to review the document for StoreCredit puzzle\n" + 
				"> r, to start ReverseWord function\n" +
				"> s, to start StoreCredit function\n" +
				"> ");
		Scanner sc = new Scanner(System.in);
		try {
			while (sc.hasNextLine()) {

				String command = sc.nextLine();
				if (command.equals("ReverseWord -r"))
					System.out.print(reserveWordDescription);
				else if (command.equals("StoreCredit -s"))
					System.out.print(storeCreditDescription);
				else if (command.equals("s"))
					storeCredit(sc);
				else if (command.equals("r"))
					reverseWord(sc);
			}
		} catch (Exception e) {
			System.out.println("bye");
		}
	}

}
