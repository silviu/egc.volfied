import java.awt.*;
import java.util.*;
public class Terrain {
	ArrayList<Point> poli = new ArrayList<Point>();
	public Terrain() {
		poli.add(new Point(0, 0));
		poli.add(new Point(Volfied.BOARD_WIDTH, 0));
		poli.add(new Point(Volfied.BOARD_WIDTH, Volfied.BOARD_HEIGHT));
		poli.add(new Point(0, Volfied.BOARD_HEIGHT));
		poli.add(new Point(0, 0));

	}
}
