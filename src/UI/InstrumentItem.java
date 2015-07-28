package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class InstrumentItem extends JPanel{

	private String type;
	private String shape;
	private int pixelNb;
	private int size = 50;

	private ArrayList<Color> pixelColor = new ArrayList<>();


	public InstrumentItem()
	{	
		init();
	}

	private void init()
	{
		this.setPreferredSize(new Dimension(size*2,size));			
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.WHITE);
		g.fillOval(this.getWidth()/2 - size/4, this.getHeight()/2 - size/4, size/2, size/2);
	}
	

}
