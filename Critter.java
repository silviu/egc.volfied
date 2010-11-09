import java.awt.*;
import java.util.*;

public class Critter {
	int WIDTH = 40, HEIGHT=40;
	int x = 50;
	int y = 50;

	Point TOP_LEFT_EDGE  = new Point();
	Point TOP_RIGHT_EDGE = new Point();
	Point LOW_LEFT_EDGE  = new Point();
	Point LOW_RIGHT_EDGE = new Point();

	int keep_direction = 0;
	int direction = EAST;

	static final int NORTH = 1;
	static final int EAST  = 2;
	static final int SOUTH = 3;
	static final int WEST  = 4;
	static final int NEAST = 5;
	static final int NWEST = 6;
	static final int SEAST = 7;
	static final int SWEST = 8;

	Random rand = new Random();

	Point p1, p2 ,p3, p4;
	double angle = 45;

	public Critter(int init_x, int init_y) {
		x = init_x;
		y = init_y;
	}

	public void paint(Graphics g) {

		g.setColor(Color.black);
		g.drawRect(x + Volfied.GRID_X, y + Volfied.GRID_Y, WIDTH, HEIGHT);
		g.setColor(Color.orange);
		g.fillRect(x + 1 + Volfied.GRID_X, y + Volfied.GRID_Y + 1, WIDTH-1, HEIGHT-1);
	}


	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}


	public boolean isDead() {
		//TODO: FIXME: implement me
		if (!Volfied.terain.poli.toPolygon().contains(this.x, this.y))
			return true;
		return false;
	}

	public void generateDirection() {
		direction = rand.nextInt(SWEST - NORTH + 1) + NORTH;
	}

	public void animate() {
		//System.out.println("LEFT_EDGE = " + TOP_LEFT_EDGE.x + ", " + TOP_LEFT_EDGE.y);
		if (keep_direction == 105) {
			generateDirection();
			keep_direction = 0;
		}
		keep_direction++;
		switch (direction) {
		case NORTH:
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + WIDTH/2, TOP_LEFT_EDGE.y + HEIGHT/2- 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x + WIDTH/2, TOP_RIGHT_EDGE.y + HEIGHT/2 - 1)))
				y--;
			else generateDirection();
			break;

		case SOUTH:
			if (!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x+ WIDTH/2, LOW_LEFT_EDGE.y + WIDTH/2+ 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ WIDTH/2, LOW_RIGHT_EDGE.y + WIDTH/2+ 1)))
				y++;
			else generateDirection();
			break;

		case EAST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x+ WIDTH/2 + 1, TOP_LEFT_EDGE.y+ WIDTH/2)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2+ 1, LOW_RIGHT_EDGE.y+ WIDTH/2)))
				x++;
			else generateDirection();
			break;

		case WEST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + WIDTH/2- 1, TOP_LEFT_EDGE.y+ WIDTH/2)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ WIDTH/2 - 1, LOW_RIGHT_EDGE.y+ WIDTH/2)))
				x--;
			else generateDirection();
			break;

		case NEAST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x+ WIDTH/2 + 1, TOP_RIGHT_EDGE.y+ WIDTH/2 - 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x+ WIDTH/2 + 1, TOP_LEFT_EDGE.y + WIDTH/2- 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2+ 1, LOW_RIGHT_EDGE.y+ WIDTH/2 - 1))) {
				x++;
				y--;
			}
			else generateDirection();
			break;

		case NWEST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + WIDTH/2- 1, TOP_LEFT_EDGE.y + WIDTH/2- 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2- 1, LOW_RIGHT_EDGE.y + WIDTH/2- 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x+ WIDTH/2 - 1, TOP_RIGHT_EDGE.y + WIDTH/2- 1))) {
				x--;
				y--;
			}
			else generateDirection();
			break;

		case SEAST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2+ 1, LOW_RIGHT_EDGE.y+ WIDTH/2 + 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x+ WIDTH/2 + 1, TOP_RIGHT_EDGE.y+ WIDTH/2 + 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x + WIDTH/2+ 1, LOW_LEFT_EDGE.y + WIDTH/2+ 1))) {
				y++;
				x++;
			}
			else generateDirection();
			break;

		case SWEST:
			if (!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x + WIDTH/2- 1, LOW_LEFT_EDGE.y + WIDTH/2+ 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + WIDTH/2- 1, TOP_LEFT_EDGE.y + WIDTH/2+ 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2- 1, LOW_RIGHT_EDGE.y + WIDTH/2+ 1))) {
				y++;
				x--;
			}
			else generateDirection();
			break;

		default:
			generateDirection();
			break;
		}

		TOP_LEFT_EDGE.x  = x - WIDTH/2;
		TOP_LEFT_EDGE.y  = y - HEIGHT/2;
		TOP_RIGHT_EDGE.x = x + WIDTH/2;
		TOP_RIGHT_EDGE.y = y - HEIGHT/2;
		LOW_LEFT_EDGE.x  = x - WIDTH/2;
		LOW_LEFT_EDGE.y  = y + HEIGHT/2;
		LOW_RIGHT_EDGE.x = x + WIDTH/2;
		LOW_RIGHT_EDGE.y = y + HEIGHT/2;
	}
}
