package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class RemotePanel extends JPanel{

	private Color keyBlue = Color.decode("#42A5F5");
	private Color keyRed = Color.decode("#EF5350");
	private Color keyGreen = Color.decode("#9CCC65");
	private Color metalBlue = Color.decode("#90A4AE");

	private ArrayList<JButton> keyPad = new ArrayList<>();
	private String[] keyPadLabel = {"1", "2", "3", "A", "4", "5", "6", "B", "7", "8", "9", "C", "*", "0", "#", "D"}; 
	GridBagConstraints gbc = new GridBagConstraints();
	
	private JButton remoteVisibilityButton = new JButton("Hide Remote");

	private JButton triggerButton = new JButton("Send");
	private JButton modeButton = new JButton("Mode");

	public RemotePanel()
	{
		this.setLayout(new GridBagLayout());
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		remoteVisibilityButton.addActionListener(new VisibilityListener());
		remoteVisibilityButton.setBackground(metalBlue);
		remoteVisibilityButton.setBorderPainted(false);
		remoteVisibilityButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		remoteVisibilityButton.setFocusPainted(false);	
		remoteVisibilityButton.setPreferredSize(new Dimension(100,(int)(70*0.6)));
		this.add(remoteVisibilityButton, gbc);
		

		for(int i=0; i<16; i++)
		{
			JButton but = new JButton();
			but.setText(keyPadLabel[i]);
			but.addActionListener(new KeyPadListener());
			if((i%4 < 3 && i<12) || i== 13)
				but.setBackground(keyBlue);
			else
				but.setBackground(keyRed);
			but.setBorderPainted(false);
			but.setFont(new Font("Tahoma", Font.PLAIN, 38));
			but.setFocusPainted(false);	
			keyPad.add(but);
			
			gbc.gridx = i%4;
			gbc.gridy = (int)(i/4) + 1;
			gbc.gridwidth = 1;
			this.add(keyPad.get(i), gbc);
		}


		triggerButton.addActionListener(new TriggerListener());
		triggerButton.setFont(new Font("Tahoma", Font.PLAIN, 38));
		triggerButton.setBorderPainted(false);
		triggerButton.setFocusPainted(false);
		triggerButton.setBackground(keyGreen);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		this.add(triggerButton, gbc);

		modeButton.addActionListener(new ModeListener());
		modeButton.setFont(new Font("Tahoma", Font.PLAIN, 38));
		modeButton.setBorderPainted(false);
		modeButton.setFocusPainted(false);
		modeButton.setBackground(keyGreen);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		this.add(modeButton, gbc);
		
		changeVisibilityRemote();

	}
	
	public void changeVisibilityRemote()
	{
		if(remoteVisibilityButton.getText() == "Show Remote")
			remoteVisibilityButton.setText("Hide Remote");
		else
			remoteVisibilityButton.setText("Show Remote");
		
		for(JButton but : this.keyPad)
			but.setVisible(!but.isVisible());
		
		triggerButton.setVisible(!triggerButton.isVisible());
		modeButton.setVisible(!modeButton.isVisible());
	}
	
	class VisibilityListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{				
			changeVisibilityRemote(); 
		}		
	}
	
	class KeyPadListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//
		}		
	}

	class TriggerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//
		}		
	}

	class ModeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//
		}		
	}

}
