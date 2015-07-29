package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstrumentPanel extends JPanel {

	private JPanel linesPanel = new JPanel();
	
	private JButton addBottomLineButton = new JButton("Add new line"); 
	private JButton addTopLineButton = new JButton("Add new line"); 
	
	private JPanel instrumentLinesPanel = new JPanel();
	private JScrollPane scrollPanel = new JScrollPane(instrumentLinesPanel);	
	private ArrayList<JPanel> lines = new ArrayList<>();
    GridBagConstraints gbc = new GridBagConstraints();
    
    private JPanel instrumentItemPanel = new JPanel();
    private ArrayList<InstrumentItem> instrumentExamples = new ArrayList<>();
    
	private Color backColor = Color.decode("#EEEEEE");
	
	public InstrumentPanel()
	{
		this.setLayout(new BorderLayout());
		this.setBackground(backColor);
		linesPanel.setLayout(new BorderLayout());
		instrumentLinesPanel.setLayout(new GridBagLayout());

		configureNewLineButton(addBottomLineButton);
		configureNewLineButton(addTopLineButton);
		
		instrumentLinesPanel.setBackground(backColor);
	    scrollPanel.setBorder(BorderFactory.createEmptyBorder());
		
	    linesPanel.add(addTopLineButton, BorderLayout.NORTH);
	    linesPanel.add(scrollPanel, BorderLayout.CENTER);
	    linesPanel.add(addBottomLineButton, BorderLayout.SOUTH);
	    
	    configureInstrumentExamples();
	    
	    this.add(linesPanel, BorderLayout.CENTER);
	    this.add(instrumentItemPanel, BorderLayout.SOUTH);
	}
	
	private void configureNewLineButton(JButton but)
	{
		but.addActionListener(new AddLineListener());
		but.setBackground(backColor);
		but.setBorderPainted(false);
		but.setFont(new Font("Tahoma", Font.PLAIN, 26));
		but.setFocusPainted(false);
	}
	
	private void configureInstrumentExamples()
	{
		instrumentItemPanel.setBackground(Color.gray);
		instrumentItemPanel.setLayout(new GridBagLayout());
		
		instrumentExamples.add(new InstrumentItem("Surdo 1", 42));
		instrumentExamples.add(new InstrumentItem("Surdo 2", 42));
		instrumentExamples.add(new InstrumentItem("Surdo 3", 42));
		instrumentExamples.add(new InstrumentItem("Repique", 28));
		instrumentExamples.add(new InstrumentItem("Caixa", 28));
		instrumentExamples.add(new InstrumentItem("Tarol", 28));
		instrumentExamples.add(new InstrumentItem("Agogo", 10));
		instrumentExamples.add(new InstrumentItem("Tamborim", 13));
		instrumentExamples.add(new InstrumentItem("Chocalho", 7));
		instrumentExamples.add(new InstrumentItem("Cuica", 28));
		instrumentExamples.add(new InstrumentItem("Balloon", 28));

		for(int i = 0; i<instrumentExamples.size(); i++)
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			instrumentItemPanel.add(instrumentExamples.get(i), gbc);
		}
		
		
	}


	public void addLine(int pos)
	{
		InstrumentLine iL = new InstrumentLine(lines.size() + 1);
		if(pos == 1)
			lines.add(iL);
		else
			lines.add(0, iL);
		updateLinesDisplay();
	}
	
	public void updateLinesDisplay()
	{
		for(int i =0; i<lines.size(); i++)
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = lines.size() - 1 - i;
			gbc.weightx = 1.0;
		    gbc.gridwidth = GridBagConstraints.REMAINDER;
		    gbc.insets = new Insets( 0, 0, 20, 0 );
			instrumentLinesPanel.add(lines.get(i), gbc);
		}
		this.revalidate();
	}
	
	
	
	class AddLineListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			JButton but = ((JButton)arg0.getSource());
			if(but == addBottomLineButton)
				addLine(0);		
			else
				addLine(1);
		}		
	}
}






