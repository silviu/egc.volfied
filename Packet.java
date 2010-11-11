import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;


public class Packet {
	final static int SIZE = 50;
	
	final static int INCREASE_SPEED = 0;
	final static int DECREASE_SPEED = 1;
	final static int STOP_TIME = 2;
	final static int BOMBS = 3;
	
	int x;
	int y;
	int trait;
	boolean taken = false;
	static boolean time_stopped = false;
	String message = "";
	boolean acted = false;
	
	public Packet(int init_x, int init_y, int trait) {
		this.x = init_x;
		this.y = init_y;
		this.trait = trait;
	}
	
	public void paint(Graphics g) {
		if (!checkIfTaken()) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillPolygon(this.getPaintable());
		}
		else {
			if (!acted)
				actOnPacket();
			g.setColor(Color.green);
			g.fillOval(x + Volfied.GRID_X, y + Volfied.GRID_Y, 50, 50);
			g.setColor(Color.black);
			g.drawString(message, x + 2*(Volfied.GRID_X-4), y + 2*Volfied.GRID_Y);
			acted = true;
		}
	}


	public void draw(Graphics g_main){
		this.paint(g_main);
	}
	
	public void actOnPacket() {
		switch (trait) {
		case INCREASE_SPEED:
			Volfied.player.setSpeed(20);
			message = "S";
			break;
		case DECREASE_SPEED:
			Volfied.player.setSpeed(5);
			message = "D";
			break;
		case STOP_TIME:
			message = "T";
			time_stopped = true;
			break;
			
		case BOMBS:
			message = "B";
			break;
		}
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
