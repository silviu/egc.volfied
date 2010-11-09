import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

class Segment {

	public final Point p2;
	public final Point p1;

	public Segment(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public boolean isPointOnSegment(Point lookup) {
		// the point we're looking for identical to a margin of the segment
		if (lookup.y == p1.y && lookup.x == p1.x)
			return true;
		if (lookup.y == p2.y && lookup.x == p2.x)
			return true;


		// this is a horizontal segment, and the point we're looking for
		// is between the X coords of the two points
		if  (lookup.y == p1.y && lookup.y == p2.y &&
				(lookup.x  > p1.x && lookup.x  < p2.x ||
						lookup.x  < p1.x && lookup.x  > p2.x))
			return true;

		// this is a vertical segment, and the point we're looking for
		// is between the Y coords of the two points
		if  (lookup.x == p1.x && lookup.x == p2.x &&
				(lookup.y  > p1.y && lookup.y  < p2.y ||
						lookup.y  < p1.y && lookup.y  > p2.y))
			return true;

		return false;
	}

	//TODO: add tests.
}

public class BrokenLine {

	boolean isClosedLine;
	public ArrayList<Point> points = new ArrayList<Point>();


	public BrokenLine(boolean isClosedLine) {
		this.isClosedLine = isClosedLine;
	}


	public BrokenLine(boolean isClosedLine, Point [] arr) {
		this.isClosedLine = isClosedLine;
		for (Point p:arr)
			this.addPointExtdeningSegment(p);
	}


	public BrokenLine(boolean isClosedLine, ArrayList<Point> arr) {
		this.isClosedLine = isClosedLine;
		for (Point p:arr)
			this.addPointExtdeningSegment(p);
	}


	public Segment getLinePointIsOn(Point lookup) {
		int n = points.size();

		for (int i = 0; i < n - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i == n - 1 ? 0 : i + 1);

			Segment seg = new Segment(p1, p2);
			if (seg.isPointOnSegment(lookup))
				return seg;
		}

		if (!isClosedLine)
			return null;

		// in closed lines there is a line from the last to the first point
		Segment seg = new Segment(points.get(0), points.get(n-1));
		if (seg.isPointOnSegment(lookup))
			return seg;

		return null;
	}



	public boolean isPointOnPerimeter(Point lookup)
	{
		// if there exists a segment on which this point resides then
		// the point is on the perimeter of the broken line
		return getLinePointIsOn(lookup) != null;
	}


	public void draw(Graphics g, int off_x, int off_y)
	{
		int n = points.size();

		for (int i = 0; i < n - 1; i++)
			g.drawLine(off_x + points.get(i).x,   off_y + points.get(i).y,
					off_x + points.get(i+1).x, off_y + points.get(i+1).y);

		if (isClosedLine)
			g.drawLine(off_x + points.get(0).x,   off_y + points.get(0).y,
					off_x + points.get(n-1).x, off_y + points.get(n-1).y);
	}


	@Override
	public String toString()
	{
		String ret = "";
		for (Point p : this.points)
			ret += p + " ";
		ret += "\n";
		return ret;
	}


	public void addPointExtdeningSegment(Point p)
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
		if (prev.x == pre_prev.x && prev.x == p.x ||
				prev.y == pre_prev.y && prev.y == p.y)
			this.points.set(prev_pos, p); // overwrite last point with the new one
		else
			this.points.add(p);
	}


	public Polygon toPolygon()
	{
		int n = this.points.size();
		int x[] = new int[n];
		int y[] = new int[n];
		for (int i = 0; i < n; i++)
		{
			Point p = this.points.get(i);
			x[i] = p.x;
			y[i] = p.y;
		}
		return new Polygon(x, y, n);
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
		BrokenLine bo = new BrokenLine(false, points);
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

		BrokenLine bo = new BrokenLine(false, points);
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


	public void removePointsinBetween(int start_i, int end_i) {
		System.out.println("******************start_i = " + start_i);
		System.out.println("******************end_i   = " + end_i);
		for (int i = start_i+1; i < end_i; i++ )
			points.remove(start_i+1);
	}


	public void cutTerrain(BrokenLine trail) {
		int trail_size    = trail.points.size();
		Point start_point = trail.points.get(0);
		Point end_point   = trail.points.get(trail_size - 1);

		Segment lineOfStart = this.getLinePointIsOn(start_point);
		Segment lineOfEnd   = this.getLinePointIsOn(end_point);



		int insert_pos = this.points.indexOf(lineOfStart.p1);

		int start_deletable_zone = this.points.indexOf(lineOfStart.p1);
		int end_deletable_zone   = this.points.indexOf(lineOfEnd.p2);
		this.removePointsinBetween(start_deletable_zone, end_deletable_zone); //TODO



		//doar punctul de inceput si de final vor avea 2 outer
		for (int j = trail_size-1; j >= 0; j--) {
			this.points.add(insert_pos+1, trail.points.get(j));
		}

	}
}
