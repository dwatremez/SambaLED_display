package UI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseFocusListener extends MouseAdapter
{
	@Override
	public void mouseEntered(MouseEvent event)
	{
		event.getComponent().setBackground(analogColor((event.getComponent().getBackground())));
		event.getComponent().repaint();
	}

	@Override
	public void mouseExited(MouseEvent event) 
	{
		event.getComponent().setBackground(analogColor((event.getComponent().getBackground()),2));
		event.getComponent().repaint();
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