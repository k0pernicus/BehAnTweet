package View;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Tweet extends JPanel {

	private JLabel label;

	private String nettoyer;
	
	private String nonNettoyer;

	private JComboBox comboBox;

	private String evalAlgo;

	private String methode;
	
	private String nbrLetters;
	
	private String gramme;

	public Tweet(String infoTweet, String infoTweetClean,String tweetText, String evalAlgo, String methode, String nbrLetters, String gramme) {
		super();
		this.evalAlgo = evalAlgo;
		this.nonNettoyer = infoTweet;
		this.nettoyer = infoTweetClean;
		this.methode = methode;
		this.label = new JLabel(tweetText);
		this.nbrLetters = nbrLetters;
		this.gramme = gramme;

		this.comboBox = new JComboBox();
		this.comboBox.addItem("Indetermine");
		this.comboBox.addItem("Positif");
		this.comboBox.addItem("Negatif");
		this.comboBox.setSelectedItem(evalAlgo);


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
		str = nettoyer + ";" + evalAlgo + ";" + this.comboBox.getSelectedItem() +";" +  methode + ";" + nbrLetters + ";" + gramme;

		return str;
	}










}
