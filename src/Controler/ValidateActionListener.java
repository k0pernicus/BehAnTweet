package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;

import Model.Model;
import View.ButtonPanel;
import View.MainPanel;

public class ValidateActionListener implements ActionListener {

	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet Listener
	 * @param model Le modèle du projet
	 */
	public ValidateActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton validateButton = (JButton) e.getSource();	
		ButtonPanel sBParent = (ButtonPanel) validateButton.getParent();
		MainPanel sBParentParentParent = (MainPanel) validateButton.getParent().getParent().getParent();
		sBParent.setSearchButton(false);
		sBParent.setValidateButton(false);
		try {
//			model.cleanCSVFile();
//			model.resetCSVFile();
			model.writeIntoCSVFileBitch(sBParentParentParent.getTweetList());
		} catch (FileNotFoundException fileNotFound) {
			//Fichier non trouvé
			fileNotFound.printStackTrace();
		} catch (IOException writeError) {
			//Erreur lors de l'écriture des octets dans le fichier initial
			writeError.printStackTrace();
		}
		sBParent.setSearchButton(true);
	}
}
