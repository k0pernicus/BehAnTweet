import View.MainWindow;
import Model.Model;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		Model model = new Model();
		MainWindow window = new MainWindow(model, "MyWindow");
		model.run("sarkozy :)");
		
	}

}
