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
	
	Random rand = new Random();
	
	Image offscreen;
	
	int delay, frame;
	
	Player player   = new Player();
	Ship ship = new Ship(rand.nextInt(900 - 100 + 1) + 100, rand.nextInt(600 - 100 + 1) + 100);
	Critter critter1 = new Critter(rand.nextInt(900 - 100 + 1) + 100, rand.nextInt(600 - 100 + 1) + 100);
	Critter critter2 = new Critter(rand.nextInt(900 - 100 + 1) + 100, rand.nextInt(600 - 100 + 1) + 100);
	Critter critter3 = new Critter(rand.nextInt(900 - 100 + 1) + 100, rand.nextInt(600 - 100 + 1) + 100);


	Thread animator = new Thread(this);
	static Terrain terain  = new Terrain();
	
	Graphics g_main, bufferGraphics;
	public static int window_width, window_height;
	public static int keyState;

	public void init() {
		addKeyListener(this);
		int fps = 100;
		delay = (fps > 0) ? (1000 / fps) : 100;
		offscreen = createImage(this.getSize().width,this.getSize().height);
		bufferGraphics = offscreen.getGraphics();
	}
	
	 /**
     * This method is called when the applet becomes visible on
     * the screen. Create a thread and start it.
     */
    public void start() {
		animator.start();
    }
	
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
	    	//player.pl_move();
		}
	}
	public void paint(Graphics g_main) {
		window_width  = this.getSize().width;
		window_height = this.getSize().height;
		
		bufferGraphics.clearRect(0,0, window_width, window_height); 
		
		
		
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, window_width, window_height);
		
		bufferGraphics.setColor(Color.black);
		int n = terain.poli.size()-1;
		for (int i = 0; i < n; i++)
			bufferGraphics.drawLine(GRID_X + terain.poli.get(i).x, GRID_Y + terain.poli.get(i).y ,
							GRID_X + terain.poli.get(i+1).x, GRID_Y + terain.poli.get(i+1).y);
		//g_main.drawRect(GRID_X, GRID_Y, BOARD_WIDTH, BOARD_HEIGHT);
		player.draw(bufferGraphics);
		ship.draw(bufferGraphics);
		critter1.draw(bufferGraphics);
		critter2.draw(bufferGraphics);
		critter3.draw(bufferGraphics);
		g_main.drawImage(offscreen,0, 0, this);
	}

	public void update(Graphics g_main) {
		paint(g_main);
	}

	public void keyPressed(KeyEvent ke) {
		keyState = ke.getKeyCode();
		player.key_decide(keyState);
	}

	public void keyReleased(KeyEvent ke) {}
	public void keyTyped(KeyEvent ke) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	public static void main (String[] args) {
		Applet applet = new Volfied();
		Frame frame = new Frame();
		frame.addWindowListener(new WindowAdapter() {
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