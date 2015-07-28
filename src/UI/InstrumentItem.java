package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class InstrumentItem extends JPanel{

	private String type = "Caixa";
	private String shape = "Circle";
	private int pixelNb = 28;
	private int size = 60;

	private ArrayList<Color> pixelColor = new ArrayList<>();

	private Color instrumentColor = Color.WHITE;

	public InstrumentItem()
	{	
		init();
	}


	public void setParameters(String pType)
	{
		this.setParameters(pType,  0);
	}

	public void setParameters(String pType, int pPixelNb)
	{
		this.type = pType;
		this.pixelNb = pPixelNb;
		
		switch(pType)
		{
		case "Surdo 1":
		case "Surdo 2":
		case "Surdo 3":
			this.shape = "Circle";
			this.pixelNb = 42;
			this.size = 100;
			break;
		case "Caixa":
		case "Repique":
		case "Tarol":
			this.shape = "Circle";
			this.pixelNb = 28;
			this.size = 60;
			break;				
		case "Tamborim":
			this.shape = "Circle";
			this.pixelNb = 13;
			this.size = 30;
			break;
		case "Agogo":
			this.shape = "3Bar";
			this.pixelNb = 15;
			this.size = 50;
			break;
		case "Chocalho":
			this.shape = "Bar";
			this.pixelNb = 7;
			this.size = 50;
			break;
		case "Cuica":
			this.shape = "Circle";
			this.pixelNb = 28;
			this.size = 60;
			break;
		default:		
		}

		if(pPixelNb != 0)
			this.pixelNb = pPixelNb;
		
		this.revalidate();
	}

	private void init()
	{
		this.setPreferredSize(new Dimension(size*2,size));			
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(instrumentColor);
		switch(shape)
		{
		case "Circle":
			g.fillOval(this.getWidth()/2 - size/4, this.getHeight()/2 - size/4, size/2, size/2);
			break;
		case "Bar":
			g.fillRect(this.getWidth()/2 - size/2, this.getHeight()/2 - size/8, 2*size, size/4);
			break;	
		case "3Bar":
			g.fillRect((int)((this.getWidth()/2 - size/8) - size/3), this.getHeight()/2 - size/4, size/4, size/2);
			g.fillRect((this.getWidth()/2 - size/8), this.getHeight()/2 - size/4, size/4, size/2);
			g.fillRect((int)((this.getWidth()/2 - size/8) + size /3), this.getHeight()/2 - size/4, size/4, size/2);
			break;					
		default:

		}
	}


}
