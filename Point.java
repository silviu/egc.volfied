import java.awt.event.KeyEvent;


public class Point {

	int x;
	int y;

	public Point() {}
	public Point(int p_x, int p_y) {
		this.x = p_x;
		this.y = p_y;
	}

	@Override
	public String toString()
	{
		return "(" + this.x + ", " + this.y + ")";
	}

	/*
	 * Returns the new position considering that the player would move
	 * according to the key pressed.
	 */
	public Point getNewPosition(int keyCode, int pase)
	{
		int diff_x = 0;
		int diff_y = 0;

		switch (keyCode) {
		case KeyEvent.VK_UP:
			diff_x = 0;
			diff_y = -pase;
			break;
		case KeyEvent.VK_DOWN:
			diff_x = 0;
			diff_y = pase;
			break;
		case KeyEvent.VK_LEFT:
			diff_x = -pase;
			diff_y = 0;
			break;
		case KeyEvent.VK_RIGHT:
			diff_x = pase;
			diff_y = 0;
			break;
		default:
			/* ignore random key presses: the new position is the same as the current one */
			break;
		}
		return new Point(this.x + diff_x, this.y + diff_y);
	}

	public boolean equals(Point p)
	{
		return p.x == x && p.y == y;
	}


	public int dist(Point p)
	{
		// this is only valid for horizontal/vertical lines
		// This works because one coord. is constant =>
		// the length is the difference of the other two coords.
		return Math.abs(this.x - p.x) + Math.abs(this.y - p.y);

	}
}
