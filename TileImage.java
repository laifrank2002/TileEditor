import java.awt.image.BufferedImage;
/**
 * A TileImage that is a TYPE of tile.
 * We do this for the format in exporting to JSON.
 * This is STRICTLY for the TileLoader and TileExporter. 
 * NOT A REPRESENTATION OF A TILE!
 * 
 * @Frank Lai
 * @2018-12-28
 */
public class TileImage
{
    private static int nextID = 0;
    private int ID; // represents the order that the images were loaded in
    private BufferedImage image; 
    private String name; // string identifier
    /**
     * Constructor for TileImage
     */
    public TileImage(BufferedImage image, String name)
    {
        this.image = image;
        this.ID = ++nextID;
        this.name = name;
    } // end constructor TileImage(BufferedImage image)
    
    /**
     * @return the image
     */
    public BufferedImage getImage()
    {
        return image;
    } // end method getImage()
    
    /**
     * @return the ID
     */
    public int getID()
    {
        return ID;
    } // end method getID()
    
} // end class TileImage
