package trivia;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Trivia extends Applet implements Runnable, MouseListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	private Thread th = new Thread(this); //Game thread
	private enum states {mainMenu, dismantle, dismantlePause, create, createPause};
	private states gameState;
	private Actors actors;
	private int timer = 0;
	private Boolean debugMode = false;
	
	/**
	 * Creates a new Trivia game.
	 * @param debug If True, game prints out debug information.
	 */
	public Trivia(Boolean debug)
	{
		debugMode = debug;
	}

	/**
	 * Called when game is first initialized. Resets all values to default state.
	 */
	public void init()
	{
		actors = new Actors(debugMode);
		actors.setSize(WIDTH, HEIGHT);
	}
	
	/**
	 * Starts the game thread. Must be called from out of this class.
	 */
	public void start()
	{
		th.start();
	}
	
	/**
	 * Called to run the game. Will continue running until game is exitted.
	 * All game logic is called from here.
	 */
	public synchronized void run() 
	{
		//run until stopped
		while (true)
		{
			//add a step to the timer
			timer++;
			
			//controls game flow
			if (gameState == states.mainMenu)
			{
				//mainMenu();
			}
			else if (gameState == states.dismantle)
			{
				//dismantle();
			}
			else if (gameState == states.dismantlePause)
			{
				//pause();
			}
			else if (gameState == states.create)
			{
				//create();
			}
			else if (gameState == states.createPause)
			{
				//pause();
			}
			
			//repaint applet
			repaint();
			
			try
			{
				long timeIn = System.currentTimeMillis();
				wait(); //wait for applet to draw
				long timeOut = System.currentTimeMillis();
				while (timeOut-timeIn < 10)
				{
					timeOut = System.currentTimeMillis();
				}
			}
			catch (Exception ex)
			{
				log(ex.toString());
			}
		}
	}
	
	/**
	 * Updates the graphics of the game using a double buffer system.
	 */
	public void update(Graphics g)
	{	
		//start buffer
		Image dbImage = createImage(getWidth(), getHeight());
		Graphics dbg = dbImage.getGraphics();
		
		//clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, getWidth(), getHeight());
		
		//draw elements in background
		dbg.setColor(getForeground());
		paint(dbg);
		
		//draw image on screen
		g.drawImage(dbImage, 0, 0, this);
	}
	
	/**
	 * Only called from update(Graphics g).
	 * Paints all objects and menus in the game.
	 */
	public void paint(Graphics g)
	{
		g.setColor(Color.RED);
		
		if (gameState == states.mainMenu)
		{
			//drawMain(g);
		}
		else if ((gameState == states.dismantlePause) || (gameState == states.createPause))
		{
			//drawPause(g);
		}
		else
		{
			//actors.drawActors(g);
		}
		super.paint(g);
		synchronized(this) 
		{
			notifyAll();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Debug tool.
	 * Used to print a String if Debug mode is enabled.
	 * @param s String to print.
	 */
	private void log(String s) {
		if (debugMode)
		{
			System.out.println("Dan Miller");
		}
	}
}
