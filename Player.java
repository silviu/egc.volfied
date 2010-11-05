import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Player extends Shape {
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	int x = Volfied.BOARD_WIDTH/2;
	int y = 0;
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
		if (p1.outer.isEmpty() || p2.outer.isEmpty())
			return -7;
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
	
	public ArrayList<Point> getOuterForNoCornerLine(Point pt) {
		
		ArrayList<Point> linePointIsOn = getLinePointIsOn(new Point(this.x, this.y));
		ArrayList<Point> ret = new ArrayList<Point>();
		
		Point start_point  = linePointIsOn.get(0);
		Point end_point    = linePointIsOn.get(1);
		
		int start_point_i  = Volfied.terain.poli.indexOf(start_point);
		int end_point_i    = Volfied.terain.poli.indexOf(end_point);
		
		Point left_corner  = Volfied.terain.poli.get(start_point_i - 1);
		Point right_corner = Volfied.terain.poli.get(end_point_i + 1);
		
		ret.add(left_corner);
		ret.add(right_corner);
		return ret;
	}
	
	public boolean isOuter(int keyCode) {
		Point start_point = new Point();
		ArrayList<Point> line = getLinePointIsOn(new Point(this.x, this.y));
		System.out.println("inrat in ISOUTER");
		boolean special = false;
		int outer = -9;
		
		if (line.isEmpty())
			return false;
		
		for (int i = 0; i < line.size(); i++)
			System.out.println("LINIAAAAAAA=" + line.get(i).x + ", " + line.get(i).y);
		if (line.get(1).outer.size() == 2){
			System.out.println("AL DOILEA IF == 2");
			start_point = line.get(1);
		}
		else if (line.get(0).outer.size() == 2){
			System.out.println("PRIMUL IF == 2");
			start_point = line.get(0);
		}
		else {
			ArrayList<Point> ret = getOuterForNoCornerLine(new Point(this.x, this.y));
			outer = getConstantOuter(ret.get(0), ret.get(1));
			System.out.println("SPECIAAAALLLLLLLLLLLLLLLLLLL");
			special = true;
		}
		
		switch(keyCode) {
			case KeyEvent.VK_UP:
				System.out.println("UUUUUUUUUUP");
				if (special && outer == Point.UP && !isPointonMyTerrain(new Point(this.x, this.y - this.pase)))
					return true;
				else 
				if (!special && !isPointonMyTerrain(new Point(this.x, this.y - this.pase)) && 
					(start_point.outer.get(0) == Point.UP || start_point.outer.get(1) == Point.UP)) {
					System.out.println("UP FAIL");
					return true;
				}
				break;
			
			case KeyEvent.VK_DOWN:
				System.out.println("DOOOWN");
				if (special && outer == Point.DO && !isPointonMyTerrain(new Point(this.x, this.y + this.pase)))
					return true;
				
				if (!special && !isPointonMyTerrain(new Point(this.x, this.y + this.pase)) && 
						(start_point.outer.get(0) == Point.DO || start_point.outer.get(1) == Point.DO)){
					System.out.println("DOWN FAIL");
					return true;
				}
				break;
				
			
			case KeyEvent.VK_LEFT:
				System.out.println("LEEFT");
				if (special && outer == Point.LE && !isPointonMyTerrain(new Point(this.x - this.pase, this.y)))
					return true;
				
				if (!special && !isPointonMyTerrain(new Point(this.x - this.pase, this.y)) && 
						(start_point.outer.get(0) == Point.LE || start_point.outer.get(1) == Point.LE)){
					System.out.println("left fail");
					return true;
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				System.out.println("RIIGHT");
				if (special && outer == Point.RI && !isPointonMyTerrain(new Point(this.x + this.pase, this.y)))
					return true;
				
				if (!special && !isPointonMyTerrain(new Point(this.x + this.pase, this.y)) && 
						(start_point.outer.get(0) == Point.RI || start_point.outer.get(1) == Point.RI)){
					System.out.println("RIGHTTT fail");
					return true;
				}
				break;
			
		}
		return false;
	}
	
	//decide care va fi OUTER pentru un punct
	//primeste parametrii liniile de unde a pornit taierea teritoriului
	//si linia unde termina
	public int decideOuterForPoint(ArrayList<Point> line) {
		
			//daca punctul de inceput al liniei sau punctul de final
			//are doua outer-uri (e colț) alege outer-ul comun
			if (line.get(0).outer.size() == 2 || 
				line.get(1).outer.size() == 2) {
				int outer = getConstantOuter(line.get(0), line.get(1));
				return outer;
			}
			else 
				switch (line.get(0).outer.get(0)) {
					case Point.LE:
						return Point.LE;
					case Point.UP:
						return Point.UP;
					case Point.RI:
						return Point.RI;
					case Point.DO:
						return Point.DO;
				}
		return -7;
	}
	
	public int decideIndivOuter(Point corner, int trail_i) {
		Point next_point = new Point();
		Point next_next_point = new Point();
		if (trail_i == 0) {
			next_point	  = trail.get(trail_i + 1);
			next_next_point = trail.get(trail_i + 2);
		}
		
		if (trail_i == trail.size() - 1) {
			next_point	  = trail.get(trail_i - 1);
			next_next_point = trail.get(trail_i -2);
		}
		
		if (corner.outer.get(0) == Point.UP ||
			corner.outer.get(0) == Point.DO) {
					if (next_next_point.x > next_point.x)
						return Point.RI;
					if (next_next_point.x < next_point.x)
						return Point.LE;
			}
		if (corner.outer.get(0) == Point.LE ||
				corner.outer.get(0) == Point.RI) {
					System.out.println("DEBUG OUTER RIGHT pentr trail_i = " + trail_i +"; " +" next_next_point=[" + next_next_point.x + "," + next_next_point.y
										+ "]; next_point=[" + next_point.x + "," + next_point.y + "]");
					if (next_next_point.y > next_point.y && trail_i == 0) {
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
						return Point.DO;
					}
					
					if (next_next_point.y > next_point.y && trail_i == (trail.size() - 1)) {
						System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK`");
						return Point.DO;
					}
					
					if (next_next_point.y < next_point.y && trail_i == (trail.size() - 1)) {
						System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
						return Point.UP;
					}
					
					if (next_next_point.y < next_point.y && trail_i == 0) {
						System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!111");
						return Point.UP;
					}
			}
		return -8;
		
	}
	
	public void cutTerrain() {
		int trail_size  = this.trail.size();
		
		int start_i = -1, end_i = -1;
		
		Point start_point = new Point();
		Point end_point = new Point();
		
		Point potential_start = trail.get(0);
		Point potential_end   = trail.get(trail_size - 1);
		
		System.out.println("POT_start=[" + potential_start.x + "," + potential_start.y +"]");
		System.out.println("POT_end=[" + potential_end.x + "," + potential_end.y +"]");
		
		
		ArrayList<Point> line = getLinePointIsOn(trail.get(0));
		
		int outer_for_smallest = getConstantOuter(line.get(0), line.get(1));
		System.out.println("OUTER SMALLEST=[" + outer_for_smallest + "]");
		
		
		start_i = 0;
		end_i = trail_size - 1;
		
		switch (outer_for_smallest){
			case Point.UP:
				if (potential_start.x > potential_end.x) 
					Collections.reverse(trail);
				break;
				
			case Point.DO:
				if (potential_start.x < potential_end.x) 
					Collections.reverse(trail);
				break;
				
			case Point.LE:
				if (potential_start.y < potential_end.y)
					Collections.reverse(trail);
				break;
			
			case Point.RI:
				if (potential_start.y > potential_end.y)
					Collections.reverse(trail);
				break;
		}
		
		start_point = trail.get(0);
		end_point	= trail.get(trail_size -1);
		
		ArrayList<Point> lineOfStart = getLinePointIsOn(start_point);
		ArrayList<Point> lineOfEnd = getLinePointIsOn(end_point);
		
		int common_outer_start = decideOuterForPoint(lineOfStart);
		int common_outer_end = decideOuterForPoint(lineOfEnd);
		
		System.out.println("Start_i=[" + start_i +"]");
		System.out.println("End_i=[" + end_i +"]");
		
		System.out.println("Start_point=[" + start_point.x + "," + start_point.y +"]");
		System.out.println("End_point=[" + end_point.x + "," + end_point.y +"]");
		
		trail.get(start_i).addOuter(common_outer_start); // adaug proprietatile de outer pt start_point
		trail.get(end_i).addOuter(common_outer_end); // adaug proprietatile de outer pt end_point
		
		int individual_outer_start = decideIndivOuter(start_point, start_i);
		int individual_outer_end = decideIndivOuter(end_point, end_i);
		
		trail.get(start_i).addOuter(individual_outer_start); // adaug proprietatile de outer pt start_point
		trail.get(end_i).addOuter(individual_outer_end); // adaug proprietatile de outer pt end_point
		
		ArrayList<Point> bla = getLinePointIsOn(start_point);
		int insert_pos = Volfied.terain.poli.indexOf(bla.get(0));
		
		//doar punctul de inceput si de final vor avea 2 outer
		for (int j = trail_size-1; j >= 0; j--) {
			Volfied.terain.poli.add(insert_pos+1,trail.get(j));
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
				print_trail();
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
				print_trail();

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
				print_trail();

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
						//finalize attack
						isAttacking = false;
						first_time = true;
						cutTerrain();
						this.trail.clear();
						return;
					}
			print_trail();

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
				if (!isOuter(keyCode)) {
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
				}
				break;
			
			case KeyEvent.VK_DOWN:
				if (!isOuter(keyCode)) {
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
				}
				break;
			
			case KeyEvent.VK_LEFT:
				if (!isOuter(keyCode)) {
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
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if (!isOuter(keyCode)) {
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
				}
				break;
		}
	}
}
