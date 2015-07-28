package UI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	
	private JPanel mainContainer = new JPanel();
	private InstrumentPanel instPanel = new InstrumentPanel();

	public MainWindow(){
		this.setTitle("SambaLED Display");
		this.setSize(1000, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		mainContainer.setLayout(new BorderLayout());		
		
		mainContainer.add(instPanel, BorderLayout.CENTER);
		
		this.setContentPane(mainContainer);
		this.setVisible(true); 
	}
}
