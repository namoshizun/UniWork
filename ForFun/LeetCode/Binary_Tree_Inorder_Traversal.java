package LeetCode;

import java.util.*;

/**
 * TreeNode buildTree(char[] OJtree);
 * List<Integer> Recur_Naive(TreeNode root);
 * 				 Itr_Naive (TreeNode root);
 * 				 Morris_Traversal (TreeNode root);
 */

public class Binary_Tree_Inorder_Traversal {
	/*-
	 * https://leetcode.com/problems/binary-tree-inorder-traversal/
	 * How does the char[] represents a tree:
	 * 
	 *  '#' signifies a path terminator where no node exists below.
	 * { '1', '2', '3', '#', '#', '4', '#', '#', '5' }
	 * each non-'#' char contributes two out-degree, therefore adds the size
	 * of the next level by 2.
	 * 
	 */
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	public void insertNode(TreeNode parent, TreeNode child, boolean isLeftChild) {
		if (isLeftChild) {
			parent.left = child;
		} else {
			parent.right = child;
		}
	}

	/**
	 * Generate a binary tree according to the input receipt
	 */
	public TreeNode buildTree(char[] OJtree) {
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		TreeNode root = null;
		int length = OJtree.length;

		if (OJtree[0] != 0 && OJtree[0] != '#') {
			int power = 0;
			int count = 0;
			int beginIdx = 0;
			int nodesLeft = length - 1;
			int numNodesCurr = 1;
			root = new TreeNode(OJtree[0] - '0');
			queue.add(root);

			while (nodesLeft > 0) {

				// construct the tree
				for (int i = beginIdx; i < beginIdx + numNodesCurr; ++i) {
					if (OJtree[i] != '#') {
						/*-
						 * Find the index of corresponding child:
						 * begin + numNodesCurr = begin index of next level
						 * 			  + count*2 = begin index of the corresponding child
						 */
						TreeNode node = queue.poll();
						int idxLeftChild = beginIdx + numNodesCurr + count * 2;
						char left = OJtree[idxLeftChild];
						char right = idxLeftChild == length - 1 ? '#' : OJtree[idxLeftChild + 1];

						if (left != '#') {
							TreeNode lChild = new TreeNode(left - '0');
							insertNode(node, lChild, true);
							queue.add(lChild);
						}
						if (right != '#') {
							TreeNode rChild = new TreeNode(right - '0');
							insertNode(node, rChild, false);
							queue.add(rChild);
						}
						/*
						 * power: used to calculate the number of nodes on the
						 * next level count: the number of non-'#' nodes on this
						 * level
						 */
						++power;
						++count;
					}
				}

				// updates for the next round;
				beginIdx += numNodesCurr;
				numNodesCurr = (int) Math.pow(2, power);
				nodesLeft -= numNodesCurr;
				power = 0;
				count = 0;
			}
		}

		return root;
	}

	/**
	 * Recursion version - naive and trivial
	 */
	public List<Integer> Recur_Naive(TreeNode root) {
		List<Integer> l = new ArrayList<Integer>();
		if (root.left != null) {
			l.addAll(Recur_Naive(root.left));
		}

		l.add(root.val);

		if (root.right != null) {
			l.addAll(Recur_Naive(root.right));
		}

		return l;
	}

	/**
	 * Iterative version - no recursion.
	 */
	public List<Integer> Itr_Naive(TreeNode root) {
		if (root == null)
			return new ArrayList<Integer>();

		List<Integer> l = new ArrayList<Integer>();
		Stack<TreeNode> stack = new Stack<TreeNode>();
		TreeNode currNode = root;
		TreeNode node = null;

		while (!stack.isEmpty() || currNode != null) {

			// always try to add the leftmost node to the traversal list
			if (currNode != null) {
				stack.push(currNode);
				currNode = currNode.left;
				/*
				 * if no further down to the left side add the currently
				 * leftmost node to be list go to the right sub-tree.
				 */
			} else {
				node = stack.pop();
				l.add(node.val);
				currNode = node.right;
			}
		}
		return l;
	}

	/*-
	 * Source: http://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion-and-without-stack/
	 * 
	 * Time complexity: O(n) Space complexity: O(1) <- PRIVILLEGE PART! GENERALLY FASTER THAN THE OTHER TWO! 
	 * 
	 * Using Morris Traversal, we can traverse the tree without using stack and recursion. 
	 * The idea of Morris Traversal is based on Threaded Binary Tree. In this traversal, 
	 * we first create links to Inorder successor and print the data using these links, 
	 * and finally revert the changes to restore original tree.
	 * 
	 * 1. Initialize current as root 
	 * 2. While current is not NULL
	 * 	If current does not have left child
	 * 		a) Print current’s data
	 * 		b) Go to the right, i.e., current = current->right
	 * 	Else
	 * 		a) Make current as right child of the rightmost node in current's left subtree   <- MAGIC PART!
	 * 		b) Go to this left child, i.e., current = current->left
	 * 
	 * Although the tree is modified through the traversal, it is reverted back to its original shape 
	 * after the completion. Unlike Stack based traversal, no extra space is required for this traversal.
	 */
	public List<Integer> MorrisTraversal(TreeNode root) {
		if (root == null)
			return new ArrayList<Integer>();

		List<Integer> l = new ArrayList<Integer>();
		TreeNode curr = root;
		TreeNode prev = null; // the predecessor of curr

		while (curr != null) {
			
			if (curr.left == null) {
				// visit leftmost child
				l.add(curr.val);
				// jump to the link 
				curr = curr.right;
				
			} else {
				/*
				 * jump to the left sub-tree
				 * find the rightmost right-child
				 */
				prev = curr.left;
				while (prev.right != null && prev.right != curr) {
					prev = prev.right;
				}

				// make the link (rightmost child -> curr) if there hasn't been one
				if (prev.right == null) {
					prev.right = curr;
					curr = curr.left;
					
				} else {
					// if there has been the link, restore the tree structure (each leaf should be null)
					prev.right = null;
					// all left sub-nodes are visited, so visit the current node
					l.add(curr.val);
					// start right sub-tree
					curr = curr.right;
				}
			}
		}

		return l;
	}

	public static void main(String[] args) {
		Binary_Tree_Inorder_Traversal run = new Binary_Tree_Inorder_Traversal();
		char[] OJtree = { '1', '2', '3', '#', '#', '4', '#', '#', '5' };
		// char[] OJtree = { '1', '#', '2', '3' };
		TreeNode treeRoot = run.buildTree(OJtree);

		System.out.println(run.Recur_Naive(treeRoot));
		System.out.println(run.Itr_Naive(treeRoot));
		System.out.println(run.MorrisTraversal(treeRoot));
	}

}