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
	
	/*
	 * Boite deroulante avec les choix predefinient du nombre de tweets a recuperer
	 */
	private JComboBox addNbrTweets;
	
	/*
	 * Boite deroulante avec les choix de méthodes
	 */
	private JComboBox addMethods;
	
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
		
	}

	public JComboBox getNbrTweets() {
		return this.addNbrTweets;
	}
	
	public JComboBox getClassname() {
		return this.addMethods;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
