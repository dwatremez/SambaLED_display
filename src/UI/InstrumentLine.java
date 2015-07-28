package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InstrumentLine extends JPanel{	
	
	private Dimension dim = new Dimension();

	private ArrayList<InstrumentItem> instruments = new ArrayList<>();
	private JPanel instrumentsPanel = new JPanel();
    GridBagConstraints gbc = new GridBagConstraints();
	
	private JButton addButton = new JButton("+"); 	
	private JButton testButton = new JButton("test"); 

	private JLabel lineLabel = new JLabel("New Line");
    
	private Color backColor = Color.GRAY;
	

	public InstrumentLine()
	{	
		initObject();
	}
	
	
	public InstrumentLine(int i)
	{
		initObject();
		lineLabel.setText("Line " + i);
	}
	
	private void initObject()
	{
		this.setBackground(backColor);		
		this.setLayout(new BorderLayout());
		dim.height = 50;
		this.setMinimumSize(dim);
		this.add(lineLabel, BorderLayout.WEST);
		
		this.add(addButton, BorderLayout.EAST);
		addButton.addActionListener(new AddListener());
		addButton.setBackground(backColor);
		addButton.setBorderPainted(false);
		Font myFont = new Font("Tahoma", Font.PLAIN, 26);
		addButton.setFont(myFont);
		addButton.setFocusPainted(false);
		
		instrumentsPanel.setLayout(new GridBagLayout());
		this.add(instrumentsPanel, BorderLayout.CENTER);
		
		// Set every background the same color
		for (int i = 0; i < this.getComponentCount(); i++) {
            this.getComponent(i).setBackground(backColor);
        }
	}
	
	public void addInstrument(InstrumentItem iI)
	{
		instruments.add(iI);
		for(int i =0; i<instruments.size(); i++)
		{
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 0.5;
			instrumentsPanel.add(instruments.get(i), gbc);
		}
		instrumentsPanel.add(testButton);
		testButton.addActionListener(new ButListener());
		this.revalidate();
	}

	  
	class AddListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			InstrumentItem iI = new InstrumentItem();
			addInstrument(iI);
		}		
	}
	
	class ButListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			instruments.get(0).setParameters("Chocalho");
		}		
	}

}
