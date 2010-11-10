import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class Packet {
	final static int SIZE = 50;
	
	final static int INCREASE_SPEED = 1;
	final static int DECREASE_SPEED = 1;
	final static int STOP_TIME = 2;
	final static int BOMBS = 3;
	
	int x;
	int y;
	int trait;
	boolean tacken = false;
	
	public Packet(int init_x, int init_y, int trait) {
		this.x = init_x;
		this.y = init_y;
		this.trait = trait;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillPolygon(this.getPaintable());
	}


	public void draw(Graphics g_main){
		this.paint(g_main);
	}

	public boolean checkIfTaken() {
		if (!Volfied.terain.poli.toPolygon().contains(this.getTranslatedPolygon().getBounds2D()))
			return true;
		return false;
	}
	
	public Polygon getPolygon() {
		Polygon p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(0, SIZE);
		p.addPoint(SIZE, SIZE);
		p.addPoint(SIZE, 0);
		return p;
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
}
