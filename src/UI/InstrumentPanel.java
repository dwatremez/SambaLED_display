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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InstrumentPanel extends JPanel {

	private File openFile;

	private MyGlassPanel myGlass;

	private JPanel instrumentLinesPanel = new JPanel();
	private JScrollPane scrollPanel = new JScrollPane(instrumentLinesPanel);
	private ArrayList<InstrumentLine> lines = new ArrayList<>();
	private GridBagConstraints gbc = new GridBagConstraints();

	private JButton openButton = new JButton("Open");
	private JButton saveButton = new JButton("Save");

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
		scrollPanel.setBorder(null);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

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
		instrumentItemPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

		for (int i = 0; i < instrumentExamples.size(); i++) 
		{
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = i;
			gbc.gridy = 0;
			gbc.gridheight = 2;
			gbc.weightx = 1.0;
			instrumentItemPanel.add(instrumentExamples.get(i), gbc);
			instrumentExamples.get(i).setListenersForGlass(myGlass);
		}

		gbc.gridx++;

		openButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}

		});
		configureOptionButton(openButton, 0);

		saveButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAs();
			}

		});
		configureOptionButton(saveButton, 1);
	}

	private void configureOptionButton(JButton but, int pos) 
	{
		but.setFont(new Font("Tahoma", Font.PLAIN, 20));
		but.setBorderPainted(false);
		but.setFocusPainted(false);
		but.setForeground(Color.WHITE);
		but.setBackground(this.instrumentItemPanel.getBackground());
		but.addMouseListener(new MouseFocusListener(but.getBackground()));

		gbc.gridy = pos;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		instrumentItemPanel.add(but, gbc);
	}

	public void addLine() 
	{
		addLine(lines.size());
	}

	public void addLine(InstrumentLine iLine, Point p) 
	{
		if (!lines.isEmpty()) {
			for (int i = 0; i < lines.size(); i++) 
			{
				if (p.getY() < lines.get(i).getY()) 
				{
					// System.out.println("lines(" + i + ").getY: " +
					// lines.get(i).getY());
					addLine(iLine, i);
					break;
				}
			}
			if (p.getY() > lines.get(lines.size() - 1).getY() && p.getY() < instrumentLinesPanel.getHeight())
				addLine(iLine, lines.size());
		} 
		else if (p.getY() < instrumentLinesPanel.getHeight())
			addLine(iLine, 0);
	}

	public void addLine(int pos) 
	{
		InstrumentLine iL = new InstrumentLine();
		addLine(iL, pos);
	}

	public void addLine(InstrumentLine iLine, int pos) 
	{
		if (pos < lines.size())
			lines.add(pos, iLine);
		else
			lines.add(iLine);

		updateLinesDisplay();
	}

	public void removeLine(InstrumentLine iLine) 
	{
		if (lines.contains(iLine))
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

	public JScrollPane getScrollPanel() 
	{
		return this.scrollPanel;
	}

	public int getScroll() 
	{
		return this.scrollPanel.getVerticalScrollBar().getValue();
	}

	public JPanel getInstrumentLinesPanel() 
	{
		return instrumentLinesPanel;
	}

	public void updateLinesDisplay() 
	{
		instrumentLinesPanel.removeAll();
		for (int i = 0; i < lines.size(); i++) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.insets = new Insets(10, 0, 10, 0);
			lines.get(i).setNumber(lines.size() - i);
			instrumentLinesPanel.add(lines.get(i), gbc);
		}
		this.revalidate();
		this.repaint();
	}

	public void openFile() 
	{
		JFileChooser fileChoose = new JFileChooser(new File("."));
		fileChoose.setFileFilter(new FileNameExtensionFilter("Batucada files (.batuc)", "batuc"));
		File file;

		if (fileChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			file = fileChoose.getSelectedFile();

			if (file.exists()) {
				try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) 
				{
					openFile = file;
					this.lines.clear();
					String line;
					while ((line = reader.readLine()) != null) 
					{
						InstrumentLine instrumentLine = new InstrumentLine();
						int nbInstrument = Integer.parseUnsignedInt(String.valueOf(line.charAt(0)));
						// System.out.println(nbInstrument);
						for (int i = 0; i < nbInstrument; i++) 
						{
							String name = line.substring(line.indexOf('[') + 1,	line.indexOf(','));
							line = line.substring(line.indexOf(',') + 1);
							String type = line.substring(0, line.indexOf(','));
							line = line.substring(line.indexOf(',') + 1);
							String shape = line.substring(0, line.indexOf(','));
							line = line.substring(line.indexOf(',') + 1);
							int pixel = Integer.parseInt(line.substring(0,line.indexOf(']')));
							// System.out.println(name + " " + type + " " +
							// shape + " " + pixel);
							InstrumentItem item = new InstrumentItem(name,type, shape, pixel);
							item.setListenersForGlass(myGlass);
							instrumentLine.addInstrument(item);
						}
						this.addLine(instrumentLine, this.lines.size());
					}
					repaint();
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void saveAs() 
	{
		JFileChooser fileChoose;

		if (this.openFile != null)
			fileChoose = new JFileChooser(openFile);
		else
			fileChoose = new JFileChooser(new File("."));

		fileChoose.setFileFilter(new FileNameExtensionFilter("Batucada files (.batuc)", "batuc"));

		if (fileChoose.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			File fileToSave = fileChoose.getSelectedFile();
			if (!fileChoose.getSelectedFile().getAbsolutePath().endsWith(".batuc"))
				fileToSave = new File(fileToSave.getAbsolutePath() + ".batuc");

			save(fileToSave);
		}
	}

	public void save(File f) 
	{
		if (f.exists()) {
			int choice = JOptionPane.showConfirmDialog(null,"Do you want to replace " + f.getName() + "?", "Save",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (choice != JOptionPane.YES_OPTION)
				return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(
				f.getAbsolutePath()))) 
		{
			for (int i = 0; i < lines.size(); i++) 
			{
				writer.write(String.valueOf(lines.get(i).getInstruments().size()));
				for (int j = 0; j < lines.get(i).getInstruments().size(); j++) 
				{
					writer.write(lines.get(i).getInstruments().get(j).toString());
				}
				writer.write("\n");
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
