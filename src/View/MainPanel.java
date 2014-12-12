package View;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Model.Model;

/**
 * Classe MainPanel
 * Classe permettant d'instancier le panel principal, qui contiendra tout les composants necessaires à l'application 
 * @extends JPanel
 * @implements Observer
 * @author antonin
 * @author verkyndt
 */
public class MainPanel extends JPanel implements Observer{
	
	/**
	 * Panel permettant de scroller dans la fenêtre
	 */
	protected JScrollPane scrollPane;
	
	/**
	 * Zone de texte contenant les tweets à afficher
	 */
	protected ResultPanel resultPanel;
	
	/**
	 * Zone permettant à l'utilisateur d'entrée le mot-clef qu'il désire, pour sa recherche
	 */
	private SearchPanel searchPanel;
	
	/**
	 * Zone permettant de choisir plusieurs propriétés quant au projet
	 */
	private PropertiesPanel propertiesPanel;
	
	/**
	 * Modèle contenant les méthodes permettant d'intéragir avec les données, via la GUI 
	 */
	private Model model;

	/**
	 * Constructeur de l'objet MainPanel
	 * @param model Le modèle du projet
	 * @throws IOException Exception levée par l'instanciation de l'objet SearchPanel
	 */
	public MainPanel(Model model) throws IOException {
		super();
		/*
		 * Gestion de la forme et de l'agencement du panel
		 */
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		/*
		 * Liaison avec le modèle
		 */
		this.model = model;
		model.addObserver(this);
		
		/*
		 * Ajout des diffèrents composants qui composeront notre panel
		 */
		resultPanel = new ResultPanel(model);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(resultPanel);
		add(scrollPane, BorderLayout.CENTER);
		
		this.searchPanel = new SearchPanel(this.model);
		this.add(this.searchPanel, BorderLayout.NORTH);
		
		this.propertiesPanel = new PropertiesPanel(this.model);
		this.add(this.propertiesPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Méthode permettant de retourner le nombre de tweets voulus, par page
	 * @return Le nombre de tweets par page
	 */
	public int getSelectedNbrTweets() {
		int nbrTweets = Integer.parseInt((String) this.propertiesPanel.getNbrTweets().getSelectedItem()); 
		return nbrTweets;
	}
	
	/**
	 * Méthode permettant de retourner le nom de la classe de classification voulue
	 * @return La classe de classification voulue
	 */
	public String getSelectedClassname() {
		switch((String) this.propertiesPanel.getClassname().getSelectedItem()) {
			case "KNN":
				return "KNN_Model";
			case "Bayes_Presence":
				return "Bayes_Model_Presence";
			case "Bayes_Frequence":
				return "Bayes_Model_Frequence";
			default:
				return "Dict_Model";
		}
	}
	
	/**
	 * Méthode permettant de retourner le type de gramme voulu
	 * @return Le type de gramme voulu
	 */
	public String getSelectedGramme() {
		switch((String) this.propertiesPanel.getGramme().getSelectedItem()) {
		case "Unigramme":
			return "Unigramme";
		case "Bigramme":
			return "Bigramme";
		case "Uni-Bigramme":
			return "Uni-Bigramme";
		default:
			return "Unigramme";
		}
	}
	
	
	/**
	 * Méthode permettant de retourner la taille des mots voulus
	 * @return La taille minimal des mots voulus
	 */
	public String getSelectedNbrLetters() {
		switch((String) this.propertiesPanel.getNbrLettres().getSelectedItem()) {
			case "+ de 3 lettres":
				return "plus_trois_lettres";
			default:
				return "toutes_les_lettres";
		}
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		/*
		 * rafraîchissement du panel
		 */
		repaint(); 
		revalidate();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		repaint();
		revalidate();

	}

	/**
	 * Méthode permettant de renvoyer le contenu du champ principal de recherche
	 * @return Le contenu du champ principal de recherche
	 */
	public String getSearchText() {
		return this.searchPanel.getText();
	}
	
	/**
	 * Méthode permettant de renvoyer tous les tweets contenus dans la zone de résultats
	 * @return Les tweets résultants
	 */
	public String[] getTweetList(){
		return resultPanel.getTweetList();
	}
	
	/**
	 * Méthode permettant de renvoyer la vue ResultPanel
	 * @return La vue ResultPanel
	 */
	public ResultPanel getResultPanel() {
		return this.resultPanel;
	}
}
