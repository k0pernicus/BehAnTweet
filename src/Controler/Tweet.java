package Controler;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Classe Tweet
 * Classe permettant de représenter un tweet
 * @author antonin
 * @author verkyndt
 */
public class Tweet extends JPanel {

	/**
	 * Label ajouté au tweet
	 */
	private JLabel label;
	
	/**
	 * Tweet nettoyé
	 */
	private String nettoye;
	
	/**
	 * Tweet non-nettoyé
	 */
	private String nonNettoye;

	/**
	 * JComboBox contenant les évaluations possibles : Positif, Négatif, Indéterminé
	 */
	private JComboBox<String> comboBox;

	/**
	 * Evaluation donnée par la méthode de classification choisie
	 */
	private String evalAlgo;

	/**
	 * Méthode de classification choisie
	 */
	private String methode;
	
	/**
	 * Nombre de lettres prises en compte - pour la méthode Bayes par exemple
	 */
	private String nbrLetters;
	
	/**
	 * Choix du gramme - pour la méthode Bayes par exemple
	 */
	private String gramme;

	/**
	 * Constructeur de l'objet Tweet
	 * @param infoTweet Tout le contenu renvoyé par l'API, lors de la recherche
	 * @param infoTweetClean Le contenu nettoyé du tweet
	 * @param tweetText Seulement le corps du tweet
	 * @param evalAlgo L'évaluation de la méthode utilisée
	 * @param methode La méthode utilisée pour l'évaluation
	 * @param nbrLetters Le nombre de lettres utilisé
	 * @param gramme Le gramme utilisé
	 */
	public Tweet(String infoTweet, String infoTweetClean,String tweetText, String evalAlgo, String methode, String nbrLetters, String gramme) {
		super();
		
		/*
		 * Initialisation des principaux attributs
		 */
		this.evalAlgo = evalAlgo;
		this.nonNettoye = infoTweet;
		this.nettoye = infoTweetClean;
		this.methode = methode;
		this.label = new JLabel(tweetText);
		this.nbrLetters = nbrLetters;
		this.gramme = gramme;

		/*
		 * Initialisation de l'attribut contenant la JComboBox
		 */
		this.comboBox = new JComboBox<String>();
		this.comboBox.addItem("Indetermine");
		this.comboBox.addItem("Positif");
		this.comboBox.addItem("Negatif");
		this.comboBox.setSelectedItem(evalAlgo);

		/*
		 * Positionnement
		 */
		this.setLayout(new BorderLayout(0, 0));
		this.add(label,BorderLayout.CENTER);
		this.add(comboBox, BorderLayout.WEST);
	}
	
	/**
	 * Méthode permettant de renvoyer le label du tweet
	 * @return Un JLabel contenant le label de l'objet sur lequel on invoque la méthode
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * Méthode permettant de renvoyer la liste des évaluations possibles
	 * @return Une JComboBox contenant toutes les évaluations possibles : Positif, Négatif, Indéterminé
	 */
	public JComboBox<String> getComboBox() {
		return comboBox;
	}
	
	/**
	 * Méthode permettant de renvoyer une chaîne de caractères contenant toutes les informations de l'objet sur lequel on invoque la méthode
	 * @return Toutes les informations du tweet sur lequel on invoque la méthode
	 */
	public String toString(){
		return (nettoye + ";" + evalAlgo + ";" + this.comboBox.getSelectedItem() +";" +  methode + ";" + nbrLetters + ";" + gramme);	
	}
}
