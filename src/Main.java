import Model.Model;
import View.MainFrame;

/**
 * 
 * @author verkyndt
 *
 */
public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args){
		Model model = new Model();
		try{
			model.generateCSVFile();
			model.resetCSVFile();
			model.generateDictionnaireFile();
			MainFrame window = new MainFrame(model);
			window.setVisible(true);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
