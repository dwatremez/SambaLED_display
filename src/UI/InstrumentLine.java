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

	private Color backColor = Color.GRAY;

	private InstrumentItem iSelected;


	public InstrumentLine()
	{	
		initObject();
	}


	public InstrumentLine(int i)
	{
		initObject();
		lineLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lineLabel.setText(String.valueOf(i));
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

		// Set every background the same color
		for (int i = 0; i < this.getComponentCount(); i++) {
			this.getComponent(i).setBackground(backColor);
		}
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
	}

	public void addInstrument(InstrumentItem iI)
	{
		addInstrument(iI, null);
	}

	private void mouseMenu(InstrumentItem iI)
	{
		iI.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger())
				{
					iSelected = iI;
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

	}


	class DeleteInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {			
			instruments.remove(iSelected);
			updateDisplay();	
		}		
	}

	class RenameInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Object o = JOptionPane.showInputDialog(null, "Name", "Player's name", JOptionPane.QUESTION_MESSAGE, null, null, iSelected.getText());
			if(o != null)
				iSelected.setTypeText(o.toString());
			updateDisplay();
		}		
	}	

	class EditInstrumentListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
						
			updateDisplay();
		}		
	}	



}
