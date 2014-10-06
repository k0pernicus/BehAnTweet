package View;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.Model;

public class MainPanel extends JPanel implements Observer {


	private LogoPanel logo_panel;
	private ResultPanel result_panel;
	private JTextField search_bar;
	private JButton search_button;
	private Model model;

	public MainPanel(Model model) {
		super();
		this.model = model;
		model.addObserver(this);
		InitialisationMainPanelComponent();
	}


	private void InitialisationMainPanelComponent(){
		setLayout(new GridLayout());
		result_panel = new ResultPanel(model);
		add(result_panel);
		
	}

	/**
	 * Method which allows to paint the main window
	 */
	public void paintComponent(Graphics g){
		repaint();
		revalidate();
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
