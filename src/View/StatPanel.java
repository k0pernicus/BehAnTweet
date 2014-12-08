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
	
	private PieTest demo;
	
	public StatPanel(Model model) {
		this.model = model;
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		removeAll();
		ArrayList<Double> integer = new ArrayList<Double>();
		integer.add(33.33);
		integer.add(33.33);
		integer.add(33.33);
		
		ArrayList<Color> color = new ArrayList<Color>();
		color.add(Color.RED);//positif
		color.add(Color.BLUE);//negatif
		color.add(Color.WHITE);//neutre
		demo = new PieTest(integer, color);
		
		
	}
	
}
