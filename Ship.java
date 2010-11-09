import java.awt.*;
import java.util.*;

public class Ship {
	final static int MIN_SIZE = 75;
	final static int MAX_SIZE = 120;

	final static int MOVEMENT_PASE = 5;
	final static int RESIZING_PASE = 1;

	static final int NORTH = 1;
	static final int EAST  = 2;
	static final int SOUTH = 3;
	static final int WEST  = 4;
	static final int NEAST = 5;
	static final int NWEST = 6;
	static final int SEAST = 7;
	static final int SWEST = 8;

	int x = 50;
	int y = 50;
	int size = MAX_SIZE;

	int keep_direction = 0;
	int direction = NORTH;
	boolean growing = true;



	Random rand = new Random();

	public Ship(int init_x, int init_y) {
		this.x = init_x;
		this.y = init_y;
	}

	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillPolygon(this.getPaintable());
	}


	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}

	@Override
	public String toString() {
		String ret = "SHIP position=" + x + ", " + y + "\n";
		Polygon poli = getPolygon();
		for (int i = 0; i < poli.npoints; i++)
			ret += "P[" + i + "]=[" + poli.xpoints[i] + ", " + poli.ypoints[i] + "] ";
		ret += "\n";
		return ret;
	}

	private Polygon getPolygon() {
		Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(0, size);
		p.addPoint(size, size);
		p.addPoint(size, 0);
		return p;
	}

	public void resize() {
		if (growing && size == MAX_SIZE)
			growing = false;
		else if (!growing && size == MIN_SIZE)
			growing = true;


		int old_size = size;

		if (growing)
			size += RESIZING_PASE;
		else
			size -= RESIZING_PASE;

		// if with the new size we're getting out of the terrain
		// revert the resize operation (keep the old size) and
		// revert the growing/shrinking status
		if (isOuter(new Point(x, y))) {
			size = old_size;
			growing = !growing;
		}
	}

	public Polygon getPaintable() {
		Polygon poli = getPolygon();
		Polygon ret = new Polygon(poli.xpoints, poli.ypoints, poli.npoints);
		ret.translate(x + Volfied.GRID_X, y + Volfied.GRID_Y);
		return ret;
	}

	public Polygon getFactorisedPolygon() {
		Polygon poli = getPolygon();
		Polygon cp_poly = new Polygon(poli.xpoints, poli.ypoints, poli.npoints);
		int n = cp_poly.npoints;
		for (int i = 0; i < n; i++) {
			cp_poly.xpoints[i] += x;
			cp_poly.ypoints[i] += y;
		}
		return cp_poly;
	}

	public int getPosition(BrokenLine[] polys) {
		if (polys[0].toPolygon().contains(this.x, this.y))
			return 0;
		if (polys[1].toPolygon().contains(this.x, this.y))
			return 1;
		return 1;
	}


	public void generateDirection() {
		direction = rand.nextInt(SWEST - NORTH + 1) + NORTH;
	}


	public boolean isOuter(Point nex_ship_pos) {
		Polygon poli = getPolygon();
		int n = poli.npoints;
		for (int i = 0; i < n; i++)
			if (Volfied.terain.isOuter(new Point(poli.xpoints[i] + nex_ship_pos.x, poli.ypoints[i] + nex_ship_pos.y)))
				return true;
		return false;
	}

	public void animate() {
		resize();
		if (keep_direction == 400) {
			generateDirection();
			keep_direction = 0;
		}
		Point curr_ship_pos = new Point(this.x, this.y);
		Point nex_shipp_pos = curr_ship_pos.getShipNewPosition(direction, MOVEMENT_PASE);

		if(isOuter(nex_shipp_pos))
			generateDirection();
		else {
			this.x = nex_shipp_pos.x;
			this.y = nex_shipp_pos.y;
		}

		keep_direction++;

	}
}
