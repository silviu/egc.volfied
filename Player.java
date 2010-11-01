import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Player extends Shape {
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	int x = Volfied.BOARD_WIDTH;
	int y = Volfied.BOARD_HEIGHT;
	int pase = 7;
	int prev_key;
	boolean isAttacking = false;
	
	ArrayList<Point> trail  = new ArrayList<Point>();

	public void draw(Graphics g_main){
		this.paint(g_main);
	}
	
	public void changeLineThickness(Graphics g_main) {}
	public void scale() {}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(Volfied.OFFSET_GRID + x, Volfied.OFFSET_GRID + y, WIDTH, HEIGHT);
		g.setColor(Color.blue);
		int trail_size = this.trail.size();
		for (int i = 0; i < trail_size-1; i++)
			g.drawLine(Volfied.OFFSET_GRID + WIDTH/2 + trail.get(i).x, Volfied.OFFSET_GRID +  trail.get(i).y + HEIGHT/2,
					Volfied.OFFSET_GRID + trail.get(i+1).x +WIDTH/2, Volfied.OFFSET_GRID + trail.get(i+1).y+HEIGHT/2);
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
	
	public boolean canMoveNotAttack(int keyCode) {
		ArrayList<Point> position = onWhatLine();
		int nr_elem = position.size();
		if (nr_elem == 0)
			return false;
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
	
	public void calc_trail(int keyCode)
	{
		switch(keyCode) {
			case KeyEvent.VK_UP:
				if (canMoveNotAttack(keyCode)) {
					if (this.y - this.pase < 0)
						this.y = 0;
					else this.y -= this.pase;
				}
				break;
		
			case KeyEvent.VK_DOWN:
				if (canMoveNotAttack(keyCode)) {
					if (this.y + this.pase > Volfied.BOARD_HEIGHT)
						this.y = Volfied.BOARD_HEIGHT;
					else this.y += this.pase;
				}
				break;
		
			case KeyEvent.VK_LEFT:
				if (canMoveNotAttack(keyCode)) {
					if (this.x - this.pase < 0)
						this.x = 0;
					else this.x -= this.pase;
				}
				break;
		
			case KeyEvent.VK_RIGHT:
				if (canMoveNotAttack(keyCode)) {
					if (this.x + this.pase > Volfied.BOARD_WIDTH)
						this.x = Volfied.BOARD_WIDTH;
					else this.x += this.pase;
				}
				break;
		}
	}
	
	public boolean isValidAttack(int keyCode) {
		switch(keyCode) {
			case KeyEvent.VK_UP:
				if (this.y == 0)
					return false;
				break;
	
			case KeyEvent.VK_DOWN:
					if (this.y == Volfied.BOARD_HEIGHT)
						return false;
				break;
	
			case KeyEvent.VK_LEFT:
					if (this.x == 0)
						return false;
				break;
	
			case KeyEvent.VK_RIGHT:
					if (this.x == Volfied.BOARD_WIDTH)
						return false;
				break;
		}
		return true;
	}

	public void key_decide(int keyCode){
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (canMoveNotAttack(keyCode)) {
					if (this.y - this.pase < 0)
						this.y = 0;
					else this.y -= this.pase;
				}
				else if (isValidAttack(keyCode)) {
					System.out.println("Atac in sus");
					this.y -= this.pase;
					this.trail.add(new Point(this.x, this.y));
				}
					
			break;
			
			case KeyEvent.VK_DOWN:
				if (canMoveNotAttack(keyCode)) {
					if (this.y + this.pase > Volfied.BOARD_HEIGHT)
						this.y = Volfied.BOARD_HEIGHT;
					else this.y += this.pase;
				}
				else if (isValidAttack(keyCode)) {
					System.out.println("Atac in jos");
					this.y += this.pase;
					this.trail.add(new Point(this.x, this.y));

				}
			break;
			
			case KeyEvent.VK_LEFT:
				if (canMoveNotAttack(keyCode)) {
					System.out.println("CIca ma pot misca la stanga");
					if (this.x - this.pase < 0)
						this.x = 0;
					else this.x -= this.pase;
				}
				else if (isValidAttack(keyCode)) {
					System.out.println("Atac in stanga");
					this.x -= this.pase;
					this.trail.add(new Point(this.x, this.y));
				}
			break;
			
			case KeyEvent.VK_RIGHT:
				if (canMoveNotAttack(keyCode)) {
					System.out.println("CIca ma pot misca");
					if (this.x + this.pase > Volfied.BOARD_WIDTH)
						this.x = Volfied.BOARD_WIDTH;
					else this.x += this.pase;
				}
				else if (isValidAttack(keyCode)) {
					System.out.println("Atac in dreapta");
					this.x += this.pase;
					this.trail.add(new Point(this.x, this.y));
				}
			break;
		}
	}
}
