import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Player extends Shape {
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static int x = Volfied.BOARD_WIDTH/2;
	static int y = 0;
	static int pase = 5;
	boolean isAttacking = false;
	boolean first_time = true;
	int cut = 0;
	int angle = 45;

	BrokenLine trail = new BrokenLine(false); // an open broken line of points

	public void draw(Graphics g_main){
		this.paint(g_main);
	}

	public void changeLineThickness(Graphics g_main) {}
	public void scale() {}

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





	public boolean isMovingOnPerimeter(int keyCode)
	{
		Point curr_player_pos = new Point(this.x, this.y);
		Point next_player_pos = curr_player_pos.getNewPosition(keyCode, pase); 

		return  Volfied.terain.isPointOnPerimeter(curr_player_pos) &&
				Volfied.terain.isPointOnPerimeter(next_player_pos);
	}


	public boolean isValidAttack(int keyCode) {
		Point next_point;
		switch(keyCode) {
		case KeyEvent.VK_UP:
			next_point = new Point(x, y - pase);
			if (trail.isPointOnPerimeter(next_point))
				return false;
			if (y == 0)
				return false;
			break;

		case KeyEvent.VK_DOWN:
			next_point = new Point(x, y + pase);
			if (trail.isPointOnPerimeter(next_point))
				return false;
			if (y == Volfied.BOARD_HEIGHT)
				return false;
			break;

		case KeyEvent.VK_LEFT:
			next_point = new Point(x - pase, y);
			if (trail.isPointOnPerimeter(next_point))
				return false;
			if (x == 0)
				return false;
			break;

		case KeyEvent.VK_RIGHT:
			next_point = new Point(x + pase, y);
			if (trail.isPointOnPerimeter(next_point))
				return false;
			if (x == Volfied.BOARD_WIDTH)
				return false;
			break;
		}
		return true;
	}


	public void attack(int keyCode) {

		Point curr_player_pos = new Point(this.x, this.y);
		Point next_player_pos = curr_player_pos.getNewPosition(keyCode, pase); 

		if (first_time) {
			trail.addPointExtdeningSegment(curr_player_pos);
			first_time = false;
		}

		
		this.trail.addPointExtdeningSegment(next_player_pos);

		if (Volfied.terain.isPointOnPerimeter(next_player_pos)) {
			// finalize attack
			isAttacking = false;
			first_time = true;
			Volfied.terain.cutTerrain(trail.points);
			trail.points.clear();
		}

		System.out.println("TRAIL:" + trail);
		
		this.x = next_player_pos.x;
		this.y = next_player_pos.y;
	}


	public void print_territory() {
		System.out.println("TERIT:" + Volfied.terain.poli);
	}

	public void key_decide(int keyCode){
		Point curr_player_pos = new Point(this.x, this.y);
		Point next_player_pos = curr_player_pos.getNewPosition(keyCode, pase); 

		if (Volfied.terain.isOuter(next_player_pos))
		{
			System.out.println("next point is outer: " + next_player_pos);
			return;
		}
			

		if (isMovingOnPerimeter(keyCode)) {
			print_territory();
			this.x = next_player_pos.x;
			this.y = next_player_pos.y;
		} else if (isValidAttack(keyCode)) {
			isAttacking = true;
			attack(keyCode);
		}

	}
}
