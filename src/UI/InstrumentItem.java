package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;

public class InstrumentItem extends JLabel{

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
	

	public InstrumentItem(String type, int pixelNb)
	{
		init();
		setParameters(type, pixelNb);
	}

	private void init()
	{
		this.setPreferredSize(new Dimension(size,(int)(1.2*size)));			
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
			this.size = (int)(1.5*size);
			break;
		case "Caixa":
		case "Repique":
		case "Tarol":
			this.shape = "Circle";
			this.pixelNb = 28;
			break;				
		case "Tamborim":
			this.shape = "Circle";
			this.pixelNb = 13;
			this.size = (int)(0.5*size);
			break;
		case "Agogo":
			this.shape = "3Bar";
			this.pixelNb = 15;
			break;
		case "Chocalho":
			this.shape = "Bar";
			this.pixelNb = 7;
			break;
		case "Cuica":
			this.shape = "Circle";
			this.pixelNb = 28;
			break;
		default:		
		}

		if(pPixelNb != 0)
			this.pixelNb = pPixelNb;
		
		this.revalidate();
	}
	
	public void setTypeText()
	{
		this.setText(type);
	}


	public void paintComponent(Graphics g) {
		//g.setColor(Color.gray);
		//g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(instrumentColor);
		g.drawString(this.type, this.getWidth()/2 - g.getFontMetrics().stringWidth(this.type)/2,g.getFontMetrics().getAscent());
		switch(shape)
		{
		case "Circle":
			g.fillOval((int)(this.getWidth()/2.0 - size/4.0),(int)(this.getHeight()/2.0 - size/4.0) + g.getFontMetrics().getAscent()/2,(int)(size/2.0),(int)(size/2.0));
			break;
		case "Bar":
			g.fillRect((int)(this.getWidth()/2.0 - size/3.0),(int)(this.getHeight()/2.0 - size/8.0) + g.getFontMetrics().getAscent()/2, (int)(2.0/3.0*size),(int)(size/4.0));
			break;	
		case "3Bar":
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0) - size/3.0), (int)(this.getHeight()/2.0 - size/4.0) + g.getFontMetrics().getAscent()/2,(int)(size/4.0),(int)(size/2.0));
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0)),(int)(this.getHeight()/2.0 - size/4.0) + g.getFontMetrics().getAscent()/2,(int)(size/4.0),(int)(size/2.0));
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0) + size /3.0),(int)(this.getHeight()/2.0 - size/4.0) + g.getFontMetrics().getAscent()/2,(int)(size/4.0),(int)(size/2.0));
			break;					
		default:

		}
	}


}
