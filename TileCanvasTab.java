import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Tile Canvas Tab!
 * So we can tab and not worry.
 */
public class TileCanvasTab extends JScrollPane
{
	private JTabbedPane pane;
	
	private String title;
	private TileCanvas canvas;
	
	/**
	 * Constructor with many prerequisites
	 */
	public TileCanvasTab(JTabbedPane pane, TileCanvas canvas)
	{
		super(canvas);
		this.title = "untitled";
		this.canvas = canvas;
		this.pane = pane;
		pane.addTab(this.title, this);
		pane.setTabComponentAt(pane.getTabCount() - 1, new TileCanvasTabButton(pane)); 
	} // end constructor TileCanvasTab(JTabbedPane pane, TileCanvas canvas)
	
	/**
	 * Constructor with many prerequisites
	 */
	public TileCanvasTab(JTabbedPane pane, TileCanvas canvas, String title)
	{
		super(canvas);
		this.title = title;
		this.canvas = canvas;
		this.pane = pane;
		pane.addTab(this.title, this);
		pane.setTabComponentAt(pane.getTabCount() - 1, new TileCanvasTabButton(pane)); 
	} // end constructor TileCanvasTab(JTabbedPane pane, TileCanvas canvas, String title)
	
	/**
	 * Gets component
	 */
	public TileCanvas getTileCanvas()
	{
		return canvas;
	} // end getter getTileCanvas()
	
	/**
	 * Gets the title of the canvas 
	 */
	public String getTitle()
	{
		return title;
	} // end getter getTitle()
	
	/**
	 * Takes a String and sets it as title of the canvas 
	 * Also tries to change the component title as well.
	 */
	public void setTitle(String title)
	{
		this.title = title;
				
		int tabCount = pane.getTabCount();
		for(int index = 0; index < tabCount; index++) 
		{
			if(pane.getComponentAt(index) == this)
			{
				pane.setTitleAt(index, title);
				return;
			}
		}
	} // end setter setTitle(String title)
	
	/**
	 * Sets our canvas active (or not!)
	 * @param active if the canvas is disabled or not
	 */
	public void setActive(boolean active)
	{
		canvas.setActive(active);
	} // end setter setActive(boolean active)
	
	/*
	 * How we're able to close this tab!
	 */
	private class TileCanvasTabButton extends JPanel
	{
		private JTabbedPane pane;
		
		public TileCanvasTabButton(JTabbedPane pane)
		{
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			this.pane = pane;
			setOpaque(false);
			
			// make our label get the same title as from the JTabbedPane
			JLabel label = new JLabel()
			{
				public String getText()
				{
					int index = pane.indexOfTabComponent(TileCanvasTabButton.this);
					if(index > -1) 
					{
						return pane.getTitleAt(index);
					}
					return null;
				}
			};
			
			add(label);
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			JButton button = new TabButton();
			add(button);
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		}
		
		private class TabButton extends JButton implements ActionListener
		{
			public TabButton()
			{
				setPreferredSize(new Dimension(17,17));
				setToolTipText("close this tab");
				
				setBorder(BorderFactory.createEtchedBorder());
				setBorderPainted(false);
				
				setUI(new BasicButtonUI());
				setContentAreaFilled(false);
				
				setFocusable(false);
				addMouseListener(new ButtonMouseListener());
				setRolloverEnabled(true);
				addActionListener(this);
			}
			
			public void actionPerformed(ActionEvent event)
			{	
				int index = pane.indexOfTabComponent(TileCanvasTabButton.this);
				if (index > -1) 
				{
					pane.remove(index);
				}
			}
			
			public void updateUI(){}
			
			public void paintComponent(Graphics graphics)
			{
				super.paintComponent(graphics);
				
				Graphics2D graphics2D = (Graphics2D) graphics.create();
				if(getModel().isPressed()) graphics2D.translate(1,1);
				
				graphics2D.setStroke(new BasicStroke(2));
				graphics2D.setColor(Color.BLACK);
				if (getModel().isRollover()) graphics2D.setColor(Color.RED);
				
				int shift = 6;
				graphics2D.drawLine(shift, shift, getWidth() - shift - 1, getHeight() - shift - 1);
				graphics2D.drawLine(getWidth() - shift - 1, shift, shift, getHeight() - shift - 1);
				// we dispose graphics2D because we were responsible for creating it. Don't dispose the graphics!
				graphics2D.dispose();
			}
		}
	} // end of class TileCanvasTabButton extends JPanel
	
	private class ButtonMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent event){}
		
		public void mouseEntered(MouseEvent event)
		{
			Component component = event.getComponent();
			if(component instanceof AbstractButton)
			{
				((AbstractButton) component).setBorderPainted(true);
			}
		}
		
		public void mouseExited(MouseEvent event)
		{
			Component component = event.getComponent();
			if(component instanceof AbstractButton)
			{
				((AbstractButton) component).setBorderPainted(false);
			}
		}
		
		public void mousePressed(MouseEvent event){}
		
		public void mouseReleased(MouseEvent event){}
	}
}