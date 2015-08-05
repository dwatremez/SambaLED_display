package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class InstrumentPanel extends JPanel {

	private MyGlassPanel myGlass;

	private JPanel instrumentLinesPanel = new JPanel();
	private JScrollPane scrollPanel = new JScrollPane(instrumentLinesPanel);	
	private ArrayList<InstrumentLine> lines = new ArrayList<>();
	GridBagConstraints gbc = new GridBagConstraints();

	private JPanel instrumentItemPanel = new JPanel();
	private ArrayList<InstrumentItem> instrumentExamples = new ArrayList<>();


	private static Color backColor = Color.decode("#EEEEEE");

	public InstrumentPanel()
	{
		init();		
	}

	public InstrumentPanel(MyGlassPanel glass)
	{
		this.myGlass = glass;
		init();
	}

	public void init()
	{
		this.setLayout(new BorderLayout());
		this.setBackground(backColor);
		instrumentLinesPanel.setLayout(new GridBagLayout());


		instrumentLinesPanel.setBackground(backColor);
		scrollPanel.setBorder(BorderFactory.createEmptyBorder());

		configureInstrumentExamples();
		
		this.add(scrollPanel, BorderLayout.CENTER);
		this.add(instrumentItemPanel, BorderLayout.SOUTH);
	}
	
	public void setMyGlass(MyGlassPanel glass)
	{
		this.myGlass = glass;
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
					//System.out.println("lines(" + i + ").getY: " + lines.get(i).getY());
					addLine(iLine, i);
					break;
				}				
			}
			if(p.getY() > lines.get(lines.size() -1).getY() && p.getY() < instrumentLinesPanel.getHeight())
				addLine(iLine, lines.size());
		}
		else if(p.getY() < instrumentLinesPanel.getHeight())
			addLine(iLine,0);
	}

	public void addLine(int pos)
	{
		InstrumentLine iL = new InstrumentLine();
		addLine(iL, pos);		
	}

	public void addLine(InstrumentLine iLine, int pos)
	{
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
	
	public Color getBackColor()
	{
		return this.backColor;
	}

	public ArrayList<InstrumentLine> getLines()
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
			lines.get(i).setNumber(lines.size() - i);
			instrumentLinesPanel.add(lines.get(i), gbc);
		}
		this.revalidate();
	}
}






