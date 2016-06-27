package LeetCode;

/**
 * @author YuanZhong
 * Source: https://leetcode.com/problems/rectangle-area/
 * Find the total area covered by two rectilinear rectangles in a 2D plane.
 * Each rectangle is defined by its bottom left corner and top right corner.
 * 
 * Assume that the total area is never beyond the maximum possible value of int.
 */


/**
 * -1500000001
0
-1500000000
1
1500000000
0
1500000001
1

Output:
-1294967294
Expected:
2
 * @author YuanZhong
 *
 */

public class RectangleArea {

	private class Rect {
		int width;
		int height;
		
		public Rect (int llX, int llY, int urX, int urY) {
			this.width = urX - llX;
			this.height = urY - llY;
		}
		
		public int getArea() {
			return height * width;
		}
	}
	
	// If two rectangles are not at an angle
	public int computeTotalArea(int A_llX, int A_llY, int A_urX, int A_urY,
			int B_llX, int B_llY, int B_urX, int B_urY) {
		// Calculate total area with overlapping area
		Rect rectA = new Rect(A_llX, A_llY, A_urX, A_urY);
		Rect rectB = new Rect(B_llX, B_llY, B_urX, B_urY);
		int totalArea = rectA.getArea() + rectB.getArea();
		
	//	if(B_llX > A_urX || B_llY > A_urY || B_urX < A_llX || B_urY < A_llY)
	//		return totalArea;
		
		Rect rectIntersect = new Rect(Math.max(A_llX, B_llX),
				Math.max(A_llY, B_llY), Math.min(A_urX, B_urX),
				Math.min(A_urY, B_urY));
		int intersectArea = rectIntersect.getArea();
		
		return totalArea - intersectArea;
	}
	
	public static void main(String[] args) {
		
		RectangleArea ra = new RectangleArea();
		//System.out.println(ra.computeTotalArea(-1501, 0, -1500, 1, 1500, 0, 1501, 1));
		System.out.println(ra.computeTotalArea(-1500000001, 0, -1500000000, 1, 1500000000, 0, 1500000001, 1));
	}
}
