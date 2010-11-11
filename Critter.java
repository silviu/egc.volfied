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
	int time_to_wait = 0;
	
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
	
	double angle = 45;

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
		if (Packet.time_stopped && time_to_wait < 300) {
			time_to_wait++;
			this.paint(g_main);
			return;
		}
		else {
			time_to_wait = 0;
			Packet.time_stopped = false;
		}
		animate();
		this.paint(g_main);
	}
	
	public Polygon getPolygon() {
		Polygon p = new Polygon();
		
		Point pnt = new Point(-CRITTER_SIZE/2, -CRITTER_SIZE/2);
		pnt.rotatePoint(angle);
		p.addPoint(pnt.x, pnt.y);
		
		pnt = new Point(-CRITTER_SIZE/2, CRITTER_SIZE/2);
		pnt.rotatePoint(angle);
		p.addPoint(pnt.x, pnt.y);
		
		pnt = new Point(CRITTER_SIZE/2, CRITTER_SIZE/2);
		pnt.rotatePoint(angle);
		p.addPoint(pnt.x, pnt.y);
		
		pnt = new Point(CRITTER_SIZE/2, -CRITTER_SIZE/2);
		pnt.rotatePoint(angle);
		p.addPoint(pnt.x, pnt.y);
		
		if (angle == 90)
			angle = 0;
		angle += ROTATION_PASE;
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
		for (int i = 0; i < Volfied.packets.size(); i++) {
			Polygon next_critter = getPolygon();
			next_critter.translate(nex_ship_pos.x, nex_ship_pos.y);
			if (next_critter.intersects(Volfied.packets.get(i).getTranslatedPolygon().getBounds2D()))
				return true;
		}
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
