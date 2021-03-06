import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.UIManager;

/**
 * Tile Editor, generating maps and then exporting them in different formats.
 * Availible formats:
 * .json, .tep (tile editor project)
 * License: There is none.
 * 
 * @author Frank Lai
 * @version 2018-06-29
 */
public class TileEditor
{
    /* class fields */
    private static final String FRAME_TITLE = "Tile Editor";

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private static final int MAPPALETTE_WIDTH = 100;
    private static final int MAPPALETTE_HEIGHT = 600;

    private static final int TILECANVAS_WIDTH = 400;
    private static final int TILECANVAS_HEIGHT = 300;

    private static final String TOOLBAR_NAME = "Map Options";
    private static final String[] TOOLBAR_BUTTON_PATHS = {"images\\icons\\add_column.png"
        ,"images\\icons\\remove_column.png"
        ,"images\\icons\\add_row.png"
        ,"images\\icons\\remove_row.png"
        ,"images\\icons\\zoom_in.png"
        ,"images\\icons\\zoom_out.png"};
    private static final String[] TOOLBAR_BUTTON_NAMES = {"Add Column","Remove Column","Add Row","Remove Row","Zoom In","Zoom Out"};
    /* instance */
    private static TileLoader tile;

    private static int selectedTile;
    private static int secondaryTile;
    /* GUI elements */
    private static JFrame frame;
    // menu bar
    private static JMenuBar menuBar;

    private static JMenu menuBar_file;
    private static JMenuItem menuBar_file_new;
    private static JMenuItem menuBar_file_open;
    private static JMenuItem menuBar_file_openTileSet;
    private static JMenuItem menuBar_file_save;
    private static JMenuItem menuBar_file_saveAs;
    private static JMenuItem menuBar_file_export;
    private static JMenuItem menuBar_file_quit;

    private static JMenu menuBar_edit;
    private static JMenu menuBar_options;
    private static JMenu menuBar_help;

    // tool bar
    private static JToolBar toolBar;
    private static String toolBar_name;
    private static JButton[] toolBar_button;
    
	// the main tab
	private static JTabbedPane canvasTabs;
    // map canvas
    private static TileCanvas tileCanvas;

    // map palette
    private static JPanel mapPalette;
    private static JLabel mapPaletteSelectedLabel;
    private static JButton[] mapPaletteButton;
    /**
     * Main method.
     * 
     * @param argument not used
     */
    public static void main(String[] argument)
    {
        /* init */
        tile = new TileLoader();
		// when we want a system like look and feel.
		// NOT CONSISTENT, will change across systems
		try 
		{
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception exception)
		{
			System.out.println("Unable to set look and feel: " + exception.getMessage());
		}
		
        createFrame();

    } // end of method main(String[] argument)

    /* utility functions */
    /**
     * Zooms in.
     */
    public static void zoomIn()
    {
        tileCanvas.zoomIn();
    }

    /**
     * Zooms out.
     */
    public static void zoomOut()
    {
        tileCanvas.zoomOut();
    }

    /**
     * Adds one row to the map.
     */
    public static void addRow()
    {
        tileCanvas.addRow();
    } // end of method addRow()

    /**
     * Adds one column to the map.
     */
    public static void addColumn()
    {
        tileCanvas.addColumn();
    } // end of method addColumn()

    /**
     * Removes one row from the map.
     */
    public static void removeRow()
    {
        tileCanvas.removeRow();
    } // end of method removeRow()

    /**
     * Removes one column from the map.
     */
    public static void removeColumn()
    {
        tileCanvas.removeColumn();
    } // end of method removeColumn()    

    /**
     * Gets the currently selected tile.
     * 
     * @return selected tile index.
     */
    public static int getSelectedTile()
    {
        return selectedTile;
    }

