package Engine;

public class RCSwitch {
	
	int receivedValue = 0;
	boolean available = true;
	
	public RCSwitch()
	{
		
	}
	
	public void enableReceive(int i)
	{
		
	}
	
	public boolean available()
	{
		return this.available;
	}
	
	public void resetAvailable()
	{
		this.available = true;
	}
	
	public int getReceivedValue()
	{
		return this.receivedValue;
	}

}
