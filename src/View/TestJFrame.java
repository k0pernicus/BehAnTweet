package View;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import Model.Model;

public class TestJFrame extends JFrame implements Observer{

	private JPanel contentPane;
	private JTextField textField;
	private Model model;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TestJFrame frame = new TestJFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public TestJFrame(Model model) {
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