    /**
     * Import from String.
     * 
     * @String a representation of the map. <br>
     * Format: "[1,2,4,6,7,9,12,15],2,4"
     * Format: "[tile,tile,tile,tile...],width,height"
     */
    public static void importFromString(String mapToLoad)
    {
        try
        {
            tileCanvas.importFromString(mapToLoad);
        }
        catch(IllegalArgumentException exception)
        {
            System.out.println("Invalid Format!");
        }
        catch(Exception exception)
        {
            System.out.println("Unable to load map.");
        }
    } // end of method importFromString(String mapToLoad)    

    /**
     * Import from console.
     */

    
    /**
     * Export from console.
     */
    public static void exportToConsole()
    {
        String exportedMap = "";
        exportedMap += tileCanvas.exportTileMapToString();
        System.out.println(exportedMap);
    } // end of method exportToConsole()

    /**
     * Repaints the frame, this is to allow other classes inside to repaint();.
     */
    public static void repaintFrame()
    {
        frame.repaint();
    } // end of method repaintFrame()
    /* private utility */

    /*
     * Updates the selected tile label.
     */
    private static void updateSelectedTile()
    {
        mapPaletteSelectedLabel.setIcon(new ImageIcon(tile.getTileImage(selectedTile)));

    } // end of method updateSelectedTile()
    /* GUI functions */

