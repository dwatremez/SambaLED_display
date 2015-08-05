package UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MyGlassPanel extends JPanel{

	private BufferedImage img;
	private Point focusRect[] = null;
	private Color focusColor;
	private Point Location;
	private Composite transparence;

	public MyGlassPanel(){
		setOpaque(false);
		transparence = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f);
	}   

	public void setLocation(Point location){
		this.Location = location;        
	}

	public void setImage(BufferedImage image){
		img = image;
	}

	public void setFocus(Point[] p, Color c){
		this.focusRect = p;
		this.focusColor = c;
	}

	public void paintComponent(Graphics g){
		if(img == null)
			return;
			
			
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(transparence);
		g2d.drawImage(img, (int) (Location.getX() - (img.getWidth(this)  / 2)), (int) (Location.getY() - (img.getHeight(this) / 2)), null);

		if(focusRect != null)
		{
			g.setColor(focusColor);
			g.fillRect((int)focusRect[0].getX(), (int)focusRect[0].getY(), (int)focusRect[1].getX(), (int)focusRect[1].getY());
		}
	}   

	
}