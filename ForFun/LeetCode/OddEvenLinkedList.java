package LeetCode;

import java.util.*;

public class OddEvenLinkedList {

	ListNode[] list;
	
	private class ListNode {
		int val;
		ListNode next;

		public ListNode(int val) {
			this.val = val;
		}
	}

	public OddEvenLinkedList(int len) {
		// Create a singly linked list
		// 1 -> 2 -> ... -> last -> null

		list = new ListNode[len];
		list[0] = new ListNode(1);
		for (int i = 1; i < len; ++i) {
			list[i] = new ListNode(i + 1);
			list[i - 1].next = list[i];
		}
		list[len - 1].next = null;
	}
	
	public ListNode oddEvenList(ListNode head) {
		if(head == null || head.next == null)
			return head;
		
		ListNode odd = head;
		ListNode even = head.next;
		ListNode firstEven = head.next;
		
		while(even != null && odd != null){
			odd.next = even.next;
			// only advance odd if its next is not null
			odd = odd.next == null ? odd: odd.next;
			
			even.next = odd.next;
			even = even.next;
		}
		odd.next = firstEven;
		return head;
	}

	public void printList(ListNode head, int len){
		for(int i = 0; i < len; ++i){
			System.out.println(head.val);
			head = head.next;
		}
	}

	public static void main(String[] args) {
		int len = 5;
		OddEvenLinkedList test = new OddEvenLinkedList(len);
		test.printList(test.oddEvenList(test.list[0]), len);
		
		System.out.println("hei" + 2%3);
	}

}
