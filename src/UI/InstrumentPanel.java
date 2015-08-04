package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstrumentPanel extends DragFocusJPanel {

	private MyGlassPanel myGlass;

	private JPanel linesPanel = new JPanel();

	private JButton addBottomLineButton = new JButton("Add new line"); 
	private JButton addTopLineButton = new JButton("Add new line"); 

	private JPanel instrumentLinesPanel = new JPanel();
	private JScrollPane scrollPanel = new JScrollPane(instrumentLinesPanel);	
	private ArrayList<InstrumentLine> lines = new ArrayList<>();
	GridBagConstraints gbc = new GridBagConstraints();

	private JPanel instrumentItemPanel = new JPanel();
	private ArrayList<InstrumentItem> instrumentExamples = new ArrayList<>();

	private InstrumentItem instrumentSelected;
	private InstrumentLine lineSelected;

	private static Color backColor = Color.decode("#EEEEEE");

	public InstrumentPanel()
	{
		super(backColor, false);
		init();		
	}

	public InstrumentPanel(MyGlassPanel glass)
	{
		super(backColor, false);
		this.myGlass = glass;
		init();
	}

	public void init()
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

	public void setMyGlass(MyGlassPanel glass)
	{
		this.myGlass = glass;
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

		instrumentItemPanel.setBackground(instrumentExamples.get(0).getBackground());

		for(int i = 0; i<instrumentExamples.size(); i++)
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			instrumentItemPanel.add(instrumentExamples.get(i), gbc);
			instrumentExamples.get(i).setListenersForGlass(myGlass);
		}	
	}

	public void addLine()
	{
		addLine(lines.size());
	}

	public void addLine(InstrumentLine iLine, Point p)
	{
		if(!lines.isEmpty())
		{
			for(int i = 0; i<lines.size(); i++)
			{
				if(p.getY() < lines.get(i).getY())
				{
					System.out.println("lines(" + i + ").getY: " + lines.get(i).getY());
					addLine(iLine, i);
					break;
				}				
			}

			if(p.getY() > lines.get(lines.size() -1).getY() && p.getY() < instrumentLinesPanel.getHeight())
				addLine(iLine, lines.size());
		}
		else
			addLine(iLine,0);
	}

	public void addLine(int pos)
	{
		InstrumentLine iL = new InstrumentLine(lines.size() + 1);
		addLine(iL, pos);		
	}

	public void addLine(InstrumentLine iLine, int pos)
	{
		iLine.setListenersForGlass(myGlass);
		if(pos < lines.size())
			lines.add(pos, iLine);
		else
			lines.add(iLine);	

		updateLinesDisplay();
	}

	public void removeLine(InstrumentLine iLine)
	{
		if(lines.contains(iLine))
			lines.remove(iLine);
	}

	public ArrayList getLines()
	{
		return lines;
	}

	public JPanel getInstrumentLinesPanel()
	{
		return instrumentLinesPanel;
	}

	public void updateLinesDisplay()
	{
		instrumentLinesPanel.removeAll();
		for(int i =0; i<lines.size(); i++)
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.insets = new Insets( 10, 0, 10, 0 );
			instrumentLinesPanel.add(lines.get(i), gbc);
		}
		this.revalidate();
	}



	class AddLineListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton but = ((JButton)arg0.getSource());
			if(but == addTopLineButton)
				addLine(0);		
			else
				addLine();
		}		
	}
}






