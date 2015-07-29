package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstrumentPanel extends JPanel {

	private JLabel linesLabel = new JLabel("Lines");
	
	private JButton addLineButton = new JButton("Add new line"); 
	
	private JPanel linesPanel = new JPanel();
	private JScrollPane scrollPanel = new JScrollPane(linesPanel);	
	private ArrayList<JPanel> lines = new ArrayList<>();
    GridBagConstraints gbc = new GridBagConstraints();
    
	private Color backColor = Color.decode("#EEEEEE");
	
	public InstrumentPanel()
	{
		this.setLayout(new BorderLayout());
		this.setBackground(backColor);
		linesPanel.setLayout(new GridBagLayout());

		addLineButton.addActionListener(new AddLineListener());
		addLineButton.setBackground(Color.LIGHT_GRAY);
		addLineButton.setBorderPainted(false);
		Font myFont = new Font("Tahoma", Font.PLAIN, 26);
		addLineButton.setFont(myFont);
		addLineButton.setFocusPainted(false);
		
		linesPanel.setBackground(backColor);
	    scrollPanel.setBorder(BorderFactory.createEmptyBorder());
		
		this.add(linesLabel, BorderLayout.NORTH);
		this.add(scrollPanel, BorderLayout.CENTER);
		this.add(addLineButton, BorderLayout.SOUTH);
	}


	public void addLine()
	{
		InstrumentLine iL = new InstrumentLine(lines.size() + 1);
		lines.add(iL);
		linesLabel.setText(lines.size() + " Lines");
		for(int i =0; i<lines.size(); i++)
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = lines.size() - 1 - i;
			gbc.weightx = 1.0;
		    gbc.gridwidth = GridBagConstraints.REMAINDER;
			linesPanel.add(lines.get(i), gbc);
		}
		this.revalidate();
	}
	
	
	
	class AddLineListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			addLine();		
		}		
	}
}






