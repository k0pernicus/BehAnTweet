package View;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.Model;

public class TestJFrame extends JFrame implements Observer{

	private JPanel contentPane;
	private JTextField textField;
	private Model model;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public TestJFrame(Model model) throws IOException {
		this.model = model;
		model.addObserver(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new ResultPanel(model);
		setContentPane(contentPane);

	}
	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
		validate();
		
	}

}
