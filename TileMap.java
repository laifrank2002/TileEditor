import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * A representation of a map. 
 * Sort of like a bitmap, but for tiles instead of colours.
 * A bitmap of bitmaps, if you will.
 * Inspired by Tilepaper's Wandering.
 * 
 * The way it works:
 * A column of rows. 
 * ||||
 * ||||
 * ||||
 * 
 * Note that this only works with a certain array of tileSets from TileLoader
 * If you change that, you change this, or you might have unexpected errors.
 * @author Frank Lai
 * @version 2018-08-15
 */
public class TileMap
{
    private ArrayList<Integer> map;
    private int width;
    private int height;
    
	/**
	 * A default map
	 */
	public TileMap()
    {
        map = new ArrayList<Integer>();
        // initialize
        this.width = 0;
        this.height = 0;
    } // end constructor TileMap()
	
    /**
     * A map
     * @param width the width of the map
     * @param height the height of the map
     */
    public TileMap(int width, int height)
    {
        map = new ArrayList<Integer>();
        // initialize
        this.width = width;
        this.height = height;
        addColumn(width);
        addRow(height);
        
    } // end constructor TileMap(int width, int height)
	
	/**
	 * Returns the number of rows
	 * @return the number of rows
	 */
	public int getRows()
	{
		return height;
	}
	
	/**
	 * Returns the number of rows
	 * @return the number of rows
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the number of columns
	 * @return the number of columns
	 */
	public int getColumns()
	{
		return width;
	}
	
	/**
	 * Returns the number of columns
	 * @return the number of columns
	 */
	public int getWidth()
	{
		return width;
	}
	
    /**
     * Converts from x and y to an index inside the map
     * @param x the x index (column)
     * @param y the y index (row)
     * 
     * @return the index of the map
     * -1 if out of bounds
     */
    public int getIndex(int x, int y)
    {
        if (x >= 0 && x <= width)
        {
            if (y >= 0 && y <= height)
            {
                return x * height + y;
            }
        }
        return -1;
    } // end method getIndex(int x, int y)
    
    /**
     * Gets the tile at the specified index
     * 
     * @param index the index
     * @return the tile
     */
    public Integer getTile(int index)
    {
        return map.get(index);
    } // end getTile(int index)
    
    /**
     * Gets the tile at the specified x and y indices
     * @param x the x index (column)
     * @param y the y index (row)
     * 
     * @return the tile
     */
    public Integer getTile(int x, int y)
    {
        return map.get(getIndex(x,y));
    } // end getTile(int x, int y)
    
	/**
	 * Check if Tile Exists
	 * @param x the x index (column)
     * @param y the y index (row)
	 * @return if it is a valid coordinate
	 */
	public boolean doesTileExist(int x, int y)
	{
		if(x >= 0
			&& x < width
			&& y >= 0
			&& y < height)
		{
			return true;
		}
		return false;
	}
	
    /**
     * Sets a tile using index
     * @param index the index of the set tile
     * @param tile the new value for the set tile
     */
    public void setTile(int index, Integer tile)
    {
        map.set(index, tile);
    } // end of method setTile(int index)
    
    /**
     * Sets a tile using x and y coordinates
     * @param the x coordinate of the set tile
     * @param the y coordinate of the set tile
     * @param tile the new value for the set tile
     */
    public void setTile(int x, int y, Integer tile)
    {
        setTile(getIndex(x,y), tile);
    } // end of method setTile(int x, int y)
    
	/**
     * Adds a column
     */
    public void addColumn()
    {
        for (int index = height - 1; index >= 0; index--)
        {
            map.add(new Integer(0));
        }
        width = width + 1;
    } // end method addColumn()
    
    /**
     * Adds a specified number of columns
     * @param columns the specified number of columns
     */
    public void addColumn(int columns)
    {
        for (int index = 0; index < columns; index++)
        {
            addColumn();
        }
    } // end method addColumn(int columns)
    
