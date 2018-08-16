import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Scans tiles in the tiles folder from tileList.txt and then puts them into an array.
 * 
 * @author Frank Lai
 * @version 2018-06-29
 */
public class TileLoader
{
    /* class fields */
    private static final String DEFAULT_TILE_PATH = "tiles\\";
    public static String DEFAULT_TILE_MAP_PATH = "tiles\\tileMap.txt";
    /* instance */
    private ArrayList<BufferedImage> tile;
    private LinkedHashMap tile_paths;
    /**
     * Constructor for objects of class TileLoader.
     */
    public TileLoader()
    {
        File folder = new File(DEFAULT_TILE_PATH);
        if(folder == null) return;
        File[] allFiles = folder.listFiles();
        if(allFiles == null) return;

        /* 
        Hashmap based loader.

        Steps:
        1. load tileMap from file
        2. load all tiles from tileMap
        3. load tiles from default directory and add to tileMap
        4. export tileMap to default directory of tileMap

        This is to allow for tiles to be unique. In this way, we are not loading two tiles that are the same. Also, we are able to gurantee consistancy. 
         */
        tile = new ArrayList<BufferedImage>();
        tile_paths = new LinkedHashMap<String,String>();

        // 1. load tileMap from file
        try
        {
            BufferedReader input = new BufferedReader(new FileReader(DEFAULT_TILE_MAP_PATH));
            // continue to read until full
            String inputLine;
            while((inputLine = input.readLine()) != null)
            {
                String[] input_key_value_pair = inputLine.split(":");
                // 2. load all tiles from tileMap
                File file = new File(input_key_value_pair[1]);

                try
                {
                    BufferedImage image = ImageIO.read(file);
                    if (image != null)
                    {
                        // only write in if tile is unique, else ignore it
                        if (!tile_paths.containsKey(file.getName()))
                        { 
                            tile.add(image);
                            tile_paths.put(file.getName(), file.getPath());

                        } // end of if (tile_paths.containsKey(file.getName()))
                    } // end of if(image != null)
                } // end of try
                catch (IOException exception)
                {
                    // Do nothing, as the file is not an image then.
                } // end of catch (IOException)

            } // end of while(input.readLine() != null)
            // close file to prevent too many handlers
            input.close();
        }
        catch (IOException exception)
        {
            System.out.println("Could not establish connection to tileMap in default directory.");
        } // end of catch (IOException exception)
        catch (Exception exception)
        {
            // ultimate final
            System.out.println("Invalid format in file.");
        } // end of catch (Exception exception)
        // load tiles from tileMap

        // 3. load tiles from default directory and add to tileMap
        for (File file: allFiles)
        {
            try
            {
                BufferedImage image = ImageIO.read(file);
                if (image != null)
                {
                    // only write in if tile is unique, else ignore it
                    if (!tile_paths.containsKey(file.getName()))
                    { 
                        tile.add(image);
                        tile_paths.put(file.getName(), file.getPath());

                    } // end of if (tile_paths.containsKey(file.getName()))
                } // end of if(image != null)
            } // end of try
            catch (IOException exception)
            {
                // Do nothing, as the file is not an image then.
            } // end of catch (IOException exception)
        } // end of for (File file: allFiles)

        // 4. export tileMap to default directory of tileMap
        try
        {
            // establish writer
            PrintWriter output = new PrintWriter(new FileWriter(DEFAULT_TILE_MAP_PATH));
            
            // get a set of the entries
            Set set = tile_paths.entrySet();
      
            // get an iterator
            Iterator iterator = set.iterator();
            
            // output all of the hashes
            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                output.println(entry.getKey() + ":" + entry.getValue());
            }
            // remember to close in order to ensure output, otherwise the file will be blank and it will all be my fault
            output.close();
        }
        catch (IOException exception)
        {
            System.out.println("Unable to establish connection to file. Unable to output.");
        } // end of catch (IOException)
    } // end of constructor TileLoader
    
    /**
     * Load from another directory.
     */
    /*
    public void loadTiles(String path)
    {
        File folder = new File(path);
        if(folder == null) return;
        File[] allFiles = folder.listFiles();
        if(allFiles == null) return;

        // Try to read all files. If not, then discard the file.
        tile = new ArrayList<BufferedImage>();
        for (File file: allFiles)
        {
            try
            {
                BufferedImage image = ImageIO.read(file);
                if (image != null)
                {
                    tile.add(image);
                } // end of if(image != null)
            } // end of try
            catch (IOException exception)
            {
                // Do nothing, as the file is not an image then.
            } // end of catch (IOException)
        } // end of for (File file: allFiles)
    } // end of loadTiles(String path)
    */
    /**
     * Gets a tile.
     * 
     * @param index the index of the tile to be gotten.
     * @return the image of the tile.
     */
    public BufferedImage getTile(int index)
    {
        return tile.get(index);
    } // end of getTile(int index)

    /**
     * Gets the number of tiles.
     * 
     * @return the number of tiles.
     */
    public int getTileNumber()
    {
        return tile.size();
    } // end of getTileNumber()
    
    /**
     * Gets a value from the hashmap.
     */
    
    /**
     * Outputs all tiles to the console.
     */
    public void listTiles()
    {
        for (BufferedImage image: tile)
        {
            System.out.println(image);
        } // end of for (BufferedImage image: tile)
    } // end of method listTiles()
    
    
} // end of class TileLoader
