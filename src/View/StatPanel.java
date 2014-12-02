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
	
	private PieChart demo;
	
	public StatPanel(Model model) {
		this.model = model;
		
	}

	@Override
	public void update(Observable o, Object arg) {
	}
	
}
