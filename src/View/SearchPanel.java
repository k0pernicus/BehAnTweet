package View;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controler.SearchActionListener;
import Model.Model;

/**
 * Classe permettant d'instancier un panel contenant:
 * - un logo
 * - une barre de recherche
 * - un bouton de validation
 * @author Antonin
 */
public class SearchPanel extends JPanel implements Observer {
	
	/*
	 * champ texte contenant l'URL du logo
	 */
	private String URL_logo = "./images/Alpha_Bahamut_LOGO_PROTO2.jpg";
	/*
	 * logo contient le logo du projet
	 */
	private BufferedImage logo;
	/*
	 * searchBar contient la barre de recherche
	 */
	private JTextField searchBar;
	/*
	 * searchButton contient le bouton de validation
	 */
	private JButton searchButton;
	/*
	 * Le model du projet
	 */
	private Model model;
	
	/**
	 * Constructeur de la classe SearchPanel
	 * @param model Le mod�le du projet
	 * @throws IOException Exception lev�e lors du chargement de l'image - probl�me de lecture
	 */
	public SearchPanel(Model model) throws IOException {
		/*
		 * On appelle le constructeur de JPanel 
		 */
		super();
		this.model = model;
		this.model.addObserver((Observer) this);
		
		//Initialisation du BorderLayout
		setLayout(new BorderLayout(0, 0));
		
		//Construction du logo
		this.logo = ImageIO.read(new File(URL_logo));
		
		//Construction de la searchBar
		this.searchBar = new JTextField();
		this.searchBar.setColumns(10);
		add(this.searchBar, BorderLayout.CENTER);
		
		//Construction du searchButton
		this.searchButton = new JButton("Search");
		this.searchButton.setName("searchButton");
		this.searchButton.addActionListener(new SearchActionListener(this.model));
		add(this.searchButton, BorderLayout.EAST);
	}

	public String getText() {
		return this.searchBar.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
