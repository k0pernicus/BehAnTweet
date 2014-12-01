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
 * Classe permettant d'instancier le panel principal 
 * qui contiendra tout les composants necessaire a l'application 
 * @author verkyndt
 *
 */
public class MainPanel extends JPanel implements Observer{
	
	protected JScrollPane scrollPane;
	/*
	 * Zone de texte contenant les tweets a afficher
	 */
	protected ResultPanel resultPanel;
	
	/*
	 * Zone permettant à l'utilisateur d'entrée le mot clef qu'il désir pour sa recherche
	 */
	private SearchPanel searchPanel;
	
	/*
	 * Zone permettant de choisir plusieurs propriétés quant au projet
	 */
	private PropertiesPanel propertiesPanel;
	
	/*
	 * Modele contenant les méthodes permettant d'interagir avec les données via l'interface graphique 
	 */
	private Model model;

	/**
	 * 
	 * @param model
	 * @throws IOException
	 */
	public MainPanel(Model model) throws IOException {
		super();
		/*
		 * gestion de la forme et de l'agencement du panel
		 */
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		/*
		 * liaison avec un model
		 */
		this.model = model;
		model.addObserver(this);
		
		/*
		 * ajout des differents composants qui composeront notre panel
		 */
		/* #TweetPane */
		resultPanel = new ResultPanel(model);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(resultPanel);
		add(scrollPane, BorderLayout.CENTER);
		
		/* #searchPanel */
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
			case "Bayes":
				return "Bayes_Model";
			default:
				return "Dict_Model";
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		/*
		 * rafraîchit le panel
		 */
		repaint(); 
		revalidate();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		repaint();
		revalidate();

	}

	public String getSearchText() {
		return this.searchPanel.getText();
	}
	
	public String[] getTweetList(){
		return resultPanel.getTweetList();
	}
	
	public ResultPanel getResultPanel() {
		return this.resultPanel;
	}
}
