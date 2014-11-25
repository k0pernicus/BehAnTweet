package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Bayes_Model extends Model {

	enum classe {POSITIVE,NEGATIVE,INDETERMINE};


	ArrayList<String> list_POSITIVE;//base d'aprentissage positive
	int nombre_mots_POSITIF;//nombre total de mot dans l'ensemble/* afin d'�viter de devoir le recalculer � chaque fois*/
	int nombre_tweets_POSITIF;//nombre de tweet


	ArrayList<String> list_NEGATIVE;//base d'apprentissage negative
	int nombre_mots_NEGATIF;//nombre total de mot dans la classe negative/* afin d'�viter de devoir le recalculer � chaque fois*/
	int nombre_tweets_NEGATIF;//nombre de tweet

	ArrayList<String> list_INDETERMINE;//base d'apprentissage indetermine
	int nombre_mots_INDETERMINE;//nombre total de mot dans la classe indetermine/* afin d'�viter de devoir le recalculer � chaque fois*/
	int nombre_tweets_INDETERMINE;//nombre de tweet


	/* constructor */
	public Bayes_Model() {
		super();
		// TODO Auto-generated constructor stub
	}


	/* Set the 3 arraylist with default csv file*/
	private void init_Array() throws IOException{
		FileReader fileR = new FileReader(new File(super.FILE_NAME));
		BufferedReader reader = new BufferedReader(fileR);
		String tweet = "";
		
		while((tweet = reader.readLine()) != null){
			
		}
	}


	//P(m|C) -> probabilit� d'occurence du mot m dans un tweet de la classe c � l'aide de l'ensemble d'apprentissage
	// retourner n(m,c) / n(c)
	// /!\ voir n(m,c)
	// solution : utiliser un estimateur de Laplace
	//retourner (n(m,c)+1) / (n(c) + nombre total des mot de l'ensemble) 
	//ne retournera jamais zero
	private float probOccurenceAdvanced(String mot, classe c){
		float result;

		result = (probOccurenceBasique(mot, c)+1) / (getNombreMots(c)+ getNombreMotsTotal() );

		return result;
	}

	private int getNombreMotsTotal(){
		int result = 0;

		for(classe c : classe.values()){
			result += getNombreMots(c);
		}

		return result;
	}


	//n(m,c) -> probabilit� d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut �tre nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	private float probOccurenceBasique(String mot, classe c){
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

	private ArrayList<String> getClasse(classe c){
		switch(c){
		case POSITIVE : 
			return list_POSITIVE;
		case NEGATIVE :
			return list_NEGATIVE;
		case INDETERMINE :
			return list_INDETERMINE;
		default :
			return null;
		}
	}
	//n(c) -> nombre de mot de la classe c 
	//utiliser les attributs "nombre total de mot de la classe c
	private int getNombreMots(classe c){
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

	private int getNombreTweets(classe c){
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
	private classe algoEvalTweetBayes(String tweet_clean){
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
	private int getNombreTweetsTotal(){
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
