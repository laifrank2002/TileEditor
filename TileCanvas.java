import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 * Creates a canvas that takes a bunch of tiles and renders them onscreen.
 * 
 * @author Frank Lai
 * @version 2018-06-29
 */
public class TileCanvas extends JPanel
{
    private static final int DEFAULT_TILESIZE = 16;
	private static final int DEFAULT_ZOOMSCALE = 1;
	
	private static int nextID = 1;
	
	private int ID;
	private TileMap tileMap;
    private TileLoader tileLoader;
    private int tileSize;
    private int zoomScale;
	
	private boolean isActive;
    
	/**
     * Constructor for objects of class TileCanvas
     */
    public TileCanvas(TileLoader tileLoader)
    {
		ID = nextID++;
        // initialise instance variables
        tileMap = new TileMap();
        this.tileLoader = tileLoader;
        zoomScale = DEFAULT_ZOOMSCALE;
        tileSize = DEFAULT_TILESIZE;

        TileCanvas_ClickListener actionListener = new TileCanvas_ClickListener();
        addMouseListener(actionListener);
		
		isActive = true;
    } // end of constructor TileCanvas()

    /**
     * Overridden paint component to suit our own rendering purposes.
     */
    @Override
    public void paintComponent(Graphics graphics)
    {
        TileEditor.repaintFrame();
        int mapWidth = tileMap.getWidth();
        int mapHeight = tileMap.getHeight();

        for (int x = 0; x < mapWidth; x++)
        {
            for (int y = 0; y < mapHeight; y++)
            {
                // rendering
                try
                {
                    graphics.drawImage(tileLoader.getTileImage(tileMap.getTile(x,y)), x * tileSize, y * tileSize, tileSize, tileSize, null);
                }    
                catch (IndexOutOfBoundsException exception)
                {
                    System.out.println("Tile exists out of bounds: " + exception.getMessage());
                } // end of catch (IndexOutOfBoundsException exception)
            } // end of for (int y = 0; y < tileHeight; y++)       
        } // end of for (int x = 0; x < tileWidth; x++)

    }

