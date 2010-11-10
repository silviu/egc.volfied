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
	
	static final int PERCENTAGE_TO_WIN = 10;
	static final Color LEVEL_1_BG_COLOR = Color.getHSBColor(100, 100, 99);
	static final Color LEVEL_1_BOARD_COLOR = Color.getHSBColor(45, 45, 45);
	
	static final Color LEVEL_2_BG_COLOR = Color.getHSBColor(90, 170, 60);
	static final Color LEVEL_2_BOARD_COLOR = Color.getHSBColor(45, 45, 45);
	
	static Color bg_color = LEVEL_1_BG_COLOR;
	static Color board_color = LEVEL_1_BOARD_COLOR;
	
	int level = 1;
	
	static Random rand = new Random();

	Image offscreen;

	int delay, frame;
	
	static int n_critter = 2;
	boolean level_start = true;
	
	static Player player = new Player();
	static Ship ship;
	static ArrayList<Critter> critters = new ArrayList<Critter>();
	Thread animator = new Thread(this);
	static Terrain terain  = new Terrain();
	static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	Graphics g_main, bufferGraphics;
	public static int window_width, window_height;
	public static int keyState;

	
	
	public void create_stuff() {
		terain = new Terrain();
		ship = new Ship(rand.nextInt(700 - 100 + 1) + 100, rand.nextInt(400 - 100 + 1) + 100);
		for (int i = 0; i < n_critter; i++)
			critters.add(new Critter(rand.nextInt(700 - 100 + 1) + 100, rand.nextInt(400 - 100 + 1) + 100));
		
		packets.add(new Packet(1, BOARD_HEIGHT-Packet.SIZE, Packet.INCREASE_SPEED));
		packets.add(new Packet(BOARD_WIDTH-Packet.SIZE, BOARD_HEIGHT-Packet.SIZE, Packet.DECREASE_SPEED));
		packets.add(new Packet(1, 1, Packet.STOP_TIME));
		packets.add(new Packet(BOARD_WIDTH-Packet.SIZE, 1, Packet.BOMBS));
	}
	
	public static void waiting (int n){
		long t0, t1;
		t0 =  System.currentTimeMillis();
		do{
			t1 = System.currentTimeMillis();
		}
		while ((t1 - t0) < (n * 1000));
	}
	
	public void change_level() {
		n_critter++;
		bg_color = LEVEL_2_BG_COLOR;
		board_color = LEVEL_2_BOARD_COLOR;
	}
	
	@Override
	public void init() {
		addKeyListener(this);
		int fps = 50;
		delay = fps > 0 ? 1000 / fps : 100;
		offscreen = createImage(this.getSize().width,this.getSize().height);
		bufferGraphics = offscreen.getGraphics();
		create_stuff();
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

		bufferGraphics.setColor(bg_color);
		bufferGraphics.fillRect(0, 0, window_width, window_height);
		
		if (terain.percentageOccupied() >= PERCENTAGE_TO_WIN && level == 1) {
			bufferGraphics.drawString("YOU WON!", BOARD_WIDTH/2, BOARD_HEIGHT/2);
			change_level();
			level++;
			create_stuff();
			g_main.drawImage(offscreen,0, 0, this);
		}
		
		if (terain.percentageOccupied() >= PERCENTAGE_TO_WIN && level == 2) {
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
		
		ship.draw(bufferGraphics);
		for (int i = 0; i < n_critter; i++)
		if (!critters.get(i).isDead())
			critters.get(i).draw(bufferGraphics);
		else bufferGraphics.drawString("Dead!", critters.get(i).x, critters.get(i).y);

		
		for (int i = 0; i < packets.size(); i++)
			packets.get(i).draw(bufferGraphics);
		
		player.draw(bufferGraphics);
		
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