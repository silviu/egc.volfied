import java.util.ArrayList;


public class Point {
	public static final int LE  = -2;
	public static final int UP  = -1;
	public static final int RI  = 0;
	public static final int DO  = 1;
	public static final int NAN = -5;
	
	int x;
	int y;
	ArrayList<Integer> outer = new ArrayList<Integer>();
	
	public Point(){};
	public Point(int p_x, int p_y) {
		this.x = p_x;
		this.y = p_y;
	}
	public Point(int p_x, int p_y, int p_outer1, int p_outer2) {
		this.x = p_x;
		this.y = p_y;
		if (p_outer1 != NAN)
			this.outer.add(p_outer1);
		if (p_outer2 != NAN)
			this.outer.add(p_outer2);
	}
	public void addOuter(int p_outer) {
		this.outer.add(p_outer);
	}
}
