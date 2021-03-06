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
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public class DragDropFocusJPanel extends JPanel implements Cloneable{

	private MyGlassPanel myGlass;

	protected Color backColor = Color.gray;

	public DragDropFocusJPanel()
	{
		super();
		init();
	}

	public DragDropFocusJPanel(Color backColor)
	{
		super();
		this.backColor = backColor;
		this.setBackground(backColor);
		init();
	}

	private void init()
	{
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		this.myGlass = glass;
		this.addMouseListener(new MouseFocusListener(backColor));
		this.addMouseListener(new MouseGlassListener(myGlass));
		this.addMouseMotionListener(new MouseGlassListener(myGlass));	
		this.addMouseListener(new MouseDnDropListener());			
		this.addMouseMotionListener(new MouseDnDropListener());
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

	public void deleteItemAndEmptyLine(InstrumentItem source)
	{			
		InstrumentLine sourceLine;
		InstrumentPanel panel;
		if(source.getParent().getParent() != null)
			if(source.getParent().getParent().getClass() == InstrumentLine.class)
			{
				sourceLine = (InstrumentLine)source.getParent().getParent();
				//System.out.println("suppr: " + source + " from " + sourceLine.toString());
				//System.out.println("origin: " + sourceLine.getInstruments().toString());
				sourceLine.removeInstrument((InstrumentItem)source);
				//System.out.println("remain: " + sourceLine.getInstruments().toString());
				if(!sourceLine.getInstruments().isEmpty())
					sourceLine.repaint();
				else if((panel = findInstrumentPanel(sourceLine)) != null)
				{
					panel.removeLine(sourceLine);
					panel.updateLinesDisplay();
				}
			}			
	}


	public class MouseGlassListener extends MouseAdapter{

		private MyGlassPanel myGlass;
		private BufferedImage image;

		InstrumentLine lineSelected;
		InstrumentPanel panelSelected;

		public MouseGlassListener(MyGlassPanel glass){
			myGlass = glass;
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

			// Print copy if CTRL pressed
			if(event.isControlDown())
				myGlass.setString("Copy");
			else
				myGlass.setString("");

			myGlass.setLocation(location);
			myGlass.setImage(image);
			myGlass.setVisible(true);
		}

		public void mouseReleased(MouseEvent event) 
		{
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

		public void mouseDragged(MouseEvent event) 
		{			
			Component source = event.getComponent();

			Point pg = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(pg, source);
			SwingUtilities.convertPointFromScreen(pg, myGlass);

			myGlass.setLocation(pg);
			myGlass.setFocus(null, null);

			// Draw Focus
			if(source.getClass() == InstrumentItem.class && findComponentUnderMouse() != null)
			{
				Point p = (Point)pg.clone();
				SwingUtilities.convertPointToScreen(p, myGlass);
				// Draw focus on Line
				if((lineSelected = findInstrumentLine(findComponentUnderMouse())) != null)
				{
					SwingUtilities.convertPointFromScreen(p, lineSelected);
					myGlass.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

					// Print copy if CTRL pressed
					if(event.isControlDown())
						myGlass.setString("Copy");
					else
						myGlass.setString("");
					//System.out.println("Dragged on Line");
					if(!lineSelected.getInstruments().isEmpty())
					{
						int position = 0;
						boolean inLine = false;
						for(int i = 0; i<lineSelected.getInstruments().size(); i++)
						{
							if(p.getX() < lineSelected.getInstruments().get(i).getX() + lineSelected.getLabel().getWidth())
							{
								position = i;
								inLine = true;
								break;
							}
						}
						if(p.getX() < lineSelected.getWidth() && !inLine)
						{
							position = lineSelected.getInstruments().size();
							inLine = true;
						}
						if(position != 0)
						{
							if(p.getX() < lineSelected.getInstruments().get(position-1).getX() + lineSelected.getInstruments().get(position-1).getWidth())
							{
								myGlass.setBadFocus();
								inLine = false;
							}
						}

						if(inLine)
						{
							Point p0 = new Point();
							Point p1 = new Point();
							if(position == 0)
								p0.setLocation(0, 0);
							else
								p0.setLocation(lineSelected.getInstruments().get(position-1).getX() + lineSelected.getInstruments().get(position-1).getWidth(), 0);

							if(position == lineSelected.getInstruments().size())
								p1.setLocation(lineSelected.getWidth(), lineSelected.getHeight());							
							else
								p1.setLocation(lineSelected.getLabel().getWidth() + lineSelected.getInstruments().get(position).getX(), lineSelected.getHeight());

							p0.setLocation(p0.getX() + lineSelected.getLabel().getWidth(), p0.getY() - findInstrumentPanel(findComponentUnderMouse()).getScroll());
							p1.setLocation(p1.getX() - p0.getX(), p1.getY());


							if(p0.getY() + p1.getY() > findInstrumentPanel(findComponentUnderMouse()).getScrollPanel().getHeight())
								p1.setLocation(p1.getX(), findInstrumentPanel(findComponentUnderMouse()).getScrollPanel().getHeight() - p0.getY());

							SwingUtilities.convertPointToScreen(p0, lineSelected);
							SwingUtilities.convertPointFromScreen(p0, myGlass);

							//System.out.println(p0.toString() + p1.toString());

							Point[] focusP = {p0, p1};
							myGlass.setFocus(focusP, analogColor(lineSelected.getBackColor(),2));	
						}
					}

				}
				// Draw focus on Panel
				else if((panelSelected =  findInstrumentPanel(findComponentUnderMouse()))!= null)
				{
					SwingUtilities.convertPointFromScreen(p, panelSelected);
					//System.out.println("Dragged on Panel: ");
					if(!panelSelected.getLines().isEmpty())
					{
						int position = 0;
						boolean inPanel = false;
						if(p.getY() < panelSelected.getScrollPanel().getHeight())
						{
							position = panelSelected.getLines().size();
							inPanel = true;
						}
						for(int i = 0; i<panelSelected.getLines().size(); i++)
						{
							if(p.getY() < panelSelected.getLines().get(i).getY()  - panelSelected.getScroll())
							{
								position = i;
								break;
							}
						}						

						if(inPanel)
						{
							myGlass.setString("New line");
							myGlass.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
							Point p0 = new Point();
							Point p1 = new Point();
							if(position == 0)
								p0.setLocation(0, 0);
							else
								p0.setLocation(0, panelSelected.getLines().get(position-1).getY() + panelSelected.getLines().get(position-1).getHeight());

							if(position == panelSelected.getLines().size())
								p1.setLocation(panelSelected.getScrollPanel().getWidth(), panelSelected.getScrollPanel().getHeight() - p0.getY());
							else
								p1.setLocation(panelSelected.getScrollPanel().getWidth(), panelSelected.getLines().get(position).getY() - p0.getY());

							p0.setLocation(p0.getX(), p0.getY() - panelSelected.getScroll());

							if(p0.getY() + p1.getY() > panelSelected.getScrollPanel().getHeight())
								p1.setLocation(p1.getX(), panelSelected.getScrollPanel().getHeight() - p0.getY());
							
							SwingUtilities.convertPointToScreen(p0, panelSelected);
							SwingUtilities.convertPointFromScreen(p0, myGlass);

							Point[] focusP = {p0, p1};
							myGlass.setFocus(focusP, analogColor(panelSelected.getBackColor()));		
						}
					}
					else
					{
						if(p.getY() < panelSelected.getScrollPanel().getHeight())
						{		
							myGlass.setString("New line");
							myGlass.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));				

							Point p0 = new Point();
							Point p1 = new Point();
							p0.setLocation(0, 0);
							p1.setLocation(panelSelected.getScrollPanel().getWidth(), panelSelected.getScrollPanel().getHeight());

							SwingUtilities.convertPointToScreen(p0, panelSelected);
							SwingUtilities.convertPointFromScreen(p0, myGlass);

							Point[] focusP = {p0, p1};
							myGlass.setFocus(focusP, analogColor(panelSelected.getBackColor()));
						}
						else
						{
							myGlass.setString("");
							myGlass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));								
						}
					}
				}
			}
			myGlass.repaint();			
		}

		private Color analogColor(Color c)
		{
			return analogColor(c, 10);
		}

		private Color analogColor(Color c, int i)
		{
			float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
			if(hsb[1] > 0.1)
				return new Color(Color.HSBtoRGB((float)(hsb[0] + i * 30.0/360.0), hsb[1], hsb[2]));
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
					//System.out.println(" --------------------- ");
					//System.out.println("copy: " + source + " in " + lineSelected.toString());
					InstrumentItem clone = (InstrumentItem)((InstrumentItem)source).clone();
					clone.resetListenersForGlass();

					int nbInstru = lineSelected.getInstruments().size();
					lineSelected.addInstrument(clone, getPointInComponent(event, lineSelected));
					lineSelected.setBackground(lineSelected.getBackColor());
					lineSelected.repaint();

					// Delete source if Control not pressed
					if(!event.isControlDown() && nbInstru != lineSelected.getInstruments().size())
						deleteItemAndEmptyLine((InstrumentItem)source);
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

					// Delete source if Control not pressed
					if(!event.isControlDown())
						deleteItemAndEmptyLine((InstrumentItem)source);
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
	}
}
