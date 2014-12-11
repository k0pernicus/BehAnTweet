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
		
		try {
			if(sBParentParent.getSearchText().trim().length()<1)
				return;
			sBParent.setSearchButton(false);
			sBParentParent.getResultPanel().setVisibilityStatPanel(true);
			model.run(sBParentParent.getSearchText(), sBParentParent.getSelectedNbrTweets(), sBParentParent.getSelectedClassname(), sBParentParent.getSelectedGramme(),sBParentParent.getSelectedNbrLetters());
			
			model.countResult(sBParentParent.getResultPanel().getTweetList());
			sBParentParent.repaint();
			sBParentParent.revalidate();
			
		} catch (FileNotFoundException fileNotFound) {
			System.out.println("Fichier non trouvé");
			System.out.println(fileNotFound.getMessage());
		} catch (IOException writeError) {
			System.out.println("Erreur lors de l'écriture des octets dans le fichier initial");
			System.out.println(writeError.getMessage());
		} catch (TwitterException e1) {
			System.out.println("Erreur lors de l'envoi de la requête sur les serveurs de Twitter");
			
			System.out.println(e1.getMessage());
		} catch (NullPointerException e1){
			System.out.println(e1.getMessage());
		}
		sBParent.setSearchButton(true);
		sBParent.setValidateButton(true);
	}

}
