package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainWindow extends JFrame {

	private MyGlassPanel myGlass = new MyGlassPanel();

	private JPanel mainContainer = new JPanel();
	private InstrumentPanel instPanel = new InstrumentPanel(myGlass);
	private SidePanel sidePanel = new SidePanel();

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

		mainContainer.setLayout(new BorderLayout());

		mainContainer.add(instPanel, BorderLayout.CENTER);
		mainContainer.add(sidePanel, BorderLayout.WEST);

		// instPanel.addLine();

		setMinimumSize(new Dimension(1400, 500));

		this.setContentPane(mainContainer);
		this.setGlassPane(myGlass);
		this.setVisible(true);
	}
}
