package Engine;

import java.awt.Color;
import java.util.ArrayList;

public class Adafruit_NeoPixel {

	private ArrayList<Color> colors = new ArrayList();
	private int pixels;
	private int brightness = 255;

	public Adafruit_NeoPixel(int nb, int pin, int x)
	{
		this.pixels = nb;
		begin();
	}

	public void begin()
	{
		for(int i=0; i<this.pixels; i++)
		{
			colors.add(new Color(0,0,0,0));
		}
	}

	public void show()
	{	

	}

	public int Color(int r, int g, int b)
	{
		int color = r * 1000000 + g * 1000 + b;
		return color;
	}

	public void setPixelColor(int i, int c)
	{
		if(c == 0)
			colors.set(i, new Color(0,0,0,0));
		else
		{
			String colorStr = String.valueOf(c);
			int l = 9-colorStr.length();
			if(colorStr.length() < 9)
				for(int j = 0; j<l; j++)
					colorStr = "0" + colorStr;
			colors.set(i, new Color(Integer.parseInt(colorStr.substring(0,3)), Integer.parseInt(colorStr.substring(3,6)), Integer.parseInt(colorStr.substring(6)), this.brightness));	
		}
	}
	
	public int numPixels()
	{
		return this.pixels;
	}

	public void setBrightness(int b)
	{
		this.brightness = b;
	}

}
