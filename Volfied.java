import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;


public class Volfied extends Applet implements KeyListener, Runnable
{
	private static final long serialVersionUID = 1L;

	static final int OFFSET_GRID   = 10;
	static final int GRID_X 	   = OFFSET_GRID + Player.WIDTH/2;
	static final int GRID_Y 	   = OFFSET_GRID + Player.HEIGHT/2;
	static final int BOARD_WIDTH   = 1000;
	static final int BOARD_HEIGHT  = 600;

	static Random rand = new Random();

	Image offscreen;

	int delay, frame;
	static ArrayList<Critter> critters = new ArrayList<Critter>();
	static int n_critter = 3;
	boolean level_start = true;
	
	static Player player = new Player();
	static Ship ship = new Ship(rand.nextInt(700 - 100 + 1) + 100, rand.nextInt(400 - 100 + 1) + 100);

	Thread animator = new Thread(this);
	static Terrain terain  = new Terrain();

	Graphics g_main, bufferGraphics;
	public static int window_width, window_height;
	public static int keyState;

	
	public static void waiting (int n){
		long t0, t1;
		t0 =  System.currentTimeMillis();
		do{
			t1 = System.currentTimeMillis();
		}
		while ((t1 - t0) < (n * 1000));
	}
	
	@Override
	public void init() {
		addKeyListener(this);
		int fps = 50;
		delay = fps > 0 ? 1000 / fps : 100;
		offscreen = createImage(this.getSize().width,this.getSize().height);
		bufferGraphics = offscreen.getGraphics();
		System.out.println("TERAIN AREA=" + terain.calcArea());
		System.out.println("PRCENTAGE = " + terain.percentage());
		String critter_names[] = {"A", "B", "C"};
		for (int i = 0; i < n_critter; i++)
			critters.add(new Critter(rand.nextInt(700 - 100 + 1) + 100, rand.nextInt(400 - 100 + 1) + 100, critter_names[i]));
	}

	/**
	 * This method is called when the applet becomes visible on
	 * the screen. Create a thread and start it.
	 */
	@Override
	public void start() {
		animator.start();
	}
	
	@SuppressWarnings("deprecation")
	public void stop() {
		animator.stop();
	}

	@Override
	public void run() {
		// Remember the starting time
		long tm = System.currentTimeMillis();
		while (true) {
			// Display the next frame of animation.
			repaint();
			// Delay depending on how far we are behind.
			try {
				tm += delay;
				Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			} catch (InterruptedException e) {break;}
			// Advance the frame
			frame++;
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	public void paint(Graphics g_main) {
		window_width  = this.getSize().width;
		window_height = this.getSize().height;

		bufferGraphics.clearRect(0,0, window_width, window_height);

		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, window_width, window_height);
		
		System.out.println("Percentage=" + terain.percentage());
		if (terain.percentage() <= 20) {
			bufferGraphics.drawString("YOU WON!", BOARD_WIDTH/2, BOARD_HEIGHT/2);
			g_main.drawImage(offscreen,0, 0, this);
			animator.stop();
		}
		if (level_start) {
			level_start = false;
			bufferGraphics.setColor(Color.black);
			terain.poli.draw(bufferGraphics, GRID_X, GRID_Y);
			bufferGraphics.drawString("Level 1", BOARD_WIDTH/2, BOARD_HEIGHT/2);
			g_main.drawImage(offscreen,0, 0, this);
			waiting(1);
		}
		
		bufferGraphics.setColor(Color.black);
		terain.poli.draw(bufferGraphics, GRID_X, GRID_Y);
		player.draw(bufferGraphics);
		ship.draw(bufferGraphics);
		for (int i = 0; i < n_critter; i++)
		if (!critters.get(i).isDead())
			critters.get(i).draw(bufferGraphics);
		else bufferGraphics.drawString("Dead!", critters.get(i).x, critters.get(i).y);
		
		bufferGraphics.setColor(Color.black);
		int life_x = 10, life_y = 10;
		bufferGraphics.drawString("Lives", life_x, life_y);
		life_x += 20;
		life_y -= 10;
		if (player.getLives() == 0) {
			bufferGraphics.drawString("GAME OVER!", BOARD_WIDTH/2, BOARD_HEIGHT/2);
			stop();
		}
		for (int i = 0; i < Player.lives; i++) {
			life_x += 15;
			bufferGraphics.fillOval(life_x, life_y, 11, 11);
		}
		g_main.drawImage(offscreen,0, 0, this);
	}

	@Override
	public void update(Graphics g_main) {
		paint(g_main);
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		keyState = ke.getKeyCode();
		player.key_decide(keyState);
	}

	@Override
	public void keyReleased(KeyEvent ke) {}
	@Override
	public void keyTyped(KeyEvent ke) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}

	public static void main (String[] args) {
		Applet applet = new Volfied();
		Frame frame = new Frame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		applet.setPreferredSize(new Dimension(2 * GRID_X + BOARD_WIDTH, 2* GRID_Y + BOARD_HEIGHT));

		frame.add(applet);
		frame.pack();

		applet.init();
		applet.start();

		frame.setVisible(true);
	}

}