import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;

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
		if (lookup.y == p1.y
				&& lookup.y == p2.y
				&& (lookup.x > p1.x && lookup.x < p2.x || lookup.x < p1.x
						&& lookup.x > p2.x))
			return true;

		// this is a vertical segment, and the point we're looking for
		// is between the Y coords of the two points
		if (lookup.x == p1.x
				&& lookup.x == p2.x
				&& (lookup.y > p1.y && lookup.y < p2.y || lookup.y < p1.y
						&& lookup.y > p2.y))
			return true;

		return false;
	}

	public boolean equals(Segment s) {
		return p1.equals(s.p1) && p2.equals(s.p2)
		|| p2.equals(s.p1) && p1.equals(s.p2);
	}

	public int length() {
		return p1.dist(p2);
	}

	@Override
	public String toString() {
		return "Segment {p1=" + p1 + ", p2=" + p2 + "}";
	}

	// TODO: add tests.
}

public class BrokenLine {

	boolean isClosedLine;
	public ArrayList<Point> points = new ArrayList<Point>();

	public BrokenLine(boolean isClosedLine) {
		this.isClosedLine = isClosedLine;
	}

	public BrokenLine(boolean isClosedLine, Point[] arr) {
		this.isClosedLine = isClosedLine;
		for (Point p : arr)
			this.addPointExtdeningSegment(p);
	}

	public BrokenLine(boolean isClosedLine, ArrayList<Point> arr) {
		this.isClosedLine = isClosedLine;
		for (Point p : arr)
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
		Segment seg = new Segment(points.get(n - 1), points.get(0));
		if (seg.isPointOnSegment(lookup))
			return seg;

		return null;
	}

	public boolean isPointOnPerimeter(Point lookup) {
		// if there exists a segment on which this point resides then
		// the point is on the perimeter of the broken line
		return getLinePointIsOn(lookup) != null;
	}

	public void draw(Graphics g, int off_x, int off_y) {
		int n = points.size();

		for (int i = 0; i < n - 1; i++)
			g.drawLine(off_x + points.get(i).x, off_y + points.get(i).y,
					off_x + points.get(i + 1).x, off_y + points.get(i + 1).y);

		if (isClosedLine)
			g.drawLine(off_x + points.get(n - 1).x, off_y + points.get(n - 1).y,
					off_x + points.get(0).x, off_y + points.get(0).y);

	}

	@Override
	public String toString() {
		String ret = "";
		for (Point p : this.points)
			ret += p + " ";
		ret += "\n";
		return ret;
	}

	public void addPointExtdeningSegment(Point p) {
		int prev_pos = this.points.size() - 1;
		int pre_prev_pos = this.points.size() - 2;

		// there is no segment to extend
		if (pre_prev_pos < 0) {
			this.points.add(p);
			return;
		}

		Point prev = this.points.get(prev_pos);
		Point pre_prev = this.points.get(pre_prev_pos);
		if (prev.x == pre_prev.x && prev.x == p.x || prev.y == pre_prev.y
				&& prev.y == p.y)
			this.points.set(prev_pos, p); // overwrite last point with the new
		// one
		else
			this.points.add(p);
	}

	public Polygon toPolygon() {
		int n = this.points.size();
		int x[] = new int[n];
		int y[] = new int[n];
		for (int i = 0; i < n; i++) {
			Point p = this.points.get(i);
			x[i] = p.x;
			y[i] = p.y;
		}
		return new Polygon(x, y, n);
	}

	public int getPerimeterLength() {
		int len = 0;
		int n = points.size();

		for (int i = 0; i < n - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i == n - 1 ? 0 : i + 1);
			Segment seg = new Segment(p1, p2);
			len += seg.length();
		}

		if (isClosedLine) {
			// in closed lines there is a line from the last to the first point
			Segment seg = new Segment(points.get(n - 1), points.get(0));
			len += seg.length();
		}

