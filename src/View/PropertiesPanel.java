package View;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import Model.Model;

/**
 * Classe PropertiesPanel
 * Classe permettant d'implémenter un panel contenant un large choix de propriétés
 * @author antonin
 */
public class PropertiesPanel extends JPanel implements Observer {
	
	/**
	 * Nombre de tweets a retourner
	 */
	private String[] nbrTweets;
	
	/**
	 * Toutes les méthodes de classification
	 */
	private String[] methods;
	
	/**
	 * Toutes les grammes - pour la classification Bayesienne
	 */
	private String[] grammes;

	/**
	 * Tout le choix concernant le nombre de lettres pour les grammes
	 */
	private String[] nbrLettre;

	/**
	 * Boîte déroulante contenant le nombre de tweets à récupérer sur le sujet voulu
	 */
	private JComboBox addNbrTweets;
	
	/**
	 * Boîte déroulante contenant tous les choix de méthodes
	 */
	private JComboBox addMethods;
	
	/**
	 * Boîte déroulante contenant tous les choix de grammes
	 */
	private JComboBox addGrammes;
	
	/**
	 * Boite déroulante contenant tous les choix du nombre de lettres choisies
	 */
	private JComboBox addNbrLettres;
	
	/**
	 * Modèle du projet
	 */
	private Model model;
	
	/**
	 * Constructeur de l'objet PropertiesPanel
	 * @param model Le modèle du projet
	 */
	public PropertiesPanel(Model model) {
		
		super();
		
		/*
		 * Relation modèle - objet
		 */
		this.model = model;
		this.model.addObserver(this);
	
		/*
		 * Quantité par défaut des tweets récupérables
		 * 100 au maximum - limité par la libraire Twitter4J et l'API Twitter
		 */
		this.nbrTweets = new String[] {"20", "40", "60", "80", "100"};
		
		/*
		 * Différentes méthodes de de classification disponibles
		 */
		this.methods = new String[] {"Dictionnaire", "KNN", "Bayes_Presence", "Bayes_Frequence"};
		
		/*
		 * Différents types de grammes disponibles
		 */
		this.grammes = new String[] {"Unigramme", "Bigramme", "Uni-Bigramme"};
		
		/*
		 * Différents critères de sélection d'un mot disponibles
		 */
		this.nbrLettre = new String[] {"toutes les lettres", "+ de 3 lettres"};
		
		/* #NbrTweets */
		this.addNbrTweets = new JComboBox(this.nbrTweets);
		this.addNbrTweets.setName("NbrTweetsButton");
		this.addNbrTweets.setSelectedIndex(0);
		this.add(addNbrTweets, BorderLayout.WEST);
		
		/* #Methods */
		this.addMethods = new JComboBox(this.methods);
		this.addMethods.setName("Methods");
		this.addMethods.setSelectedIndex(0);
		this.add(addMethods, BorderLayout.EAST);
		
		/* #Grammes*/
		this.addGrammes = new JComboBox(this.grammes);
		this.addGrammes.setName("Grammes");
		this.addGrammes.setSelectedIndex(0);
		this.add(addGrammes, BorderLayout.EAST);
		
		/* #NbrLettres*/
		this.addNbrLettres = new JComboBox(this.nbrLettre);
		this.addNbrLettres.setName("nbrLettres");
		this.addNbrLettres.setSelectedIndex(0);
		this.add(addNbrLettres, BorderLayout.EAST);
		
	}

	/**
	 * Méthode permettant de renvoyer une JComboBox contenant toutes les possibilités données quant au nombre de tweets à récupérer
	 * @return JComboBox contenant toutes les possibilitées données quant au nombre de tweets à récupérer
	 */
	public JComboBox getNbrTweets() {
		return this.addNbrTweets;
	}
	
	/**
	 * Méthode permettant de renvoyer une JComboBox contenant toutes les possibilités données quant aux méthodes de classification utilisées
	 * @return JComboBox contenant toutes les possibilitées données quant aux méthodes de classification utilisées
	 */
	public JComboBox getClassname() {
		return this.addMethods;
	}
	
	/**
	 * Méthode permettant de renvoyer une JComboBox contenant toutes les possibilités données quant aux critères de sélection d'un mot disponibles
	 * @return JComboBox contenant toutes les possibilitées données quant aux critères de sélection d'un mot disponibles
	 */
	public JComboBox getNbrLettres() {
		return this.addNbrLettres;
	}
	
	/**
	 * Méthode permettant de renvoyer une JComboBox contenant toutes les possibilités données quant aux grammes disponibles
	 * @return JComboBox contenant toutes les possibilitées données quant aux grammes disponibles
	 */
	public JComboBox getGramme() {
		return this.addGrammes;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub	
	}
}
