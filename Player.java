import java.awt.*;
import java.awt.event.KeyEvent;


public class Player {
	static final int SIZE = 20; // the pentagon's side size
	int pase = 10;
	
	// initial positions
	int x = Volfied.BOARD_WIDTH/2;
	int y = Volfied.BOARD_HEIGHT;
	
	final static int RIGHT = 0;
	final static int DOWN  = 1;
	final static int LEFT  = 2;
	final static int UP    = 3;
	
	boolean first_time = true;
	boolean dead = false;
	boolean isAttacking = false;
	boolean speed_changed = false;
	int time_to_normal = 0;
	double pentagon_rotation_angle = 0;
	boolean hasBombs = false;
	int orientation = RIGHT;
	
	Bomb bomb = new Bomb(x, y, RIGHT);
	
	static int lives = 3;
	BrokenLine trail = new BrokenLine(false); // an open broken line of points

	
	public Polygon getPolygon() {
		Polygon p = new Polygon();
		int n = 5; // pentagon has 5 points.
		double angle_between_poligon_points = 2 * Math.PI / n;
		int r = SIZE;
		
		for(int i=0; i < n; i++) {
			double v = i * angle_between_poligon_points + pentagon_rotation_angle;
			int x, y;
			x = (int) Math.round(r * Math.cos(v));
			y = (int) Math.round(r * Math.sin(v));
			p.addPoint(x, y);
		}
		return p;
	}

	public void draw(Graphics g) {
		if (!isDead()) {
			if (speed_changed && time_to_normal == 600) {
				setSpeed(10);
				speed_changed = false;
				time_to_normal = 0;
			}
			else
				time_to_normal++;
			if (hasBombs) {
				bomb.draw(g);
				bomb.animate();
			}
			g.setColor(Color.blue);
			trail.draw(g, Volfied.GRID_X, Volfied.GRID_Y);
			g.setColor(Color.CYAN);
			g.fillPolygon(getPaintable());
		}
		else  {
			g.drawString("Dead!", x + Volfied.GRID_X, y + Volfied.GRID_Y);
			manageDeath();
			lives--;
		}
	}

	public void shoot() {
		int direction = 0;
		switch (orientation) {
		case UP:
			direction = Volfied.ship.NORTH;
			break;
		case DOWN:
			direction = Volfied.ship.SOUTH;
			break;
		case LEFT:
			direction = Volfied.ship.WEST;
			break;
		case RIGHT:
			direction = Volfied.ship.EAST;
			break;
		}
		bomb = new Bomb(x, y, direction);
	}
	
	public String toString() {
		Polygon poli = getPolygon();
		String ret = "Player position=" + x + ", " + y + "\n";
		for (int i = 0; i < poli.npoints; i++)
			ret += "P[" + i + "]=[" + poli.xpoints[i] + ", " + poli.ypoints[i] + "] ";
		ret += "\n";
		return ret;
	}
	
	public void setSpeed(int new_speed) {
		this.pase = new_speed;
		speed_changed = true;
	}

	public Polygon getPaintable() {
		Polygon ret = getPolygon();
		ret.translate(x + Volfied.GRID_X, y + Volfied.GRID_Y);
		return ret;
	}
	
	public Polygon getTranslatedPolygon() {
		Polygon cp_poly = getPolygon();
		cp_poly.translate(x, y);
		return cp_poly;
	}
	
	public boolean isMovingOnPerimeter(Point curr_player_pos, Point next_player_pos) {
		return Volfied.terain.isPointOnPerimeter(curr_player_pos)
		&& Volfied.terain.isPointOnPerimeter(next_player_pos);
	}
	
	public int getLives() {
		return lives;
	}
	
	public static void waiting (int n){
		long t0, t1;
		t0 =  System.currentTimeMillis();
		do{
			t1 = System.currentTimeMillis();
		}
		while ((t1 - t0) < (n * 1000));
	}

