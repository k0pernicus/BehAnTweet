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

	private boolean isPLUSTroisLettres; 

	private boolean isBigramme;

	private boolean isUnigramme;


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
		this.isPLUSTroisLettres = false;
		this.isBigramme = false;
		this.isUnigramme = false;
		init_Nb_Tweets();
		init_Nb_Mots_Tous();			

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

	protected void init_Nb_Mots_Tous(){

		for (String str : tableau_Positif)
			nombre_mots_POSITIF += (1 + this.stringOccur(str.trim(), " " ));

		for (String str : tableau_Negatif)
			nombre_mots_NEGATIF += (1 + this.stringOccur(str.trim(), " " ));

		for (String str : tableau_Indetermine)
			nombre_mots_INDETERMINE += (1 + this.stringOccur(str.trim(), " " ));

		nombre_de_mot_TOTAL = setNombreMotsTotal();
	}

	protected void init_Nb_Mots_PLUS_Trois_Lettres(){

		for (String str : tableau_Positif)
			for(String mot : str.split(" "))
				nombre_mots_POSITIF += (mot.length()>3)?1:0;

		for (String str : tableau_Negatif)
			for(String mot : str.split(" "))
				nombre_mots_NEGATIF += (mot.length()>3)?1:0;

		for (String str : tableau_Indetermine)
			for(String mot : str.split(" "))
				nombre_mots_INDETERMINE += (mot.length()>3)?1:0;

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


		/*on a la ligne stocker dans le csv*/
		System.out.println("TWEET CLEAN BITCH : " + tweet_clean);
		String[] tab_tweet = tweet_clean.split(";");
		String string_text = tab_tweet[2];

		String strTampon = (isPLUSTroisLettres)?regexReplace(string_text):string_text;

		/*on a le tweet*/
		System.out.println("STRING TEXT BITCH : " + strTampon);
		String[] tab_text = strTampon.split(" ");



		if(isPLUSTroisLettres){
			init_Nb_Mots_PLUS_Trois_Lettres();
		}
		else{
			init_Nb_Mots_Tous();
		}
		System.out.println("GRAMME : " + gramme);
		System.out.flush();
		if(gramme.equals("Unigramme"))
			return AlgoUnigramme(tab_text, isPresence);
		else if(gramme.equals("Bigramme")){
			return AlgoBigramme(tab_text, isPresence);
		}
		else {
			return Algo_Uni_Bigramme(tab_text, isPresence);
		}


	}
	
	classe Algo_Uni_Bigramme(String[] tab, boolean isPresence){
		classe result = classe.POSITIVE;
		/***************************************************************************/
		/* Unigramme */
		
		float p_POSITIVE = 1;
		float p_NEGATIVE = 1;
		float p_INDETERMINE = 1;


		for(String str : tab) {
			float tmp = probOccurenceAdvanced(str , classe.POSITIVE, isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, classe.NEGATIVE, isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;

			tmp = probOccurenceAdvanced(str,classe.INDETERMINE, isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}



		p_POSITIVE    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());
		
		
		
		/***************************************************************************/
		
		/*Bigramme*/
		
		float p_POSITIVE_BI = 1;
		float p_NEGATIVE_BI = 1;
		float p_INDETERMINE_BI = 1;


		
		if(tab.length<2) return AlgoUnigramme(tab, isPresence);
		
		String[] tampon_BI = new String[2];
		tampon_BI[0] = "";
		tampon_BI[1] = "";
		
		int ready_a_deux = 0;
		
		
		for(String strfor : tab) {
			tampon_BI[0] = tampon_BI[1];
			tampon_BI[1] = strfor;
			ready_a_deux++;
			if(ready_a_deux<2) {
				continue;
			}
			ready_a_deux = 0;
			String str = tampon_BI[0].concat(" " + tampon_BI[1]);
			
			float tmp_BI = probOccurenceAdvanced(str , classe.POSITIVE, isPresence);
			p_POSITIVE_BI    *= (tmp_BI == 0)? 1 : tmp_BI ;
			tmp_BI = probOccurenceAdvanced(str, classe.NEGATIVE, isPresence);
			p_NEGATIVE_BI    *= (tmp_BI == 0)? 1 : tmp_BI ;
			tmp_BI = probOccurenceAdvanced(str,classe.INDETERMINE, isPresence);
			p_INDETERMINE_BI *= (tmp_BI == 0)? 1 : tmp_BI ;
		}

		p_POSITIVE_BI    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE_BI    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE_BI *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());
		
		
		
		/*******************************************************/
		
		/*Uni-Bigramme*/
		
		float p_POSITIVE_UNI_BI = p_POSITIVE * p_POSITIVE_BI;
		float p_NEGATIVE_UNI_BI = p_NEGATIVE * p_NEGATIVE_BI;
		float p_INDETERMINE_UNI_BI = p_INDETERMINE * p_INDETERMINE_BI;
		

		if (p_NEGATIVE_UNI_BI > p_POSITIVE_UNI_BI && p_NEGATIVE_UNI_BI > p_INDETERMINE_UNI_BI)
			return classe.NEGATIVE;
		if (p_POSITIVE_UNI_BI > p_INDETERMINE_UNI_BI )
			return classe.POSITIVE;
		else
			return classe.INDETERMINE;
		
	}

	classe AlgoUnigramme(String[] tab, boolean isPresence){

		classe result = classe.POSITIVE;
		float p_POSITIVE = 1;
		float p_NEGATIVE = 1;
		float p_INDETERMINE = 1;


		for(String str : tab) {
			float tmp = probOccurenceAdvanced(str , classe.POSITIVE, isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, classe.NEGATIVE, isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;

			tmp = probOccurenceAdvanced(str,classe.INDETERMINE, isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}

//		System.out.println("Proba Positif av : " + p_POSITIVE);
//		System.out.println("Proba Negatif av : " + p_NEGATIVE);
//		System.out.println("Proba Indeter av  : " + p_INDETERMINE);



		p_POSITIVE    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());

//		System.out.println("Proba Positif : " + p_POSITIVE);
//		System.out.println("Proba Negatif : " + p_NEGATIVE);
//		System.out.println("Proba Indeter : " + p_INDETERMINE);


		if (p_NEGATIVE > p_POSITIVE && p_NEGATIVE > p_INDETERMINE)
			return classe.NEGATIVE;
		if (p_POSITIVE > p_INDETERMINE )
			return classe.POSITIVE;
		else
			return classe.INDETERMINE;
	}

	classe AlgoBigramme(String[] tab, boolean isPresence){

		classe result = classe.POSITIVE;
		float p_POSITIVE = 1;
		float p_NEGATIVE = 1;
		float p_INDETERMINE = 1;


		
		if(tab.length<2) return AlgoUnigramme(tab, isPresence);
		
		String[] tampon = new String[2];
		tampon[0] = "";
		tampon[1] = "";
		
		int ready_a_deux = 0;
		
		
		for(String strfor : tab) {
			tampon[0] = tampon[1];
			tampon[1] = strfor;
			ready_a_deux++;
			if(ready_a_deux<2) {
				continue;
			}
			ready_a_deux = 0;
			String str = tampon[0].concat(" " + tampon[1]);
			
			float tmp = probOccurenceAdvanced(str , classe.POSITIVE, isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, classe.NEGATIVE, isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str,classe.INDETERMINE, isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}

		p_POSITIVE    *= ((float)getNombreTweets(classe.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((float)getNombreTweets(classe.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((float)getNombreTweets(classe.INDETERMINE) / getNombreTweetsTotal());

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


	public void setBooleanNbrLetters(boolean b) {
		this.isPLUSTroisLettres = b;

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

	public final String regexReplace(String text){
		String tampon = text;
		Pattern p = Pattern.compile("\\s\\S\\S\\S\\s");
		Matcher m = p.matcher(tampon);
		StringBuffer sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, " ");
		m.appendTail(sb);

		return sb.toString();
	}


	public static void main(String[] args) {
		String tweet = ";;Mommy c'est trop bien;;;;Positif;";
		Bayes_Model model = new Bayes_Model();

		System.out.println(model.getEvaluationTweetBayes(tweet, true));



	}

	public void setBooleanBigramme(boolean b) {
		this.isBigramme = b;
	}

	public void setBooleanUnigramme(boolean b){
		this.isUnigramme = b;
	}


}
