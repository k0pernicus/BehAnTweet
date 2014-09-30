import java.awt.Color;

import javax.swing.JFrame;

import View.MainWindow;
import View.Panel;

public class Frame_exemple {

	public static void main(String[] args) {

		MainWindow newMW = new MainWindow("BehAnTweet");
		//Fenêtre non-modifiable
		newMW.setResizable(false);
		
		Panel titlePanel = new Panel("BehAnTweet");
		
		newMW.setContentPane(titlePanel);
		
		//TODO Ajouter une JPanel à l'intérieur du MainWindow 
		//Make the window visible
		newMW.setVisible(true);
	}

}
