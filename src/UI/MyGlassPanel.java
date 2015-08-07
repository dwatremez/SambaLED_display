package UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MyGlassPanel extends JPanel {

	private BufferedImage img;
	private Point focusRect[] = null;
	private Color focusColor;
	private String text = "";
	private Point Location;
	private Composite transparence;

	public MyGlassPanel() {
		setOpaque(false);
		transparence = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.55f);
	}

	public void setLocation(Point location) {
		this.Location = location;
	}

	public void setImage(BufferedImage image) {
		img = image;
	}

	public void setFocus(Point[] p, Color c) {
		this.focusRect = p;
		this.focusColor = c;
	}

	public void setBadFocus() {
		if (img == null)
			return;

		this.focusRect = new Point[2];
		this.focusRect[0] = new Point(
				(int) (Location.getX() - (img.getWidth(this) / 2)),
				(int) (Location.getY() - (img.getHeight(this) / 2)));
		this.focusRect[1] = new Point((int) img.getWidth(this),
				(int) img.getHeight(this));
		this.focusColor = Color.red;

	}

	public void setString(String str) {
		this.text = str;
	}

	public void paintComponent(Graphics g) {
		if (img == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(transparence);
		g2d.drawImage(img, (int) (Location.getX() - (img.getWidth(this) / 2)),
				(int) (Location.getY() - (img.getHeight(this) / 2)), null);

		g.setFont(new Font("Tahoma", Font.PLAIN, 32));
		g.drawString(this.text, (int) (Location.getX() - g.getFontMetrics()
				.stringWidth(text) / 2),
				(int) (Location.getY() + (img.getWidth(this) / 2) + g
						.getFontMetrics().getHeight() / 4));

		if (focusRect != null) {
			g.setColor(focusColor);
			g.fillRect((int) focusRect[0].getX(), (int) focusRect[0].getY(),
					(int) focusRect[1].getX(), (int) focusRect[1].getY());

		}
	}

}