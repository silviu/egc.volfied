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
	
	public static ArrayList<Point> trail  = new ArrayList<Point>();

	public void draw(Graphics g_main){
		this.paint(g_main);
	}
	
	public void changeLineThickness(Graphics g_main) {}
	public void scale() {}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(Volfied.OFFSET_GRID + x, Volfied.OFFSET_GRID + y, WIDTH, HEIGHT);
		g.setColor(Color.blue);
		int trail_size = trail.size();
		for (int i = 0; i < trail_size-1; i++)
			g.drawLine(Volfied.OFFSET_GRID + WIDTH/2 + trail.get(i).x, Volfied.OFFSET_GRID +  trail.get(i).y + HEIGHT/2,
					Volfied.OFFSET_GRID + trail.get(i+1).x +WIDTH/2, Volfied.OFFSET_GRID + trail.get(i+1).y+HEIGHT/2);
	}
	
	
	public boolean isPointonTrail(Point lookup) {
		int n = trail.size();
		
		for (int i = 0; i < n; i++) {
			Point curr_point = trail.get(i);
			Point next_point = trail.get((i == n - 1) ? 0 : i + 1);
			
			if (lookup.y == curr_point.y && lookup.x == curr_point.x)
			{
				return true;
			}
			
			if ((lookup.y == curr_point.y) && (lookup.y == next_point.y))
				if (((lookup.x >  curr_point.x) && (lookup.x <  next_point.x)) ||
					((lookup.x <  curr_point.x) && (lookup.x >  next_point.x))) 
				{
			   		return true;
				}
			
			if ((lookup.x == curr_point.x) && (lookup.x == next_point.x))
				if (((lookup.y >  curr_point.y) && (lookup.y <  next_point.y)) ||
					((lookup.y <  curr_point.y) && (lookup.y >  next_point.y))) 
				{
			   		return true;
				}

		}
		return false;
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
				if (isPointonTrail(next_point))
					return false;
				if (y == 0)
					return false;
				break;
	
			case KeyEvent.VK_DOWN:
				next_point = new Point(x, y + pase);
				if (isPointonTrail(next_point))
					return false;
				if (y == Volfied.BOARD_HEIGHT)
					return false;
				break;
	
			case KeyEvent.VK_LEFT:
				next_point = new Point(x - pase, y);
				if (isPointonTrail(next_point))
					return false;
				if (x == 0)
					return false;
				break;
	
			case KeyEvent.VK_RIGHT:
				next_point = new Point(x + pase, y);
				if (isPointonTrail(next_point))
					return false;
				if (x == Volfied.BOARD_WIDTH)
					return false;
				break;
		}
		return true;
	}
	
	public void trace_trail(int constant_param) {
		
		int prev_pos, pre_prev_pos;
		prev_pos     = trail.size() - 1;
		pre_prev_pos = trail.size() - 2;
			
		if (pre_prev_pos >= 0) {
			Point prev     = trail.get(prev_pos);
			Point pre_prev = trail.get(pre_prev_pos);
			if (constant_param == 0) {
				if (prev.x == pre_prev.x) 
					trail.set(prev_pos, new Point(x, y));
				else trail.add(new Point(x, y));
			}
			else {
				if (prev.y == pre_prev.y) 
					trail.set(prev_pos, new Point(x, y));
				else trail.add(new Point(x, y));
			}
		}
		else trail.add(new Point(x, y));
		
		if (Volfied.terain.isPointonMyTerrain(new Point(x, y))) {
			//finalize attack
			isAttacking = false;
			first_time = true;
			Volfied.terain.cutTerrain();
			trail.clear();
		}
	}
	
	public void attack(int keyCode) {
		
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (first_time) {
					trail.add(new Point(x, y));
					first_time = false;
				}
				if (y - pase < 0) y = 0;
				else y -= pase;
					
				trace_trail(0);
				print_trail();
				break;

			case KeyEvent.VK_DOWN:
				if (first_time) {
					trail.add(new Point(x, y));
					first_time = false;
				}
				if (y + pase > Volfied.BOARD_HEIGHT)
					y = Volfied.BOARD_HEIGHT;
				else y += pase;
					
				trace_trail(0);
				print_trail();
				break;
				
			case KeyEvent.VK_LEFT:
				if (first_time) {
					trail.add(new Point(x, y));
					first_time = false;
				}
				if (x - pase < 0) x = 0;
				else x -= pase;
					
				trace_trail(1);
				print_trail();
				break;
				
		case KeyEvent.VK_RIGHT:
			if (first_time) {
				trail.add(new Point(x, y));
				first_time = false;
			}
			if (x + pase > Volfied.BOARD_WIDTH)
				x = Volfied.BOARD_WIDTH;
			else x += pase;
				
			trace_trail(1);
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
		int n = trail.size();
		for (int i = 0; i < n; i++)
			System.out.print("TRAIL: X=[" + trail.get(i).x +
							   "]; Y=[" 	+ trail.get(i).y + 
							   "]; OUTER=" + trail.get(i).outer.toString() + " ");
		System.out.println();
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
