package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Model.Model;

/**
 * Classe NbrTweetsActionListener
 * ActionListener du JComboBox contenant le nombre de tweets de la GUI
 * @implements ActionListener
 * @author antonin
 */
public class NbrTweetsActionListener implements ActionListener {
	
	/**
	 * Attribut contenant le modèle du listener
	 */
	private Model model;
	
	/**
	 * Constructeur de l'objet NbrTweetsActionListener
	 * @param model Le modèle du projet
	 */
	public NbrTweetsActionListener(Model model){
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox rstComboBox = (JComboBox) e.getSource();
	}

}
