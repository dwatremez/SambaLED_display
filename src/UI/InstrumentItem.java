package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class InstrumentItem extends DragDropFocusJPanel {

	private String name = "Luc";
	private String type = "Caixa";
	private String shape = "Circle";
	private int pixelNb = 28;
	private int size = 70;

	private ArrayList<Color> pixelColor = new ArrayList<>();

	private static Color instrumentColor = Color.WHITE;
	private static Color backColor = Color.decode("#90A4AE");

	public InstrumentItem()
	{	
		super(backColor);
		init();
	}


	public InstrumentItem(String type, int pixelNb)
	{
		super(backColor);
		init();
		setParameters(type, pixelNb);
	}	

	public InstrumentItem(String name, String type, String shape, int pixelNb)
	{
		super(backColor);
		init();
		setParameters(name, type, shape, pixelNb);
	}
	
	public String toString()
	{
		return "[" + this.name + "," + this.type + "," + this.shape + "," + this.pixelNb + "]";
	}

	private void init()
	{
		this.setPreferredSize(new Dimension(size,(int)(1.2*size)));
		this.setMinimumSize(new Dimension(size,(int)(1.2*size)));
	}


	public void setParameters(String pType)
	{
		this.setParameters(pType,  0);
	}

	public void setParameters(String name, String type, String shape, int pixelNb)
	{
		this.name = name;
		this.type = type;
		this.shape = shape;
		this.pixelNb = pixelNb;
		this.repaint();
	}

	public void setParameters(String pType, int pPixelNb)
	{
		this.type = pType;
		this.name = pType;
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
			this.shape = "Triple Bar";
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

	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(instrumentColor);
		Font nameFont = new Font("Tahoma", Font.PLAIN, 12);
		g.setFont(nameFont);
		g.drawString(this.name, this.getWidth()/2 - g.getFontMetrics().stringWidth(this.name)/2,nameFont.getSize());
		Font typeFont = new Font("Tahoma", Font.PLAIN, 9);
		g.setFont(typeFont);
		g.drawString(this.type, this.getWidth()/2 - g.getFontMetrics().stringWidth(this.type)/2,nameFont.getSize() + typeFont.getSize());
		switch(shape)
		{
		case "Circle":
			g.fillOval((int)(this.getWidth()/2.0 - size/4.0),(int)(this.getHeight()/2.0 - size/4.0) + (nameFont.getSize() + typeFont.getSize())/2,(int)(size/2.0),(int)(size/2.0));
			break;
		case "Bar":
			g.fillRect((int)(this.getWidth()/2.0 - size/3.0),(int)(this.getHeight()/2.0 - size/8.0) + (nameFont.getSize() + typeFont.getSize())/2, (int)(2.0/3.0*size),(int)(size/4.0));
			break;	
		case "Triple Bar":
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0) - size/3.0), (int)(this.getHeight()/2.0 - size/4.0) + (nameFont.getSize() + typeFont.getSize())/2,(int)(size/4.0),(int)(size/2.0));
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0)),(int)(this.getHeight()/2.0 - size/4.0) + (nameFont.getSize() + typeFont.getSize())/2,(int)(size/4.0),(int)(size/2.0));
			g.fillRect((int)((this.getWidth()/2.0 - size/8.0) + size /3.0),(int)(this.getHeight()/2.0 - size/4.0) + (nameFont.getSize() + typeFont.getSize())/2,(int)(size/4.0),(int)(size/2.0));
			break;					
		default:

		}
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public int getPixelNb() {
		return pixelNb;
	}


	public void setPixelNb(int pixelNb) {
		this.pixelNb = pixelNb;
	}


	public String getShape() {
		return shape;
	}


	public void setShape(String shape) {
		this.shape = shape;
	}
}
