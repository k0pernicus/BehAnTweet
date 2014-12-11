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
 * Classe SearchActionListener
 * ActionListener du bouton "Search" de la GUI
 * @author antonin
 * @author verkyndt
 */
public class SearchActionListener implements ActionListener {

	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet SearchActionListener
	 * @param model Le modèle du projet
	 */
	public SearchActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton searchButton = (JButton) e.getSource();
		/*
		 * sBParent contiendra le ButtonPanel
		 */
		ButtonPanel sBParent = (ButtonPanel) searchButton.getParent();
		/*
		 * sBParentParent contiendra le MainPanel
		 */
		MainPanel sBParentParent = (MainPanel) searchButton.getParent().getParent().getParent();
		/*
		 * On essaie d'appeler la méthode 'run', qui permettra d'automatiser toute la procédure de récupération de tweets et d'analyse de comportements.
		 * On recharge le MainPanel
		 */
		try {
			if(sBParentParent.getSearchText().trim().length()<1)
				return;
			sBParent.setSearchButton(false);
			model.run(sBParentParent.getSearchText(), sBParentParent.getSelectedNbrTweets(), sBParentParent.getSelectedClassname(), sBParentParent.getSelectedGramme(),sBParentParent.getSelectedNbrLetters());
			
			sBParentParent.repaint();
			sBParentParent.revalidate();
		}
		/*
		 * Renvoie d'exception
		 */
		catch (FileNotFoundException fileNotFound) {
			System.out.println("Fichier non trouvé");
			System.out.println(fileNotFound.getMessage());
		} catch (IOException writeError) {
			System.out.println("Erreur lors de l'écriture des octets dans le fichier initial");
			System.out.println(writeError.getMessage());
		} catch (TwitterException twitterException) {
			System.out.println("Erreur lors de l'envoi de la requête sur les serveurs de Twitter");
			System.out.println(twitterException.getMessage());
		} catch (NullPointerException npException){
			System.out.println("Erreur lors du chargement du modèle");
			System.out.println(npException.getMessage());
		}
		/*
		 * On autorise l'utilisateur a validé une nouvelle recherche, mais aussi à valider manuellement les résultats des algos de comportement
		 */
		sBParent.setSearchButton(true);
		sBParent.setValidateButton(true);
	}
}