	public void manageDeath() {
		this.x = trail.points.get(0).x;
		this.y = trail.points.get(0).y;
		isAttacking = false;
		first_time = true;
		trail.points.clear();
	}
	
	public void attack(Point curr_player_pos, Point next_player_pos) {

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
			BrokenLine polys[] = Volfied.terain.poli.cutTerrain(trail);
			int monsterPosition = Volfied.ship.getPosition(polys);

			trail = new BrokenLine(false);
			Volfied.terain.poli = polys[monsterPosition];
		}
	}

	public boolean hasMoreLives() {
		if (lives > 0)
			return true;
		return false;
	}
	
	public boolean isTrailOnPoly(Polygon enemy) {
		int n = enemy.npoints;
		for (int i = 0; i < n; i++)
			if (trail.isPointOnPerimeter(new Point(enemy.xpoints[i], enemy.ypoints[i])))
				return true;
		return false;
	}

	public boolean isDead() {
		Polygon cp_ship     = Volfied.ship.getTranslatedPolygon();
		Polygon cp_player   = getTranslatedPolygon();
		
		if (isAttacking && (cp_player.intersects(cp_ship.getBounds()))) 
			return true;
		
		for (int i = 0; i < Volfied.n_critter; i++) {
			Volfied.critters.get(i);
			if (isAttacking && cp_player.intersects(Volfied.critters.get(i).getPolygon().getBounds()))
				return true;
			
			if (isAttacking && isTrailOnPoly(Volfied.critters.get(i).getTranslatedPolygon()))
				return true;
		}
		
		if (isAttacking && (isTrailOnPoly(cp_ship)))
			return true;
		
		for (int i = 0; i < Volfied.ship.bombs.size(); i++)
			if (isAttacking && Volfied.ship.bombs.get(i).getTranslated().intersects(cp_player.getBounds2D()))
				return true;
		
		return false;
	}
	
	public boolean isPacket(Point next_player_pos) {
		for (int i = 0; i < Volfied.packets.size(); i++) {
			Polygon next_player = getPolygon();
			next_player.translate(next_player_pos.x, next_player_pos.y);
			if (next_player.intersects(Volfied.packets.get(i).getTranslatedPolygon().getBounds2D()))
				return true;
		}
		return false;
	}
	
	private void updatePlayerRotationAngle(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_UP:
			pentagon_rotation_angle = 3 * Math.PI / 2;
			break;
		case KeyEvent.VK_DOWN:
			pentagon_rotation_angle = Math.PI / 2;
			break;
		case KeyEvent.VK_LEFT:
			pentagon_rotation_angle = Math.PI;
			break;
		case KeyEvent.VK_RIGHT:
			pentagon_rotation_angle = 0;
			break;
		default:
			break;
		}
	}

	
	public void key_decide(int keyCode) {
		updatePlayerRotationAngle(keyCode);
		
		Point curr_player_pos = new Point(this.x, this.y);

		Point next_player_pos = null;

		int p = pase;
		while (p > 0)
		{
			next_player_pos = curr_player_pos.getNewPosition(keyCode, p);
			if (!Volfied.terain.isOuter(next_player_pos))
				break;
			// when nearing an edge we might not have enough space to do a full pase
			// in that case retry with a smaller pase.
			p--;
		}

		if (p == 0) {
			// if moving a single pixel from the current position in the direction
			// indicated by keyCode will get us out of the terrain, then we
			// surely cannot move in that direction.
			System.out.println("next point is outer: " + next_player_pos);
			return;
		}

		if (isMovingOnPerimeter(curr_player_pos, next_player_pos)) {
			this.x = next_player_pos.x;
			this.y = next_player_pos.y;
			System.out.println("TERIT:" + Volfied.terain.poli);
		}
		else if (!isPacket(next_player_pos)) {
			isAttacking = true;
			attack(curr_player_pos, next_player_pos);
			this.x = next_player_pos.x;
			this.y = next_player_pos.y;
		}

		
	}
}
