import Controler.NegativeDictionnaire;
import Controler.PositiveDictionnaire;
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
			model.resetCSVFile();
			MainFrame window = new MainFrame(model);
			window.setVisible(true);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