    public void importFromString(String mapToLoad)
    {
        String[] mapHalves = mapToLoad.split("]");
        int width;
        int height;

        try
        {
            String[] dimensions = mapHalves[1].split(",");
            width = Integer.parseInt(dimensions[1]);
            height = Integer.parseInt(dimensions[2]);

            if (width < 0 || height < 0)
            {
                throw new IllegalArgumentException();
            } // end if
        }
        catch(NumberFormatException exception)
        {
            throw new IllegalArgumentException();
        } // end catch(NumberFormatException exception)

        tileMap = new TileMap(width,height);
        
        String[] tiles = mapHalves[0].substring(1,mapHalves[0].length()).split(",");
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // ensure integrity
                Integer tile;
                try
                {
                    tile = new Integer(Integer.parseInt(tiles[y * width + x]));
                }
                catch(NumberFormatException exception)
                {
                    throw new IllegalArgumentException();
                }
				tileMap.setTile(x,y,tile);
            }
        }

        System.out.println(width + "," + height);

    }
	
	/**
	 * Gets ID of component 
	 */
	public int getID()
	{
		return this.ID;
	} // end getter getID()
	
    /**
     * Imports a map from file using default format.
     */

    /**
     * Imports a map from file using a different selected format.
     */

    /**
     * Exports a map to file using default format.
     */

    /**
     * Exports a map to file using a different selected format.
     */
	
	/**
	 * Sets the active state of TileCanvas 
	 */
	public void setActive(boolean active)
	{
		this.isActive = active;
	}
	
    /**
     * toString
     * 
     * @return A string representation of the map. (Since that's all we REALLY care about.)
     */
    public String toString()
    {
		String toString = "TileMap" + tileMap.toString();
		toString += " ";
		toString += "TileCanvas[" + "zoomScale: " + zoomScale + "," + "tileSize: " + tileSize + "]";
        return toString;
    }
	
	/**
	 * Export tileMap to string 
	 */
	public String exportTileMapToString()
	{
		return tileMap.exportMapToString();
	}
	
    /**
     * Zooms in.
     */
    public void zoomIn()
    {
        zoomScale = zoomScale * 2;
        tileSize = tileSize * 2;
        rescale();
    }

    /**
     * Zooms out.
     */
    public void zoomOut()
    {
		if(zoomScale > 1)
		{
			zoomScale = zoomScale / 2;
			tileSize = tileSize / 2;
			rescale();
		}
    }

    /**
     * Adds one column to the map.
     */
    public void addColumn()
    {
		tileMap.addColumn();
        rescale();
    } // end of method addRow()

    /**
     * Adds one row to the map.
	 * NOTE! DUE TO A WAY THAT TILE EDITOR WORKS, THIS WON'T WORK IF THERE ARE NO COLUMNS!!!
     */
    public void addRow()
    {
        tileMap.addRow();
        rescale();
    } // end of method addColumn()

    /**
     * Removes one column from the map.
     */
    public void removeColumn()
    {
        tileMap.removeColumn();
        rescale();
    } // end of method removeRow()

    /**
     * Removes one row from the map.
     */
    public void removeRow()
    {
        tileMap.removeRow();
        rescale();
    } // end of method removeColumn()    
	
	/**
	 * Sets a tile.
	 * @param x tile column
	 * @param y tile row
	 */
	public void setTile(int x, int y, Integer tile)
	{
		tileMap.setTile(x,y,tile);
	}
	
	/**
	 * Sets a point based on x and y cartesian
	 * @param point a point relative to the tileCanvas, which starts at 0,0
	 */
	public void setTileFromCartesian(Point point, Integer tile)
	{
		
	}
	
	/**
	 * Checks if a cartesian point relative to this canvas is in bounds.
	 */
	public boolean isCartesianInBounds(int x, int y)
	{
		return tileMap.doesTileExist((int)Math.floor(x/tileSize),(int)Math.floor(y/tileSize));
	}
	
	/**
	 * Converts a cartesian coordinate to tile index coordinates 
	 * @param x x in cartesian
	 * @param y y in cartesian
	 * @return an xy point of tile index
	 */
	public Point convertCartesianToCoordinates(int x, int y)
	{
		return new Point((int)Math.floor(x/tileSize),(int)Math.floor(y/tileSize));
	}
	
    /**
     * Rescales the map automatically.
     */
    private void rescale()
    {
        int mapWidth = tileMap.getWidth();
        int mapHeight = tileMap.getHeight();

        // we must use PreferredSize in order for the scrollPane to work
        setPreferredSize(new Dimension(mapWidth * tileSize, mapHeight * tileSize)); 
        // System.out.println(mapWidth + "," + mapHeight);
        // set this for our scrollPane client to know to update itself
        revalidate();
        repaint();
    } // end of method rescale()

    /* private class */

    /*
     * Action Listener for tile canvas, uses AWT instead of Swing.
     */
    private class TileCanvas_ClickListener implements MouseListener
    {

        public void mouseClicked(MouseEvent event){}
        
        public void mouseEntered(MouseEvent event){} 

        public void mouseExited(MouseEvent event){}
        
        /**
         * When the user first clicks, paint there.
         * 
         * @param event the state received
         */
        public void mousePressed(MouseEvent event)
        {
			if(!isActive) return;
			
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            // convert from absolute to relative for our JPanel
            SwingUtilities.convertPointFromScreen(mousePoint, TileCanvas.this);

            int eventX = (int) mousePoint.getX();
            int eventY = (int) mousePoint.getY();
            System.out.println(mousePoint);
			
			// NOTE TO SELF: REDO COMPLETELY!
            // search map to determine source
            for (int x = 0; x < tileMap.getWidth(); x++)
            {
                for (int y = 0; y < tileMap.getHeight(); y++)
                {
                    if(eventX > (x * tileSize) && eventX < ((x + 1) * tileSize)
                    && eventY > (y * tileSize) && eventY < ((y + 1) * tileSize))
                    {
                        // change to selected
                        tileMap.setTile(x,y, TileEditor.getSelectedTile());
                        // paints only the relevant section 
                        paintImmediately(x * tileSize, y * tileSize, tileSize, tileSize);
                        // repaint();
                        // System.out.println(x + "," + y);
                        return;
                    }
                }
            }
        } // end of method mousePressed(MouseEvent event)

        public void mouseReleased(MouseEvent event){}

    } // end of class TileCanvas_ClickListener implements ItemListener
	
	/*
	 * I'M not going to click 1000 times, and neither will the user.
	 * Dragging is better.
	 */
	private class TileCanvas_MouseMotionListener implements MouseMotionListener
	{
		/**
		 *	On state changed, use this for drag painting
         * 
         * @param event the state received
		 */
		public void mouseDragged(MouseEvent event)
		{
			
		}
		
		public void mouseMoved(MouseEvent event){}
	}
} // end of class TileCanvas