    /*
     * Creates a new JFrame with associated elements attached.
     */
    private static void createFrame()
    {
        frame = new JFrame(FRAME_TITLE);
        frame.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));

        createMenuBar();
        frame.setJMenuBar(menuBar);

		createCanvasTabs();
        frame.add(canvasTabs,BorderLayout.CENTER);
		// initialize!
		TileCanvasTab canvasTab = createCanvasTab();
		tileCanvas = canvasTab.getTileCanvas();
		// createCanvasTab();
		
        createMapPalette();
        frame.add(mapPalette,BorderLayout.EAST);

        createToolBar();
        frame.add(toolBar,BorderLayout.NORTH);

        frame.pack();
        frame.validate();
        frame.setVisible(true);
    } // end of method createFrame()
        
    /*
     * Creates a menubar with all associated functions.
     */
    private static void createMenuBar()
    {
        menuBar = new JMenuBar();
        ActionListener actionListener = new MenuBar_File_ActionListener();
        // file menubar

        menuBar_file = new JMenu("File");
        menuBar_file.getAccessibleContext().setAccessibleDescription(
            "file menu");
        
        menuBar_file_new = new JMenuItem("New");
        menuBar_file_new.getAccessibleContext().setAccessibleDescription(
            "create a new file");
        menuBar_file_new.addActionListener(actionListener);
        menuBar_file.add(menuBar_file_new);
        
        menuBar_file_open = new JMenuItem("Open");
        menuBar_file_open.getAccessibleContext().setAccessibleDescription(
            "open a new file");
        menuBar_file_open.addActionListener(actionListener);
        menuBar_file.add(menuBar_file_open);

        menuBar_file_openTileSet = new JMenuItem("Open Tile Set");
        menuBar_file_openTileSet.getAccessibleContext().setAccessibleDescription(
            "load a tile set");
        menuBar_file_openTileSet.addActionListener(actionListener);
        menuBar_file.add(menuBar_file_openTileSet);

        menuBar_file_save = new JMenuItem("Save");
        menuBar_file_save.getAccessibleContext().setAccessibleDescription(
            "save your file");
        menuBar_file_save.addActionListener(actionListener);    
        menuBar_file.add(menuBar_file_save);

        menuBar_file_saveAs = new JMenuItem("Save As");
        menuBar_file_saveAs.getAccessibleContext().setAccessibleDescription(
            "save your file as...");
        menuBar_file_saveAs.addActionListener(actionListener);    
        menuBar_file.add(menuBar_file_saveAs);

        menuBar_file_export = new JMenuItem("Export");
        menuBar_file_export.getAccessibleContext().setAccessibleDescription(
            "export your file");
        menuBar_file_export.addActionListener(actionListener);      
        menuBar_file.add(menuBar_file_export);

        menuBar_file_quit = new JMenuItem("Quit");
        menuBar_file_quit.getAccessibleContext().setAccessibleDescription(
            "quit the program"); 
        menuBar_file_quit.addActionListener(actionListener);   
        menuBar_file.add(menuBar_file_quit);

        menuBar.add(menuBar_file);

    } // end of method createMenuBar()

    /*
     * Loads an image from file
     * @param path the path of the file
     * @return the buffered image, null if nothing can be read
     */
    private static BufferedImage readImageFromFile(String path)
    {
        File file = new File(path);

        try
        {
            BufferedImage image = ImageIO.read(file);
            return image;
        } // end of try
        catch (IOException exception)
        {
            return null;
        } // end of catch (IOException)

    } // end of method readImageFromFile(String path)
	
	/*
	 * Creates a new tab of type TileCanvasTab with a TileCanvas inside!
	 * @return TileCanvasTab a new TileCanvasTab with a default TileCanvas inside!
	 */
	private static TileCanvasTab createCanvasTab()
	{
		TileCanvas canvas = new TileCanvas(tile);
		canvas.setPreferredSize(new Dimension(TILECANVAS_WIDTH,TILECANVAS_HEIGHT));
		TileCanvasTab tab = new TileCanvasTab(canvasTabs,canvas);
		
		return tab;
	} // end of method createCanvasTab()

    /*
     * Creates a new map palette.
     */
    private static void createMapPalette()
    {
        mapPalette = new JPanel();
        mapPalette.setPreferredSize(new Dimension(MAPPALETTE_WIDTH,MAPPALETTE_HEIGHT));
        MapPalette_ButtonListener actionListener = new MapPalette_ButtonListener();
        // selected tile
        selectedTile = 0;

        mapPaletteSelectedLabel = new JLabel();
        mapPaletteSelectedLabel.setIcon(new ImageIcon(tile.getTileImage(selectedTile)));
        mapPaletteSelectedLabel.setPreferredSize(new Dimension(tile.getTileImage(selectedTile).getWidth(), tile.getTileImage(selectedTile).getHeight()));
        mapPalette.add(mapPaletteSelectedLabel);
        // auto generate tiles
        mapPaletteButton = new JButton[tile.getTileNumber()];
        for (int index = 0; index < tile.getTileNumber(); index++)
        {
            mapPaletteButton[index] = new JButton();
            mapPaletteButton[index].setIcon(new ImageIcon(tile.getTileImage(index)));
            mapPaletteButton[index].setPreferredSize(new Dimension(tile.getTileImage(index).getWidth(), tile.getTileImage(index).getHeight()));
            mapPaletteButton[index].addActionListener(actionListener);
            mapPalette.add(mapPaletteButton[index]);
        }// end of for (int index = 0; index < tile.getTileNumber(); index++) 

    } // end of method createMapPalette()

    /*
     * Creates a new tool bar.
     */
    private static void createToolBar()
    {
        toolBar = new JToolBar(TOOLBAR_NAME);
        ToolBar_ButtonListener actionListener = new ToolBar_ButtonListener();
        
        toolBar_button = new JButton[TOOLBAR_BUTTON_PATHS.length];
        
        for (int index = 0; index < TOOLBAR_BUTTON_PATHS.length; index++)
        {
            toolBar_button[index] = new JButton();
            toolBar_button[index].setIcon(new ImageIcon(readImageFromFile(TOOLBAR_BUTTON_PATHS[index])));
            toolBar_button[index].addActionListener(actionListener);
            toolBar.add(toolBar_button[index]);
        } // end for
    } // end of method createToolBar()
    /* private class */
	
	/*
	 * Create the main tabber.
	 */
	private static void createCanvasTabs()
	{
		canvasTabs = new JTabbedPane();
		canvasTabs.addChangeListener(new CanvasTabs_ChangeListener());
	} // end of method createCanvasTabs()
	
	/*
	 * Sets a tab as active or not.
	 */
	private static void setCanvasTabActive(int index, boolean active)
	{
		TileCanvasTab pane = (TileCanvasTab)canvasTabs.getComponentAt(index);
		if(pane == null)
		{
			System.out.println("Tab at: " + index + " was found to be null.");
			return;
		}
		pane.getTileCanvas().setActive(active);
	} // end of method setCanvasTabActive(int index, boolean active)
	
    /*
     * Button listener for toolBar 
     */
    private static class ToolBar_ButtonListener implements ActionListener
    {
        /**
         * On action performed.
         * 
         * @param the action received.
         */
        public void actionPerformed(ActionEvent event)
        {
            Object source = event.getSource();
            if (source == toolBar_button[0])
            {
                addColumn();
            } // end if
            else if (source == toolBar_button[1])
            {
                removeColumn();
            } // end if
            else if (source == toolBar_button[2])
            {
                addRow();
            } // end if
            else if (source == toolBar_button[3])
            {
                removeRow();
            } // end if
            else if (source == toolBar_button[4])
            {
                zoomIn();
            } // end if
            else if (source == toolBar_button[5])
            {
                zoomOut();
            } // end if
            
        } // end of method ActionPerformed(ActionEvent event)
    } // end of class class ToolBar_ButtonListener implements ActionListener

    /*
     * Button listener for map palette.
     */
    private static class MapPalette_ButtonListener implements ActionListener
    {

        /**
         * On action performed.
         * 
         * @param the action received.
         */
        public void actionPerformed(ActionEvent event)
        {
            Object source = event.getSource();
            for (int index = 0; index < tile.getTileNumber(); index++)
            {
                if (source == mapPaletteButton[index])
                {
                    selectedTile = index;
                    updateSelectedTile();
                    return;
                } // end of if (source == mapPaletteButton[index])
            }// end of for (int index = 0; index < tile.getTileNumber(); index++) 

        } // end of method ActionPerformed(ActionEvent event)
    } // end of class MapPalette_ButtonListener implements ActionListener
    
    /*
     * Action listener for menu bar for File
     */
    private static class MenuBar_File_ActionListener implements ActionListener
    {
        /**
         * On action performed.
         * 
         * @param the action received.
         */
        public void actionPerformed(ActionEvent event)
        {
            Object source = event.getSource();
            if (source == menuBar_file_new)
            {
                // action for new 
				createCanvasTab();
            }
            else if (source == menuBar_file_open)
            {
                
            }
            else if (source == menuBar_file_openTileSet)
            {
                
            }
            else if (source == menuBar_file_save)
            {
                
            }
            else if (source == menuBar_file_saveAs)
            {
                
            }
            else if (source == menuBar_file_export)
            {
                
            }
            else if (source == menuBar_file_quit)
            {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to quit?","Warning",dialogButton);
                
                if(dialogResult == JOptionPane.YES_OPTION)
                {
                      // shut down procedures
                      System.exit(0);
                }
            }
        } // end of method actionPerformed(ActionEvent event)
        
    } // end of class MenuBar_ActionListener implments ActionListener
	
	/*
		Change listener for Canvas Tabs 
	 */
	private static class CanvasTabs_ChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			Object source = event.getSource();
			
			// iterate through all tabs to find the one we want
			// sets them all to inactive, then we set the one we want to active.
			int tabCount = canvasTabs.getTabCount();
			for(int index = 0; index < tabCount; index++)
			{
				setCanvasTabActive(index,false);
			}
			
			int selectedIndex = canvasTabs.getSelectedIndex();
			// because if we delete, we'll find that it'll cause an exception.
			if(selectedIndex > -1 && selectedIndex < tabCount)
			{
				tileCanvas = ((TileCanvasTab)canvasTabs.getComponentAt(selectedIndex)).getTileCanvas();
				setCanvasTabActive(canvasTabs.getSelectedIndex(),true);
			}
			else if(tabCount > 0)
			{
				tileCanvas = ((TileCanvasTab)canvasTabs.getComponentAt(0)).getTileCanvas();
				setCanvasTabActive(canvasTabs.getSelectedIndex(),true);
			}
		}
	} // end of class CanvasTabs_ChangeListener implements ChangeListener
	
} // end of class TileEditor 