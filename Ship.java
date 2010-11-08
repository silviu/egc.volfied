import java.awt.*;
import java.util.*;

public class Ship {
	int WIDTH = 76, HEIGHT=76;
	int x = 50;
	int y = 50;
	
	Point TOP_LEFT_EDGE  = new Point();
	Point TOP_RIGHT_EDGE = new Point();
	Point LOW_LEFT_EDGE  = new Point();
	Point LOW_RIGHT_EDGE = new Point();
	
	int keep_direction = 0;
	int direction = NORTH;
	
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
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(x + Volfied.GRID_X, y + Volfied.GRID_Y, WIDTH, HEIGHT);
		g.setColor(Color.red);
		g.fillRect(x + 1 + Volfied.GRID_X, y + Volfied.GRID_Y + 1, WIDTH-1, HEIGHT-1);
	}
	
	
	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}
	
	public void generateDirection() {
		direction = rand.nextInt(SWEST - NORTH + 1) + NORTH;
	}
	
	
	public boolean canGrow() {
		if (WIDTH == 120 || HEIGHT == 120) {
			growing = false;
		}
		
		if (WIDTH == 75 || HEIGHT == 75) {
			growing = true;
		}
		
		if (growing) {
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + (WIDTH+1)/2, TOP_LEFT_EDGE.y + (HEIGHT+1)/2- 1)) &&
				!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x + (WIDTH+1)/2, TOP_RIGHT_EDGE.y + (HEIGHT+1)/2 - 1)) &&
				!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x+ (WIDTH+1)/2, LOW_LEFT_EDGE.y + (WIDTH+1)/2+ 1)) &&
				!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ (WIDTH+1)/2, LOW_RIGHT_EDGE.y + (WIDTH+1)/2+ 1)) &&
				!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + (WIDTH+1)/2- 1, TOP_LEFT_EDGE.y+ (WIDTH+1)/2)) &&
				!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ (WIDTH+1)/2 - 1, LOW_RIGHT_EDGE.y+ (WIDTH+1)/2)))
				return true;
		}
		else 
			if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + (WIDTH-1)/2, TOP_LEFT_EDGE.y + (HEIGHT-1)/2- 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_RIGHT_EDGE.x + (WIDTH-1)/2, TOP_RIGHT_EDGE.y + (HEIGHT-1)/2 - 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x+ (WIDTH-1)/2, LOW_LEFT_EDGE.y + (WIDTH-1)/2+ 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ (WIDTH-1)/2, LOW_RIGHT_EDGE.y + (WIDTH-1)/2+ 1)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + (WIDTH-1)/2- 1, TOP_LEFT_EDGE.y+ (WIDTH-1)/2)) &&
					!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ (WIDTH-1)/2 - 1, LOW_RIGHT_EDGE.y+ (WIDTH-1)/2)))
					return true;
		return false;
	}
	
	public void animate() {
		if (keep_direction == 400) {
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
					if (!Volfied.terain.isPointOnPerimeter(new Point(LOW_LEFT_EDGE.x+ WIDTH/2, LOW_LEFT_EDGE.y + HEIGHT/2+ 1)) &&
						!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ WIDTH/2, LOW_RIGHT_EDGE.y + HEIGHT/2+ 1)))
						y++;
					else generateDirection();
					break;
					
				case EAST:
					if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x+ WIDTH/2 + 1, TOP_LEFT_EDGE.y+ HEIGHT/2)) &&
						!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x + WIDTH/2+ 1, LOW_RIGHT_EDGE.y+ HEIGHT/2)))
						x++;
					else generateDirection();
					break;
					
				case WEST:
					if (!Volfied.terain.isPointOnPerimeter(new Point(TOP_LEFT_EDGE.x + WIDTH/2- 1, TOP_LEFT_EDGE.y+ HEIGHT/2)) &&
						!Volfied.terain.isPointOnPerimeter(new Point(LOW_RIGHT_EDGE.x+ WIDTH/2 - 1, LOW_RIGHT_EDGE.y+ HEIGHT/2)))
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

			
			if (canGrow())
				if (growing) {
					WIDTH++;
					HEIGHT++;
				}
				else {
					WIDTH--;
					HEIGHT--;
				}
			
			else generateDirection();
			
			
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
