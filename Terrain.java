import java.awt.*;
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
}
