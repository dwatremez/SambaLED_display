package UI;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class SidePanel extends JPanel {
	
	private RemotePanel remPanel = new RemotePanel();
	private EditPanel editPanel = new EditPanel();
	
	public SidePanel()
	{
		this.setLayout(new BorderLayout());
		
		this.add(editPanel, BorderLayout.CENTER);
		this.add(remPanel, BorderLayout.SOUTH);
		
	}

}
