
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




	public boolean isOuter(Point p) {
		return !(this.poli.toPolygon().contains(p.x, p.y) ||
				this.poli.isPointOnPerimeter(p));
	}



}
