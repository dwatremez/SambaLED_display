package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
			setDnD(instrumentExamples.get(i));
			instrumentItemPanel.add(instrumentExamples.get(i), gbc);
		}		
		


	}
	
	private void setDnD(InstrumentItem i)
	{			
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(i,
				DnDConstants.ACTION_COPY, new DragGestureListImp());		
	}


	public void addLine(int pos)
	{
		InstrumentLine iL = new InstrumentLine(lines.size() + 1);
		new MyDropTargetListImp(iL); // Add to DropList
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
			JButton but = ((JButton)arg0.getSource());
			if(but == addBottomLineButton)
				addLine(0);		
			else
				addLine(1);
		}		
	}
	
	class DragGestureListImp implements DragGestureListener {

		@Override
		public void dragGestureRecognized(DragGestureEvent event) {
			InputEvent ie = event.getTriggerEvent();
			if((ie.getModifiers() & InputEvent.BUTTON1_MASK) == 0)
				return;
				
			Cursor cursor = null;
			InstrumentItem myItem = (InstrumentItem) event.getComponent();

			if (event.getDragAction() == DnDConstants.ACTION_COPY) {
				cursor = DragSource.DefaultCopyDrop;
			}

			event.startDrag(cursor, new TransferableInstrument(myItem));
		}
	}

	class MyDropTargetListImp extends DropTargetAdapter implements
			DropTargetListener {

		private DropTarget dropTarget;
		private JPanel panel;

		public MyDropTargetListImp(JPanel panel) {
			this.panel = panel;

			dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this,
					true, null);
		}

		public void drop(DropTargetDropEvent event) {
			try {
				Transferable tr = event.getTransferable();
				InstrumentItem myItem = (InstrumentItem) tr.getTransferData(TransferableInstrument.instrumentFlavor);

				if (event.isDataFlavorSupported(TransferableInstrument.instrumentFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					DragSource ds = new DragSource();
					ds.createDefaultDragGestureRecognizer(myItem,
							DnDConstants.ACTION_MOVE, new DragGestureListImp());	
					((InstrumentLine)this.panel).addInstrument(myItem, event.getLocation());
					event.dropComplete(true);
					this.panel.validate();
					return;
				}
				event.rejectDrop();
			} catch (Exception e) {
				e.printStackTrace();
				event.rejectDrop();
			}
		}
	}
	
		
	static class TransferableInstrument implements Transferable {
		  protected static DataFlavor instrumentFlavor = new DataFlavor(InstrumentItem.class, InstrumentItem.class.getSimpleName());
		  protected static DataFlavor[] supportedFlavors = { instrumentFlavor };
		  InstrumentItem myItem;
		  public TransferableInstrument(InstrumentItem item) {
		    this.myItem = item;
		  }

		  public DataFlavor[] getTransferDataFlavors() {
		    return supportedFlavors;
		  }

		  public boolean isDataFlavorSupported(DataFlavor flavor) {
		    if (flavor.equals(instrumentFlavor))
		      return true;
		    return false;
		  }

		  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		    if (flavor.equals(instrumentFlavor))
		      return myItem;
		    else
		      throw new UnsupportedFlavorException(flavor);
		  }
		}
}






