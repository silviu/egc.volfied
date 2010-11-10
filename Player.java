import java.awt.*;


public class Player{
	static final int WIDTH = 45;
	static final int HEIGHT = 25;
	int x = Volfied.BOARD_WIDTH*9/10;
	int y = 0;
	static int pase = 10;
	boolean first_time = true;
	boolean dead = false;
	boolean isAttacking = false;
	
	double angle = 108;
	static int lives = 3;
	BrokenLine trail = new BrokenLine(false); // an open broken line of points
	public Polygon poli = new Polygon();
	
	public Polygon getPolygon() {
		Polygon p = new Polygon();
		
		Point point = new Point(30, 0);
		p.addPoint(point.x, point.y);
		
		for (int i = 0; i < 4; i++) {
			point.rotatePoint(angle);
			p.addPoint(point.x, point.y);
		}
		return p;
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
		else  {
			g.drawString("Dead!", x + Volfied.GRID_X, y + Volfied.GRID_Y);
			manageDeath();
			lives--;
		}
	}

	public String toString() {
		String ret = "Player position=" + x + ", " + y + "\n";
		for (int i = 0; i < poli.npoints; i++)
			ret += "P[" + i + "]=[" + poli.xpoints[i] + ", " + poli.ypoints[i] + "] ";
		ret += "\n";
		return ret;
	}

	public Polygon getPaintable() {
		Polygon ret = getPolygon();
		ret.translate(x + Volfied.GRID_X - WIDTH/2, y + Volfied.GRID_Y - HEIGHT/2);
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
