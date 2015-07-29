package UI;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class SidePanel extends JPanel {
	
	private RemotePanel remPanel = new RemotePanel();
	
	public SidePanel()
	{
		this.setLayout(new BorderLayout());
		
		this.add(remPanel, BorderLayout.SOUTH);
		
	}

}
