package View;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	private String URL_logo = "./images/Bahamut_icon.jpg";
	/*
	 * logo contient le logo du projet
	 */
	private BufferedImage logo;
	/*
	 * searchBar contient la barre de recherche
	 */
	private JTextField searchBar;
	/*
	 * Panel contenant les boutons d'interactions
	 */
	private ButtonPanel buttonPanel;
	/*
	 * Le model du projet
	 */
	private Model model;
	
	/**
	 * Constructeur de la classe SearchPanel
	 * @param model Le modèle du projet
	 * @throws IOException Exception levée lors du chargement de l'image - problème de lecture
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
		BufferedImage myPicture = ImageIO.read(new File(URL_logo));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		add(picLabel, BorderLayout.WEST);
		
		//Construction de la searchBar
		this.searchBar = new JTextField();
		this.searchBar.setColumns(10);
		add(this.searchBar, BorderLayout.CENTER);
		
		//Construction du buttonPanel
		this.buttonPanel = new ButtonPanel(model);
		add(this.buttonPanel, BorderLayout.EAST);
	}

	public String getText() {
		return this.searchBar.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
