import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Player extends Shape {
	static final int WIDTH = 50;
	static final int HEIGHT = 50;
	int x = Volfied.BOARD_WIDTH;
	int y = Volfied.BOARD_HEIGHT;
	int pase = 7;
	static final int ATACK = 0;
	
	ArrayList<Point> trail  = new ArrayList<Point>();

	public void draw(Graphics g_main){
		this.paint(g_main);
	}
	
	public void changeLineThickness(Graphics g_main) {}
	public void scale() {}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(Volfied.OFFSET_GRID + x, Volfied.OFFSET_GRID + y, WIDTH, HEIGHT);
	}
	
	public ArrayList<Point> onWhatLine() {
		ArrayList<Point> ret = new ArrayList<Point>();
		int n = Volfied.terain.poli.size();
		
		for (int i = 0; i < n; i++) {
			Point curr_point = Volfied.terain.poli.get(i);
			Point next_point = Volfied.terain.poli.get((i == n - 1) ? 0 : i + 1);
			
			if (this.y == curr_point.y && this.x == curr_point.x)
			{
				Point prev_point = Volfied.terain.poli.get(i == 0 ? n - 1 : i - 1);
				ret.add(prev_point);
				ret.add(curr_point);
				ret.add(next_point);
				return ret;
			}
			
			if ((this.y == curr_point.y) && (this.y == next_point.y))
				if (((this.x >  curr_point.x) && (this.x <  next_point.x)) ||
					((this.x <  curr_point.x) && (this.x >  next_point.x))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}
			
			if ((this.x == curr_point.x) && (this.x == next_point.x))
				if (((this.y >  curr_point.y) && (this.y <  next_point.y)) ||
					((this.y <  curr_point.y) && (this.y >  next_point.y))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}

		}
		return ret;
	}
	
	public boolean canMove(int keyCode) {
		ArrayList<Point> position = onWhatLine();
		int nr_elem = position.size();
		System.out.println(nr_elem);
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (nr_elem == 2) {
					if (position.get(0).x == position.get(1).x)
						return true;
				}
				else if ((position.get(0).x == position.get(1).x) ||
						(position.get(1).x == position.get(2).x))
						return true;
				break;
			
			case KeyEvent.VK_DOWN:
				if (nr_elem == 2) {
					if (position.get(0).x == position.get(1).x)
						return true;
				}
				else if ((position.get(0).x == position.get(1).x) ||
						 (position.get(1).x == position.get(2).x))
						return true;
				break;
			
			case KeyEvent.VK_LEFT:
				if (nr_elem == 2) {
					if (position.get(0).y == position.get(1).y)
						return true;
				}
				else if ((position.get(0).y == position.get(1).y) ||
						 (position.get(1).y == position.get(2).y))
						return true;
				break;
			
			case KeyEvent.VK_RIGHT:
				if (nr_elem == 2) {
					if (position.get(0).y == position.get(1).y)
						return true;
				}
				else if ((position.get(0).y == position.get(1).y) ||
						 (position.get(1).y == position.get(2).y))
						return true;
				break;
		}
		return false;
	}

	public void key_decide(int keyCode){
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (canMove(keyCode)) {
					if (this.y - this.pase < 0)
						this.y = 0;
					else this.y -= this.pase;
				}
			break;
			
			case KeyEvent.VK_DOWN:
				if (canMove(keyCode)) {
					if (this.y + this.pase > Volfied.BOARD_HEIGHT)
						this.y = Volfied.BOARD_HEIGHT;
					else this.y += this.pase;
				}
			break;
			
			case KeyEvent.VK_LEFT:
				if (canMove(keyCode)) {
					if (this.x - this.pase < 0)
						this.x = 0;
					else this.x -= this.pase;
				}
			break;
			
			case KeyEvent.VK_RIGHT:
				if (canMove(keyCode)) {
					if (this.x + this.pase > Volfied.BOARD_WIDTH)
						this.x = Volfied.BOARD_WIDTH;
					else this.x += this.pase;
				}
			break;
		}
	}
}
