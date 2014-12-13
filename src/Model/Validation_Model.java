package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 
 * @author verkyndt
 */
public class Validation_Model extends Bayes_Model {

	/*Taux d'erreur*/
	private float taux_ensemble_1;
	private float taux_ensemble_2;
	private float taux_ensemble_3;
	
	
	/*Sous-ensembles*/
	private ArrayList<String> ensemble_1;
	private ArrayList<String> ensemble_2;
	private ArrayList<String> ensemble_3;
	
	
	/*Ensembles*/
	private ArrayList<String> ensemble_Positif;
	private ArrayList<String> ensemble_Negatif;
	private ArrayList<String> ensemble_Indetermine;
	
	/*Constructeur*/
	
	public Validation_Model() throws IOException {
		super();
		ensemble_Positif = new ArrayList<String>();
		ensemble_Negatif = new ArrayList<String>();
		ensemble_Indetermine = new ArrayList<String>();
		ensemble_1 = new ArrayList<String>();
		ensemble_2 = new ArrayList<String>();
		ensemble_3 = new ArrayList<String>();
		
		/*lancement*/
		GestionnaireException();
	}
	
	
	/* Methode Resultat*/
	
	public float getTauxErreur(){
		
		return (float)(taux_ensemble_1 + taux_ensemble_2 + taux_ensemble_3)/(float)3.0;
		
	}
	 
	/* Methode principales*/
	
	public void Echantillonnage() throws IOException {
		/*Recuperation des tweets par resultat*/
		getByCSVFile("./resources/tweets_clean.csv");
		
		/*Pour repartition dans les ensembles de facon equitable*/
		
		/*on recupere le nombre d'occurence*/
		int nb_tweets_positifs = ensemble_Positif.size(),  nb_tweets_negatifs = ensemble_Negatif.size(),    nb_tweets_indetermines = ensemble_Indetermine.size();
		
		/*on melange les listes*/
		Collections.shuffle(ensemble_Positif);    Collections.shuffle(ensemble_Negatif);   Collections.shuffle(ensemble_Indetermine);
		
		/*on verifie que chaque sous ensemble est un multiple de trois, sinon on fait en sorte que*/
		if(nb_tweets_positifs%3 == 1) ensemble_Positif.remove(--nb_tweets_positifs);
		if(nb_tweets_positifs%3 == 2){ ensemble_Positif.remove(--nb_tweets_positifs); ensemble_Positif.remove(--nb_tweets_positifs);}
		
		if(nb_tweets_negatifs%3 == 1) ensemble_Negatif.remove(--nb_tweets_negatifs);
		if(nb_tweets_negatifs%3 == 2){ ensemble_Negatif.remove(--nb_tweets_negatifs); ensemble_Negatif.remove(--nb_tweets_negatifs);}
		
		if(nb_tweets_indetermines%3 == 1) ensemble_Indetermine.remove(--nb_tweets_indetermines);
		if(nb_tweets_indetermines%3 == 2){ ensemble_Indetermine.remove(--nb_tweets_indetermines); ensemble_Indetermine.remove(--nb_tweets_indetermines);}
		
		
		/*on repartie les tweets positifs dans les 3 sous-ensembles*/
		for (int i = 0; i < nb_tweets_positifs; i++) {
			if           (i<nb_tweets_positifs/3) ensemble_1.add(ensemble_Positif.get(i));
			else if(i<((nb_tweets_positifs/3)*2)) ensemble_2.add(ensemble_Positif.get(i));
			else                                  ensemble_3.add(ensemble_Positif.get(i));
		}
		/*on repartie les tweets negatifs dans les 3 sous-ensembles*/
		for (int i = 0; i < nb_tweets_negatifs; i++) {
			if           (i<nb_tweets_negatifs/3) ensemble_1.add(ensemble_Negatif.get(i));
			else if(i<((nb_tweets_negatifs/3)*2)) ensemble_2.add(ensemble_Negatif.get(i));
			else                                  ensemble_3.add(ensemble_Negatif.get(i));
		}
		/*on repartie les tweets indeterminer dans les 3 sous-ensembles*/
		for (int i = 0; i < nb_tweets_indetermines; i++) {
			if           (i<nb_tweets_indetermines/3) ensemble_1.add(ensemble_Indetermine.get(i));
			else if(i<((nb_tweets_indetermines/3)*2)) ensemble_2.add(ensemble_Indetermine.get(i));
			else                                      ensemble_3.add(ensemble_Indetermine.get(i));
		}
		
		
		
	}
	
	/**
	 * GestionnaireException
	 * @version 0.1
	 * Méthode permettant de gérer les exceptions, suite à l'invocation de méthodes principales
	 */
	public void GestionnaireException(){
		try {
			Echantillonnage();
			
		} catch (IOException e) {
			System.err.println("Methode Echantillonnage");
			System.err.println(e.getMessage());
		}
		
		
	}
	
	
	/* Methode secondaires utilisees par les fonctions principales*/
	
	public void getByCSVFile(String path) throws IOException {
		
		
		
		File csvFile = new File(path);
		if (!csvFile.exists())
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas...");
		else{
			BufferedReader buffer = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] words = line.split(";");
				String tweet = words[2];
				String avis = words[5];
				switch (avis) {
				case "Indetermine":
					ensemble_Indetermine.add(tweet);
					break;
				case "Positif":
					ensemble_Positif.add(tweet);
					break;
				case "Negatif":
					ensemble_Negatif.add(tweet);
					break;
				default :
					ensemble_Indetermine.add(tweet);
				}
			}
			buffer.close();
		}
	}

}
