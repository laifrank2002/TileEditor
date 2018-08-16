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

    private List<List<Integer>> tileMap;
    private TileLoader tileLoader;
    private int tileSize;
    private int zoomScale;

    /**
     * Constructor for objects of class TileCanvas
     */
    public TileCanvas(TileLoader tileLoader)
    {
        // initialise instance variables
        tileMap = new ArrayList<List<Integer>>();
        this.tileLoader = tileLoader;
        zoomScale = 1;
        tileSize = DEFAULT_TILESIZE;

        TileCanvas_ClickListener actionListener = new TileCanvas_ClickListener();
        addMouseListener(actionListener);

    } // end of constructor TileCanvas()

    /**
     * Overridden paint component to suit our own rendering purposes.
     */
    @Override
    public void paintComponent(Graphics graphics)
    {
        TileEditor.repaintFrame();
        int mapWidth = tileMap.size();
        int mapHeight = 0;
        // ensure no nulls
        try
        {
            mapHeight = tileMap.get(0).size();
        }
        catch(IndexOutOfBoundsException exception)
        {
            // do nothing because we know mapHeight is 0 already
        }

        for (int x = 0; x < mapWidth; x++)
        {
            for (int y = 0; y < mapHeight; y++)
            {
                // rendering
                try
                {
                    graphics.drawImage(tileLoader.getTile(tileMap.get(x).get(y)), x * tileSize, y * tileSize, tileSize, tileSize, null);
                }    
                catch (IndexOutOfBoundsException exception)
                {
                    // do nothing for now, temporary debugging
                    // System.out.println(exception);
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

        tileMap = new ArrayList<List<Integer>>();

        for (int index = 0; index < width; index++)
        {
            addColumn();
        } // end of for (int index = 0; index < width; index ++)

        for (int index = 0; index < height; index++)
        {
            addRow();
        } // end of for (int index = 0; index < height; index ++)
        
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
                tileMap.get(x).set(y,tile);
            }
        }

        System.out.println(width + "," + height);

    }

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
     * Exports toString.
     * 
     * @return A string representation of the map.
     */
    public String mapToString()
    {
        String mapString = "";
        int mapWidth = tileMap.size();
        int mapHeight = 0;
        // ensure no nulls
        try
        {
            mapHeight = tileMap.get(0).size();
        }
        catch(IndexOutOfBoundsException exception)
        {
            // do nothing because we know mapHeight is 0 already
        }

        for (int y = 0; y < mapWidth; y++)
        {
            for (int x = 0; x < mapHeight; x++)
            {
                if (x == mapWidth - 1 && y == mapHeight - 1)
                {
                    mapString += tileMap.get(x).get(y);
                }
                else
                {
                    mapString += tileMap.get(x).get(y) + ",";
                }
            } // end of for (int y = 0; y < tileHeight; y++)       
        } // end of for (int x = 0; x < tileWidth; x++)
        
        // flip map string becuase it was inverted somewhere
        
        return  "[" + mapString + "]" + "," + mapWidth + "," + mapHeight;
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
        zoomScale = zoomScale / 2;
        tileSize = tileSize / 2;
        rescale();
    }

    /**
     * Adds one column to the map.
     */
    public void addColumn()
    {
        tileMap.add(new ArrayList<Integer>());

        // adds enough rows to fill up the column to sync it

        for (int index = 0; index < tileMap.get(0).size(); index++)
        {
            tileMap.get(tileMap.size() - 1).add(new Integer(0));
        }

        rescale();
    } // end of method addRow()

    /**
     * Adds one row to the map.
     */
    public void addRow()
    {
        for (List row : tileMap)
        {
            row.add(new Integer(0));
        } // end of for (ArrayList row : tileMap)
        rescale();
    } // end of method addColumn()

    /**
     * Removes one column from the map.
     */
    public void removeColumn()
    {
        if (tileMap.size() > 0)
        {
            tileMap.remove(tileMap.size() - 1);
        } // end of if (tileMap.size() > 0)
        rescale();
    } // end of method removeRow()

    /**
     * Removes one row from the map.
     */
    public void removeRow()
    {
        // if statements broken up to prevent null errors
        if (tileMap.size() > 0)
        {
            if (tileMap.get(0).size() > 0)
            {
                for (List row : tileMap)
                {
                    row.remove(row.size() - 1);
                } // end of for (ArrayList row : tileMap)
            } // end of if (tileMap.get(0).size() > 0)
        } // end of if (tileMap.size() > 0)
        rescale();
    } // end of method removeColumn()    

    /*
     * Rescales the map automatically.
     */
    private void rescale()
    {
        int mapWidth = tileMap.size();
        int mapHeight = 0;
        // ensure no nulls
        try
        {
            mapHeight = tileMap.get(0).size();
        }
        catch(IndexOutOfBoundsException exception)
        {
            // do nothing because we know mapHeight is 0 already
        }
        //setSize(new Dimension(mapWidth * tileSize, mapHeight * tileSize));
        repaint();
    } // end of method rescale()

    /* private class */

    /*
     * Action Listener for tile canvas, uses AWT instead of Swing.
     */
    private class TileCanvas_ClickListener implements MouseListener
    {

        /**
         * On state changed.
         * 
         * @param the state received.
         */
        public void mouseClicked(MouseEvent event)
        {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            // convert from absolute to relative for our JPanel
            SwingUtilities.convertPointFromScreen(mousePoint, TileCanvas.this);

            int eventX = (int) mousePoint.getX();
            int eventY = (int) mousePoint.getY();
            System.out.println(mousePoint);

            // search map to determine source
            for (int x = 0; x < tileMap.size(); x++)
            {
                for (int y = 0; y < tileMap.get(0).size(); y++)
                {
                    if(eventX > (x * tileSize) && eventX < ((x + 1) * tileSize)
                    && eventY > (y * tileSize) && eventY < ((y + 1) * tileSize))
                    {
                        // change to selected
                        tileMap.get(x).set(y, TileEditor.getSelectedTile());
                        repaint();
                        System.out.println(x + "," + y);
                        return;
                    }
                }
            }
        } // end of method itemStateChanged(ItemEvent event)

        public void mouseEntered(MouseEvent event){}

        public void mouseExited(MouseEvent event){}

        public void mousePressed(MouseEvent event){}

        public void mouseReleased(MouseEvent event){}

    } // end of class TileCanvas_ClickListener implements ItemListener
} // end of class TileCanvas
