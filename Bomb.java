import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class Bomb {
	int x, y;
	int direction;
	int pase = 15;
	
	public Bomb(int init_x, int init_y, int direction) {
		this.x = init_x;
		this.y = init_y;
		this.direction = direction;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g.setColor(Color.RED);
		g2.fill(getTranslated());
	}

	public void draw(Graphics g_main){
		animate();
		this.paint(g_main);
	}
	
	public Ellipse2D getTranslated(){
		 return new Ellipse2D.Double(x + Volfied.GRID_X, y + Volfied.GRID_Y, 10, 10);
	}
	
	public void animate() {
		Point curr_bomb_pos = new Point(x,y);
		Point next_bomb_pos = curr_bomb_pos.getBombNewPosition(direction, pase);
		this.x = next_bomb_pos.x;
		this.y = next_bomb_pos.y;
	}
}
