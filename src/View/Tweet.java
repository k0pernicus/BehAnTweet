package View;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Tweet extends JPanel {

	private JLabel label;
	
	private String nettoyer;
	private String nonNettoyer;
	
	private JComboBox comboBox;

	
	
	public Tweet(String infoTweet) {
		super();

		this.comboBox = new JComboBox();
		this.comboBox.addItem("Positif");
		this.comboBox.addItem("Negatif");
		this.comboBox.addItem("Indetermine");
		
		this.label = new JLabel(infoTweet);
		
		this.setLayout(new BorderLayout(0, 0));
		this.add(label,BorderLayout.CENTER);
		
		this.add(comboBox, BorderLayout.WEST);
	}



	public JLabel getLabel() {
		return label;
	}



	public JComboBox getComboBox() {
		return comboBox;
	}

	
	
	
	
	
	
	
	
	
	
	
	
}
