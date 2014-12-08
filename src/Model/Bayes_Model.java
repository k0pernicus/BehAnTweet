package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import sun.security.action.GetBooleanAction;

public class Bayes_Model extends KNN_Model {

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

	protected int nombre_de_mot_TOTAL; 

	private int nombre_occurence_total;


	/* constructor */
	public Bayes_Model() {
		super();
		init_Bayes();



	}

	protected void init_Bayes(){
		try {
			init_Array();
		} catch (IOException e) {
			// TODO: handle exception
		}

		init_Nb_Tweets();
		init_Nb_Mots();
	}

	/* Set the 3 arraylist with default csv file and the numbre of tweets and words*/
	protected void init_Array() throws IOException{
		super.getByCSVFile(BASE_APPRENTISSAGE);


	}
	protected void init_Nb_Tweets(){
		nombre_tweets_POSITIF = tableau_Positif.size();
		nombre_tweets_NEGATIF = tableau_Negatif.size();
		nombre_tweets_INDETERMINE = tableau_Indetermine.size();
	}

	protected void init_Nb_Mots(){

		for (String str : tableau_Positif)
			nombre_mots_POSITIF += (1 + this.stringOccur(str.trim(), " " ));

		for (String str : tableau_Negatif)
			nombre_mots_NEGATIF += (1 + this.stringOccur(str.trim(), " " ));

		for (String str : tableau_Indetermine)
			nombre_mots_INDETERMINE += (1 + this.stringOccur(str.trim(), " " ));

		nombre_de_mot_TOTAL = setNombreMotsTotal();
	}



	//P(m|C) -> probabilit� d'occurence du mot m dans un tweet de la classe c � l'aide de l'ensemble d'apprentissage
	// retourner n(m,c) / n(c)
	// /!\ voir n(m,c)
	// solution : utiliser un estimateur de Laplace
	//retourner (n(m,c)+1) / (n(c) + nombre total des mot de l'ensemble) 
	//ne retournera jamais zero
	protected float probOccurenceAdvanced(String mot, classe c, boolean isPresence){
		float result, pOBP, nBM;
		pOBP = (isPresence)?
				probOccurenceBasiquePresence(mot, c) : //isPresence
				probOccurenceBasiqueFrequence(mot, c); //!isPresence
		if(pOBP == 0)
			return 0;
		nBM = getNombreMots(c);
		result = (pOBP +1) / ( nBM+ nombre_de_mot_TOTAL );

		System.out.println("probOccurenceBasique : " + pOBP);
		System.out.println("Nb mots              : " + nBM);
		System.out.println("result               : " + result);
		return result;
	}

	protected int setNombreMotsTotal(){
		int result = 0;

		for(classe c : classe.values()){
			result += getNombreMots(c);
		}

		return result;
	}


	//n(m,c) -> probabilit� d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut �tre nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	protected float probOccurenceBasiquePresence(String mot, classe c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = getNombreTweets(c);//MODIF
		int nb_occur = 0;

		for(String str : list){
			if(str.contains(mot))
				nb_occur++;
		}
		System.out.println("OCCURS : " + nb_occur);
		//System.out.println("s'assurer que ca retourne bien un float : " + (float)nb_occur/(float)nb_tweets);

