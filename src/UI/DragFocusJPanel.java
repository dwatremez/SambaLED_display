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
		setFocusColors(backColor);
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

	public void setFocusColors(Color c)
	{
		backColor = c;
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		if(hsb[1] > 0.1)
			focusColor = analogColor(c);
		else if(hsb[2] < 0.4)
			focusColor = backColor.brighter();
		else
			focusColor = backColor.darker();
		this.setBackground(backColor);
	}

	private Color analogColor(Color c)
	{
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return new Color(Color.HSBtoRGB((float)(hsb[0] + 10 * 30.0/360.0), hsb[1], hsb[2]));
	}

	public void setListenersForGlass(MyGlassPanel glass)
	{
		if(this.draggable)
		{
			this.addMouseListener(new MouseFocusListener());
			this.myGlass = glass;
			this.addMouseListener(new MouseGlassListener(myGlass));
			this.addMouseMotionListener(new MouseGlassMotionListener(myGlass));	
			this.addMouseListener(new MouseDnDropListener());			
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
		SwingUtilities.convertPointFromScreen(location, container);
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


	public class MouseFocusListener extends MouseAdapter{

		@Override
		public void mouseEntered(MouseEvent event) {	
			setBackground(focusColor);						
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent event) {
			setBackground(backColor);
			repaint();
		}
	}

	public class MouseGlassListener extends MouseAdapter{

		private MyGlassPanel myGlass;
		private BufferedImage image;

		public MouseGlassListener(MyGlassPanel glass){
			myGlass = glass;
		}

		public void mousePressed(MouseEvent event) {
			if(event.getButton() != MouseEvent.BUTTON1)
				return;

			//On récupère le composant pour en déduire sa position
			Component composant = event.getComponent();
			Point location = (Point)event.getPoint().clone();

			//Les méthodes ci-dessous permettent, dans l'ordre, 
			//de convertir un point en coordonnées d'écran
			//et de reconvertir ce point en coordonnées fenêtres
			SwingUtilities.convertPointToScreen(location, composant);
			SwingUtilities.convertPointFromScreen(location, myGlass);

			//Les instructions ci-dessous permettent de redessiner le composant
			image = new BufferedImage(composant.getWidth(), composant.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			composant.paint(g);

			//On passe les données qui vont bien à notre GlassPane
			myGlass.setLocation(location);
			myGlass.setImage(image);

			//On n'oublie pas de dire à notre GlassPane de s'afficher
			myGlass.setVisible(true);
		}

		public void mouseReleased(MouseEvent event) {
			if(event.getButton() != MouseEvent.BUTTON1)
				return;

			//On récupère le composant pour en déduire sa position
			Component composant = event.getComponent();
			Point location = (Point)event.getPoint().clone();
			//Les méthodes ci-dessous permettent, dans l'ordre, 
			//de convertir un point en coordonnées d'écran
			//et de reconvertir ce point en coordonnées fenêtre
			SwingUtilities.convertPointToScreen(location, composant);
			SwingUtilities.convertPointFromScreen(location, myGlass);

			//On passe les données qui vont bien à notre GlassPane
			myGlass.setLocation(location);
			myGlass.setImage(null);
			//On n'oublie pas de ne plus l'afficher
			myGlass.setVisible(false);
		}
	}


	public class MouseGlassMotionListener extends MouseAdapter{

		private MyGlassPanel myGlass;

		public MouseGlassMotionListener(MyGlassPanel glass){
			myGlass = glass;
		}

		public void mouseDragged(MouseEvent event) {
			Component c = event.getComponent();

			Point p = (Point) event.getPoint().clone();
			SwingUtilities.convertPointToScreen(p, c);
			SwingUtilities.convertPointFromScreen(p, myGlass);
			myGlass.setLocation(p);
			myGlass.repaint();
		}
	}


	public class MouseDnDropListener extends MouseAdapter{

		public void mouseReleased(MouseEvent event) 
		{
			if(event.getButton() != MouseEvent.BUTTON1)
				return;
			//---------------------------------------------------------------------
			//On implémente le transfert lorsqu'on relâche le bouton de souris
			//Ceci afin de ne pas supplanter le fonctionnement du déplacement
			JComponent source = (JComponent)event.getSource(); 


			if(source.getClass() == InstrumentItem.class)
			{
				// Move and Copy InstrumentItem in InstrumentLine
				InstrumentLine line;
				InstrumentPanel panel;
				if((line = findInstrumentLine(findComponentUnderMouse())) != null)
				{
					System.out.println(" --------------------- ");
					System.out.println("copy: " + source + " in " + line.toString());
					InstrumentItem clone = (InstrumentItem)((InstrumentItem)source).clone();
					clone.resetListenersForGlass();
					line.addInstrument(clone, getPointInComponent(event, line));
					line.repaint();
					// Delete source
					deleteOldItem((InstrumentItem)source);
				}

				// Move and Copy InstrumentItem in new Line in InstrumentPanel
				else if((panel = findInstrumentPanel(findComponentUnderMouse())) != null)
				{
					line = new InstrumentLine((int)System.currentTimeMillis());
					InstrumentItem clone = (InstrumentItem)((InstrumentItem)source).clone();
					clone.resetListenersForGlass();
					line.addInstrument(clone);
					panel.addLine(line,getPointInComponent(event, panel.getInstrumentLinesPanel()));
					panel.repaint();
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
			System.out.println(p);
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
