package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;

import Model.Model;
import View.ResultPanel;

/**
 * ActionListener du bouton "Search" dans le main
 */
public class SearchActionListener implements ActionListener {

	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet Listener
	 * @param model Le modèle du projet
	 */
	public SearchActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton searchButton = (JButton) e.getSource();	
		ResultPanel sBParent = (ResultPanel) searchButton.getParent();
		try {
			model.run(sBParent.getSearchText());
		} catch (FileNotFoundException fileNotFound) {
			//Fichier non trouvé
			fileNotFound.printStackTrace();
		} catch (IOException writeError) {
			//Erreur lors de l'écriture des octets dans le fichier initial
			writeError.printStackTrace();
		}	
	}

}
