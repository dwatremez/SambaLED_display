package UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class DragFocusJPanel extends JPanel implements Cloneable{

	private MyGlassPanel myGlass;

	private boolean draggable = false;
	private boolean focusable = false;

	protected Color backColor = Color.gray;
	private Color focusColor = Color.DARK_GRAY;

	public DragFocusJPanel()
	{
		super();
		init();
	}

	public DragFocusJPanel(Color backColor,  boolean drag)
	{
		super();
		this.draggable = drag;
		this.focusable = true;
		this.backColor = backColor;
		this.setBackground(backColor);
		init();
	}

	private void init()
	{
		if(this.draggable)
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		if(this.focusable)
			this.setOpaque(false);		
	}

	public Object clone() {
		Object o = null;
		try 
		{
			o = super.clone();
		} 
		catch(CloneNotSupportedException cnse) 
		{
			cnse.printStackTrace(System.err);
		}
		return o;
	}

	public void setListenersForGlass(MyGlassPanel glass)
	{
		if(this.draggable)
		{
			this.myGlass = glass;
			this.addMouseListener(new MouseGlassListener(myGlass));
			this.addMouseMotionListener(new MouseGlassListener(myGlass));	
			this.addMouseListener(new MouseDnDropListener());			
			this.addMouseMotionListener(new MouseDnDropListener());
		}
	}

	public void resetListenersForGlass()
	{		
		for(MouseListener mgl : this.getMouseListeners())
			this.removeMouseListener(mgl);

		for(MouseMotionListener mgl: this.getMouseMotionListeners())
			this.removeMouseMotionListener(mgl);		

		setListenersForGlass(myGlass);
	}

	private InstrumentLine findInstrumentLine(Component c)
	{			
		while(c.getParent() != null && c.getClass() != InstrumentLine.class)
		{
			c = c.getParent();
		}
		if(c.getClass() == InstrumentLine.class)
			return (InstrumentLine)c;
		return null;
	}

	private InstrumentPanel findInstrumentPanel(Component c)
	{			
		while(c.getParent() != null && c.getClass() != InstrumentPanel.class)
		{
			c = c.getParent();
		}
		if(c.getClass() == InstrumentPanel.class)
			return (InstrumentPanel)c;
		return null;
	}

	private Component findComponentUnderMouse() 
	{
		Container container = findContentPane();
		Point location = MouseInfo.getPointerInfo().getLocation();
		try
		{
			SwingUtilities.convertPointFromScreen(location, container);
		}
		catch(NullPointerException e)
		{
			return null;
		}
		return SwingUtilities.getDeepestComponentAt(container, location.x, location.y);
	}

	private Container findContentPane() 
	{
		for (Window window : Window.getWindows()) 
		{
			if (window.getMousePosition(true) != null && window instanceof JFrame)
				return ((JFrame)window).getContentPane();
		}

		return null;
	}


	public class MouseGlassListener extends MouseAdapter{

		private MyGlassPanel myGlass;
		private BufferedImage image;		

		InstrumentLine lineSelected;
		InstrumentPanel panelSelected;

		public MouseGlassListener(MyGlassPanel glass){
			myGlass = glass;
		}
		@Override
		public void mouseEntered(MouseEvent event) {	
			setBackground(analogColor(backColor));						
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent event) {
			setBackground(backColor);
			repaint();
		}

		public void mousePressed(MouseEvent event) {
			if(event.getButton() != MouseEvent.BUTTON1)
				return;

			this.myGlass.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			Component composant = event.getComponent();
			Point location = (Point)event.getPoint().clone();
			SwingUtilities.convertPointToScreen(location, composant);
			SwingUtilities.convertPointFromScreen(location, myGlass);

			image = new BufferedImage(composant.getWidth(), composant.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			composant.paint(g);

			myGlass.setLocation(location);
			myGlass.setImage(image);
			myGlass.setVisible(true);
		}

		public void mouseReleased(MouseEvent event) {
			if(event.getButton() != MouseEvent.BUTTON1)
				return;

			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			Component composant = event.getComponent();
			Point location = (Point)event.getPoint().clone();			
			SwingUtilities.convertPointToScreen(location, composant);
			SwingUtilities.convertPointFromScreen(location, myGlass);

			myGlass.setLocation(location);
			myGlass.setImage(null);
			myGlass.setFocus(null, null);
			myGlass.setVisible(false);
		}

		public void mouseDragged(MouseEvent event) {
			Component source = event.getComponent();

			Point p = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, source);
			SwingUtilities.convertPointFromScreen(p, myGlass);

			myGlass.setLocation(p);

			// Draw Focus
			if(source.getClass() == InstrumentItem.class && findComponentUnderMouse() != null)
			{
				if((lineSelected = findInstrumentLine(findComponentUnderMouse())) != null)
				{
					//System.out.println("Dragged on Line");
					if(!lineSelected.getInstruments().isEmpty())
					{
						int position = 0;
						boolean inLine = false;
						if(p.getX() < lineSelected.getWidth())
						{
							position = lineSelected.getInstruments().size();
							inLine = true;
						}
						for(int i = 0; i<lineSelected.getInstruments().size(); i++)
						{
							if(p.getX() < lineSelected.getInstruments().get(i).getX())
							{
								position = i;
								break;
							}
						}
						
						if(inLine)
						{
							Point p0 = new Point();
							Point p1 = new Point();
							if(position == 0)
								p0.setLocation(lineSelected.getLabel().getWidth(), lineSelected.getY());
							else
								p0.setLocation(lineSelected.getLabel().getWidth() + lineSelected.getInstruments().get(position-1).getX() + lineSelected.getInstruments().get(position-1).getWidth(), lineSelected.getY());

							if(position == lineSelected.getInstruments().size())
								p1.setLocation(lineSelected.getWidth() - p0.getX(), lineSelected.getHeight());
							else
								p1.setLocation(lineSelected.getLabel().getWidth() + lineSelected.getInstruments().get(position).getX() - p0.getX(), lineSelected.getHeight());

							Point[] focusP = {p0, p1};
							myGlass.setFocus(focusP, analogColor(lineSelected.getBackColor()));		
							
						}
					}

				}
				else if((panelSelected =  findInstrumentPanel(findComponentUnderMouse()))!= null)
				{
					//System.out.println("Dragged on Panel: ");
					if(!panelSelected.getLines().isEmpty())
					{
						int position = 0;
						boolean inPanel = false;
						if(p.getY() < panelSelected.getInstrumentLinesPanel().getHeight())
						{
							position = panelSelected.getLines().size();
							inPanel = true;
						}
						for(int i = 0; i<panelSelected.getLines().size(); i++)
						{
							if(p.getY() < panelSelected.getLines().get(i).getY())
							{
								position = i;
								break;
							}
						}						

						if(inPanel)
						{
							Point p0 = new Point();
							Point p1 = new Point();
							if(position == 0)
								p0.setLocation(0, 0);
							else
								p0.setLocation(0, panelSelected.getLines().get(position-1).getY() + panelSelected.getLines().get(position-1).getHeight());

							if(position == panelSelected.getLines().size())
								p1.setLocation(panelSelected.getInstrumentLinesPanel().getWidth(), panelSelected.getInstrumentLinesPanel().getHeight() - p0.getY());
							else
								p1.setLocation(panelSelected.getInstrumentLinesPanel().getWidth(), panelSelected.getLines().get(position).getY() - p0.getY());

							Point[] focusP = {p0, p1};
							myGlass.setFocus(focusP, analogColor(panelSelected.getBackColor()));		
						}
					}
					else
					{
						Point p0 = new Point();
						Point p1 = new Point();
						p0.setLocation(0, 0);
						p1.setLocation(panelSelected.getInstrumentLinesPanel().getWidth(), panelSelected.getInstrumentLinesPanel().getHeight() - p0.getY());

						Point[] focusP = {p0, p1};
						myGlass.setFocus(focusP, analogColor(panelSelected.getBackColor()));	
					}
				}
			}
			myGlass.repaint();			
		}

		public Point getPointInComponent(MouseEvent e, Component c)
		{
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, e.getComponent());
			SwingUtilities.convertPointFromScreen(p, c);
			//System.out.println(p);
			return p;
		}

		private Color analogColor(Color c)
		{
			float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			if(hsb[1] > 0.1)
				return new Color(Color.HSBtoRGB((float)(hsb[0] + 10 * 30.0/360.0), hsb[1], hsb[2]));
			else if(hsb[2] < 0.4)
				return c.brighter();
			else
				return c.darker();
		}
	}


	public class MouseDnDropListener extends MouseAdapter{

		InstrumentLine lineSelected;
		InstrumentPanel panelSelected;

		public void mouseReleased(MouseEvent event) 
		{
			if(event.getButton() != MouseEvent.BUTTON1)
				return;

			JComponent source = (JComponent)event.getSource(); 

			if(source.getClass() == InstrumentItem.class)
			{
				// Move and Copy InstrumentItem in InstrumentLine
				if((lineSelected = findInstrumentLine(findComponentUnderMouse())) != null)
				{
					System.out.println(" --------------------- ");
					System.out.println("copy: " + source + " in " + lineSelected.toString());
					InstrumentItem clone = (InstrumentItem)((InstrumentItem)source).clone();
					clone.resetListenersForGlass();

					lineSelected.addInstrument(clone, getPointInComponent(event, lineSelected));
					lineSelected.setBackground(lineSelected.getBackColor());
					lineSelected.repaint();

					// Delete source
					deleteOldItem((InstrumentItem)source);
				}

				// Move and Copy InstrumentItem in new Line in InstrumentPanel
				else if((panelSelected = findInstrumentPanel(findComponentUnderMouse())) != null)
				{
					InstrumentItem clone = (InstrumentItem)((InstrumentItem)source).clone();
					clone.resetListenersForGlass();

					lineSelected = new InstrumentLine();
					lineSelected.addInstrument(clone);
					panelSelected.addLine(lineSelected,getPointInComponent(event, panelSelected.getInstrumentLinesPanel()));
					panelSelected.repaint();

					// Delete source
					deleteOldItem((InstrumentItem)source);
				}
			}
		}

		public Point getPointInComponent(MouseEvent e, Component c)
		{
			Point p = (Point) e.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, e.getComponent());
			SwingUtilities.convertPointFromScreen(p, c);
			//System.out.println(p);
			return p;
		}

		public void deleteOldItem(InstrumentItem source)
		{
			InstrumentLine sourceLine;
			InstrumentPanel panel;
			if(source.getParent().getParent() != null)
				if(source.getParent().getParent().getClass() == InstrumentLine.class)
				{
					sourceLine = (InstrumentLine)source.getParent().getParent();
					System.out.println("suppr: " + source + " from " + sourceLine.toString());
					System.out.println("origin: " + sourceLine.getInstruments().toString());
					sourceLine.removeInstrument((InstrumentItem)source);
					System.out.println("remain: " + sourceLine.getInstruments().toString());
					if(!sourceLine.getInstruments().isEmpty())
						sourceLine.repaint();
					else if((panel = findInstrumentPanel(sourceLine)) != null)
					{
						panel.removeLine(sourceLine);
						panel.updateLinesDisplay();
					}
				}			
		}
	}
}
