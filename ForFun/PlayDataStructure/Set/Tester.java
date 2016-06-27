package Set;

public class Tester {

	public static void main(String[] args) {
		System.out.println(1%7);
		HashSet test = new HashSet(10);
		HashSet ha = new HashSet(10);
		test.add("a");
		test.add("b");
		test.add("c");
		test.add("f");
		
		ha.add("h");
		ha.add("l");
		ha.add("a");
		
		System.out.println(test.getMembers());
		test.union(ha);
		System.out.println(test.getMembers());
	}

}
