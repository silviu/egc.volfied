import java.awt.*;

public class Player extends Shape {
	static final int WIDTH = 20;
	static final int HEIGHT = 20;
	int pos_x = 0;
	int pos_y = 0;
	
	public Player() {
	}

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
	public void pl_move() {
		this.pos_x += 10;
		this.pos_y += 10;
	}
}
