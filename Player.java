import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Player extends Shape {
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	int x = Volfied.BOARD_WIDTH;
	int y = Volfied.BOARD_HEIGHT;
	int pase = 5;
	boolean isAttacking = false;
	boolean first_time = true;
	
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
	
	public boolean isPointonTrail(Point lookup) {
		int n = this.trail.size();
		
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
	
	public boolean isPointonMyTerrain(Point lookup) {
		int n = Volfied.terain.poli.size();
		
		for (int i = 0; i < n; i++) {
			Point curr_point = Volfied.terain.poli.get(i);
			Point next_point = Volfied.terain.poli.get((i == n - 1) ? 0 : i + 1);
			
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
		ArrayList<Point> position = onWhatLine();
		int nr_elem = position.size();
		if (nr_elem == 0)
			return false;
		System.out.println(nr_elem);
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (isPointonMyTerrain(new Point(this.x, this.y - this.pase))) {
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
				if (isPointonMyTerrain(new Point(this.x, this.y + this.pase))){
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
				if (isPointonMyTerrain(new Point(this.x - this.pase, this.y))){
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
				if (isPointonMyTerrain(new Point(this.x + this.pase, this.y))){
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
				next_point = new Point(this.x, this.y - this.pase);
				if (isPointonTrail(next_point))
					return false;
				if (this.y == 0)
					return false;
				break;
	
			case KeyEvent.VK_DOWN:
				next_point = new Point(this.x, this.y + this.pase);
				if (isPointonTrail(next_point))
					return false;
				if (this.y == Volfied.BOARD_HEIGHT)
					return false;
				break;
	
			case KeyEvent.VK_LEFT:
				next_point = new Point(this.x - this.pase, this.y);
				if (isPointonTrail(next_point))
					return false;
				if (this.x == 0)
					return false;
				break;
	
			case KeyEvent.VK_RIGHT:
				next_point = new Point(this.x + this.pase, this.y);
				if (isPointonTrail(next_point))
					return false;
				if (this.x == Volfied.BOARD_WIDTH)
					return false;
				break;
		}
		return true;
	}
	
	public boolean isPointOnLine(Point lookup, Point start, Point end)
	{
		if ((lookup.y == start.y) && (lookup.y == end.y))
			if (((lookup.x >  start.x) && (lookup.x <  end.x)) ||
				((lookup.x <  start.x) && (lookup.x >  end.x))) 
			{
		   		return true;
			}
		
		if ((lookup.x == start.x) && (lookup.x == end.x))
			if (((lookup.y >  start.y) && (lookup.y <  end.y)) ||
				((lookup.y <  start.y) && (lookup.y >  end.y))) 
			{
		   		return true;
			}
		return false;
	}
	
	public int getConstantOuter(Point p1, Point p2) {
			if (p1.outer.get(0) == p2.outer.get(0))
				return p1.outer.get(0);
			
			if (p1.outer.get(0) == p2.outer.get(1))
				return p1.outer.get(0);
			
			if (p1.outer.get(1) == p2.outer.get(0))
				return p1.outer.get(1);
			
			if (p1.outer.get(1) == p2.outer.get(1))
				return p1.outer.get(1);
			return -7;
	}
	
	public void cutTerrain() {
		int terain_size = Volfied.terain.poli.size();
		int trail_size  = this.trail.size();
		
		Point start_point = new Point();
		Point end_point = new Point();
		
		for (int i = 0; i < terain_size-1; i++) {
			if (isPointOnLine(trail.get(0), Volfied.terain.poli.get(i), Volfied.terain.poli.get(i+1))) {
				start_point = trail.get(0);
				end_point   = trail.get(trail_size-1);
				System.out.println("Start_point=[" + start_point.x + "," + start_point.y +"]");
				System.out.println("End_point=[" + end_point.x + "," + end_point.y +"]");

				break;
			}
			if (isPointOnLine(trail.get(trail_size-1), Volfied.terain.poli.get(i), Volfied.terain.poli.get(i+1))) {
				start_point = trail.get(trail_size -1);
				end_point   = trail.get(0);
				System.out.println("Start_point=[" + start_point.x + "," + start_point.y +"]");
				System.out.println("End_point=[" + end_point.x + "," + end_point.y +"]");
				break;
			}
		}
		ArrayList<Point> lineOfStart = getLinePointIsOn(start_point);
		ArrayList<Point> lineOfEnd = getLinePointIsOn(end_point);
		int i = 0;
				for (int j = trail_size-1; j >= 0; j--)
					if (isSameLine(lineOfStart, lineOfEnd)) {
							int outer = getConstantOuter(lineOfStart.get(0), lineOfStart.get(1));
							trail.get(j).addOuter(outer);
								
								/*switch (lineOfStart.get(0).outer.get(k)){
									case Point.LE:
										trail.get(j).addOuter(Point.LE);
										break;
									case Point.UP:
										trail.get(j).addOuter(Point.UP);
										break;
									case Point.RI:
										trail.get(j).addOuter(Point.RI);
										break;
									case Point.DO:
										trail.get(j).addOuter(Point.DO);
										break;
							}
							*/
							Volfied.terain.poli.add(i+1,trail.get(j));
						}
						
	}
	
	public boolean isSameLine(ArrayList<Point> line1, ArrayList<Point> line2) {
		if ((line1.get(0).x == line2.get(0).x) && (line1.get(0).y == line2.get(0).y))
			return true;
		return false;
	}
	
	public ArrayList<Point> getLinePointIsOn(Point lookup) {
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
	
	public void attack(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_UP:{
					if (first_time) {
						this.trail.add(new Point(this.x, this.y));
						first_time = false;
					}
					if (this.y - this.pase < 0)
						this.y = 0;
					else 
						this.y -= this.pase;
					
					int prev_pos     = trail.size() - 1;
					int pre_prev_pos = trail.size() - 2;
					
					if (pre_prev_pos >= 0) {
						Point prev     = trail.get(prev_pos);
						Point pre_prev = trail.get(pre_prev_pos);
						if (prev.x == pre_prev.x) {
							System.out.println("SEEEETTT");
						this.trail.set(prev_pos, new Point(this.x, this.y));
						}
						else 
							this.trail.add(new Point(this.x, this.y));
					}
					else this.trail.add(new Point(this.x, this.y));
					if (isPointonMyTerrain(new Point(this.x, this.y))){
						isAttacking = false;
						first_time = true;
						cutTerrain();
						this.trail.clear();
						return;
					}
				System.out.println("TRAIL: " + trail.toString());
				break;
			}
			case KeyEvent.VK_DOWN:{
					if (first_time) {
						this.trail.add(new Point(this.x, this.y));
						first_time = false;
					}
					if (this.y + this.pase > Volfied.BOARD_HEIGHT)
						this.y = Volfied.BOARD_HEIGHT;
					else 
						this.y += this.pase;
					
						int prev_pos     = trail.size() - 1;
						int pre_prev_pos = trail.size() - 2;
					
						if (pre_prev_pos >= 0) {
							Point prev     = trail.get(prev_pos);
							Point pre_prev = trail.get(pre_prev_pos);
							if (prev.x == pre_prev.x) {
								System.out.println("SEEEETTT");
							this.trail.set(prev_pos, new Point(this.x, this.y));
						}
							else 
								this.trail.add(new Point(this.x, this.y));
						}
						else this.trail.add(new Point(this.x, this.y));
						if (isPointonMyTerrain(new Point(this.x, this.y))){
							//finalize attack
							isAttacking = false;
							first_time = true;
							cutTerrain();
							this.trail.clear();
							return;
				}
				System.out.println("TRAIL: " + trail.toString());

				break;
			}
			case KeyEvent.VK_LEFT:{
					if (first_time) {
						this.trail.add(new Point(this.x, this.y));
						first_time = false;
					}
					if (this.x - this.pase < 0)
						this.x = 0;
					else 
						this.x -= this.pase;
					
						int prev_pos     = trail.size() - 1 ;
						int pre_prev_pos = trail.size() - 2;
					
						if (pre_prev_pos >= 0) {
							Point prev     = trail.get(prev_pos);
							Point pre_prev = trail.get(pre_prev_pos);
							if (prev.y == pre_prev.y){
								System.out.println("SEEEETTT");
							this.trail.set(prev_pos, new Point(this.x, this.y));
						}
							else 
								this.trail.add(new Point(this.x, this.y));
						}
						else this.trail.add(new Point(this.x, this.y));
						if (isPointonMyTerrain(new Point(this.x, this.y))){
							//finalize attack
							isAttacking = false;
							first_time = true;
							cutTerrain();
							this.trail.clear();
							return;
						}
				System.out.println("TRAIL: " + trail.toString());

				break;
			}
		case KeyEvent.VK_RIGHT:{
				if (first_time) {
					this.trail.add(new Point(this.x, this.y));
					first_time = false;
				}
				if (this.x + this.pase > Volfied.BOARD_WIDTH)
					this.x = Volfied.BOARD_WIDTH;
				else 
					this.x += this.pase;
				
					int prev_pos     = trail.size() - 1;
					int pre_prev_pos = trail.size() - 2;
				
					if (pre_prev_pos >= 0) {
						Point prev     = trail.get(prev_pos);
						Point pre_prev = trail.get(pre_prev_pos);
						if (prev.y == pre_prev.y) {
							System.out.println("SEEEETTT");
							this.trail.set(prev_pos, new Point(this.x, this.y));
						}
						else 
							this.trail.add(new Point(this.x, this.y));
					}
					else this.trail.add(new Point(this.x, this.y));
					if (isPointonMyTerrain(new Point(this.x, this.y))){
						this.trail.add(new Point(this.x, this.y));
						//finalize attack
						isAttacking = false;
						first_time = true;
						cutTerrain();
						this.trail.clear();
						return;
					}
			System.out.println("TRAIL: " + trail.toString());

			break;
		}
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
	
	public void key_decide(int keyCode){
		switch (keyCode) {
			case KeyEvent.VK_UP:
				if (canMoveNotAttack(keyCode) && isPointonMyTerrain(new Point(this.x, this.y - this.pase))) {
					//System.out.println("TERRIT " + Volfied.terain.poli.toString());
					print_territory();
					if (this.y - this.pase < 0)
						this.y = 0;
					else this.y -= this.pase;
				}
				else
					if (isValidAttack(keyCode)) {
						//System.out.println("ATACK! " + Volfied.terain.poli.toString());
						isAttacking = true;
						attack(keyCode);
					}
				break;
			
			case KeyEvent.VK_DOWN:
				if (canMoveNotAttack(keyCode) && isPointonMyTerrain(new Point(this.x, this.y + this.pase))) {
					//System.out.println("TERRIT " + Volfied.terain.poli.toString());
					print_territory();
					if (this.y + this.pase > Volfied.BOARD_HEIGHT)
						this.y = Volfied.BOARD_HEIGHT;
					else this.y += this.pase;
				}
				else
					if (isValidAttack(keyCode)) {
						//System.out.println("ATACK! " + Volfied.terain.poli.toString());
						isAttacking = true;
						attack(keyCode);
					}
				break;
			
			case KeyEvent.VK_LEFT:
				if (canMoveNotAttack(keyCode) && isPointonMyTerrain(new Point(this.x - this.pase, this.y))) {
					//System.out.println("TERRIT " + Volfied.terain.poli.toString());
					print_territory();
					if (this.x - this.pase < 0)
						this.x = 0;
					else this.x -= this.pase;
				}
				else
					if (isValidAttack(keyCode)) {
						//System.out.println("ATACK! " + Volfied.terain.poli.toString());
						isAttacking = true;
						attack(keyCode);
					}
				break;
			
			case KeyEvent.VK_RIGHT:
				if (canMoveNotAttack(keyCode) && isPointonMyTerrain(new Point(this.x + this.pase, this.y))) {
					//System.out.println("TERRIT " + Volfied.terain.poli.toString());
					print_territory();
					if (this.x + this.pase > Volfied.BOARD_WIDTH)
						this.x = Volfied.BOARD_WIDTH;
					else this.x += this.pase;
				}
				else
					if (isValidAttack(keyCode)) {
						//System.out.println("ATACK! " + Volfied.terain.poli.toString());
						isAttacking = true;
						attack(keyCode);
					}
				break;
		}
	}
}
