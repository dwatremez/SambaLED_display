package UI;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {

	private MyGlassPanel myGlass = new MyGlassPanel();

	private InstrumentPanel instPanel = new InstrumentPanel(myGlass);
	private SidePanel sidePanel = new SidePanel();	

	private JSplitPane mainContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, instPanel);

	public MainWindow() {
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
		
		mainContainer.setDividerSize(1);
		mainContainer.setContinuousLayout(true);
		
		mainContainer.setResizeWeight(0.5);

		this.setContentPane(mainContainer);
		this.setGlassPane(myGlass);
		this.setVisible(true);
	}
}
