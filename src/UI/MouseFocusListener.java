package UI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MouseFocusListener extends MouseAdapter
{
	Color backColor;
	
	public MouseFocusListener()
	{
	}
	
	
	public MouseFocusListener(Color c)
	{
		this.backColor = c;		
	}
	
	
	@Override
	public void mouseEntered(MouseEvent event)
	{
		if(backColor == null)
			event.getComponent().setBackground(analogColor((event.getComponent().getBackground())));
		else
			event.getComponent().setBackground(analogColor(backColor));
			
		event.getComponent().repaint();
	}

	@Override
	public void mouseExited(MouseEvent event) 
	{
		if(backColor == null)
			event.getComponent().setBackground(cancelAnalogColor((event.getComponent().getBackground())));
		else
			event.getComponent().setBackground(backColor);
		event.getComponent().repaint();
	}
	
	private Color analogColor(Color c)
	{
		return analogColor(c, 10);
	}
	
	private Color cancelAnalogColor(Color c)
	{
		return analogColor(c, 2);
	}

	private Color analogColor(Color c, int i)
	{
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		if(hsb[1] > 0.1 && backColor != null)
			return new Color(Color.HSBtoRGB((float)(hsb[0] + i * 30.0/360.0), hsb[1], hsb[2]));
		else if(hsb[2] < 0.4 || (hsb[2] > 0.4 && i == 2))
			return c.brighter();
		else if(hsb[2] > 0.4 || (hsb[2] < 0.4 && i == 2))
			return c.darker();
		return c;
	}		
}