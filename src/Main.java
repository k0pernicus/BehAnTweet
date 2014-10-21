import java.io.IOException;

import Model.Model;
import View.TestJFrame;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args){
		Model model = new Model();
		try{
			TestJFrame window = new TestJFrame(model);
			window.setVisible(true);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
