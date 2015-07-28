package UI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstrumentLine extends JPanel {	


	private JLabel newLineLabel = new JLabel("New Line");
	
	
	public InstrumentLine()
	{	
		initObject();
	}
	
	
	public InstrumentLine(int i)
	{
		initObject();
		newLineLabel.setText("Line " + i);
	}
	
	private void initObject()
	{
		this.setBackground(Color.GRAY);		
		this.setLayout(new BorderLayout());
		this.add(newLineLabel, BorderLayout.WEST);			
		
	}

}
