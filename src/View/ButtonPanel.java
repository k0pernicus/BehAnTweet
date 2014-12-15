package View;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controler.CleanActionListener;
import Controler.SearchActionListener;
import Controler.ValidateActionListener;
import Model.Model;

/**
 * Classe ButtonPanel
 * Vue permettant l'instanciation des boutons 'Search' et 'Validate', dans la GUI
 * @extends JPanel
 * @author verkyndt
 */
public class ButtonPanel extends JPanel {

	/**
	 * Attribut instanciant un bouton 'Search'
	 */
	private JButton btnSearch;
	/**
	 * Attribut instanciant un bouton 'Validate'
	 */
	private JButton btnValidate;
	
	/**
	 * Attribut instanciant un bouton 'Clean'
	 */
	private JButton btnClean;
	
	
	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet ButtonPanel
	 * @param model Le modèle du projet
	 */
	public ButtonPanel(Model model) {
		super();
		
		this.model = model;
		
		/*
		 * Attribution des places pour les boutons
		 */
		
		/*
		 * SearchButton - placé avant le bouton Validate
		 * Le bouton 'Search' permettra à l'utilisateur de rechercher et classifier des tweets en relation avec sa recherche
		 */
		setLayout(new BorderLayout(0, 0));
		btnSearch = new JButton("Search");
		btnSearch.setName("SearchButton");
		btnSearch.addActionListener(new SearchActionListener(this.model));
		add(btnSearch, BorderLayout.WEST);
		
		/*
		 * ValidateButton - placé sous le bouton 'Search'
		 * Le bouton 'Validate' permettra à l'utilisateur de sauver le comportement final des tweets recherchés, dans une base d'apprentissage
		 */
		btnValidate = new JButton("Validate");
		btnValidate.setName("ValidateButton");
		btnValidate.addActionListener(new ValidateActionListener(this.model));
		btnValidate.setEnabled(false);
		add(btnValidate, BorderLayout.SOUTH);
		
		btnClean = new JButton("Clean");
		btnClean.setName("CleanButton");
		btnClean.addActionListener(new CleanActionListener(this.model));
		btnClean.setEnabled(true);
		add(btnClean, BorderLayout.EAST);
	}

	/**
	 * Méthode permettant de renvoyer le modèle du projet
	 * @return Le modèle du projet
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Méthode permettant d'activer ou de désactiver le bouton 'Search'
	 * @param bool - True pour activer / False pour désactiver 
	 */
	public void setSearchButton(boolean bool){
		this.btnSearch.setEnabled(bool);
	}
	
	/**
	 * Méthode permettant d'activer ou de désactiver le bouton 'Validate'
	 * @param bool - True pour activer / False pour désactiver 
	 */
	public void setValidateButton(boolean bool){
		this.btnValidate.setEnabled(bool);
	}
}
