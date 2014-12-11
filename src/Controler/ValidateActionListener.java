package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;

import Model.Model;
import View.ButtonPanel;
import View.MainPanel;

/**
 * Classe ValidateActionListener
 * ActionListener du bouton "Validate" de la GUI
 * @author antonin
 */
public class ValidateActionListener implements ActionListener {

	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet ValidateActionListener
	 * @param model Le modèle du projet
	 */
	public ValidateActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton validateButton = (JButton) e.getSource();
		/*
		 * sBParent contiendra le ButtonPanel
		 */
		ButtonPanel sBParent = (ButtonPanel) validateButton.getParent();
		/*
		 * sBParentParent contiendra le MainPanel
		 */
		MainPanel sBParentParentParent = (MainPanel) validateButton.getParent().getParent().getParent();
		/*
		 * On désactive le bouton 'Search' à l'utilisateur
		 */
		sBParent.setSearchButton(false);
		/*
		 * On en fait autant pour le bouton 'Validate'
		 */
		sBParent.setValidateButton(false);
		/*
		 * A l'appui du bouton 'Validate', nous allons sauver toutes les entrées de comportement dans le fichier CSV, afin d'en constituer une base d'apprentissage
		 */
		try {
			model.writeIntoCSVFile(sBParentParentParent.getTweetList());
		}
		/*
		 * Fichier non trouvé
		 */
		catch (FileNotFoundException fileNotFound) {
			System.out.println("Fichier non trouvé");
			fileNotFound.printStackTrace();
		} 
		/*
		 * Erreur lors de l'écriture des octets, dans le fichier (trouvé)
		 */
		catch (IOException writeError) {
			System.out.println("Erreur lors de l'écriture des octets dans le fichier initial");
			writeError.printStackTrace();
		}
		/*
		 * On autorise de nouveau à effectuer une recherche, via la réactivation du bouton 'Search' de la GUI
		 */
		sBParent.setSearchButton(true);
	}
}
