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
		int trail_size = trail.points.size();
		for (int i = 0; i < trail_size-1; i++)
			g.drawLine(Volfied.OFFSET_GRID + WIDTH/2 + trail.points.get(i).x, Volfied.OFFSET_GRID +  trail.points.get(i).y + HEIGHT/2,
					Volfied.OFFSET_GRID + trail.points.get(i+1).x +WIDTH/2, Volfied.OFFSET_GRID + trail.points.get(i+1).y+HEIGHT/2);
	}
	
	

	
	public boolean canMoveNotAttack(int keyCode) {
		ArrayList<Point> position = Volfied.terain.onWhatLine();
		int nr_elem = position.size();
		if (nr_elem == 0)
			return false;
		System.out.println(nr_elem);
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (Volfied.terain.isPointonMyTerrain(new Point(x, y - pase))) {
				if (nr_elem == 2) {
					if (position.get(0).x == position.get(1).x)
						return true;
				}
				else if ((position.get(0).x == position.get(1).x) ||
						(position.get(1).x == position.get(2).x))
						return true;
				}
				break;
			
			case KeyEvent.VK_DOWN:
				if (Volfied.terain.isPointonMyTerrain(new Point(x, y + pase))){
				if (nr_elem == 2) {
					if (position.get(0).x == position.get(1).x)
						return true;
				}
				else if ((position.get(0).x == position.get(1).x) ||
						 (position.get(1).x == position.get(2).x))
						return true;
				}
				break;
			
			case KeyEvent.VK_LEFT:
				if (Volfied.terain.isPointonMyTerrain(new Point(x - pase, y))){
				if (nr_elem == 2) {
					if (position.get(0).y == position.get(1).y)
						return true;
				}
				else if ((position.get(0).y == position.get(1).y) ||
						 (position.get(1).y == position.get(2).y))
						return true;
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if (Volfied.terain.isPointonMyTerrain(new Point(x + pase, y))){
				if (nr_elem == 2) {
					if (position.get(0).y == position.get(1).y)
						return true;
				}
				else if ((position.get(0).y == position.get(1).y) ||
						 (position.get(1).y == position.get(2).y))
						return true;
				}
				break;
		}
		return false;
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
	
	public void trace_trail() {
		this.trail.addPointExteningSegment(new Point(x, y));
		
		if (Volfied.terain.isPointonMyTerrain(new Point(x, y))) {
			//finalize attack
			isAttacking = false;
			first_time = true;
			Volfied.terain.cutTerrain(trail.points);
			trail.points.clear();
		}
	}
	
	public void attack(int keyCode) {
		
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (first_time) {
					trail.addPointExteningSegment(new Point(x, y));
					first_time = false;
				}
				if (y - pase < 0) y = 0;
				else y -= pase;
					
				trace_trail();
				print_trail();
				break;

			case KeyEvent.VK_DOWN:
				if (first_time) {
					trail.addPointExteningSegment(new Point(x, y));
					first_time = false;
				}
				if (y + pase > Volfied.BOARD_HEIGHT)
					y = Volfied.BOARD_HEIGHT;
				else y += pase;
					
				trace_trail();
				print_trail();
				break;
				
			case KeyEvent.VK_LEFT:
				if (first_time) {
					trail.addPointExteningSegment(new Point(x, y));
					first_time = false;
				}
				if (x - pase < 0) x = 0;
				else x -= pase;
					
				trace_trail();
				print_trail();
				break;
				
		case KeyEvent.VK_RIGHT:
			if (first_time) {
				trail.addPointExteningSegment(new Point(x, y));
				first_time = false;
			}
			if (x + pase > Volfied.BOARD_WIDTH)
				x = Volfied.BOARD_WIDTH;
			else x += pase;
				
			trace_trail();
			print_trail();
			break;
		}
	}

		
	public void print_territory() {
		int n = Volfied.terain.poli.size();
		for (int i = 0; i < n; i++)
			System.out.print("TERIT: X=[" + Volfied.terain.poli.get(i).x +
							   "]; Y=[" 	+ Volfied.terain.poli.get(i).y + 
							   "]; OUTER=" + Volfied.terain.poli.get(i).outer.toString() + " ");
		System.out.println();
	}
	
	public void print_trail() {
		System.out.println(trail);
	}
	
	public void key_decide(int keyCode){
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (!Volfied.terain.isOuter(keyCode)) {
				if (canMoveNotAttack(keyCode) && Volfied.terain.isPointonMyTerrain(new Point(x, y - pase))) {
					print_territory();
					if (y - pase < 0)
						y = 0;
					else y -= pase;
				}
				else
					if (isValidAttack(keyCode)) {
						isAttacking = true;
						attack(keyCode);
					}
				}
				break;
			
			case KeyEvent.VK_DOWN:
				if (!Volfied.terain.isOuter(keyCode)) {
					if (canMoveNotAttack(keyCode) && Volfied.terain.isPointonMyTerrain(new Point(x, y + pase))) {
						print_territory();
						if (y + pase > Volfied.BOARD_HEIGHT)
							y = Volfied.BOARD_HEIGHT;
						else y += pase;
					}
					else
						if (isValidAttack(keyCode)) {
							isAttacking = true;
							attack(keyCode);
						}
				}
				break;
			
			case KeyEvent.VK_LEFT:
				if (!Volfied.terain.isOuter(keyCode)) {
					if (canMoveNotAttack(keyCode) && Volfied.terain.isPointonMyTerrain(new Point(x - pase, y))) {
						print_territory();
						if (x - pase < 0) x = 0;
						else x -= pase;
					}
					else
						if (isValidAttack(keyCode)) {
							isAttacking = true;
							attack(keyCode);
						}
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if (!Volfied.terain.isOuter(keyCode)) {
					if (canMoveNotAttack(keyCode) && Volfied.terain.isPointonMyTerrain(new Point(x + pase, y))) {
						print_territory();
						if (x + pase > Volfied.BOARD_WIDTH)
							x = Volfied.BOARD_WIDTH;
						else x += pase;
					}
					else
						if (isValidAttack(keyCode)) {
							isAttacking = true;
							attack(keyCode);
						}
				}
				break;
		}
	}
}
