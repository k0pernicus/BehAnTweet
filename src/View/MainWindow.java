package View;

import java.awt.Graphics;
import java.awt.HeadlessException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import Model.Model;

/**
 * Class which allows to create a basic frame, in the project
 * @author WebTogz
 * @extends JFrame
 */
public class MainWindow extends JFrame implements Observer {
	
	private Model model;
	private MainPanel mPanel;
	
	
	public MainWindow(Model model, String title) throws HeadlessException {
		super(title);
		this.model = model;
		model.addObserver(this);
		setSize(1074, 768);
		setResizable(true);
		//setLocationRelativeTo(null) -> put frame in center
		setLocationRelativeTo(null);
		//Exit program when clicking on the red curse
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//JPanel as a contentPane where we will put the graphics component
		mPanel = new MainPanel(model);
		setContentPane(mPanel);
		//Frame will be visible
		setVisible(true);
	}
	
	
	
	/**
	 * Method which allows to paint the main window
	 */
	public void paintComponent(Graphics g){
		
	}



	@Override
	public void update(Observable o, Object arg) {
		repaint();
		validate();
		
	}

}
