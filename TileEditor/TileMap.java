
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Maps different files to dictionaries.
 *
 * @author Frank Lai
 * @version 2018-08-15
 */
public class TileMap
{
    
    
    
    private ArrayList<BufferedImage> tile;
    
    /**
     * Imports the tilemap from the default directory.
     */
    public TileMap()
    {
        
        tile = new ArrayList<BufferedImage>();
    }
    
    /**
     * Imports the tilemap from another directory.
     */
    public void importFromFile(String path)
    {
        
    }
    
    /**
     * Exports a map the default directory.
     */
    public void exportDefault()
    {
        
    }
    
    /**
     * Exports a map to another directory.
     */
    public void exportToFile(String path)
    {
        
    }
} // end of class TileMap
