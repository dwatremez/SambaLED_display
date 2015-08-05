package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class InstrumentLine extends JPanel{	
	
	private Dimension dim = new Dimension();

	private ArrayList<InstrumentItem> instruments = new ArrayList<>();
	private JPanel instrumentsPanel = new JPanel();
	GridBagConstraints gbc = new GridBagConstraints();

	private JLabel lineLabel = new JLabel("New Line");

	private JPopupMenu instrumentPopMenu  = new JPopupMenu();

	private JMenuItem deleteInstrumentMenuItem = new JMenuItem("Delete instrument");
	private JMenuItem renameInstrumentMenuItem = new JMenuItem("Rename player");
	private JMenuItem editInstrumentMenuItem = new JMenuItem("Edit instrument parameters");

	private static Color backColor = Color.decode("#90A4AE");

	private InstrumentItem instrumentSelected;


	public InstrumentLine()
	{	
		//super(backColor, false);
		initObject();
	}


	public InstrumentLine(int i)
	{
		//super(backColor, false);
		initObject();
		lineLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lineLabel.setText(String.valueOf(i));
	}
	
	public String toString()
	{
		return "Line: " + lineLabel.getText();
	}

	private void initObject()
	{
		this.setBackground(backColor);		
		this.setLayout(new BorderLayout());
		dim.height = 50;
		this.setMinimumSize(dim);
		this.add(lineLabel, BorderLayout.WEST);

		instrumentsPanel.setLayout(new GridBagLayout());
		this.add(instrumentsPanel, BorderLayout.CENTER);		

		deleteInstrumentMenuItem.addActionListener(new DeleteInstrumentListener());
		renameInstrumentMenuItem.addActionListener(new RenameInstrumentListener());
		editInstrumentMenuItem.addActionListener(new EditInstrumentListener());
		

		setEveryComponentColor(backColor);
		
	}
	
	public void setNumber(int i)
	{
		lineLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lineLabel.setText(String.valueOf(i));		
	}
	
	private void setEveryComponentColor(Color c)
	{
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setBackground(c);
		}
	}
	
	
	public void repaint()
	{
		setEveryComponentColor(this.getBackground());
		super.repaint();
	}

	public void addInstrument(InstrumentItem iI, Point p)
	{
		if(p!=null && !instruments.isEmpty())
		{
			int pos = 0;
			while(true)
			{
				if(p.getX() < instrumentsPanel.getSize().getWidth() / (2 * instruments.size()) * (1 + 2*pos))
				{
					instruments.add(pos, iI);
					break;
				}
				else
					pos++;					
			}								
		}
		else
			instruments.add(iI);

		this.mouseMenu(iI);
		this.updateDisplay();
		this.repaint();
	}

	public void addInstrument(InstrumentItem iI)
	{
		addInstrument(iI, null);
	}
	
	public void removeInstrument(InstrumentItem iI)
	{
		this.instruments.remove(iI);
		this.updateDisplay();
	}

	private void mouseMenu(InstrumentItem iI)
	{
		iI.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger())
				{
					instrumentSelected = iI;
					instrumentPopMenu.add(deleteInstrumentMenuItem);
					instrumentPopMenu.add(renameInstrumentMenuItem);
					instrumentPopMenu.add(editInstrumentMenuItem);

					instrumentPopMenu.show(iI, e.getX(), e.getY());
				}
			}
		});

	}
	
	
	public void updateDisplay()
	{	
		instrumentsPanel.removeAll();
		for(int i =0; i<instruments.size(); i++)
		{
			//gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 0.5;
			instrumentsPanel.add(instruments.get(i), gbc);
		}
		this.revalidate();
		this.repaint();
	}
	
	public ArrayList<InstrumentItem> getInstruments()
	{
		return instruments;
	}
	
	public  Color getBackColor()
	{
		return this.backColor;
	}

	public JLabel getLabel()
	{
		return this.lineLabel;
	}

	class DeleteInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {	
			removeInstrument(instrumentSelected);
			repaint();	
		}		
	}

	class RenameInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object o = JOptionPane.showInputDialog(null, "Name", "Musician's name", JOptionPane.QUESTION_MESSAGE, null, null, instrumentSelected.getName());
			if(o != null)
				instrumentSelected.setName(o.toString());
			repaint();
		}		
	}	

	class EditInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
						
			InstrumentDialog iDialog = new InstrumentDialog(null, "Edit instrument parameters", true, instrumentSelected);
			InstrumentItem i = iDialog.showZDialog();
			if(i != null)
			{
				instrumentSelected.setParameters(i.getName(), i.getType(), i.getShape(), i.getPixelNb());
				repaint();
			}
		}		
	}	



}
