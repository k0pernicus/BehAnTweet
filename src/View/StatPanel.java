package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Model;

public class StatPanel extends JPanel implements Observer{

	private Model model;
	
	public StatPanel(Model model) {
		this.model = model;
		
		PieChart demo = new PieChart("Comparaison", "", );
		add(demo);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
