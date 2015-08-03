package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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
	private ArrayList<InstrumentLine> lines = new ArrayList<>();
	GridBagConstraints gbc = new GridBagConstraints();

	private JPanel instrumentItemPanel = new JPanel();
	private ArrayList<InstrumentItem> instrumentExamples = new ArrayList<>();

	private InstrumentItem instrumentSelected;
	private InstrumentLine lineSelected;

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


		new LineDropTargetListImp(instrumentLinesPanel); // Add to DropList

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
			setInstrumentDnDCopy(instrumentExamples.get(i));
			instrumentItemPanel.add(instrumentExamples.get(i), gbc);
		}		



	}

	private void setInstrumentDnDCopy(InstrumentItem i)
	{			
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(i,
				DnDConstants.ACTION_COPY, new InstrumentDragGestureListImp());		
	}
	
	private void setInstrumentDnDMove(InstrumentItem i)
	{			
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(i,
				DnDConstants.ACTION_MOVE, new InstrumentDragGestureListImp());		
	}

	private void setLineDnDMove(InstrumentLine l)
	{			
		DragSource ds = new DragSource();
		ds.createDefaultDragGestureRecognizer(l,
				DnDConstants.ACTION_MOVE, new LineDragGestureListImp());		
	}

	public void addLine()
	{
		addLine(lines.size());
	}

	public void addLine(Point p, InstrumentLine iLine)
	{
		for(int i = 0; i<lines.size(); i++)
		{
			if(p.getY() < lines.get(i).getY())
			{
				addLine(i, iLine);
				break;
			}				
		}
		if(p.getY() > lines.get(lines.size() -1).getY() && p.getY() < instrumentLinesPanel.getHeight())
			addLine(lines.size(), iLine);
	}
	
	public void addLine(int pos)
	{
		InstrumentLine iL = new InstrumentLine(lines.size() + 1);
		addLine(pos, iL);		
	}

	public void addLine(int pos, InstrumentLine iL)
	{
		new InstrumentDropTargetListImp(iL); // Add to DropList
		setLineDnDMove(iL);
		if(pos < lines.size())
			lines.add(pos, iL);
		else
			lines.add(iL);	
		allInstrumentItemDnDMove(iL);
		updateLinesDisplay();
	}
	
	public void allInstrumentItemDnDMove(InstrumentLine iL)
	{
		for(int i = 0; i<iL.getInstruments().size(); i++)
		{
			setInstrumentDnDMove(iL.getInstruments().get(i));
		}
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

	class InstrumentDragGestureListImp implements DragGestureListener {

		@Override
		public void dragGestureRecognized(DragGestureEvent event) {
			InputEvent ie = event.getTriggerEvent();
			if((ie.getModifiers() & InputEvent.BUTTON1_MASK) == 0)
				return;

			Cursor cursor = null;
			InstrumentItem myItem = (InstrumentItem) event.getComponent();

			if (event.getDragAction() == DnDConstants.ACTION_COPY)
				cursor = DragSource.DefaultCopyDrop;
			else if(event.getDragAction() == DnDConstants.ACTION_MOVE)
			{	
				instrumentSelected = myItem;
				lineSelected = ((InstrumentLine)(myItem.getParent().getParent()));
				cursor = DragSource.DefaultMoveDrop;
			}


			event.startDrag(cursor, new TransferableInstrument(myItem));
		}
	}

	class LineDragGestureListImp implements DragGestureListener {

		@Override
		public void dragGestureRecognized(DragGestureEvent event) {
			InputEvent ie = event.getTriggerEvent();
			if((ie.getModifiers() & InputEvent.BUTTON1_MASK) == 0)
				return;

			Cursor cursor = null;
			InstrumentLine myLine = (InstrumentLine) event.getComponent();

			if (event.getDragAction() == DnDConstants.ACTION_COPY)
				cursor = DragSource.DefaultCopyDrop;
			else if(event.getDragAction() == DnDConstants.ACTION_MOVE)
			{	
				lineSelected = myLine;
				cursor = DragSource.DefaultMoveDrop;
			}


			event.startDrag(cursor, new TransferableLine(myLine));
		}
	}

	class InstrumentDropTargetListImp extends DropTargetAdapter implements
	DropTargetListener {

		private DropTarget dropTarget;
		private JPanel panel;

		public InstrumentDropTargetListImp(JPanel panel) {
			this.panel = panel;

			dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this,
					true, null);
		}

		public void drop(DropTargetDropEvent event) {
			try {

				if (event.isDataFlavorSupported(TransferableInstrument.instrumentFlavor)) {
					Transferable tr = event.getTransferable();
					InstrumentItem myItem = (InstrumentItem) tr.getTransferData(TransferableInstrument.instrumentFlavor);
					
					if(event.getDropAction() == DnDConstants.ACTION_COPY)
						event.acceptDrop(DnDConstants.ACTION_COPY);
					if(event.getDropAction() == DnDConstants.ACTION_MOVE)
						event.acceptDrop(DnDConstants.ACTION_MOVE);
					setInstrumentDnDMove(myItem);
					myItem.setMouseListener();
					((InstrumentLine)this.panel).addInstrument(myItem, event.getLocation());
					if(event.getDropAction() == DnDConstants.ACTION_MOVE)
					{
						lineSelected.getInstruments().remove(instrumentSelected);
						lineSelected.updateDisplay();
					}
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

	class LineDropTargetListImp extends DropTargetAdapter implements
	DropTargetListener {

		private DropTarget dropTarget;
		private JPanel panel;

		public LineDropTargetListImp(JPanel panel) {
			this.panel = panel;

			dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this,
					true, null);
		}

		public void drop(DropTargetDropEvent event) {
			try {

				if (event.isDataFlavorSupported(TransferableLine.lineFlavor)) {
					Transferable tr = event.getTransferable();
					InstrumentLine myLine = (InstrumentLine) tr.getTransferData(TransferableLine.lineFlavor);
					
					if(event.getDropAction() == DnDConstants.ACTION_MOVE)
						event.acceptDrop(DnDConstants.ACTION_MOVE);
					addLine(event.getLocation(), myLine);
					lines.remove(lineSelected);
					updateLinesDisplay();
					/*
					(InstrumentLine)this.panel).addInstrument(myItem, event.getLocation());
					lineSelected.getInstruments().remove(instrumentSelected);
					lineSelected.updateDisplay();*/
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

	static class TransferableLine implements Transferable {
		protected static DataFlavor lineFlavor = new DataFlavor(InstrumentLine.class, InstrumentLine.class.getSimpleName());
		protected static DataFlavor[] supportedFlavors = { lineFlavor };
		InstrumentLine myLine;
		public TransferableLine(InstrumentLine line) {
			this.myLine = line;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return supportedFlavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (flavor.equals(lineFlavor))
				return true;
			return false;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (flavor.equals(lineFlavor))
				return myLine;
			else
				throw new UnsupportedFlavorException(flavor);
		}
	}
}






