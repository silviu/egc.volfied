import java.awt.*;
import java.util.*;

public class Ship {
	int WIDTH = 100, HEIGHT=100;
	int x = (Volfied.BOARD_WIDTH - 2 * Volfied.GRID_X)/2;
	int y = (Volfied.BOARD_HEIGHT -2*  Volfied.GRID_Y)/2;
	int UPPER_LINE = y - 50;
	int LOWER_LINE = y + 50;
	int LEFT_LINE  = x - 50;
	int RIGHT_LINE = x + 50;
	
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
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(x, y, WIDTH, HEIGHT);
		g.setColor(Color.red);
		g.fillRect(x+1, y+1, WIDTH-1, HEIGHT-1);
	}
	
	
	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}
	
	public void generateDirection() {
		direction = rand.nextInt(SWEST - NORTH + 1) + NORTH;
	}
	
	public void animate() {
		if (keep_direction == 100) {
			generateDirection();
			keep_direction = 0;
		}
		keep_direction++;
		switch (direction) {
			case NORTH:
				y--;
				break;
			case SOUTH:
				y++;
				break;
			case EAST:
				x++;
				break;
			case WEST:
				x--;
				break;
			case NEAST:
				x++;
				y--;
				break;
			case NWEST:
				x--;
				y--;
				break;
			case SEAST:
				y++;
				x++;
				break;
			case SWEST:
				y++;
				x--;
		}
	}
}
