package UI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {
	
	private JPanel mainContainer = new JPanel();
	private InstrumentPanel instPanel = new InstrumentPanel();
	private SidePanel sidePanel = new SidePanel();

	public MainWindow(){
		this.setTitle("SambaLED Display");
		this.setSize(1500, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch (ClassNotFoundException e) {
		      e.printStackTrace();
		    } catch (InstantiationException e) {
		      e.printStackTrace();
		    } catch (IllegalAccessException e) {
		      e.printStackTrace();
		    } catch (UnsupportedLookAndFeelException e) {
		      e.printStackTrace();
		    }
		
		
		mainContainer.setLayout(new BorderLayout());		
		
		mainContainer.add(instPanel, BorderLayout.CENTER);
		mainContainer.add(sidePanel, BorderLayout.EAST);
		
		instPanel.addLine(0);
		instPanel.addLine(0);
		
		this.setContentPane(mainContainer);
		this.setVisible(true); 
	}
}
