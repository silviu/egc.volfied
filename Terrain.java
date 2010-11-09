import java.util.*;
public class Terrain {

	BrokenLine poli = new BrokenLine(true);
	
	public Terrain() {
		poli.addPointExtdeningSegment(new Point(0, 					 0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, 0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, Volfied.BOARD_HEIGHT));
		poli.addPointExtdeningSegment(new Point(0, 					 Volfied.BOARD_HEIGHT));
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
	
	
	
	public boolean isOuter(Point p) {
		return !(this.poli.toPolygon().contains(p.x, p.y) ||
				 this.poli.isPointOnPerimeter(p));
	}
	
	
	public void cutTerrain(ArrayList<Point> trail_points) {
		int trail_size  = trail_points.size();
		Point start_point = trail_points.get(0);
		Point end_point   = trail_points.get(trail_size -1);
		
		int start_i = 0, end_i = trail_size - 1;		
		
		
		Segment lineOfStart = Volfied.terain.poli.getLinePointIsOn(start_point);
		Segment lineOfEnd   = Volfied.terain.poli.getLinePointIsOn(end_point);
		
		
		
		int insert_pos = Volfied.terain.poli.points.indexOf(lineOfStart.p1);
		
		int start_deletable_zone = Volfied.terain.poli.points.indexOf(lineOfStart.p1);
		int end_deletable_zone   = Volfied.terain.poli.points.indexOf(lineOfEnd.p2);
		Volfied.terain.removePointsinBetween(start_deletable_zone, end_deletable_zone); //TODO
		
		
		
		//doar punctul de inceput si de final vor avea 2 outer
		for (int j = trail_size-1; j >= 0; j--) {
			Volfied.terain.poli.points.add(insert_pos+1, trail_points.get(j));
		}
	}

}
