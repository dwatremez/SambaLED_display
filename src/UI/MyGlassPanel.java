package UI;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class MyGlassPanel extends JPanel{

	//L'image qui sera dessin�e
	private BufferedImage img;
	//Les coordonn�es de l'image
	private Point location;
	//La transparence de notre glace
	private Composite transparence;

	public MyGlassPanel(){
		//Afin de ne peindre que ce qui nous int�resse
		setOpaque(false);
		//On d�finit la transparence
		transparence = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f);
	}   

	public void setLocation(Point location){
		this.location = location;        
	}

	public void setImage(BufferedImage image){
		img = image;
	}

	public void paintComponent(Graphics g){
		//Si on n'a pas d'image � dessiner, on ne fait rien�
		if(img == null)
			return;

		//Dans le cas contraire, on dessine l'image souhait�e
		Graphics2D g2d = (Graphics2D)g;
		g2d.setComposite(transparence);
		g2d.drawImage(img, (int) (location.getX() - (img.getWidth(this)  / 2)), (int) (location.getY() - (img.getHeight(this) / 2)), null);
	}   

	
}