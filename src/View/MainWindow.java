package View;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class which allows to create a basic frame, in the project
 * @author WebTogz
 * @extends JFrame
 */
public class MainWindow extends JFrame {

	public MainWindow(String title) throws HeadlessException {
		super(title);
		setSize(1074, 768);
		setResizable(false);
		//setLocationRelativeTo(null) -> put frame in center
		setLocationRelativeTo(null);
		//Exit program when clicking on the red curse
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Frame will be visible
		setVisible(true);
	}
	
	/**
	 * Method which allows to paint the main window
	 */
	private void paintComponent() {
		
	}

}
