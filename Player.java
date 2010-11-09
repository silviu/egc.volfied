import java.awt.*;


public class Player{
	static final int WIDTH = 45;
	static final int HEIGHT = 25;
	int x = Volfied.BOARD_WIDTH*9/10;
	int y = 0;
	static int pase = 5;
	boolean first_time = true;
	boolean dead = false;
	boolean isAttacking = false;

	int lives = 3;
	BrokenLine trail = new BrokenLine(false); // an open broken line of points
	public Polygon poli = new Polygon();

	public Player() {
		poli.addPoint(0, 0);
		poli.addPoint(WIDTH/2, -5);
		poli.addPoint(WIDTH, 12);
		poli.addPoint(WIDTH/2, 28);
		poli.addPoint(0, HEIGHT);
	}

	public void draw(Graphics g_main) {
		this.paint(g_main);
	}

	public void paint(Graphics g) {
		if (!isDead()) {
			g.setColor(Color.blue);
			trail.draw(g, Volfied.OFFSET_GRID + WIDTH/2, Volfied.OFFSET_GRID + HEIGHT/2);
			g.setColor(Color.CYAN);
			g.fillPolygon(getPaintable());
		}
		else g.drawString("Dead!", x + Volfied.GRID_X, y + Volfied.GRID_Y);
	}

	public String toString() {
		String ret = "Player position=" + x + ", " + y + "\n";
		for (int i = 0; i < poli.npoints; i++)
			ret += "P[" + i + "]=[" + poli.xpoints[i] + ", " + poli.ypoints[i] + "] ";
		ret += "\n";
		return ret;
	}

	public Polygon getPaintable() {
		Polygon ret = new Polygon(poli.xpoints, poli.ypoints, poli.npoints);
		ret.translate(x + Volfied.GRID_X - WIDTH/2, y + Volfied.GRID_Y - HEIGHT/2);
		return ret;
	}
	
	public Polygon getFactorisedPolygon() {
		Polygon cp_poly = new Polygon(poli.xpoints, poli.ypoints, poli.npoints);
		int n = cp_poly.npoints;
		for (int i = 0; i < n; i++) {
			cp_poly.xpoints[i] += x;
			cp_poly.ypoints[i] += y;
		}
		return cp_poly;
	}
	
	public void rotate(int degrees) {
		int n = poli.npoints;
		for (int i = 0; i < n; i++) {
			poli.xpoints[i] = (int) (poli.xpoints[i] * Math.cos(degrees) - poli.ypoints[i] * Math.sin(degrees));
			poli.ypoints[i] = (int) (poli.xpoints[i] * Math.sin(degrees) + poli.ypoints[i] * Math.cos(degrees));
		}
	}

	public boolean isMovingOnPerimeter(Point curr_player_pos, Point next_player_pos) {
		return Volfied.terain.isPointOnPerimeter(curr_player_pos)
		&& Volfied.terain.isPointOnPerimeter(next_player_pos);
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
		else
			if (isDead())
				dead = true;
	}

	public boolean hasMoreLives() {
		if (this.lives > 0)
			return true;
		return false;
	}

	public int getLives() {
		return lives;
	}
	
	public boolean isTrailOnPoly(Polygon enemy) {
		int n = enemy.npoints;
		for (int i = 0; i < n; i++)
			if (trail.isPointOnPerimeter(new Point(enemy.xpoints[i], enemy.ypoints[i])))
				return true;
		return false;
	}

	public boolean isDead() {
		Polygon cp_ship     = Volfied.ship.getFactorisedPolygon();
		Polygon cp_player   = getFactorisedPolygon();
		Polygon cp_critter1 = Volfied.critter1.getFactorisedPolygon();
		Polygon cp_critter2 = Volfied.critter2.getFactorisedPolygon();
		Polygon cp_critter3 = Volfied.critter3.getFactorisedPolygon();
		
		if (isAttacking && (cp_player.intersects(cp_ship.getBounds()) 	  || 
							cp_player.intersects(cp_critter1.getBounds()) ||
							cp_player.intersects(cp_critter2.getBounds()) ||
							cp_player.intersects(cp_critter3.getBounds())))
			return true;
		
		if (isAttacking && (isTrailOnPoly(cp_ship) 	   ||
							isTrailOnPoly(cp_critter1) ||
							isTrailOnPoly(cp_critter2) ||
							isTrailOnPoly(cp_critter3)))
			return true;
		return false;
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
		else {
			isAttacking = true;
			attack(curr_player_pos, next_player_pos);
		}

		this.x = next_player_pos.x;
		this.y = next_player_pos.y;
	}
}
