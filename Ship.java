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
	int time_to_bombs = 200;

	int keep_direction = 0;
	int direction = NORTH;
	boolean growing = true;
	int time_to_wait = 0;

	ArrayList<Bomb> bombs = new ArrayList<Bomb>();

	Random rand = new Random();

	public Ship(int init_x, int init_y) {
		this.x = init_x;
		this.y = init_y;
	}

	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fillPolygon(this.getPaintable());
		for (int i = 0; i < bombs.size(); i++)
			bombs.get(i).draw(g);
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
	
	public void init_boms(Point next_ship_pos) {
		bombs.clear();
		bombs.add(new Bomb(next_ship_pos.x, next_ship_pos.y, NORTH));
		bombs.add(new Bomb(next_ship_pos.x, next_ship_pos.y, WEST));
		
		bombs.add(new Bomb(next_ship_pos.x + size, next_ship_pos.y, NORTH));
		bombs.add(new Bomb(next_ship_pos.x + size, next_ship_pos.y, EAST));
		
		bombs.add(new Bomb(next_ship_pos.x + size, next_ship_pos.y + size, SOUTH));
		bombs.add(new Bomb(next_ship_pos.x + size, next_ship_pos.y + size, EAST));
		
		bombs.add(new Bomb(next_ship_pos.x, next_ship_pos.y + size, SOUTH));
		bombs.add(new Bomb(next_ship_pos.x, next_ship_pos.y + size, WEST));
		
	}
	
	public boolean isDead() {
		if (Volfied.player.hasShipBombs && Volfied.player.bomb.getTranslated().intersects(getTranslatedPolygon().getBounds2D()))
			return true;
		return false;
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
		poli.translate(x + Volfied.GRID_X, y + Volfied.GRID_Y);
		return poli;
	}

	public Polygon getTranslatedPolygon() {
		Polygon poli = getPolygon();
		poli.translate(x, y);
		return poli;
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
		for (int i = 0; i < Volfied.packets.size(); i++) {
			Polygon next_ship = getPolygon();
			next_ship.translate(nex_ship_pos.x, nex_ship_pos.y);
			if (next_ship.intersects(Volfied.packets.get(i).getTranslatedPolygon().getBounds2D()))
				return true;
		}
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
		
		if (time_to_bombs == 200) {
			init_boms(nex_shipp_pos);
			time_to_bombs = 0;
		}
		

		if(isOuter(nex_shipp_pos))
			generateDirection();
		else {
			this.x = nex_shipp_pos.x;
			this.y = nex_shipp_pos.y;
		}

		keep_direction++;
		time_to_bombs++;

	}
}
