package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Model.Model;

/**
 * Classe NbrTweetsActionListener
 * Classe permettant d'instancier le Listener du nombre de tweets de la GUI
 * @author antonin
 */
public class NbrTweetsActionListener implements ActionListener {
	
	/**
	 * Modèle du listener
	 */
	private Model model;
	
	/**
	 * Constructeur de la classe
	 * @param model Modèle du listener
	 */
	public NbrTweetsActionListener(Model model){
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox rstComboBox = (JComboBox) e.getSource();
		//this.model.setNbrTweets(Integer.parseInt((String) rstComboBox.getSelectedItem()));
	}

}
