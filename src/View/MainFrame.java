package View;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Model.Model;

/**
 * Classe MainFrame
 * Vue permettant d'instancier la fenêtre principale du programme
 * @author verkyndt
 *
 */
public class MainFrame extends JFrame implements Observer{

	/**
	 * Panel de contenu
	 */
	private JPanel contentPanel;
	/**
	 * Panel de texte
	 */
	private JTextField textField;
	/**
	 * Modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur permettant de créer la fenêtre
	 * @throws IOException Exception pouvant être déclenchée par la construction de MainPanel
	 */
	public MainFrame(Model model) throws IOException {
		this.model = model;
		/*
		 * On ajoute MainFrame comme un observeur du modèle
		 */
		model.addObserver(this);
		/*
		 * Ajout d'une petite croix permettant de fermer l'application
		 */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		 * La fenêtre principale sera en taille maximale de l'écran
		 */
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		/*
		 * Toute la fenêtre contiendra l'objet MainPanel
		 */
		contentPanel = new MainPanel(model);
		/*
		 * On ajoute contentPanel comme composant de la fenêtre
		 */
		setContentPane(contentPanel);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		/*
		 * Régénère l'objet MainFrame
		 */
		repaint();
		validate();
	}
}