	// NOTE! THIS IS... WEIRD?
    /**
     * Adds a row
     */
    public void addRow()
    {
        // you cannot add if width is 0, or else arrayOutOfBounds
        if (width > 0)
        {
			for (int index = width - 1; index >= 0; index--)
			{
				map.add(getIndex(index,height),new Integer(0));
			}
        }
        height = height + 1;
    } // end method addRow()
    
    /**
     * Adds a specified number of rows
     * @param rows the specified number of rows
     */
    public void addRow(int rows)
    {
        for (int index = 0; index < rows; index++)
        {
            addRow();
        }
    } // end method addRow(int rows)
    
	// KEYWORD: CHECK REMOVEROW AND REMOVECOLUMN!!!
    /**
     * Removes the last column
     */
    public void removeColumn()
    {
        // prevents silly errors like having -5 width
        if (width > 0)
        {
            // remove backwards
            for (int index = height - 1; index >= 0; index --)
            {
                map.remove(index + (width-1)*height);
            }
            width = width - 1;
        }
    } // end method removeColumn()
    
    /**
     * Removes a specified number of columns from the end
     * @param columns the specified number of columns
     */
    public void removeColumn(int columns)
    {
        for (int index = 0; index < columns; index++)
        {
            removeColumn();
        }
    } // end method removeColumn(int columns)
    
    /**
     * Removes the last row
     */
    public void removeRow()
    {
        if (height > 0)
        {
            // remove it backwards so that the index that we want to change to is unaffected by the adding of elements
            for (int index = width - 1; index >= 0; index--)   
            {
                map.remove(getIndex(index,height-1));
            }
            height = height - 1;
        }
    } // end method removeRow()
    
    /**
     * Removes a specified number of rows from the end
     * @param rows the specified number of rows
     */
    public void removeRow(int rows)
    {
        for (int index = 0; index < rows; index++)
        {
            removeRow();
        }
    } // end method removeRow(int rows)

    /**
     * Loads a map from string
     * @param string the string representation of the map
     */
    public void importMap(String string) // NOT FINISHED
    {
        try
        {
            String mapString = string.split("]")[0].split("[")[1];
            String parameters = string.split("]")[1];
            String widthString = parameters.split("width: ")[1].split(",")[0];
            String heightString = parameters.split("height: ")[1].split("}")[0];
            
            int width = Integer.parseInt(widthString);
            int height = Integer.parseInt(heightString);
            ArrayList<Integer> map = new ArrayList<Integer>();
            String[] mapArray = mapString.split(",");
            for (int index = 0; index < mapArray.length; index++)
            {
				try 
				{
					map.add(new Integer(Integer.parseInt(mapArray[index])));
				}
                catch(NumberFormatException exception)
				{
					System.out.println("Unable to parse tile: " + mapArray[index]);
				}
            }
            
            this.map = map;
            this.width = width;
            this.height = height;
        }
        catch(Exception exception)
        {
            System.out.println("Unable to load map with string: " + string);
            exception.printStackTrace();
        }
    } // end method importMap(String string)
    
    /**
     * Exports a map to string,
     * Also known as "saving"
     */
    public String exportMapToString()
    {
        String map = "";
        map += "{[";
        for (int index = 0; index < this.map.size(); index++)
        {
            if (index == this.map.size() - 1)
            {
                map += this.map.get(index) + "]";
            }
            else
            {
                map += this.map.get(index) + ",";
            }
        }
        map += "," + "width: " + width + "," + "height: " + height + "}";
        return map;
    } // end method exportMapToString()
    
    /**
     * Exports a map to a data readable format
     * For debugging purposes.
     */
    public String toString()
    {
        String map = "";
        map += "{[";
        for (int index = 0; index < this.map.size(); index++)
        {
            if (index == this.map.size() - 1)
            {
                map += this.map.get(index) + "]";
            }
            else
            {
                map += this.map.get(index) + ",";
            }
        }
        map += "," + "width: " + width + "," + "height: " + height + "}";
        return map;
    } // end method toString()
    
} // end of class TileMap
