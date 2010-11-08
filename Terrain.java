import java.awt.event.KeyEvent;
import java.util.*;
public class Terrain {
	ArrayList<Point> poli = new ArrayList<Point>();
	public Terrain() {
		poli.add(new Point(0, 0, Point.LE, Point.UP));
		poli.add(new Point(Volfied.BOARD_WIDTH, 0, Point.UP, Point.RI));
		poli.add(new Point(Volfied.BOARD_WIDTH, Volfied.BOARD_HEIGHT, Point.RI, Point.DO));
		poli.add(new Point(0, Volfied.BOARD_HEIGHT, Point.DO, Point.LE));
		poli.add(new Point(0, 0, Point.LE, Point.UP));

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
			next_point	  = Player.trail.get(trail_i + 1);
			next_next_point = Player.trail.get(trail_i + 2);
		}
		
		if (trail_i == Player.trail.size() - 1 ) {
			next_point	  = Player.trail.get(trail_i - 1);
			next_next_point = Player.trail.get(trail_i -2);
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
					
					if (next_next_point.y > next_point.y && trail_i == (Player.trail.size() - 1)) {
						System.out.println("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK`");
						return Point.DO;
					}
					
					if (next_next_point.y < next_point.y && trail_i == (Player.trail.size() - 1)) {
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
	
	public void removePointsinBetween(int start_i, int end_i) {
		
		System.out.println("******************start_i = " + start_i);
		System.out.println("******************end_i = " + end_i);
		for (int i = start_i+1; i < end_i; i++ )
		Volfied.terain.poli.remove(start_i+1);
		
	}
	
	public static boolean isSameLine(ArrayList<Point> line1, ArrayList<Point> line2) {
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
			
			if (lookup.y == curr_point.y && lookup.x == curr_point.x)
			{
				Point prev_point = Volfied.terain.poli.get(i == 0 ? n - 1 : i - 1);
				ret.add(prev_point);
				ret.add(curr_point);
				ret.add(next_point);
				return ret;
			}
			
			if ((lookup.y == curr_point.y) && (lookup.y == next_point.y))
				if (((lookup.x >  curr_point.x) && (lookup.x <  next_point.x)) ||
					((lookup.x <  curr_point.x) && (lookup.x >  next_point.x))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}
			
			if ((lookup.x == curr_point.x) && (lookup.x == next_point.x))
				if (((lookup.y >  curr_point.y) && (lookup.y <  next_point.y)) ||
					((lookup.y <  curr_point.y) && (lookup.y >  next_point.y))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}

		}
		return ret;
	}
	
	
	public ArrayList<Point> onWhatLine() {
		ArrayList<Point> ret = new ArrayList<Point>();
		int n = Volfied.terain.poli.size();
		
		for (int i = 0; i < n; i++) {
			Point curr_point = Volfied.terain.poli.get(i);
			Point next_point = Volfied.terain.poli.get((i == n - 1) ? 0 : i + 1);
			
			if (Player.y == curr_point.y && Player.x == curr_point.x)
			{
				Point prev_point = Volfied.terain.poli.get(i == 0 ? n - 1 : i - 1);
				ret.add(prev_point);
				ret.add(curr_point);
				ret.add(next_point);
				return ret;
			}
			
			if ((Player.y == curr_point.y) && (Player.y == next_point.y))
				if (((Player.x >  curr_point.x) && (Player.x <  next_point.x)) ||
					((Player.x <  curr_point.x) && (Player.x >  next_point.x))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}
			
			if ((Player.x == curr_point.x) && (Player.x == next_point.x))
				if (((Player.y >  curr_point.y) && (Player.y <  next_point.y)) ||
					((Player.y <  curr_point.y) && (Player.y >  next_point.y))) 
				{
			   		ret.add(curr_point);
			   		ret.add(next_point);
			   		return ret;
				}

		}
		return ret;
	}
	
public ArrayList<Point> getOuterForNoCornerLine(Point pt) {
		
		ArrayList<Point> linePointIsOn = Volfied.terain.getLinePointIsOn(new Point(Player.x, Player.y));
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
		ArrayList<Point> line = Volfied.terain.getLinePointIsOn(new Point(Player.x, Player.y));
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
			ArrayList<Point> ret = getOuterForNoCornerLine(new Point(Player.x, Player.y));
			outer = Volfied.terain.getConstantOuter(ret.get(0), ret.get(1));
			System.out.println("SPECIAAAALLLLLLLLLLLLLLLLLLL");
			special = true;
		}
		
		switch(keyCode) {
			case KeyEvent.VK_UP:
				System.out.println("UUUUUUUUUUP");
				if (special && outer == Point.UP && !Volfied.terain.isPointonMyTerrain(new Point(Player.x, Player.y - Player.pase)))
					return true;
				else 
				if (!special && !Volfied.terain.isPointonMyTerrain(new Point(Player.x, Player.y - Player.pase)) && 
					(start_point.outer.get(0) == Point.UP || start_point.outer.get(1) == Point.UP)) {
					System.out.println("UP FAIL");
					return true;
				}
				break;
			
			case KeyEvent.VK_DOWN:
				System.out.println("DOOOWN");
				if (special && outer == Point.DO && !Volfied.terain.isPointonMyTerrain(new Point(Player.x, Player.y + Player.pase)))
					return true;
				
				if (!special && !Volfied.terain.isPointonMyTerrain(new Point(Player.x, Player.y + Player.pase)) && 
						(start_point.outer.get(0) == Point.DO || start_point.outer.get(1) == Point.DO)){
					System.out.println("DOWN FAIL");
					return true;
				}
				break;
				
			
			case KeyEvent.VK_LEFT:
				System.out.println("LEEFT");
				if (special && outer == Point.LE && !Volfied.terain.isPointonMyTerrain(new Point(Player.x - Player.pase, Player.y)))
					return true;
				
				if (!special && !Volfied.terain.isPointonMyTerrain(new Point(Player.x - Player.pase, Player.y)) && 
						(start_point.outer.get(0) == Point.LE || start_point.outer.get(1) == Point.LE)){
					System.out.println("left fail");
					return true;
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				System.out.println("RIIGHT");
				if (special && outer == Point.RI && !Volfied.terain.isPointonMyTerrain(new Point(Player.x + Player.pase, Player.y)))
					return true;
				
				if (!special && !Volfied.terain.isPointonMyTerrain(new Point(Player.x + Player.pase, Player.y)) && 
						(start_point.outer.get(0) == Point.RI || start_point.outer.get(1) == Point.RI)){
					System.out.println("RIGHTTT fail");
					return true;
				}
				break;
			
		}
		return false;
	}
	
	public void depth_outer(Point start_point, Point end_point) {
		int i = 2;
		int end_i = Player.trail.size() - 1;
		int common_outer_to_use = start_point.outer.get(0);
		
		while (i < end_i -2) {
			
			Point prev_point = Player.trail.get(i-1);
			Point curr_point = Player.trail.get(i);
			Point next_point = Player.trail.get(i + 1);
			Point next_next_point = Player.trail.get(i + 2);
			
			
			if (Volfied.terain.isSameLine(getLinePointIsOn(start_point), getLinePointIsOn(end_point))) {

				if (i == 2) {
					if (start_point.outer.get(0) == Point.UP) {
						if (next_next_point.x > next_point.x)
							start_point.outer.add(Point.RI);
						else start_point.outer.add(Point.LE);
					}
					
					if (start_point.outer.get(0) == Point.DO) {
						if (next_next_point.x < next_point.x)
							start_point.outer.add(Point.LE);
						else start_point.outer.add(Point.RI);
					}
					
					if (start_point.outer.get(0) == Point.LE) {
						if (next_next_point.y > next_point.y)
							start_point.outer.add(Point.UP);
						else start_point.outer.add(Point.DO);
					}
					
					if (start_point.outer.get(0) == Point.RI) {
						if (next_next_point.y < next_point.y)
							start_point.outer.add(Point.UP);
						else start_point.outer.add(Point.DO);
					}
				}
				
				curr_point.outer.add(common_outer_to_use);
				System.out.println("ACEEASIIIIIIIIIII LINIEEEEEEEEE");
				if (curr_point.outer.get(0) == Point.UP) {
					if (prev_point.x < curr_point.x)
						curr_point.outer.add(Point.RI);
					else curr_point.outer.add(Point.LE);
				
					if (next_next_point.y == next_point.y) {
						i++;
						common_outer_to_use = end_point.outer.get(0);
					}
				}
			
				if (curr_point.outer.get(0) == Point.DO) {
					if (prev_point.x > curr_point.x)
						curr_point.outer.add(Point.LE);
					else curr_point.outer.add(Point.RI);
					
					if (next_next_point.y == next_point.y) {
						i++;
						common_outer_to_use = end_point.outer.get(0);
					}
				}
			
				if (curr_point.outer.get(0) == Point.LE) {
					if (prev_point.y > curr_point.y)
						curr_point.outer.add(Point.UP);
					else curr_point.outer.add(Point.DO);
					
					if (next_next_point.x == next_point.x) {
						i++;
						common_outer_to_use = end_point.outer.get(0);
					}
				}
				if (curr_point.outer.get(0) == Point.RI) {
					if (prev_point.y < curr_point.y)
						curr_point.outer.add(Point.DO);
					else curr_point.outer.add(Point.UP);
					
					if (next_next_point.x == next_point.x) {
						i++;
						common_outer_to_use = end_point.outer.get(0);
					}
				}
			}
			i+=2;
		}
	}
	
	public void cutTerrain() {
		int trail_size  = Player.trail.size();
		
		int start_i = -1, end_i = -1;
		
		Point start_point = new Point();
		Point end_point = new Point();
		
		Point potential_start = Player.trail.get(0);
		Point potential_end   = Player.trail.get(trail_size - 1);
		
		ArrayList<Point> line_of_start = Volfied.terain.getLinePointIsOn(Player.trail.get(0));
		ArrayList<Point> line_of_end = Volfied.terain.getLinePointIsOn(Player.trail.get(trail_size -1));
		
		int outer_for_smallest = Volfied.terain.getConstantOuter(line_of_start.get(0), line_of_start.get(1));
		int outer_for_biggest = Volfied.terain.getConstantOuter(line_of_end.get(0), line_of_end.get(1));
		
		start_i = 0;
		end_i = trail_size - 1;
		
		if (outer_for_smallest == Point.UP && Point.UP == outer_for_biggest)
			if (potential_start.x > potential_end.x) 
				Collections.reverse(Player.trail);
		
		if (outer_for_smallest == Point.DO && Point.DO == outer_for_biggest)
			if (potential_start.x < potential_end.x) 
				Collections.reverse(Player.trail);
		
		if (outer_for_smallest == Point.LE && Point.LE == outer_for_biggest)
			if (potential_start.y < potential_end.y) 
				Collections.reverse(Player.trail);
		
		if (outer_for_smallest == Point.RI && Point.RI == outer_for_biggest)
			if (potential_start.y > potential_end.y) 
				Collections.reverse(Player.trail);
		
		if (outer_for_smallest == Point.DO && Point.UP == outer_for_biggest)
			Collections.reverse(Player.trail);
		
		start_point = Player.trail.get(0);
		end_point	= Player.trail.get(trail_size -1);
		
		ArrayList<Point> lineOfStart = Volfied.terain.getLinePointIsOn(start_point);
		ArrayList<Point> lineOfEnd = Volfied.terain.getLinePointIsOn(end_point);
		
		int common_outer_start = Volfied.terain.decideOuterForPoint(lineOfStart);
		int common_outer_end = Volfied.terain.decideOuterForPoint(lineOfEnd);
		
		System.out.println("Start_i=[" + start_i +"]");
		System.out.println("End_i=[" + end_i +"]");
		
		System.out.println("Start_point=[" + start_point.x + "," + start_point.y +"]");
		System.out.println("End_point=[" + end_point.x + "," + end_point.y +"]");
		
		
		Player.trail.get(start_i).addOuter(common_outer_start); // adaug proprietatile de outer pt start_point
		Player.trail.get(end_i).addOuter(common_outer_end); // adaug proprietatile de outer pt end_point
		
		int individual_outer_start = -10, individual_outer_end = -10;
		//tai pe acceasi linie sau pe linii adiacente
		if (Player.trail.size() == 4 || Player.trail.size() == 3) {
			System.out.println("TRAILSIZE == 4 sau ==3");
			individual_outer_start = Volfied.terain.decideIndivOuter(start_point, start_i);
			individual_outer_end = Volfied.terain.decideIndivOuter(end_point, end_i);
			Player.trail.get(start_i).addOuter(individual_outer_start); // adaug proprietatile de outer pt start_point
			Player.trail.get(end_i).addOuter(individual_outer_end); // adaug proprietatile de outer pt end_point
		}
		//sus jos + stanga dreapta
		if (Player.trail.size() == 2) {
			System.out.println("TRAILSIZE == 2");
			
			if (start_point.outer.get(0) == Point.UP && end_point.outer.get(0) == Point.DO){
				individual_outer_start = Point.RI;
				individual_outer_end   = Point.RI;
			}
			if (start_point.outer.get(0) == Point.DO && end_point.outer.get(0) == Point.UP){
				individual_outer_start = Point.RI;
				individual_outer_end   = Point.RI;
			}
			
			if (start_point.outer.get(0) == Point.RI && end_point.outer.get(0) == Point.LE){
				individual_outer_start = Point.DO;
				individual_outer_end   = Point.DO;
			}
			if (start_point.outer.get(0) == Point.LE && end_point.outer.get(0) == Point.RI){
				individual_outer_start = Point.DO;
				individual_outer_end   = Point.DO;
			}
			Player.trail.get(start_i).addOuter(individual_outer_start); // adaug proprietatile de outer pt start_point
			Player.trail.get(end_i).addOuter(individual_outer_end); // adaug proprietatile de outer pt end_point
		}
		
		
		
		if (Player.trail.size() > 4) {
			System.out.println("TRAILSIZE > 4");
			Point next_next_point = Player.trail.get(Player.trail.size() - 3);
			Point next_point 	  = Player.trail.get(Player.trail.size() - 2);
			if (end_point.outer.get(0) == Point.UP) {
				if (next_next_point.x < next_point.x)
					end_point.outer.add(Point.LE);
				else end_point.outer.add(Point.RI);
			}
			
			if (end_point.outer.get(0) == Point.DO) {
				if (next_next_point.x > next_point.x)
					end_point.outer.add(Point.RI);
				else end_point.outer.add(Point.LE);
			}
			
			if (end_point.outer.get(0) == Point.LE) {
				if (next_next_point.y > next_point.y)
					end_point.outer.add(Point.DO);
				else end_point.outer.add(Point.UP);
			}
			
			if (end_point.outer.get(0) == Point.RI) {
				if (next_next_point.y > next_point.y)
					end_point.outer.add(Point.DO);
				else end_point.outer.add(Point.UP);
			}
			depth_outer(start_point, end_point);
			
			
			
			
		}
		
		
		
		ArrayList<Point> bla = Volfied.terain.getLinePointIsOn(start_point);
		int insert_pos = Volfied.terain.poli.indexOf(bla.get(0));
		
		int start_deletable_zone = Volfied.terain.poli.indexOf(lineOfStart.get(0));
		int end_deletable_zone   = Volfied.terain.poli.indexOf(lineOfEnd.get(1));
		Volfied.terain.removePointsinBetween(start_deletable_zone, end_deletable_zone);
		
		//doar punctul de inceput si de final vor avea 2 outer
		for (int j = trail_size-1; j >= 0; j--) {
			Volfied.terain.poli.add(insert_pos+1, Player.trail.get(j));
		}
	}
}
