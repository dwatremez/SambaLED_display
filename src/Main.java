import Engine.EngineBuilder;
import UI.EditPanel;
import UI.MainWindow;

public class Main {
	

	public static void main(String[] args) {

		if(args.length == 0)
		{
			MainWindow mainWindow  = new MainWindow();
		}
		else
		{
			EditPanel editPanel = new EditPanel();		
			editPanel.openFile();

			String str;
			str = editPanel.getText();


			EngineBuilder eb = new EngineBuilder(str);
			eb.editEngine();
			
			
			
		}
	}
}
