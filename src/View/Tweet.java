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
	
	private int evalAlgo;

	
	
	public Tweet(String infoTweet, String infoTweetClean,String tweetText, int evalAlgo) {
		super();
		this.evalAlgo = evalAlgo;
		this.nonNettoyer = infoTweet;
		this.nettoyer = infoTweetClean;

		this.label = new JLabel(tweetText);

		this.comboBox = new JComboBox();
		this.comboBox.addItem("Indetermine");
		this.comboBox.addItem("Positif");
		this.comboBox.addItem("Negatif");
		this.comboBox.setSelectedIndex(evalAlgo + 1);
		
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

	
	public String toString(){
		String str = "";
		str = nettoyer + ";" + evalAlgo + ";" + this.comboBox.getSelectedItem();
		
		return str;
	}
	
	
	
	
	
	
	
	
	
	
}
