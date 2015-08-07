package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class InstrumentDialog extends JDialog {

	private boolean sendData;
	InstrumentItem myItem;
	boolean loaded = false;


	JPanel optionPanel = new JPanel();
	GridBagConstraints gbc = new GridBagConstraints();
	JLabel nameLabel = new JLabel("Musician: ");
	JLabel typeLabel = new JLabel("Type: ");
	JLabel pixelNbLabel = new JLabel("Number of pixels: ");
	JLabel shapeLabel = new JLabel("Shape: ");
	JComboBox<String> typeBox = new JComboBox<>();
	JTextField nameText = new JTextField();
	JTextField pixelNbText = new JTextField();
	ButtonGroup bg = new ButtonGroup();
	JRadioButton circleShapeRadio = new JRadioButton("Circle");
	JRadioButton barShapeRadio = new JRadioButton("Bar");
	JRadioButton tripleBarShapeRadio = new JRadioButton("Triple Bar");

	JPanel instrumentItemPanel = new JPanel();

	JPanel controlPanel = new JPanel();
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");


	public InstrumentDialog(JFrame parent, String title, boolean modal, InstrumentItem i)
	{
		super(parent, title, modal);
		myItem = new InstrumentItem(i.getName(), i.getType(), i.getShape(), i.getPixelNb());
		init();
	}

	public InstrumentDialog(JFrame parent, String title, boolean modal)
	{		
		super(parent, title, modal);
		init();
	}

	private void init()
	{		
		this.setSize(450, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		this.getRootPane().setDefaultButton(okButton);
				
		optionPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		optionPanel.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;

		// Name
		optionPanel.add(nameLabel, gbc);
		gbc.gridx++;
		optionPanel.add(nameText,gbc);
		nameText.getDocument().addDocumentListener(new DocListener());
		if(myItem != null)
			nameText.setText(myItem.getName());

		// Type
		typeBox.addItem("Surdo 1");
		typeBox.addItem("Surdo 2");
		typeBox.addItem("Surdo 3");
		typeBox.addItem("Caixa");
		typeBox.addItem("Repique");
		typeBox.addItem("Tarol");
		typeBox.addItem("Agogo");
		typeBox.addItem("Tamborim");
		typeBox.addItem("Chocalho");
		typeBox.addItem("Cuica");
		typeBox.addItem("Balloon");
		gbc.gridx = 0;
		gbc.gridy++;
		optionPanel.add(typeLabel, gbc);
		gbc.gridx++;
		optionPanel.add(typeBox, gbc);
		typeBox.addActionListener(new EntryListener());
		if(myItem != null)
			typeBox.setSelectedItem(myItem.getType());

		// Shape
		bg.add(circleShapeRadio);
		bg.add(barShapeRadio);
		bg.add(tripleBarShapeRadio);
		gbc.gridx = 0;
		gbc.gridy++;
		optionPanel.add(shapeLabel, gbc);
		gbc.gridx++;
		optionPanel.add(circleShapeRadio, gbc);
		circleShapeRadio.addActionListener(new EntryListener());
		gbc.gridx++;
		optionPanel.add(barShapeRadio, gbc);
		barShapeRadio.addActionListener(new EntryListener());
		gbc.gridx++;
		optionPanel.add(tripleBarShapeRadio, gbc);
		tripleBarShapeRadio.addActionListener(new EntryListener());
		if(myItem != null)
		{
			switch(myItem.getShape())
			{
			case "Circle":
				circleShapeRadio.setSelected(true);
				break;
			case "Bar":
				barShapeRadio.setSelected(true);
				break;
			case "Triple Bar":
				tripleBarShapeRadio.setSelected(true);
				break;
			default:						
			}
		}

		// Number of pixels
		gbc.gridx = 0;
		gbc.gridy++;
		optionPanel.add(pixelNbLabel, gbc);
		gbc.gridx++;
		optionPanel.add(pixelNbText, gbc);
		//pixelNbText.getDocument().addDocumentListener(new DocListener());
		pixelNbText.addKeyListener(new NumericKeyListener());
		if(myItem != null)
		{
			String str = String.valueOf(myItem.getPixelNb());
			pixelNbText.setText(str);
		}


		this.add(optionPanel, BorderLayout.CENTER);

		// Quick view
		instrumentItemPanel.setBorder(BorderFactory.createTitledBorder("Quick View"));
		instrumentItemPanel.add(myItem);
		instrumentItemPanel.setBackground(Color.gray);
		this.add(instrumentItemPanel, BorderLayout.NORTH);

		// Control Panel
		controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		controlPanel.add(okButton);
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				sendData = true;
				setVisible(false);
			}
		});
		controlPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		});
		this.add(controlPanel, BorderLayout.SOUTH);
		
		loaded = true;
	}

	public void setDefaultFromType(String type)
	{
		switch(type)
		{
		case "Agogo":
			pixelNbText.setText(String.valueOf(10));
			tripleBarShapeRadio.setSelected(true);
			break;
		case "Chocalho":
			pixelNbText.setText(String.valueOf(7));
			barShapeRadio.setSelected(true);
			break;
		case "Surdo 1":
		case "Surdo 2":
		case "Surdo 3":
			pixelNbText.setText(String.valueOf(42));
			circleShapeRadio.setSelected(true);
			break;			
		case "Caixa":
		case "Repique":
		case "Tarol":	
		case "Cuica":
			pixelNbText.setText(String.valueOf(28));
			circleShapeRadio.setSelected(true);
			break;			
		case "Tamborim":
			pixelNbText.setText(String.valueOf(13));
			circleShapeRadio.setSelected(true);
			break;		
		case "Balloon":
			pixelNbText.setText(String.valueOf(40));
			circleShapeRadio.setSelected(true);
			break;			
		default:
		}
	}

	private void updateInstrument()
	{
		if(myItem != null && loaded)
		{
			myItem.setName(nameText.getText());
			if(myItem.getType() != typeBox.getSelectedItem().toString())
				setDefaultFromType(typeBox.getSelectedItem().toString());
			myItem.setType(typeBox.getSelectedItem().toString());
			if(!pixelNbText.getText().isEmpty())
				myItem.setPixelNb(Integer.valueOf(pixelNbText.getText()));
			else
				myItem.setPixelNb(0);
			if(circleShapeRadio.isSelected())
				myItem.setShape(circleShapeRadio.getText());
			if(barShapeRadio.isSelected())
				myItem.setShape(barShapeRadio.getText());
			if(tripleBarShapeRadio.isSelected())
				myItem.setShape(tripleBarShapeRadio.getText());

			myItem.repaint();
		}
			
	}

	public InstrumentItem showZDialog()
	{
		this.sendData = false;
		this.setVisible(true);
		return (this.sendData)? myItem : null;
	}

	class EntryListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			updateInstrument();
		}		
	}	

	class DocListener implements DocumentListener
	{
		@Override
		public void changedUpdate(DocumentEvent arg0) {}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			updateInstrument();			
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			updateInstrument();			
		}		
	}	
	
	class NumericKeyListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent arg0) {}

		@Override
		public void keyReleased(KeyEvent arg0) 
		{
		    if(!isNumeric(arg0.getKeyChar()))
		    	((JTextField)arg0.getSource()).setText(((JTextField)arg0.getSource()).getText().replace(String.valueOf(arg0.getKeyChar()), ""));
		    updateInstrument();
		}

		@Override
		public void keyTyped(KeyEvent arg0) {}
		
	    private boolean isNumeric(char carac){
	        try {
	          Integer.parseInt(String.valueOf(carac));
	        }
	        catch (NumberFormatException e) {
	          return false;            
	        }
	        return true;
	      }
		
	}
}
