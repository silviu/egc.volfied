import java.awt.*;

public class Player{
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	int x = Volfied.BOARD_WIDTH*9/10;
	int y = 0;
	static int pase = 5;
	boolean isAttacking = false;
	boolean first_time = true;

	BrokenLine trail = new BrokenLine(false); // an open broken line of points

	public void draw(Graphics g_main) {
		this.paint(g_main);
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(Volfied.OFFSET_GRID + x, Volfied.OFFSET_GRID + y, WIDTH, HEIGHT);
		g.setColor(Color.cyan);
		g.fillRect(Volfied.OFFSET_GRID + x + 1, Volfied.OFFSET_GRID + y +1, WIDTH -1, HEIGHT-1);
		g.setColor(Color.blue);
		g.fillOval(Volfied.GRID_X + x - 4, Volfied.GRID_Y - 4 + y, 7, 7);
		g.setColor(Color.blue);
		trail.draw(g, Volfied.OFFSET_GRID + WIDTH/2, Volfied.OFFSET_GRID + HEIGHT/2);
	}

	public boolean isMovingOnPerimeter(Point curr_player_pos, Point next_player_pos) {
		return Volfied.terain.isPointOnPerimeter(curr_player_pos)
		&& Volfied.terain.isPointOnPerimeter(next_player_pos);
	}

	public void attack(Point curr_player_pos, Point next_player_pos) {
		isAttacking = true;

		if (first_time) {
			trail.addPointExtdeningSegment(curr_player_pos);
			first_time = false;
		}

		this.trail.addPointExtdeningSegment(next_player_pos);
		System.out.println("TRAIL:" + trail);

		if (Volfied.terain.isPointOnPerimeter(next_player_pos)) {
			// finalize attack
			isAttacking = false;
			first_time = true; // reset first_time
			Volfied.terain.cutTerrain(trail);
			trail.points.clear();
		}
	}

	public void key_decide(int keyCode) {
		Point curr_player_pos = new Point(this.x, this.y);
		Point next_player_pos = curr_player_pos.getNewPosition(keyCode, pase);

		if (Volfied.terain.isOuter(next_player_pos)) {
			System.out.println("next point is outer: " + next_player_pos);
			return;
		}

		if (isMovingOnPerimeter(curr_player_pos, next_player_pos))
			System.out.println("TERIT:" + Volfied.terain.poli);
		else
			attack(curr_player_pos, next_player_pos);

		this.x = next_player_pos.x;
		this.y = next_player_pos.y;
	}
}
