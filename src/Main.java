import java.io.IOException;

import Model.Bayes_Model;
import Model.Dict_Model;
import Model.Model;
import View.MainFrame;

/**
 * Classe main
 * Classe principale du programme BehAnTweet
 * @author verkyndt
 */
public class Main {

	/**
	 * Méthode de chargement principal du programme BehAnTweet
	 * Cette méthode permettra de simuler le programme BehAnTweet
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{
		/*
		 * Le modèle par defaut sera Bayes_Model
		 */
		Model model = new Bayes_Model();
		try{
			/*
			 * Génération du fichier CSV
			 */
			model.generateCSVFile();
			/*
			 * Effacement du fichier - permet de ne pas sauver les élèments gardés lors de sessions antérieures
			 */
			model.resetCSVFile();
			/*
			 * Chargement des fichiers dictionnaire positif et négatif - chargement général pour une utilisation de la classification par dictionnaire
			 */
			((Dict_Model) model).generateDictionnaireFile();
			/*
			 * Chargement/Initialisation de la GUI
			 */
			MainFrame window = new MainFrame(model);
			/*
			 * Apparition de la GUI à l'utilisateur
			 */
			window.setVisible(true);
		}
		/*
		 * Si une exception est renvoyée, on l'affiche dans le terminal
		 */
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
