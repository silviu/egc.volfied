import java.awt.*;
import java.util.*;

public class Critter {
	final static int CRITTER_SIZE = 40;
	final static int MOVEMENT_PASE = 2;
	final static int ROTATION_PASE = 1;
	int x = 50;
	int y = 50;
	String name = "";

	int keep_direction = 0;
	int direction;
	
	static final int NORTH = 1;
	static final int EAST  = 2;
	static final int SOUTH = 3;
	static final int WEST  = 4;
	static final int NEAST = 5;
	static final int NWEST = 6;
	static final int SEAST = 7;
	static final int SWEST = 8;
	
	Random rand = new Random();
	
	boolean growing = true;
	
	int angle = 90;

	public Critter(int init_x, int init_y, String name) {
		this(init_x, init_y);
		this.name = name;
	}

	public Critter(int init_x, int init_y) {
		this.x = init_x;
		this.y = init_y;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillPolygon(this.getPaintable());
		g.drawString(name, x, y);
	}
	
	
	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}
	
	public Polygon getPolygon() {
		Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(0, CRITTER_SIZE);
		p.addPoint(CRITTER_SIZE, CRITTER_SIZE);
		p.addPoint(CRITTER_SIZE, 0);
		return p;
	}
	
	public boolean isDead() {
		if (!Volfied.terain.poli.toPolygon().contains(this.x, this.y))
			return true;
		return false;
	}
	
	public Polygon getPaintable() {
		Polygon ret = getPolygon();
		ret.translate(x + Volfied.GRID_X, y + Volfied.GRID_Y);
		return ret;
	}
	
	public Polygon getTranslatedPolygon() {
		Polygon cp_poly = getPolygon();
		cp_poly.translate(x, y);
		return cp_poly;
	}
	
	
	public void generateDirection() {
		direction = rand.nextInt(SWEST - NORTH + 1) + NORTH;
	}
	
	
	public boolean isOuter(Point nex_ship_pos) {
		Polygon cp_poly = getPolygon();
		int n = cp_poly.npoints;
		for (int i = 0; i < n; i++)
			if (Volfied.terain.isOuter(new Point(cp_poly.xpoints[i] + nex_ship_pos.x, cp_poly.ypoints[i] + nex_ship_pos.y)))
				return true;
		return false;
	}
	
	public void animate() {
		if (keep_direction == 400) {
			generateDirection();
			keep_direction = 0;
		}
		else if (keep_direction == 0)
			generateDirection();
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
