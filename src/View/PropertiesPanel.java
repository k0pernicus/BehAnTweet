package View;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import Model.Model;

/**
 * Classe permettant d'implémenter un choix de propriétés
 * @author antonin
 *
 */
public class PropertiesPanel extends JPanel implements Observer {
	
	/*
	 * Nombre de tweets a retourner
	 */
	private String[] nbrTweets;
	
	private String[] methods;
	
	private String[] grammes;

	private String[] nbrLettre;

	
	/*
	 * Boite deroulante avec les choix predefinient du nombre de tweets a recuperer
	 */
	private JComboBox addNbrTweets;
	
	/*
	 * Boite deroulante avec les choix de méthodes
	 */
	private JComboBox addMethods;
	
	/*
	 * Boite deroulante avec les choix d'utiliser différent types de gramme
	 */
	private JComboBox addGrammes;
	
	/*
	 * Boite deroulante avec les choix de méthodes
	 */
	private JComboBox addNbrLettres;
	
	private Model model;
	
	public PropertiesPanel(Model model) {
		
		super();
		this.model = model;
		this.model.addObserver(this);
	
		/*
		 * set des quantites par defaut des tweets recuperables
		 */
		this.nbrTweets = new String[] {"20", "40", "60", "80", "100"};
		
		/*
		 * set des méthodes de classification
		 */
		this.methods = new String[] {"Dictionnaire", "KNN", "Bayes_Presence", "Bayes_Frequence"};
		
		/*
		 * set des types de gramme
		 */
		this.grammes = new String[] {"Unigramme", "Bigramme", "Uni-Bigramme"};
		
		/*
		 * set des critères de sélection d'un mot
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

	public JComboBox getNbrTweets() {
		return this.addNbrTweets;
	}
	
	public JComboBox getClassname() {
		return this.addMethods;
	}
	
	public JComboBox getNbrLettres() {
		return this.addNbrLettres;
	}
	
	public JComboBox getGramme() {
		return this.addGrammes;
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
