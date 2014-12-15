package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import Model.Bayes_Model.Classe_e;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;



public class Validation_Model extends Bayes_Model {


	/************************************************************************************************************************************************************************************************************/

	/* Attributs*/



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




	/************************************************************************************************************************************************************************************************************/

	/*Constructeur*/



	public Validation_Model() {
		super();
		ensemble_Positif = new ArrayList<String>();
		ensemble_Negatif = new ArrayList<String>();
		ensemble_Indetermine = new ArrayList<String>();
		ensemble_1 = new ArrayList<String>();
		ensemble_2 = new ArrayList<String>();
		ensemble_3 = new ArrayList<String>();
		taux_ensemble_1 = 0;
		taux_ensemble_2 = 0;
		taux_ensemble_3 = 0;


	}


	/************************************************************************************************************************************************************************************************************/

	/* Methode Resultat*/



	public float getTauxErreur(){

		return (float)(taux_ensemble_1 + taux_ensemble_2 + taux_ensemble_3)/(float)3.0;

	}


	/************************************************************************************************************************************************************************************************************/

	/*Methodes d'initialisation*/
	/*Adaptation des methodes d'initialisation de la classe Bayes afin de pouvoir ensuite r�utiliser directement les m�thodes principales*/


	public void Echantillonnage() {

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


	public void init_Validation(){


		try {
			this.getByCSVFile(CLEAN_FILE_NAME);

		} catch (IOException e) {	System.err.println("Probleme lors du chargement des tweets contenu dans le fichier \""+ CLEAN_FILE_NAME + "\"\n" +	e.getMessage()); }

		
		Echantillonnage();
		
		clearTableau();
		init_Bayes(1);
		calcul_Erreur(1);

		clearTableau();
		init_Bayes(2);
		calcul_Erreur(2);

		clearTableau();
		init_Bayes(3);
		calcul_Erreur(3);
		
		setChanged();
		updateObservers();
	}




	/**
	 * Méthode permettant d'initialiser le modèle Bayes en fonction de l'ensemble que l'on veut tester
	 * @param numEnsemble : numero de l'ensemble que l'on va tester a l'aide des deux autres. Il existe 3 ensembles : 1 2 et 3
	 */
	protected void init_Bayes(int numEnsemble){

		init_Array(numEnsemble);


		/*
		 * Initialisation des booléens
		 */
		this.isPLUSTroisLettres = false;
		this.isBigramme = false;
		this.isUnigramme = false;
		/*
		 * Initialisation du nombre de tweets
		 */
		init_Nb_Tweets();
		/*
		 * Initialisation du nombre de mots total
		 */
		init_Nb_Mots_Tous();			

	}

	/**
	 * Méthode permettant de charger la base d'apprentissage
	 * @throws IOException Exception levée si le chemin de la base d'apprentissage est incorrect
	 */
	protected void init_Array(int numEnsemble) {
		int size_ensemble_positif = ensemble_Positif.size();
		int size_ensemble_positif_sur_trois = size_ensemble_positif/3;
		int size_ensemble_positif_deux_sur_trois = size_ensemble_positif *2/3;

		/* Pour la base d'apprentissage positive*/
		for (int i = 0; i < size_ensemble_positif_sur_trois; i++) {
			switch (numEnsemble) {
			case 1: /* on stocke 2 et 3*/
				tableau_Positif.add(ensemble_2.get(i));   tableau_Positif.add(ensemble_3.get(i));  
				break;
			case 2: /* on stocke 1 et 3*/
				tableau_Positif.add(ensemble_1.get(i));   tableau_Positif.add(ensemble_3.get(i));
				break;
			case 3: /* on stocke 1 et 2*/
				tableau_Positif.add(ensemble_1.get(i));   tableau_Positif.add(ensemble_2.get(i));
				break;
			default:
				break;
			}
		}

		int size_ensemble_negatif = ensemble_Negatif.size();
		int size_ensemble_negatif_sur_trois = size_ensemble_negatif/3;
		int size_ensemble_negatif_deux_sur_trois = size_ensemble_negatif *2/3;

		//int limite_boucle_negative = size_ensemble_negatif + size_ensemble_positif_sur_trois;

		/* Pour la base d'apprentissage negative*/
		for (int i = 0; i < size_ensemble_negatif_sur_trois; i++) {
			switch (numEnsemble) {
			case 1: /* on stocke 2 et 3*/
				/*on recupere dans la base negatif les tweet provenant des ensemble 2 et 3 si ces tweet se trouve dans le premier tier de l'ensemble negatif*/
				tableau_Negatif.add(ensemble_2.get(i + size_ensemble_positif_sur_trois));   tableau_Negatif.add(ensemble_3.get(i + size_ensemble_positif_sur_trois));
				break;
			case 2: /* on stocke 1 et 3*/
				tableau_Negatif.add(ensemble_1.get(i + size_ensemble_positif_sur_trois));   tableau_Negatif.add(ensemble_3.get(i + size_ensemble_positif_sur_trois));
				break;
			case 3: /* on stocke 1 et 2*/
				tableau_Negatif.add(ensemble_1.get(i));   tableau_Negatif.add(ensemble_2.get(i + size_ensemble_positif_sur_trois));
				break;
			default:
				break;
			}
		}



		int size_ensemble_indetermine = ensemble_Indetermine.size();
		int size_ensemble_indetermine_sur_trois = size_ensemble_indetermine/3;
		int size_ensemble_indetermine_deux_sur_trois = size_ensemble_indetermine *2/3;

		//int limite_boucle_negative = size_ensemble_negatif + size_ensemble_positif_sur_trois;

		/* Pour la base d'apprentissage negative*/
		for (int i = 0; i < size_ensemble_indetermine_sur_trois; i++) {
			switch (numEnsemble) {
			case 1: /* on stocke 2 et 3*/
				/*on recupere dans la base negatif les tweet provenant des ensemble 2 et 3 si c'est tweet se trouve dans le premier tier de l'ensemble negatif*/
				tableau_Indetermine.add(ensemble_2.get(i));   tableau_Indetermine.add(ensemble_3.get(i + size_ensemble_negatif_sur_trois + size_ensemble_positif_sur_trois));
				break;
			case 2: /* on stocke 1 et 3*/
				tableau_Indetermine.add(ensemble_1.get(i));   tableau_Indetermine.add(ensemble_3.get(i + size_ensemble_negatif_sur_trois + size_ensemble_positif_sur_trois));
				break;
			case 3: /* on stocke 1 et 2*/
				tableau_Indetermine.add(ensemble_1.get(i));   tableau_Indetermine.add(ensemble_2.get(i + size_ensemble_negatif_sur_trois + size_ensemble_positif_sur_trois));
				break;
			default:
				break;
			}
		}


	}

	/************************************************************************************************************************************************************************************************************/

	/* Methodes principales*/



	private void calcul_Erreur(int i) {
		Iterator<String> it = ((i == 1)?ensemble_1:(i==2)?ensemble_2:ensemble_3).iterator();

		int size_ensemble_positif_sur_trois = ensemble_Positif.size()/3;
		int size_ensemble_negatif_sur_trois = ensemble_Negatif.size()/3;
		int somme_des_deux = size_ensemble_negatif_sur_trois + size_ensemble_positif_sur_trois;


		String result = "";
		int nbErreur = 0;/*ICI*/
		for(int j = 0; it.hasNext(); j++){
			result = getEvaluationTweetBayes(it.next());
			if (result.equals("Positif"))
				if(j >= size_ensemble_positif_sur_trois)
					nbErreur++;
			if (result.equals("Negatif"))
				if(j<size_ensemble_positif_sur_trois || j>= somme_des_deux)
					nbErreur++;
				else
					if(j<somme_des_deux)
						nbErreur++;

		}
		
		switch (i) {
		case 1:
			taux_ensemble_1 = (float) (nbErreur*100.0/ (float)ensemble_1.size()); 
			break;
		case 2:
			taux_ensemble_2 = (float) (nbErreur*100.0/ (float)ensemble_2.size()); 
			break;
		case 3:
			taux_ensemble_3 = (float) (nbErreur*100.0/ (float)ensemble_3.size()); 
			break;
		default:
			break;
		}

	}




	/************************************************************************************************************************************************************************************************************/

	/* Methode secondaires */



	public void getByCSVFile(String path) throws IOException {
		cleanArray();


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
					ensemble_Indetermine.add(line);
					break;
				case "Positif":
					ensemble_Positif.add(line);
					break;
				case "Negatif":
					ensemble_Negatif.add(line);
					break;
				default :
					ensemble_Indetermine.add(tweet);
				}
			}
			buffer.close();
		}
	}



	private void cleanArray(){
		this.ensemble_1.clear();
		this.ensemble_2.clear();
		this.ensemble_3.clear();
		this.ensemble_Indetermine.clear();
		this.ensemble_Negatif.clear();
		this.ensemble_Positif.clear();
	}


/************************************************************************************************************************************************************************************************************/

	/* Getters/Setters*/
	
	
	public float getNbrTweets(String type){
		switch(type){
		case "positifs" :
			return ensemble_Positif.size();
		case "negatifs" :
			return ensemble_Negatif.size();
		case "indetermines" :
			return ensemble_Indetermine.size();
		default :
			return ensemble_Indetermine.size() + ensemble_Negatif.size() + ensemble_Positif.size();
			
		}
		
	}
	
	
/************************************************************************************************************************************************************************************************************/
	
	
	
	/*FIN*/
	
}
