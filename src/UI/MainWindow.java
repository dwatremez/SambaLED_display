package UI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
	
	private JPanel mainContainer = new JPanel();
	private InstrumentPanel instPanel = new InstrumentPanel();
	private SidePanel sidePanel = new SidePanel();

	public MainWindow(){
		this.setTitle("SambaLED Display");
		this.setSize(1500, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		mainContainer.setLayout(new BorderLayout());		
		
		mainContainer.add(instPanel, BorderLayout.CENTER);
		mainContainer.add(sidePanel, BorderLayout.EAST);
		
		this.setContentPane(mainContainer);
		this.setVisible(true); 
	}
}
