package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;


public class RemotePanel extends JPanel{

	private ArrayList<JButton> keyPad = new ArrayList<>();
	private String[] keyPadLabel = {"1", "2", "3", "A", "4", "5", "6", "B", "7", "8", "9", "C", "*", "0", "#", "D"}; 
	GridBagConstraints gbc = new GridBagConstraints();

	private JButton triggerButton = new JButton("Send");
	private JButton modeButton = new JButton("Mode");

	private Color keyBlue = Color.decode("#42A5F5");
	private Color keyRed = Color.decode("#EF5350");
	private Color keyGreen = Color.decode("#9CCC65");

	public RemotePanel()
	{
		this.setLayout(new GridBagLayout());

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
			but.setFont(new Font("Tahoma", Font.PLAIN, 40));
			but.setFocusPainted(false);

			keyPad.add(but);

			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = i%4;
			gbc.gridy = (int)(i/4);
			gbc.weightx = 1.0;
			this.add(keyPad.get(i), gbc);
		}


		triggerButton.addActionListener(new TriggerListener());
		triggerButton.setFont(new Font("Tahoma", Font.PLAIN, 40));
		triggerButton.setBorderPainted(false);
		triggerButton.setFocusPainted(false);
		triggerButton.setBackground(keyGreen);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		this.add(triggerButton, gbc);

		modeButton.addActionListener(new ModeListener());
		modeButton.setFont(new Font("Tahoma", Font.PLAIN, 40));
		modeButton.setBorderPainted(false);
		modeButton.setFocusPainted(false);
		modeButton.setBackground(keyGreen);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		this.add(modeButton, gbc);

	}

	class KeyPadListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//
		}		
	}

	class TriggerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//
		}		
	}

	class ModeListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			//
		}		
	}

}
