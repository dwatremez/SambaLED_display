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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;

public class EditPanel extends JPanel {

	private JPanel optionButtons = new JPanel();
	private JButton openButton = new JButton("Open");
	private JButton saveButton = new JButton("Save");
	private JButton saveAsButton = new JButton("Save as");
	private JButton launchButton = new JButton("Launch");
	GridBagConstraints gbc = new GridBagConstraints();

	private RSyntaxTextArea textArea = new RSyntaxTextArea();
	private RTextScrollPane scrollPanel = new RTextScrollPane(textArea);

	private File openFile;

	private Color backColor = Color.decode("#616161");

	public EditPanel() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.GRAY);

		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
		textArea.setCodeFoldingEnabled(true);
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.setCaretColor(Color.LIGHT_GRAY);
		textArea.setSelectionColor(Color.decode("#6D4C41"));
		textArea.setCurrentLineHighlightColor(textArea.getBackground().brighter());
		textArea.setMarginLineColor(Color.DARK_GRAY);
		textArea.setLineWrap(true);

		SyntaxScheme sc = textArea.getSyntaxScheme();

		sc.getStyle(TokenTypes.LITERAL_NUMBER_DECIMAL_INT).foreground = Color.decode("#AED581");
		sc.getStyle(TokenTypes.RESERVED_WORD).foreground = Color.decode("#FFD54F");
		sc.getStyle(TokenTypes.RESERVED_WORD_2).foreground = Color.decode("#FFD54F");
		sc.getStyle(TokenTypes.OPERATOR).foreground = Color.decode("#F5F5F5");
		sc.getStyle(TokenTypes.SEPARATOR).foreground = Color.decode("#90A4AE");
		sc.getStyle(TokenTypes.LITERAL_NUMBER_HEXADECIMAL).foreground = Color.decode("#9575CD");
		sc.getStyle(TokenTypes.COMMENT_EOL).foreground = Color.decode("#7986CB");

		scrollPanel.getGutter().setBackground(backColor);
		scrollPanel.getGutter().setLineNumberColor(Color.LIGHT_GRAY);

		scrollPanel.setPreferredSize(new Dimension(600, 100));
		scrollPanel.setBorder(null);

		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
		this.add(scrollPanel, BorderLayout.CENTER);

		scrollPanel.getVerticalScrollBar().setUnitIncrement(10);

		optionButtons.setLayout(new GridBagLayout());
		optionButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,Color.white));
		optionButtons.setBackground(backColor);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		configureOptionButton(openButton, 0);
		configureOptionButton(saveButton, 1);
		configureOptionButton(saveAsButton, 2);
		configureOptionButton(launchButton, 3);

		this.add(optionButtons, BorderLayout.SOUTH);

	}

	private void configureOptionButton(JButton but, int pos) 
	{
		but.addActionListener(new OptionButtonListener());
		but.setFont(new Font("Tahoma", Font.PLAIN, 20));
		but.setBorderPainted(false);
		but.setFocusPainted(false);
		but.setForeground(Color.LIGHT_GRAY);
		but.setBackground(backColor);
		but.setPreferredSize(new Dimension(100, (int) (70 * 0.6)));
		but.addMouseListener(new MouseFocusListener(but.getBackground()));
		gbc.gridx = pos;
		optionButtons.add(but, gbc);
	}

	public void openFile() 
	{
		JFileChooser fileChoose = new JFileChooser(new File("."));
		fileChoose.setFileFilter(new FileNameExtensionFilter("Arduino files (.ino)", "ino"));
		File file;

		if (fileChoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			file = fileChoose.getSelectedFile();

			if (file.exists()) {
				openFile = file;
				try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) 
				{
					String line;
					String all = "";
					while ((line = reader.readLine()) != null) 
						all += line + "\n";

					textArea.setText(all);
					textArea.setCaretPosition(0);
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

		fileChoose.setFileFilter(new FileNameExtensionFilter("Arduino files (.ino)", "ino"));

		if (fileChoose.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) 
		{
			File fileToSave = fileChoose.getSelectedFile();
			if (!fileChoose.getSelectedFile().getAbsolutePath()
					.endsWith(".ino"))
				fileToSave = new File(fileToSave.getAbsolutePath() + ".ino");

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
	
	public String getText()
	{
		return this.textArea.getText();
	}

	class OptionButtonListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			JButton but = (JButton) arg0.getSource();
			if (but.getText() == "Open")
				openFile();
			else if (but.getText() == "Save as")
				saveAs();
			else if (but.getText() == "Save") 
			{
				if (openFile != null)
					save(openFile);
				else
					saveAs();
			}
		}
	}

}
