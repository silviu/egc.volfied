import java.awt.*;
import java.awt.event.*;
import java.applet.*;


public class Volfied extends Applet implements KeyListener, Runnable 
{
	private static final long serialVersionUID = 1L;
	
	static final int OFFSET_GRID   = 10;
	static final int GRID_X 	   = OFFSET_GRID + Player.WIDTH/2;
	static final int GRID_Y 	   = OFFSET_GRID + Player.HEIGHT/2;
	static final int BOARD_WIDTH   = 1000;
	static final int BOARD_HEIGHT  = 600;
	
	int delay, frame;
	
	Player player   = new Player();
	Thread animator = new Thread(this);
	static Terrain terain  = new Terrain();
	
	Graphics g_main;
	public static int window_width, window_height;
	public static int keyState;

	public void init() {
		addKeyListener(this);
		int fps = 70;
		delay = (fps > 0) ? (1000 / fps) : 100;
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
		
		g_main.setColor(Color.white);
		g_main.fillRect(0, 0, window_width, window_height);
		
		g_main.setColor(Color.black);
		g_main.drawRect(GRID_X, GRID_Y, BOARD_WIDTH, BOARD_HEIGHT);
		
		player.draw(g_main);
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