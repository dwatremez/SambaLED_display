package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EditPanel extends JPanel {

	private JPanel optionButtons = new JPanel();
	private JButton openButton = new JButton("Open");
	private JButton saveButton = new JButton("Save");
	private JButton launchButton = new JButton("Launch");
	GridBagConstraints gbc = new GridBagConstraints();

	private JEditorPane textArea = new JEditorPane();
	private JScrollPane scrollPanel = new JScrollPane(textArea);

	public EditPanel()
	{
		this.setLayout(new BorderLayout());
		this.setBackground(Color.GRAY);

		textArea.setPreferredSize(new Dimension(550,100));
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.LIGHT_GRAY);

		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		this.add(scrollPanel, BorderLayout.CENTER);

		optionButtons.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		configureOptionButton(openButton, 0);
		configureOptionButton(saveButton, 1);
		configureOptionButton(launchButton, 2);

		this.add(optionButtons, BorderLayout.NORTH);

	}

	private void configureOptionButton(JButton but, int pos)
	{
		but.addActionListener(new OptionButtonListener());
		but.setFont(new Font("Tahoma", Font.PLAIN, 20));
		but.setBorderPainted(false);
		but.setFocusPainted(false);
		but.setBackground(Color.GRAY);
		gbc.gridx = pos;
		optionButtons.add(but, gbc);		
	}

	private String filterLine(String line)
	{
		if(line.startsWith("#"))
		{
			if(line.startsWith("define", 1))
				return line + "\n";
			else
				return "";
		}


		return line + "\n";
	}

	public void openFile()
	{
		JFileChooser fileChoose = new JFileChooser(new File("."));
		File file;

		if (fileChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			file = fileChoose.getSelectedFile();

			if(file.exists())
			{
				try ( BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath())))
				{
					textArea.setText("");
					String line;
					String all = "";
					while((line = reader.readLine()) != null)
					{
						all += line + "\n";
					}
					textArea.setText(all);
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

	public void save()
	{
		JFileChooser fileChoose = new JFileChooser(new File("."));

		if (fileChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChoose.getSelectedFile();

			try ( BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath())))
			{					
				writer.write(textArea.getText());
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

	class OptionButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JButton but = (JButton)arg0.getSource();
			if(but.getText() == "Open")
				openFile();		
			else if(but.getText() == "Save")
				save();
		}		
	}

}
