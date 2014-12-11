package View;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Model;

/**
 * Classe ResultPanel
 * Panel permettant d'afficher les résultats de la recherche demandée (tweets et classification de ceux-ci) dans l'application
 * @extends JPanel
 * @implements Observer
 * @author antonin
 */
public class ResultPanel extends JPanel implements Observer {

	/**
	 * Panel de tweets - zone où il est permit d'afficher les tweets réceptionnés
	 */
	protected TweetsPanel tweets_panel;
	
	/**
	 * Modèle du projet
	 */
	protected Model model;
	
	/**
	 * Constructeur de l'objet ResultPanel
	 * @param model Le modèle du projet
	 */
	public ResultPanel(Model model) {
		super();
		
		/*
		 * Relation Modèle-objet
		 */
		this.model = model;
		this.model.addObserver((Observer) this);
		
		/*
		 * Affichage au centre de l'application
		 */
		this.tweets_panel = new TweetsPanel(model);
		add(this.tweets_panel, BorderLayout.CENTER);
	}
	
	/**
	 * Méthode permettant de récupérer la liste des tweets
	 * @return Un tableau de tweets
	 */
	public String[] getTweetList() {
		return this.tweets_panel.getTweetList();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
	}
}
