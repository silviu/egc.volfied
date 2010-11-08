import java.util.ArrayList;

public class BrokenLine {

	boolean isClosedLine;
	public ArrayList<Point> points = new ArrayList<Point>();

	
	public BrokenLine(boolean isClosedLine) {
		this.isClosedLine = isClosedLine;
	}

	public BrokenLine(boolean isClosedLine, Point [] arr) {
		this.isClosedLine = isClosedLine;
		for (Point p:arr)
			this.addPointExteningSegment(p);
	}

	public BrokenLine(boolean isClosedLine, ArrayList<Point> arr) {
		this.isClosedLine = isClosedLine;
		for (Point p:arr)
			this.addPointExteningSegment(p);
	}

	
	
	public boolean isPointOnSegment(Point lookup, Point p1, Point p2)
	{
		// the point we're looking for identical to a margin of the segment 
		if (lookup.y == p1.y && lookup.x == p1.x)
			return true;
		if (lookup.y == p2.y && lookup.x == p2.x)
			return true;


		// this is a horizontal segment, and the point we're looking for
		// is between the X coords of the two points
		if  ((lookup.y == p1.y && lookup.y == p2.y) &&
			((lookup.x  > p1.x && lookup.x  < p2.x) ||
			 (lookup.x  < p1.x && lookup.x  > p2.x)))
				return true;

		// this is a vertical segment, and the point we're looking for
		// is between the Y coords of the two points
		if  ((lookup.x == p1.x && lookup.x == p2.x) &&
		    ((lookup.y  > p1.y && lookup.y  < p2.y) ||
			 (lookup.y  < p1.y && lookup.y  > p2.y)))
					return true;

		return false;
	}

	
	public boolean isPointOnPerimeter(Point lookup) {
		int n = points.size();

		for (int i = 0; i < n - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);
			if (isPointOnSegment(lookup, p1, p2))
				return true;
		}
		
		if (!isClosedLine)
			return false;
	
		// in closed lines there is a line from the last to the first point 
		return isPointOnSegment(lookup, points.get(0), points.get(n-1));
	}

	
	public String toString()
	{
		String ret = "";
		int n = this.points.size();
		for (int i = 0; i < n; i++)
			ret += "TRAIL: X=[" + this.points.get(i).x +
							 "]; Y=["   + this.points.get(i).y + 
							 "]; OUTER=" + this.points.get(i).outer.toString() + " ";
		ret += "\n";
		return ret;
	}


	public void addPointExteningSegment(Point p)
	{
		int prev_pos     = this.points.size() - 1;
		int pre_prev_pos = this.points.size() - 2;
		
		// there is no segment to extend
		if (pre_prev_pos < 0) {
			this.points.add(p);
			return;
		}
			
		Point prev     = this.points.get(prev_pos);
		Point pre_prev = this.points.get(pre_prev_pos);
		if ((prev.x == pre_prev.x && prev.x == p.x) ||
			(prev.y == pre_prev.y && prev.y == p.y))
			this.points.set(prev_pos, p); // overwrite last point with the new one
		else
			this.points.add(p);
	}
	
	
	static void test_isPointOnPerimeter()
	{
		int N = 100;
		Point points [] = {
				new Point(0, 0),
				new Point(N, 0),
				new Point(N, N),
				new Point(0, N),						
		};
		BrokenLine bo   = new BrokenLine(false, points);
		BrokenLine bc = new BrokenLine(true,  points);
		

		for (Point p:points) {
			assert bo.isPointOnPerimeter(p);
			assert bc.isPointOnPerimeter(p);
		}
		
		// check that points on the segments are picked up correctly
		// and that there IS NO segment from the last point to the first
		assert bo.isPointOnPerimeter(new Point(N/2, 0));
		assert bo.isPointOnPerimeter(new Point(N/2, N));
		assert !bo.isPointOnPerimeter(new Point(0, N/2)): "Open line sees point on the closing segment";
		assert bo.isPointOnPerimeter(new Point(N, N/2));

		// check that points on the segments are picked up correctly
		// and that there IS A segment from the last point to the first
		assert bc.isPointOnPerimeter(new Point(N/2, 0));
		assert bc.isPointOnPerimeter(new Point(N/2, N));
		assert bc.isPointOnPerimeter(new Point(0, N/2)): "Closed line does not see point on the closing segment";
		assert bc.isPointOnPerimeter(new Point(N, N/2));
		
		// an obviously not-on-the perimetter point.
		assert !bo.isPointOnPerimeter(new Point(N/2, N/2));
		assert !bc.isPointOnPerimeter(new Point(N/2, N/2));		
	}

	static void test_addPointExteningSegment()
	{
		int N = 100;
		Point points [] = {
				
				// these points are on the same line.
				// Only the first and the last must remain
				new Point(0, 0),
				new Point(N/2, 0),
				new Point(N/3, 0),
				new Point(2*N/3, 0),
				new Point(N, 0),
				
				
				new Point(N, N),
				new Point(0, N),						
		};

		BrokenLine bo   = new BrokenLine(false, points);
		BrokenLine bc = new BrokenLine(true,  points);
		
		assert bo.points.size() == 4;
		assert bc.points.size() == 4;
	}
	
	public static void main(String[] args) {
		// some tests to check if all works correctly
		test_isPointOnPerimeter();
		test_addPointExteningSegment();
		System.out.println("All tests succeeded!");
	}
}
