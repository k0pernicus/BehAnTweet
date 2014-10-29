package View;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import twitter4j.Status;
import Controler.SearchActionListener;
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
	protected TweetsPanel tweet_pane;
	
	/*
	 * Zone permettant à l'utilisateur d'entrée le mot clef qu'il désir pour sa recherche
	 */
	private SearchPanel searchPanel;
	
	/*
	 * Modele contenant les méthodes permettant d'interagir avec les données via l'interface graphique 
	 */
	private Model model;
	
	//Nombre de tweets a retourner
	private String[] nbrTweets;
	
	/*
	 * Boite deroulante avec les choix predefinient du nombre de tweets a recuperer
	 */
	private JComboBox addNbrTweets;

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
		 * set des quantites par defaut des tweets recuperables
		 */
		nbrTweets = new String[] {"20", "40", "60", "80", "100", "150", "200"};
		
		/*
		 * ajout des differents composants qui composeront notre panel
		 */
		/* #TweetPane */
		tweet_pane = new TweetsPanel(model);
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(tweet_pane);
		add(scrollPane, BorderLayout.CENTER);
		
		/* #searchPanel */
		this.searchPanel = new SearchPanel(this.model);
		add(this.searchPanel, BorderLayout.NORTH);
		
		/* #NbrTweets */
		addNbrTweets = new JComboBox(nbrTweets);
		addNbrTweets.setName("NbrTweetsButton");
		addNbrTweets.setSelectedIndex(0);
		add(addNbrTweets, BorderLayout.SOUTH);
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		/*
		 * rafraichit le panel
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

}
