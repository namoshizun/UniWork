package LeetCode;

import java.util.*;

/*-https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/
 * One way to serialize a binary tree is to use pre-order traversal. When we encounter a non-null node, 
 * we record the node's value. If it is a null node, we record using a sentinel value such as #.

     _9_
    /   \
   3     2
  / \   / \
 4   1  #  6
/ \ / \   / \
# # # #   # #

  For example, the above binary tree can be serialized to the string "9,3,4,#,#,1,#,#,2,#,6,#,#", 
  where # represents a null node.

  Given a string of comma separated values, verify whether it is a correct preorder traversal 
  serialization of a binary tree. Find an algorithm without reconstructing the tree.

  Each comma separated value in the string must be either an integer or a character '#' 
  representing null pointer.You may assume that the input format is always valid, for 
  example it could never contain two consecutive commas such as "1,,3".

	Example 1:
	"9,3,4,#,#,1,#,#,2,#,6,#,#"
	Return true
 */
public class Verify_Preorder_Serialization_of_a_Binary_Tree {

	public static boolean isValidSerialization(String preorder) {
		String[] nodes = preorder.split(",");
		int len = nodes.length;
		// Base check: see whether it is a proper binary tree.
		if (len % 2 != 1)
			return false;

		int diff = 1;

		for (String node : nodes) {
			// Each node contributes 1 indegree, therefore decrease the free
			// outdegree of its parent
			if (--diff < 0)
				return false;
			// Each node contributes 2 more free outdegrees.
			if (!node.equals("#"))
				diff += 2;
		}
		return diff == 0;
	}

	public static void main(String[] args) {
		String test = "9,#,92,#,#";
		boolean res = isValidSerialization(test);
		System.out.println(res);

	}

}
