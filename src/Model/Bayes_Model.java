package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Bayes_Model extends Model {

	protected final String BASE_APPRENTISSAGE = "src/resources/base_apprentissage.csv";
	
	enum classe {POSITIVE,NEGATIVE,INDETERMINE};

	//c'est list existe déjà dans model
	//ArrayList<String> list_POSITIVE;//base d'aprentissage positive
	protected int nombre_mots_POSITIF;//nombre total de mot dans l'ensemble/* afin d'�viter de devoir le recalculer � chaque fois*/
	protected int nombre_tweets_POSITIF;//nombre de tweet


	//ArrayList<String> list_NEGATIVE;//base d'apprentissage negative
	protected int nombre_mots_NEGATIF;//nombre total de mot dans la classe negative/* afin d'�viter de devoir le recalculer � chaque fois*/
	protected int nombre_tweets_NEGATIF;//nombre de tweet

	//ArrayList<String> list_INDETERMINE;//base d'apprentissage indetermine
	protected int nombre_mots_INDETERMINE;//nombre total de mot dans la classe indetermine/* afin d'�viter de devoir le recalculer � chaque fois*/
	protected int nombre_tweets_INDETERMINE;//nombre de tweet


	/* constructor */
	public Bayes_Model() throws IOException {
		super();
		init_Array();
		
	}


	/* Set the 3 arraylist with default csv file*/
	protected void init_Array() throws IOException{
		super.getByCSVFile(BASE_APPRENTISSAGE);
	
	}


	//P(m|C) -> probabilit� d'occurence du mot m dans un tweet de la classe c � l'aide de l'ensemble d'apprentissage
	// retourner n(m,c) / n(c)
	// /!\ voir n(m,c)
	// solution : utiliser un estimateur de Laplace
	//retourner (n(m,c)+1) / (n(c) + nombre total des mot de l'ensemble) 
	//ne retournera jamais zero
	protected float probOccurenceAdvanced(String mot, classe c){
		float result;

		result = (probOccurenceBasique(mot, c)+1) / (getNombreMots(c)+ getNombreMotsTotal() );

		return result;
	}

	protected int getNombreMotsTotal(){
		int result = 0;

		for(classe c : classe.values()){
			result += getNombreMots(c);
		}

		return result;
	}


	//n(m,c) -> probabilit� d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut �tre nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	protected float probOccurenceBasique(String mot, classe c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = getNombreTweets(c);
		int nb_occur = 0;

		for(String str : list){
			if(str.contains(mot))
				nb_occur++;
		}

		System.out.println("s'assurer que ca retourne bien un float : " + (float)nb_occur/(float)nb_tweets);

		return (float)nb_occur/(float)nb_tweets;
	}

	protected ArrayList<String> getClasse(classe c){
		switch(c){
		case POSITIVE : 
			return tableau_Positif;
		case NEGATIVE :
			return tableau_Negatif;
		case INDETERMINE :
			return tableau_Indetermine;
		default :
			return null;
		}
	}
	//n(c) -> nombre de mot de la classe c 
	//utiliser les attributs "nombre total de mot de la classe c
	protected int getNombreMots(classe c){
		switch(c){
		case POSITIVE : 
			return nombre_mots_POSITIF;
		case NEGATIVE :
			return nombre_mots_NEGATIF;
		case INDETERMINE :
			return nombre_mots_INDETERMINE;
		default :
			return 0;
		}

	}

	protected int getNombreTweets(classe c){
		switch(c){
		case POSITIVE : 
			return nombre_tweets_POSITIF;
		case NEGATIVE :
			return nombre_tweets_NEGATIF;
		case INDETERMINE :
			return nombre_tweets_INDETERMINE;
		default :
			return 0;
		}

	}

	//P(positif|t) = (P(m1|positif) * P(m2|positif) * ... ) * P(positif)
	//P(negatif|t) = (P(m1|negatif) * P(m2|negatif) * ... ) * P(negatif)
	//P(indetermine|t) = (P(m1|indetermine) * P(m2|indetermine) * ... ) * P(indetermine)
	//la valeur la plus eleve l'emporte
	protected classe algoEvalTweetBayes(String tweet_clean){
		String[] tab = tweet_clean.split(" ");
		classe result = classe.POSITIVE;
		float p_POSITIVE = 1;
		float p_NEGATIVE = 1;
		float p_INDETERMINE = 1;


		for(String str : tab) {
			p_POSITIVE    *= probOccurenceAdvanced(str, classe.POSITIVE);
			p_NEGATIVE    *= probOccurenceAdvanced(str, classe.NEGATIVE);
			p_INDETERMINE *= probOccurenceAdvanced(str,classe.INDETERMINE);
		}
		p_POSITIVE    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());
		
		
		if (p_POSITIVE > p_NEGATIVE && p_POSITIVE > p_INDETERMINE)
			return classe.POSITIVE;
		if (p_NEGATIVE > p_INDETERMINE )
			return classe.NEGATIVE;
		else
			return classe.INDETERMINE;


	}
	protected int getNombreTweetsTotal(){
		int result = 0;

		for(classe c : classe.values()){
			result += getNombreTweets(c);
		}

		return result;
	}

	public String getEvaluationTweetBayes(String tweet_clean){
		classe result;

		result = algoEvalTweetBayes(tweet_clean);

		if (result == classe.POSITIVE)
			return "Positif";
		if (result == classe.NEGATIVE)
			return "Negatif";
		else
			return "Indetermine";
	}







}
