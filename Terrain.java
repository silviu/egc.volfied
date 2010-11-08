import java.awt.event.KeyEvent;
import java.util.*;
public class Terrain {

	BrokenLine poli = new BrokenLine(true);
	
	public Terrain() {
		poli.addPointExtdeningSegment(new Point(0, 					0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, 0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, Volfied.BOARD_HEIGHT));
		poli.addPointExtdeningSegment(new Point(0, 					Volfied.BOARD_HEIGHT));
	}
	
	public boolean isPointOnPerimeter(Point lookup) {
		return poli.isPointOnPerimeter(lookup);
	}
	
		
	
	
	
	public void removePointsinBetween(int start_i, int end_i) {
		System.out.println("******************start_i = " + start_i);
		System.out.println("******************end_i   = " + end_i);
		for (int i = start_i+1; i < end_i; i++ )
		Volfied.terain.poli.points.remove(start_i+1);
		
	}
	
	public static boolean isSameLine(ArrayList<Point> line1, ArrayList<Point> line2) {
		if ((line1.get(0).x == line2.get(0).x) && (line1.get(0).y == line2.get(0).y))
			return true;
		return false;
	}
	
	public ArrayList<Point> getLinePointIsOn(Point lookup) {
		ArrayList<Point> ret = new ArrayList<Point>();
		int n = Volfied.terain.poli.points.size();
		
		for (int i = 0; i < n; i++) {
			Point curr_point = Volfied.terain.poli.points.get(i);
			Point next_point = Volfied.terain.poli.points.get((i == n - 1) ? 0 : i + 1);
			
			if (lookup.y == curr_point.y && lookup.x == curr_point.x)
			{
				Point prev_point = Volfied.terain.poli.points.get(i == 0 ? n - 1 : i - 1);
				ret.add(prev_point);
				ret.add(curr_point);
				ret.add(next_point);
				return ret;
			}
			
			if ((lookup.y == curr_point.y) && (lookup.y == next_point.y))
				if (((lookup.x >  curr_point.x) && (lookup.x <  next_point.x)) ||
					((lookup.x <  curr_point.x) && (lookup.x >  next_point.x))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}
			
			if ((lookup.x == curr_point.x) && (lookup.x == next_point.x))
				if (((lookup.y >  curr_point.y) && (lookup.y <  next_point.y)) ||
					((lookup.y <  curr_point.y) && (lookup.y >  next_point.y))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}

		}
		return ret;
	}
	
	
	
	public ArrayList<Point> getOuterForNoCornerLine(Point pt) {
		
		ArrayList<Point> linePointIsOn = Volfied.terain.getLinePointIsOn(new Point(Player.x, Player.y));
		ArrayList<Point> ret = new ArrayList<Point>();
		
		Point start_point  = linePointIsOn.get(0);
		Point end_point    = linePointIsOn.get(1);
		
		int start_point_i  = Volfied.terain.poli.points.indexOf(start_point);
		int end_point_i    = Volfied.terain.poli.points.indexOf(end_point);
		
		Point left_corner  = Volfied.terain.poli.points.get(start_point_i - 1);
		Point right_corner = Volfied.terain.poli.points.get(end_point_i + 1);
		
		ret.add(left_corner);
		ret.add(right_corner);
		return ret;
	}
	
	public boolean isOuter(Point p) {
		return !this.poli.toPolygon().contains(p.x, p.y);
	}
	
	
	public void cutTerrain(ArrayList<Point> points) {
		int trail_size  = points.size();
		
		int start_i = -1, end_i = -1;
		
		Point start_point = new Point();
		Point end_point = new Point();
		

		start_i = 0;
		end_i = trail_size - 1;
		
		
		start_point = points.get(0);
		end_point	= points.get(trail_size -1);
		
		ArrayList<Point> lineOfStart = Volfied.terain.getLinePointIsOn(start_point);
		ArrayList<Point> lineOfEnd = Volfied.terain.getLinePointIsOn(end_point);
		
		
		System.out.println("Start_i=[" + start_i +"]");
		System.out.println("End_i=[" + end_i +"]");
		
		System.out.println("Start_point=[" + start_point.x + "," + start_point.y +"]");
		System.out.println("End_point=[" + end_point.x + "," + end_point.y +"]");
		
		
		
		
		
		
		
		
		ArrayList<Point> bla = Volfied.terain.getLinePointIsOn(start_point);
		int insert_pos = Volfied.terain.poli.points.indexOf(bla.get(0));
		
		int start_deletable_zone = Volfied.terain.poli.points.indexOf(lineOfStart.get(0));
		int end_deletable_zone   = Volfied.terain.poli.points.indexOf(lineOfEnd.get(1));
		Volfied.terain.removePointsinBetween(start_deletable_zone, end_deletable_zone);
		
		//doar punctul de inceput si de final vor avea 2 outer
		for (int j = trail_size-1; j >= 0; j--) {
			Volfied.terain.poli.points.add(insert_pos+1, points.get(j));
		}
	}

	/*
	 * Rotunjeste un punct la o pozitie de pe poligon.
	 * Rotunjirea se face doar daca 'pase' e mai mic decat diferenta de la punctul curent la
	 */
	public Point roundPoint(Point p, int pase) {
		
		/*
		if (p.y - pase < 0)
			p.y = 0;
		else
			p.y -= pase;
		
		if (p.x - pase < 0)
			p.x = 0;
		else
			p.x -= pase;
		*/
		return p;
	}
}
