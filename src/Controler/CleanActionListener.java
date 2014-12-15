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
 * @implements ActionListener
 * @author antonin
 * @author verkyndt
 */
public class CleanActionListener implements ActionListener {

	/**
	 * Attribut contenant le modèle du projet
	 */
	private Model model;

	/**
	 * Constructeur de l'objet SearchActionListener
	 * @param model Le modèle du projet
	 */
	public CleanActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			model.cleanFile();
		} catch (IOException e1) {
			System.err.println("Probleme lors de la generation d'un fichier vierge pour stocker les tweets\n" + e1.getMessage());
		}
		
	}
}
