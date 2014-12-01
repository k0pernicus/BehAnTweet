package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;

import twitter4j.TwitterException;
import Model.Model;
import View.MainPanel;
import View.ButtonPanel;

/**
 * ActionListener du bouton "Search" dans le main
 * @author antonin verkyndt
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
		ButtonPanel sBParent = (ButtonPanel) searchButton.getParent();
		MainPanel sBParentParent = (MainPanel) searchButton.getParent().getParent().getParent();
		sBParent.setSearchButton(false);
		try {
			sBParentParent.getResultPanel().setVisibilityStatPanel(true);
			model.run(sBParentParent.getSearchText(), sBParentParent.getSelectedNbrTweets(), sBParentParent.getSelectedClassname());
		} catch (FileNotFoundException fileNotFound) {
			//Fichier non trouvé
			fileNotFound.printStackTrace();
		} catch (IOException writeError) {
			//Erreur lors de l'écriture des octets dans le fichier initial
			writeError.printStackTrace();
		} catch (TwitterException e1) {
			//Erreur lors de l'envoi de la requête sur les serveurs de Twitter
			e1.printStackTrace();
		}
		sBParent.setSearchButton(true);
		sBParent.setValidateButton(true);
	}

}
