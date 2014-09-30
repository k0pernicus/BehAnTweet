import Model.Model;
import View.TestJFrame;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		Model model = new Model();
		TestJFrame window = new TestJFrame(model);
		window.setVisible(true);
		model.run("sarkozy :)");
		
	}

}