		return (float)nb_occur/(float)nb_tweets;
	}

	//n(m,c) -> probabilit� d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut �tre nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	protected float probOccurenceBasiqueFrequence(String mot, classe c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = getNombreTweets(c);
		int nb_occur = 0;
		int tmp = 0;
		nombre_occurence_total = 0;
		for(String str : list){
			tmp = stringOccur(str,mot);
			if(tmp != 0){
				nombre_occurence_total += tmp;
				nb_occur += 1;
			}
		}

		//System.out.println("s'assurer que ca retourne bien un float : " + (float)nb_occur/(float)nb_tweets);

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
	protected classe algoEvalTweetBayes(String tweet_clean, boolean isPresence){

		System.out.println("TWEET CLEAN BITCH : " + tweet_clean);
		String[] tab_tweet = tweet_clean.split(";");
		String string_text = tab_tweet[2];
		System.out.println("STRING TEXT BITCH : " + string_text);
		String[] tab_text = string_text.split(" ");

		classe result = classe.POSITIVE;
		float p_POSITIVE = 1;
		float p_NEGATIVE = 1;
		float p_INDETERMINE = 1;
		//		p_POSITIVE    = ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		//		p_NEGATIVE    = ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		//		p_INDETERMINE = ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());

		for(String str : tab_text) {
			float tmp = probOccurenceAdvanced(str , classe.POSITIVE, isPresence);
			System.out.println("Proba Positif EN COURS 1   : " + p_POSITIVE);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			System.out.println("Proba Positif EN COURS 2   : " + p_POSITIVE);
			tmp = probOccurenceAdvanced(str, classe.NEGATIVE, isPresence);
			System.out.println("Proba Negatif EN COURS 1  : " + p_NEGATIVE);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;
			System.out.println("Proba Negatif EN COURS 2  : " + p_NEGATIVE);

			tmp = probOccurenceAdvanced(str,classe.INDETERMINE, isPresence);
			System.out.println("Proba Indeter EN COURS 1  : " + p_INDETERMINE);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
			System.out.println("Proba Indeter EN COURS 2  : " + p_INDETERMINE);
		}

		System.out.println("Proba Positif av : " + p_POSITIVE);
		System.out.println("Proba Negatif av : " + p_NEGATIVE);
		System.out.println("Proba Indeter av  : " + p_INDETERMINE);



		p_POSITIVE    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());

		//		System.out.println("nb positif : " + ((float)getNombreTweets(classe.POSITIVE)    /* getNombreTweetsTotal()*/));
		//		System.out.println("nb negatif : " + ((float)getNombreTweets(classe.NEGATIVE)    /* getNombreTweetsTotal()*/));
		//		System.out.println("nb indeter : " + ((float)getNombreTweets(classe.INDETERMINE) /* getNombreTweetsTotal()*/));
		//		
		//		System.out.println("proportion positif apres : " + ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal()));
		//		System.out.println("proportion negatif apres : " + ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal()));
		//		System.out.println("proportion indeter apres : " + ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal()));
		//		
		//		
		System.out.println("Proba Positif : " + p_POSITIVE);
		System.out.println("Proba Negatif : " + p_NEGATIVE);
		System.out.println("Proba Indeter : " + p_INDETERMINE);
		//		
		//		
		//		System.out.println("taille tableau positif" + tableau_Positif.size());
		//		System.out.println("taille tableau negatif" + tableau_Negatif.size());
		//		System.out.println("taille tableau indetermine" + tableau_Indetermine.size());


		if (p_NEGATIVE > p_POSITIVE && p_NEGATIVE > p_INDETERMINE)
			return classe.NEGATIVE;
		if (p_POSITIVE > p_INDETERMINE )
			return classe.POSITIVE;
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

	public String getEvaluationTweetBayes(String tweet_clean, boolean isPresence){
		classe result;

		result = algoEvalTweetBayes(tweet_clean, isPresence);

		if (result == classe.POSITIVE)
			return "Positif";
		if (result == classe.NEGATIVE)
			return "Negatif";
		else
			return "Indetermine";
	}



	/////////////////////////////////////


	/**
	 * Renvoie le nombre d'occurrences de la sous-chaine de caract�res sp�cifi�e dans la chaine de caract�res sp�cifi�e
	 * @param text chaine de caract�res initiale
	 * @param string sous-chaine de caract�res dont le nombre d'occurrences doit etre compt�
	 * @return le nombre d'occurrences du pattern sp�cifi� dans la chaine de caract�res sp�cifi�e
	 */
	public final int stringOccur(String text, String string) {
		return regexOccur(text, Pattern.quote(string));
	}

	/**
	 * Renvoie le nombre d'occurrences du pattern sp�cifi� dans la chaine de caract�res sp�cifi�e
	 * @param text chaine de caract�res initiale
	 * @param regex expression r�guli�re dont le nombre d'occurrences doit etre compt�
	 * @return le nombre d'occurrences du pattern sp�cifi� dans la chaine de caract�res sp�cifi�e
	 */
	public final int regexOccur(String text, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(text);
		int occur = 0;
		while(matcher.find()) {
			occur ++;
		}
		return occur;
	}

	public static void main(String[] args) {
		String tweet = ";;Mommy c'est trop bien;;;;Positif;";
		Bayes_Model model = new Bayes_Model();

		System.out.println(model.getEvaluationTweetBayes(tweet, true));



	}


}
