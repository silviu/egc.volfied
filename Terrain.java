import java.awt.Polygon;


public class Terrain {

	BrokenLine poli = new BrokenLine(true);
	double initial_area;

	public Terrain() {
		poli.addPointExtdeningSegment(new Point(0, 					 0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, 0));
		poli.addPointExtdeningSegment(new Point(Volfied.BOARD_WIDTH, Volfied.BOARD_HEIGHT));
		poli.addPointExtdeningSegment(new Point(0, 					 Volfied.BOARD_HEIGHT));
		initial_area = calcArea();
	}

	public boolean isPointOnPerimeter(Point lookup) {
		return poli.isPointOnPerimeter(lookup);
	}

	public double calcArea() {
		Polygon poly_terian = this.poli.toPolygon();
		int n = poly_terian.npoints;
		double area = 0;
		for (int i = 0; i < n-1; i++) {
			area += poly_terian.xpoints[i] 	 * poly_terian.ypoints[i+1] -
					poly_terian.xpoints[i+1] * poly_terian.ypoints[i];
		}
		return area/2;
	}
	
	public int percentage() {
		return (int) ((calcArea()/initial_area) * 100);
	}
	
	public boolean isOuter(Point p) {
		return !(this.poli.toPolygon().contains(p.x, p.y) ||
				this.poli.isPointOnPerimeter(p));
	}



}
