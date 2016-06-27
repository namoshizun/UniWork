
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class BinaryTreeNode {

	public int value;
	public BinaryTreeNode parent;

	public BinaryTreeNode left;
	public BinaryTreeNode right;
	
	public BinaryTreeNode(int value) {

		this.value = value;
		this.parent = null;
		this.right = null;
		this.left = null;
		
	}
}

public class BinaryTree {

	public static BinaryTreeNode root;
	public List<Integer> storedValues;

	public BinaryTree() {

		this.root = null;
		storedValues = new ArrayList<Integer>();

	}

	public void insertLeft(BinaryTreeNode parent, BinaryTreeNode child) {

		parent.left = child;
		child.parent = parent;
	}

	public void insertRight(BinaryTreeNode parent, BinaryTreeNode child) {

		parent.right = child;
		child.parent = parent;

	}

	public void insert(int n) {

		if (root == null) {
			root = new BinaryTreeNode(n);
			return;
		}

		BinaryTreeNode current = root;
		BinaryTreeNode newMember = new BinaryTreeNode(n);
		while (true) {

			if (n < current.value) {

				if (current.left == null) {
					insertLeft(current, newMember);
					return;
				} else {
					current = current.left;
				}

			} else if (n > current.value) {

				if (current.right == null) {
					insertRight(current, newMember);
					return;
				} else {
					current = current.right;
				}

			} else {
				// A node already exists with this number so be cool
				return;

			}
		}
	}

	public void insertSeries(List<Integer> values) {

		for (Integer value : values) {
			insert(value);
			storedValues.add(value);
		}
		
	}

	public boolean contains(int n) {

		if (root == null) {
			return false;
		}

		BinaryTreeNode current = root;

		while (current != null) {

			if (n < current.value) {
				current = current.left;
			} else if (n > current.value) {
				current = current.right;
			} else {
				return true;
			}
		}
		return false;
	}

	private int size(BinaryTreeNode root) {

		if (root == null) {
			return 0;
		}
		int total = 1;

		if (root.left != null) {
			total += size(root.left);
		}
		if (root.right != null) {
			total += size(root.right);
		}

		return total;
	}

	public int size() {

		return size(root);
	}

	public boolean isEmpty() {

		return root == null;
	}

	/**  Remove from Min methods */
	private BinaryTreeNode removeMin(BinaryTreeNode node) {
		
		if (node.left.left == null) {
			
			BinaryTreeNode minNode = node.left;
			node.left = node.left.right;
			return minNode;
	
		} else {
			return removeMin(node.left);
		}
		
	}

	public BinaryTreeNode removeMin() {
		
		if (root == null) {
			return null;
		} else if (root.left != null) {
			return removeMin(root);
		} else {
			
			// The case where the left child of root is none, just right - advance the root node
			BinaryTreeNode minNode = root;
			root = root.right;
			return minNode;
		}
		
	}
	/**  Remove from Min methods Ends   */
	

	public int getDiameter(BinaryTreeNode root) {        
	    if (root == null)
	        return 0;

	    int rootDiameter = getHeight(root.left) + getHeight(root.right) + 1;
	    int leftDiameter = getDiameter(root.left);
	    int rightDiameter = getDiameter(root.right);

	    return Math.max(rootDiameter, Math.max(leftDiameter, rightDiameter));
	}
	
	public int kDepthNodesNum(BinaryTreeNode root, int k){
		if(k > getHeight(root) || root == null){
			return 0;
		} else if (k == 0){
			return 1;
		} else {
			--k;
			return kDepthNodesNum(root.left, k) + kDepthNodesNum(root.right, k);
		}
	}

	public int getHeight(BinaryTreeNode root) {
	    if (root == null)
	        return 0;

	    return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
	}
	
	/** Three different ways of traversing a Binary Tree */

	public List<Integer> preOrderTraversal(BinaryTreeNode root) {

		List<Integer> list = new ArrayList<Integer>();
		list.add(root.value);

		if (root.left != null) {
			list.addAll(preOrderTraversal(root.left));
		}

		if (root.right != null) {
			list.addAll(preOrderTraversal(root.right));
		}
		return list;
	}

	public List<Integer> inOderTraversal(BinaryTreeNode root) {

		List<Integer> list = new ArrayList<Integer>();

		if (root.left != null) {
			list.addAll(inOderTraversal(root.left));
		}

		list.add(root.value);

		if (root.right != null) {
			list.addAll(inOderTraversal(root.right));
		}
		return list;
	}

	public List<Integer> postOderTraversal(BinaryTreeNode root) {

		List<Integer> list = new ArrayList<Integer>();

		if (root.left != null) {
			list.addAll(postOderTraversal(root.left));
		}

		if (root.right != null) {
			list.addAll(postOderTraversal(root.right));
		}
		list.add(root.value);

		return list;

	}

	public static void main(String[] args) {

		BinaryTree test = new BinaryTree();
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(20);
		numbers.add(22);
		numbers.add(10);
		numbers.add(9);
		numbers.add(11);
		numbers.add(5);
		numbers.add(7);
		
		test.insertSeries(numbers);

		System.out.println(test.inOderTraversal(test.root));
		System.out.println(test.preOrderTraversal(test.root));
		System.out.println(test.postOderTraversal(test.root));
		System.out.println("Values in the tree : " + test.storedValues);
	/*	while(test.root != null){
			System.out.println(test.removeMin().value);
		}
		*/
		System.out.println(test.getDiameter(root));
		System.out.println(test.kDepthNodesNum(root, 20));
		
	}
}