		return len;
	}

	/*
	 * Returns the two BrokenLine representing two polygons that result from
	 * cutting this BrokenLine by the given trail.
	 */
	public BrokenLine[] cutTerrain(BrokenLine trail) {
		int n = this.points.size();

		// make a copy of the point list to not mess up with the original.
		@SuppressWarnings("unchecked")
		ArrayList<Point> trail_points = (ArrayList<Point>) trail.points.clone();

		int trail_size    = trail_points.size();
		Point trail_start = trail_points.get(0);
		Point trail_stop  = trail_points.get(trail_size - 1);

		Segment start_seg = this.getLinePointIsOn(trail_start);
		Segment end_seg   = this.getLinePointIsOn(trail_stop);


		// decide whether we need to reverse the trail points before adding it
		// to the new borders.
		boolean reverse_tail_points = false;

		if (start_seg.equals(end_seg)) {
			/*
			 *   A trail that starts and stops on the same segment, can go
			 *   in two directions. If the trail is not normalized, we need to
			 *   reverse it to bring it to the same form as the normalized one.
			 * 
			 * 
			 *   Case I) Normalized
			 *   Resulting polygons:
			 *     - BE: ABCD 1230
			 *     - BI: DCBA
			 *
			 *   0-----A---D---1
			 *   |     |   ^   |
			 *   |     |   ^   |
			 *   |     B>>>C   |
			 *   |             |
			 *   3-------------2
			 *
			 *   Case II) Unnormalized
			 *   Resulting polygons:
			 *     - BE: DCBA 1230
			 *     - BI: ABCD
			 * 
			 *   0-----D---A---1
			 *   |     ^   |   |
			 *   |     ^   |   |
			 *   |     C<<<B   |
			 *   |             |
			 *   3-------------2
			 * 
			 *   The difference between the two cases:
			 *   - normalized:
			 *    		the first point of the trail (A) is at a SMALLER
			 *     		distance than the last point (D) from the starting
			 *     		point of the segment
			 *   - unnormalized:
			 *    		the first point of the trail (A) is at a BIGGER
			 *     		distance than the last point (D) from the starting
			 *     		point of the segment
			 */
			if (start_seg.p1.dist(trail_start) > start_seg.p1.dist(trail_stop))
				reverse_tail_points = true;
		} else {
			/*
			 *   A trail that starts and stops on different segments, can go
			 *   in two directions. If the trail is not normalized, we need to
			 *   reverse it to bring it to the same form as the normalized one.
			 * 
			 * 
			 *   Case I) Normalized
			 *   Resulting polygons:
			 *     - BE: ABC 230
			 *     - BI: 1CBA
			 *
			 *   0-----A-------1
			 *   |     |       |
			 *   |     |       |
			 *   |     B>>>>>>>C
			 *   |             |
			 *   3-------------2
			 *
			 *   Case II) Unnormalized
			 *   Resulting polygons:
			 *     - BE: CBA 230
			 *     - BI: 1ABC
			 * 
			 *   0-----C-------1
			 *   |     ^       |
			 *   |     ^       |
			 *   |     B<<<<<<<A
			 *   |             |
			 *   3-------------2
			 * 
			 *   The difference between the two cases:
			 *   - normalized:
			 *    		the first point of the trail (A) is closer to the
			 *    		start of the polygon than the last point of the trail (D)
			 *   - unnormalized:
			 *    		the first point of the trail (A) is farther from the
			 *    		start of the polygon than the last point of the trail (D)
			 */
			if (points.indexOf(start_seg.p1) > points.indexOf(end_seg.p1))
				reverse_tail_points = true;
		}

		if (reverse_tail_points) {
			Collections.reverse(trail_points);
			Segment aux = start_seg;
			start_seg = end_seg;
			end_seg = aux;
		}

		int i1 = points.indexOf(start_seg.p1);
		int i2 = points.indexOf(end_seg.p2);

		BrokenLine be = new BrokenLine(true);
		BrokenLine bi = new BrokenLine(true);


		int off_e = i1 < i2 ? n : 0;
		for (int i = i2; i != i1 + 1 + off_e; i++)
		{
			be.addPointExtdeningSegment(this.points.get(i % n));
		}

		for(int i = 0; i < trail_size; i++) {
			be.addPointExtdeningSegment(trail_points.get(i));
			bi.addPointExtdeningSegment(trail_points.get(trail_size - 1 - i));
		}


		int off_i = n - off_e;
		for (int i = i1 + 1; i != i2 + off_i; i++)
		{
			bi.addPointExtdeningSegment(this.points.get(i % n));
		}

		return new BrokenLine[] {be.rotate(), bi.rotate()};
	}

	private BrokenLine rotate() {
		int n = points.size();
		if (points.get(0).y == points.get(1).y &&
				points.get(0).x <  points.get(1).x)
			return this;
		Point last = points.remove(n-1);
		points.add(0, last);
		return this.rotate();
	}

	public void addPointsExtdeningSegment(ArrayList<Point> pts) {
		for (Point p : pts)
			this.addPointExtdeningSegment(p);
	}

	static void test_isPointOnPerimeter() {
		int N = 100;
		Point points[] = { new Point(0, 0), new Point(N, 0), new Point(N, N),
				new Point(0, N), };
		BrokenLine bo = new BrokenLine(false, points);
		BrokenLine bc = new BrokenLine(true, points);

		for (Point p : points) {
			assert bo.isPointOnPerimeter(p);
			assert bc.isPointOnPerimeter(p);
		}

		// check that points on the segments are picked up correctly
		// and that there IS NO segment from the last point to the first
		assert bo.isPointOnPerimeter(new Point(N / 2, 0));
		assert bo.isPointOnPerimeter(new Point(N / 2, N));
		assert !bo.isPointOnPerimeter(new Point(0, N / 2)) : "Open line sees point on the closing segment";
		assert bo.isPointOnPerimeter(new Point(N, N / 2));

		// check that points on the segments are picked up correctly
		// and that there IS A segment from the last point to the first
		assert bc.isPointOnPerimeter(new Point(N / 2, 0));
		assert bc.isPointOnPerimeter(new Point(N / 2, N));
		assert bc.isPointOnPerimeter(new Point(0, N / 2)) : "Closed line does not see point on the closing segment";
		assert bc.isPointOnPerimeter(new Point(N, N / 2));

		// an obviously not-on-the perimetter point.
		assert !bo.isPointOnPerimeter(new Point(N / 2, N / 2));
		assert !bc.isPointOnPerimeter(new Point(N / 2, N / 2));
	}

	static void test_addPointExteningSegment() {
		int N = 100;
		Point points[] = {

				// these points are on the same line.
				// Only the first and the last must remain
				new Point(0, 0), new Point(N / 2, 0), new Point(N / 3, 0),
				new Point(2 * N / 3, 0), new Point(N, 0),

				new Point(N, N), new Point(0, N), };

		BrokenLine bo = new BrokenLine(false, points);
		BrokenLine bc = new BrokenLine(true, points);

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
