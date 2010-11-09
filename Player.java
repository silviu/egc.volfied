import java.awt.*;

public class Player{
	static final int WIDTH = 45;
	static final int HEIGHT = 25;
	int x = Volfied.BOARD_WIDTH*9/10;
	int y = 0;
	static int pase = 5;
	boolean isAttacking = false;
	boolean first_time = true;

	int lives = 3;
	BrokenLine trail = new BrokenLine(false); // an open broken line of points
	Polygon poli = new Polygon();
	
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
		g.setColor(Color.blue);
		trail.draw(g, Volfied.OFFSET_GRID + WIDTH/2, Volfied.OFFSET_GRID + HEIGHT/2);
		g.setColor(Color.CYAN);
		g.fillPolygon(getPaintable());
		
	}
	
	public Polygon getPaintable() {
		Polygon ret = new Polygon(poli.xpoints, poli.ypoints, poli.npoints);
		ret.translate(x + Volfied.GRID_X - WIDTH/2, y + Volfied.GRID_Y - HEIGHT/2);
		return ret;
	}

	
	
	public boolean isMovingOnPerimeter(Point curr_player_pos, Point next_player_pos) {
		return Volfied.terain.isPointOnPerimeter(curr_player_pos)
		&& Volfied.terain.isPointOnPerimeter(next_player_pos);
	}

	public void attack(Point curr_player_pos, Point next_player_pos) {
		isAttacking = true;

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
			//TODO: determine where's the monster and keep that poly only
			int monsterPosition = Volfied.ship.getPosition(polys);

			trail = new BrokenLine(false);
			Volfied.terain.poli = polys[monsterPosition];
		}
	}
	
	public boolean isAlive() {
		if (this.lives > 0)
			return true;
		return false;
	}
	
	public int getLives() {
		return lives;
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
		else
			attack(curr_player_pos, next_player_pos);

		this.x = next_player_pos.x;
		this.y = next_player_pos.y;
	}
}
