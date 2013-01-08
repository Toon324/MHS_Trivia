package trivia;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * Holds both a High and Low res Image for use in drawing and collision detection, respectively.
 * Creates the outline of an Image for use in collision detection from the Low-Res.
 * Provides High-Res Image for drawing.
 * @author Cody Swendrowski
 */
@SuppressWarnings("serial")
public class GameImage extends Component
{
	private BufferedImage image1, image2;
	private ArrayList<ArrayList<Point>> outlines = new ArrayList<ArrayList<Point>>();
	//private String path = "\\resources\\";
	private String path = "";
	private int width, height;
	
	/**
	 * Creates a new container of a High and Low res Image of name s, and generates an outline.
	 * @param s Name of Image to read in
	 */
	public GameImage(String s)
	{
		try {
			/*
		image1 = ImageIO.read(getClass().getResourceAsStream(path + s + ".png")); //Low res used for outline
		image2 = ImageIO.read(getClass().getResourceAsStream(path + s + "2.png")); //High res used for drawing
		*/
			image1 = ImageIO.read(getClass().getResource(path + s + ".png"));
			image2 = ImageIO.read(getClass().getResource(path + s + "2.png"));
			
		}
		catch (Exception e)
		{
			System.out.println("Error in Image retrevial for " + s);
		}
		width = image1.getWidth();
		height = image1.getHeight();
		try {
		outlines.add(new ArrayList<Point>());
		//log("--------------------");
		//log("Starting " + s);
		generate(outlines.get(0), 0, 0);
		width = image2.getWidth();
		height = image2.getHeight();
		}	
		catch (Exception e)
		{
			System.out.println("Error in Image generation for " + s);
		}
		
	}
	
	/**
	 * Creates a new container of a High and Low res Image of name s, and generates an outline.
	 * @param s Name of Image to read in
	 */
	public GameImage(String s, int w, int h)
	{
		try {
			/*
		image1 = ImageIO.read(SpaceImage.class.getResourceAsStream(path + s + ".png")); //Low res used for outline
		image2 = ImageIO.read(SpaceImage.class.getResourceAsStream(path + s + "2.png")); //High res used for drawing
		*/
		image1 = ImageIO.read(getClass().getResource(path + s + ".png"));
		image2 = ImageIO.read(getClass().getResource(path + s + "2.png"));
		}
		catch (Exception e)
		{
			System.out.println("Error in Image retrevial for " + s);
		}
		width = w;
		height = h;
		try{
		int n = (image1.getHeight()/h) * (image1.getWidth()/w);
		for (int o=0; o<n; o++)
		{
			outlines.add(new ArrayList<Point>());
		}
		
		for (int y=0; y<image1.getHeight()/h; y++)
		{
			for (int x=0; x<image1.getWidth()/w; x++)
			{
				generate(outlines.get(y*(image1.getWidth()/w)+x), width, height);
			}
		}
		}
		catch (Exception e)
		{
			System.out.println("Error in Image generation for " + s);
		}
	}
	
	/**
	 * Generates an outline from the Low-res Image by determining pixel color and comparing to surrounding pixels to determine an edge.
	 */
	private void generate(ArrayList<Point> outline, int w, int h)
	{
		//int w = image1.getWidth();
	    //int h = image1.getHeight();
	    
	    for (int x=w; x<w+width; x++)
	    {
	    	
	    	//log("Generating outline");
	    	//log("width: " + x + "/" + (w+width));
	    	
	    	Boolean last = null;
	    	for (int y=h; y<h+height; y++)
	    	{	
	    		Boolean solid = false; //Transparent
	    		int pixel = image1.getRGB(x, y);
	    		int alpha = (pixel >> 24) & 0xff;
	    	    if (alpha != 255) //Color
	    	    {
	    	    	solid = true;
	    	    }
	    	    if (last == null)
	    	    {
	    	    	last = solid;
	    	    }
	    	    else if (last != solid)
	    	    {
	    	    	outline.add(new Point(x,y));
	    	    }
	    	    
	    	    if (((y == height) || (x == height)) && solid)
	    	    {
	    	    	outline.add(new Point(x,y));
	    	    }
	    	    
	    	    last = solid;
	    	}
	    }
	}
	
	@SuppressWarnings("unused")
	private void log(String s) 
	{
		System.out.println("" + s);
		
	}

	/**
	 * Returns an ArrayList containing Point objects representing the outline.
	 * @return outline
	 */
	public ArrayList<Point> getOutline()
	{
		return outlines.get(0);
	}
	
	/**
	 * Overloaded method, allowing a sprite sheet to return multiple outlines. Supports up to 10 outlines.
	 * @param o Int outline to return.
	 * @return Outline called.
	 */
	public ArrayList<Point> getOutline(int o)
	{
		return outlines.get(o);
	}
	
	/**
	 * Returns the segment of the High-Res Image to draw with. Autocalculated.
	 * @return BufferedImage image
	 */
	public BufferedImage getImage(int xpos, int ypos)
	{
		if (image2 == null)
		{
			System.out.println("Null Image2: Check file name.");
			return null;
		}
		BufferedImage image = image2.getSubimage(xpos*width, ypos*height, width, height);
		return image;
	}
	
	/**
	 * Returns the High-Res Image to draw with.
	 * @return image
	 */
	public BufferedImage getImage()
	{
		return image2;
	}
}
