import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Shape {
	static final int WIDTH = 20;
	static final int HEIGHT = 20;
	int pos_x = 0;
	int pos_y = 0;
	int pase = 7;

	public void draw(Graphics g_main){
		Graphics2D g2 = (Graphics2D) g_main;
		this.paint(g_main);
	}
	
	public void changeLineThickness(Graphics g_main) {}
	public void scale() {}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(Main.OFFSET_GRID + pos_x, Main.OFFSET_GRID + pos_y, WIDTH, HEIGHT);
	}
	
	
	public void key_decide(int keyCode){
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if ((this.pos_y - this.pase) >= Main.GRID_Y - HEIGHT)
					this.pos_y -= this.pase;
			break;
			
			case KeyEvent.VK_DOWN:
				if ((this.pos_y + this.pase) <= (Main.GRID_Y + Main.BOARD_HEIGHT - HEIGHT))
					this.pos_y += this.pase;
			break;
			
			case KeyEvent.VK_LEFT:
				if ((this.pos_x - this.pase) >= Main.GRID_X - WIDTH)
					this.pos_x -= this.pase;
			break;
			
			case KeyEvent.VK_RIGHT:
				if ((this.pos_x + this.pase) <= (Main.GRID_X + Main.BOARD_WIDTH - WIDTH))
					this.pos_x += this.pase;
			break;
		}
	}
}
