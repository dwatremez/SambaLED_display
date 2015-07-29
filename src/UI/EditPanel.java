package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class EditPanel extends JPanel {

	private JTextArea textArea = new JTextArea("This is an editable JTextArea. " +
			"A text area is a \"plain\" text component, " +
			"which means that although it can display text " +
			"in any font, all of the text is in the same font.");

	public EditPanel()
	{
		this.setLayout(new BorderLayout());

		textArea.setPreferredSize(new Dimension(500,100));
		textArea.setBackground(Color.DARK_GRAY);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		this.add(textArea, BorderLayout.CENTER);

	}

}
