package Model;

import java.util.ArrayList;

public class Bayes_Model extends Model {

	enum classe {POSITIVE,NEGATIVE,INDETERMINED};
	
	
	ArrayList<String> list_POSITIVE;//base d'aprentissage positive
	int nombre_mots_POSITIF;//nombre total de mot dans l'ensemble/* afin d'éviter de devoir le recalculer à chaque fois*/
	int nombre_tweets_POSITIF;//nombre de tweet
	
	
	ArrayList<String> list_NEGATIVE;//base d'apprentissage negative
	int nombre_mots_NEGATIF;//nombre total de mot dans la classe negative/* afin d'éviter de devoir le recalculer à chaque fois*/
	int nombre_tweets_NEGATIF;//nombre de tweet
	
	ArrayList<String> list_INDETERMINE;//base d'apprentissage indetermine
	int nombre_mots_INDETERMINE;//nombre total de mot dans la classe indetermine/* afin d'éviter de devoir le recalculer à chaque fois*/
	int nombre_tweets_INDETERMINE;//nombre de tweet
	
	
	/* constructor */
	public Bayes_Model() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/* Set the 3 arraylist with default csv file*/
	private void init_Array(){
		
		
	}
	
	
	//P(m|C) -> probabilité d'occurence du mot m dans un tweet de la classe c à l'aide de l'ensemble d'apprentissage
	// retourner n(m,c) / n(c)
	// /!\ voir n(m,c)
	// solution : utiliser un estimateur de Laplace
	//retourner (n(m,c)+1) / (n(c) + nombre total des mot de l'ensemble) 
	private float prob_occur_FINAL(String mot, classe c){
		float result;
		
		result = (prob_occur(mot, c)+1) / (nombre_mots(c)+ get_nb_total_mot() );
		
		return result;
	}
	
	private int get_nb_total_mot(){
		int result = 0;
		
		for(classe c : classe.values()){
			result += nombre_mots(c);
		}
		
		return result;
	}
	
	
	//n(m,c) -> probabilité d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut être nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	private float prob_occur(String mot, classe c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = nombre_tweets(c);
		int nb_occur = 0;
		
		for(String str : list){
			if(str.contains(mot))
				nb_occur++;
		}
		
		System.out.println("s'assurer que ca retourne bien un float : " + (float)nb_occur/nb_tweets);
		
		return (float)nb_occur/nb_tweets;
	}
	
	private ArrayList<String> getClasse(classe c){
		switch(c){
		case POSITIVE : 
			return list_POSITIVE;
		case NEGATIVE :
			return list_NEGATIVE;
		case INDETERMINED :
			return list_INDETERMINE;
		default :
			return null;
		}
	}
	//n(c) -> nombre de mot de la classe c 
	//utiliser les attributs "nombre total de mot de la classe c
	private int nombre_mots(classe c){
		switch(c){
		case POSITIVE : 
			return nombre_mots_POSITIF;
		case NEGATIVE :
			return nombre_mots_NEGATIF;
		case INDETERMINED :
			return nombre_mots_INDETERMINE;
		default :
			return 0;
		}
		
	}
	
	private int nombre_tweets(classe c){
		switch(c){
		case POSITIVE : 
			return nombre_tweets_POSITIF;
		case NEGATIVE :
			return nombre_tweets_NEGATIF;
		case INDETERMINED :
			return nombre_tweets_INDETERMINE;
		default :
			return 0;
		}
		
	}
	
	
	//P(positif|t) = (P(m1|positif) * P(m2|positif) * ... ) * P(positif)
	//P(negatif|t) = (P(m1|negatif) * P(m2|negatif) * ... ) * P(negatif)
	//P(indetermine|t) = (P(m1|indetermine) * P(m2|indetermine) * ... ) * P(indetermine)
	//la valeur la plus haute est la bonne et donc le tweet viendra enrichir la classe
	
	
	
	
	
	
}
