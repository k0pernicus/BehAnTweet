package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe Bayes_Model
 * Modèle contenant tous les attributs et méthodes nécessaires à la détermination des comportements via Bayes (Présence + Fréquence)
 * @author verkyndt
 */
public class Bayes_Model extends KNN_Model {
	
	
/************************************************************************************************************************************************************************************************************/

	/* Attributs*/
	
	/**
	 * Enumération interne
	 * Ces énumérations contiennent les comportements principaux du programme : Positif, Négatif et Indéterminé
	 */
	enum Classe_e {POSITIVE,NEGATIVE,INDETERMINE};

	/**
	 * Nombre total de mots dans l'ensemble positif
	 */
	protected int nombre_mots_POSITIF;
	
	/**
	 * Nombre total de tweets positifs
	 */
	protected int nombre_tweets_POSITIF;

	/**
	 * Nombre total de mots dans l'ensemble négatif
	 */
	protected int nombre_mots_NEGATIF;
	
	/**
	 * Nombre total de tweets négatifs
	 */
	protected int nombre_tweets_NEGATIF;

	/**
	 * Nombre total de mots dans l'ensemble indéterminé
	 */
	protected int nombre_mots_INDETERMINE;
	
	/**
	 * Nombre total de tweets indéterminés
	 */
	protected int nombre_tweets_INDETERMINE;

	/**
	 * Nombre total de mots - dans l'ensemble Positif + Négatif + Indéterminé
	 */
	protected int nombre_de_mot_TOTAL; 

	/**
	 * Nombre d'occurence total 
	 */
	protected int nombre_occurence_total;

	/**
	 * Booléen permettant de sauver si nous avons une requête sur plus de 3 mots
	 */
	protected boolean isPLUSTroisLettres; 

	/**
	 * Booléen permettant de savoir si la requête se fait sur des bigrammes
	 */
	protected boolean isBigramme;

	/**
	 * Booléen permettant de savoir si la requête se fait sur des unigrammes
	 */
	protected boolean isUnigramme;

	protected boolean isPresence;


	
	
/************************************************************************************************************************************************************************************************************/
	
	
	

	/* CONSTRUCTEUR*/
	
	/**
	 * Constructeur du modèle Bayes
	 * @throws IOException 
	 * @constructor
	 */
	public Bayes_Model() throws IOException {
		/*
		 * Chargement de la base d'apprentissage
		 */
		super();
		/*
		 * Initialisation du modèle via la méthode init_Bayes
		 */
		init_Bayes();
	}
	
	/**
	 * Méthode permettant d'imprimer les statistiques sur la sortie standard
	 */
	public void printInfoBaye(){
		System.out.println("nombre_mots_POSITIF " + nombre_mots_POSITIF);
		System.out.println("nombre_mots_NEGATIF " + nombre_mots_NEGATIF);
		System.out.println("nombre_mots_INDETERMINE " + nombre_mots_INDETERMINE);

		System.out.println("nombre_tweets_POSITIF " + nombre_tweets_POSITIF);
		System.out.println("nombre_tweets_NEGATIF " + nombre_tweets_NEGATIF);
		System.out.println("nombre_mots_INDETERMINE " + nombre_mots_INDETERMINE);
		
		System.out.println("nombre_de_mot_TOTAL " + nombre_de_mot_TOTAL);
	}
	
/************************************************************************************************************************************************************************************************************/	
	
	/* Methodes d'Initialisations*/
	
	/**
	 * Méthode permettant d'initialiser le modèle Bayes
	 */
	protected void init_Bayes(){
		/*
		 * Chargement de la base d'apprentissage
		 */
		try {
			super.init_Base_Apprentissage();
		} 
		/*
		 * Exception remontée si la base d'apprentissage n'a pas été trouvé
		 */
		catch (IOException e) {
			System.out.println("Fichier non trouvé");
			e.printStackTrace();
		}
		/*
		 * Initialisation des booléens
		 */
		this.isPLUSTroisLettres = false;
		this.isBigramme = false;
		this.isUnigramme = false;
		this.isPresence = false;
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
	protected void init_Array() throws IOException{	
		/*
		 * Chargement de la base d'apprentissage
		 * Ajout des tweets de la base dans chaque ensemble (Positif, Négatif, Indéterminé)
		 */
		super.getByCSVFile1(BASE_APPRENTISSAGE);
	}
	
	/**
	 * Méthode permettant d'initialiser le nombre de tweets de chaque catégorie
	 */
	protected void init_Nb_Tweets(){
		nombre_mots_POSITIF = 0;
		nombre_mots_NEGATIF = 0;
		nombre_mots_INDETERMINE = 0;
		/*
		 * Initialisation du nombre de tweets positifs dans la base d'apprentissage
		 */
		nombre_tweets_POSITIF = tableau_Positif.size();
		/*
		 * Initialisation du nombre de tweets négatifs dans la base d'apprentissage
		 */
		nombre_tweets_NEGATIF = tableau_Negatif.size();
		/*
		 * Initialisation du nombre de tweets indéterminés dans la base d'apprentissage
		 */
		nombre_tweets_INDETERMINE = tableau_Indetermine.size();
	}

	/**
	 * Méthode permettant d'initialiser le nombre de mots pour chaque ensemble, ainsi que pour l'ensemble Positif + Négatif + Indéterminé
	 */
	protected void init_Nb_Mots_Tous(){
		nombre_mots_POSITIF = 0;
		nombre_mots_NEGATIF = 0;
		nombre_mots_INDETERMINE = 0;
		nombre_de_mot_TOTAL = 0;
		/*
		 * Pour chaque mot dans l'ensemble Positif, on incrémente la valeur du nombre de mots de l'ensemble Positif
		 */
		for (String str : tableau_Positif)
			nombre_mots_POSITIF += (1 + this.stringOccur(str.trim(), " " ));
		/*
		 * Pour chaque mot dans l'ensemble Négatif, on incrémente la valeur du nombre de mots de l'ensemble Négatif
		 */
		for (String str : tableau_Negatif)
			nombre_mots_NEGATIF += (1 + this.stringOccur(str.trim(), " " ));
		/*
		 * Pour chaque mot dans l'ensemble Indéterminé, on incrémente la valeur du nombre de mots de l'ensemble Indéterminé
		 */
		for (String str : tableau_Indetermine)
			nombre_mots_INDETERMINE += (1 + this.stringOccur(str.trim(), " " ));
		/*
		 * Initialisation du nombre de mots total
		 */
		nombre_de_mot_TOTAL = setNombreMotsTotal();
	}

	/**
	 * Méthode permettant d'initialiser l'entier contenant le nombre de mots de plus de trois lettres d'un ensemble
	 */
	protected void init_Nb_Mots_PLUS_Trois_Lettres(){
		nombre_mots_POSITIF = 0;
		nombre_mots_NEGATIF = 0;
		nombre_mots_INDETERMINE = 0;
		nombre_de_mot_TOTAL = 0;
		
		/*
		 * Pour chaque mot, et dans chaque ensemble, on regarde si sa longueur est > à 3 - si oui, on incrémente la valeur de la variable
		 */
		
		for (String str : tableau_Positif)
			for(String mot : str.split(" "))
				nombre_mots_POSITIF += (mot.length()>3)?1:0;

		for (String str : tableau_Negatif)
			for(String mot : str.split(" "))
				nombre_mots_NEGATIF += (mot.length()>3)?1:0;

		for (String str : tableau_Indetermine)
			for(String mot : str.split(" "))
				nombre_mots_INDETERMINE += (mot.length()>3)?1:0;
		/*
		 * Initialisation du nombre de mots total
		 */
		nombre_de_mot_TOTAL = setNombreMotsTotal();
	}

	
	
	

/************************************************************************************************************************************************************************************************************/
	
	/* Methodes Principales*/
	
	

	/**
	 * Méthode représentant l'algorithme d'évaluation des tweets
	 * @param tweet_clean Le tweet nettoyé à évaluer
	 * @param isPresence Booléen permettant de vérifier la présence ou non
	 * @return La classe du tweet à évaluer
	 */
	//P(positif|t) = (P(m1|positif) * P(m2|positif) * ... ) * P(positif)
	//P(negatif|t) = (P(m1|negatif) * P(m2|negatif) * ... ) * P(negatif)
	//P(indetermine|t) = (P(m1|indetermine) * P(m2|indetermine) * ... ) * P(indetermine)
	//la valeur la plus eleve l'emporte
	protected Classe_e algoEvalTweetBayes(String tweet_clean){

		System.out.println(tweet_clean);
		/*
		 * Ligne stockée dans le fichier CSV
		 */
		String[] tab_tweet = tweet_clean.split(";");
		/*
		 * On récupère seulement le corps nettoyé du tweet
		 */
		String string_text = tab_tweet[2];
		
		String strTampon = (isPLUSTroisLettres)?regexReplace(string_text):string_text;

		/*
		 * Tableau de mots du tweet nettoyé
		 */
		String[] tab_text = strTampon.split(" ");

		if(isPLUSTroisLettres){
			init_Nb_Mots_PLUS_Trois_Lettres();
		}
		else{
			init_Nb_Mots_Tous();
		}
		/*
		 * Vérification du gramme, et utilisation de la bonne méthode en fonction de celui-ci
		 */
		if(gramme.equals("Unigramme"))
			return AlgoUnigramme(tab_text);
		else if(gramme.equals("Bigramme")){
			return AlgoBigramme(tab_text);
		}
		else {
			return Algo_Uni_Bigramme(tab_text);
		}
	}
	
	/**
	 * Méthode représentation l'algorithme des uni-bigrammes
	 * @param tab 
	 * @param isPresence
	 * @return
	 */
	protected Classe_e Algo_Uni_Bigramme(String[] tab){
		
		/* 
		 * Unigramme
		 */

		/*
		 * Initialisation de valeurs
		 */
		double p_POSITIVE = 1;
		double p_NEGATIVE = 1;
		double p_INDETERMINE = 1;

		/*
		 * Pour chaque 
		 */
		for(String str : tab) {
			double tmp = probOccurenceAdvanced(str, Classe_e.POSITIVE, isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, Classe_e.NEGATIVE, isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;

			tmp = probOccurenceAdvanced(str, Classe_e.INDETERMINE, isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}

		p_POSITIVE    *= ((double)getNombreTweets(Classe_e.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((double)getNombreTweets(Classe_e.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((double)getNombreTweets(Classe_e.INDETERMINE) / getNombreTweetsTotal());
		
		/*
		 * Bigramme
		 */
		
		double p_POSITIVE_BI = 1;
		double p_NEGATIVE_BI = 1;
		double p_INDETERMINE_BI = 1;
		
		if(tab.length<2) return AlgoUnigramme(tab);
		
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
			
			double tmp_BI = probOccurenceAdvanced(str, Classe_e.POSITIVE, isPresence);
			p_POSITIVE_BI    *= (tmp_BI == 0)? 1 : tmp_BI ;
			tmp_BI = probOccurenceAdvanced(str, Classe_e.NEGATIVE, isPresence);
			p_NEGATIVE_BI    *= (tmp_BI == 0)? 1 : tmp_BI ;
			tmp_BI = probOccurenceAdvanced(str, Classe_e.INDETERMINE, isPresence);
			p_INDETERMINE_BI *= (tmp_BI == 0)? 1 : tmp_BI ;
		}

		p_POSITIVE_BI    *= ((double)getNombreTweets(Classe_e.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE_BI    *= ((double)getNombreTweets(Classe_e.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE_BI *= ((double)getNombreTweets(Classe_e.INDETERMINE) / getNombreTweetsTotal());
				
		/*Uni-Bigramme*/
		
		double p_POSITIVE_UNI_BI = p_POSITIVE * p_POSITIVE_BI;
		double p_NEGATIVE_UNI_BI = p_NEGATIVE * p_NEGATIVE_BI;
		double p_INDETERMINE_UNI_BI = p_INDETERMINE * p_INDETERMINE_BI;
		

		if (p_NEGATIVE_UNI_BI > p_POSITIVE_UNI_BI && p_NEGATIVE_UNI_BI > p_INDETERMINE_UNI_BI)
			return Classe_e.NEGATIVE;
		if (p_POSITIVE_UNI_BI > p_INDETERMINE_UNI_BI )
			return Classe_e.POSITIVE;
		else
			return Classe_e.INDETERMINE;
		
	}

	protected Classe_e AlgoUnigramme(String[] tab){

		double p_POSITIVE = 1;
		double p_NEGATIVE = 1;
		double p_INDETERMINE = 1;

		for(String str : tab) {
			double tmp = probOccurenceAdvanced(str, Classe_e.POSITIVE, this.isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, Classe_e.NEGATIVE, this.isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;

			tmp = probOccurenceAdvanced(str, Classe_e.INDETERMINE, this.isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}

		p_POSITIVE    *= ((double)getNombreTweets(Classe_e.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((double)getNombreTweets(Classe_e.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((double)getNombreTweets(Classe_e.INDETERMINE) / getNombreTweetsTotal());

		if (p_NEGATIVE > p_POSITIVE && p_NEGATIVE > p_INDETERMINE)
			return Classe_e.NEGATIVE;
		if (p_POSITIVE > p_INDETERMINE )
			return Classe_e.POSITIVE;
		else
			return Classe_e.INDETERMINE;
	}

	protected Classe_e AlgoBigramme(String[] tab){

		double p_POSITIVE = 1;
		double p_NEGATIVE = 1;
		double p_INDETERMINE = 1;


		
		if(tab.length<2) return AlgoUnigramme(tab);
		
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
			
			double tmp = probOccurenceAdvanced(str, Classe_e.POSITIVE, isPresence);
			p_POSITIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, Classe_e.NEGATIVE, isPresence);
			p_NEGATIVE    *= (tmp == 0)? 1 : tmp ;
			tmp = probOccurenceAdvanced(str, Classe_e.INDETERMINE, isPresence);
			p_INDETERMINE *= (tmp == 0)? 1 : tmp ;
		}

		p_POSITIVE    *= ((double)getNombreTweets(Classe_e.POSITIVE)    / getNombreTweetsTotal());
		p_NEGATIVE    *= ((double)getNombreTweets(Classe_e.NEGATIVE)    / getNombreTweetsTotal());
		p_INDETERMINE *= ((double)getNombreTweets(Classe_e.INDETERMINE) / getNombreTweetsTotal());

		if (p_NEGATIVE > p_POSITIVE && p_NEGATIVE > p_INDETERMINE)
			return Classe_e.NEGATIVE;
		if (p_POSITIVE > p_INDETERMINE )
			return Classe_e.POSITIVE;
		else
			return Classe_e.INDETERMINE;
	}

	/**
	 * Méthode permettant, pour un tweet nettoyé, d'obtenir l'évaluation de celui-ci (par la méthode Bayes)
	 * @param tweet_clean Le tweet nettoyé à classifer
	 * @param isPresence Booléen de présence
	 * @return La classification du tweet nettoyé
	 */
	public String getEvaluationTweetBayes(String tweet_clean){
		Classe_e result;
		result = algoEvalTweetBayes(tweet_clean);
		if (result == Classe_e.POSITIVE)
			return "Positif";
		if (result == Classe_e.NEGATIVE)
			return "Negatif";
		else
			return "Indetermine";
	}




	
	
/************************************************************************************************************************************************************************************************************/
	
	/*Methodes Secondaires*/
	
	

	/**
	 * Méthode permettant de résoudre la probabilité d'occurence du mot m dans un tweet de la classe c, à l'aide de l'ensemble d'apprentissage
	 * @param m Le mot à rechercher dans un tweet de la classe c
	 * @param c Classe_e de tweet
	 * @param isPresence Présence où non dans la classe de tweet
	 * @return La probabilité d'occurence (double)
	 */
	//P(m|C) -> probabilité d'occurence du mot m dans un tweet de la classe c à l'aide de l'ensemble d'apprentissage
	// retourner n(m,c) / n(c)
	// /!\ voir n(m,c)
	// solution : utiliser un estimateur de Laplace
	//retourner (n(m,c)+1) / (n(c) + nombre total des mot de l'ensemble) 
	//ne retournera jamais zero
	private double probOccurenceAdvanced(String m, Classe_e c, boolean isPresence){
		double result, pOBP, nBM;

		pOBP = (isPresence)?
				probOccurenceBasiquePresence(m, c) : //isPresence
					probOccurenceBasiqueFrequence(m, c); //!isPresence
		if(pOBP == 0)
			return 0;
		nBM = getNombreMots(c);
		result = (pOBP +1) / ( nBM+ nombre_de_mot_TOTAL );
		/*
		 * Affichage
		 */
		/*System.out.println("probOccurenceBasique : " + pOBP);
		System.out.println("Nb mots              : " + nBM);
		System.out.println("result               : " + result);*/
		/*
		 * On retourne le résultat
		 */
		return result;
	}



	/**
	 * Méthode permettant de résoudre la probabilité d'occurence du mot m, dans un tweet de la classe c
	 * @param m Le mot à rechercher dans un tweet de la classe c
	 * @param c Classe_e de tweet
	 * @return Un flottant correspondant à la probabilité d'occurence du mot m, dans un tweet de la classe c
	 */
	//n(m,c) -> probabilité d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut être nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	private double probOccurenceBasiquePresence(String m, Classe_e c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = getNombreTweets(c);//MODIF
		int nb_occur = 0;
		for(String str : list){
			if(str.contains(m))
				nb_occur++;
		}
		return (double)nb_occur/(double)nb_tweets;
	}

	/**
	 * Méthode permettant de résoudre la probabilité de fréquence du mot m, dans un tweet de la classe c
	 * @param m Le mot à rechercher dans un tweet de la classe c
	 * @param c Classe_e de tweet
	 * @return Un flottant contenant la probabilité d'occurence du mot m, dans un tweet de la classe c
	 */
	//n(m,c) -> probabilité d'occurence du mot m dans un tweet de la classe c
	// /!\ n(m,c) peut être nulle car : si le mot n'apparait jamais on ne va faire que des divisions $0/nombre de mot du tweet$ * $0/nombre de mot du tweet$ * ... 
	private double probOccurenceBasiqueFrequence(String m, Classe_e c){
		ArrayList<String> list = getClasse(c);
		int nb_tweets = getNombreTweets(c);
		int nb_occur = 0;
		int tmp = 0;
		nombre_occurence_total = 0;
		for(String str : list){
			tmp = stringOccur(str,m);
			if(tmp != 0){
				nombre_occurence_total += tmp;
				nb_occur += 1;
			}
		}
		return (double) Math.pow((double)nb_occur/(double)nb_tweets, (double)nombre_occurence_total/(double)nb_tweets);
		//return (double)nb_occur/(double)nb_tweets;
	}
	
	
	
	
	
/************************************************************************************************************************************************************************************************************/
	
	/* Methodes de travail sur les cha\EEnes de carracteres*/
	
	
	
	/**
	 * Méthode permettant de renvoyer le nombre d'occurrences de la sous-chaine de caractères spécifiée dans la chaine de caractères spécifiée
	 * @param text chaine de caractères initiale
	 * @param string sous-chaine de caractères dont le nombre d'occurrences doit etre compté
	 * @return le nombre d'occurrences du pattern spécifié dans la chaine de caractères spécifiée
	 */
	public final int stringOccur(String text, String string) {
		return regexOccur(text, Pattern.quote(string));
	}
	
	/**
	 * Méthode permettant de renvoyer le nombre d'occurrences du pattern spécifié dans la chaine de caractères spécifiée
	 * @param text chaine de caractères initiale
	 * @param regex expression régulière dont le nombre d'occurrences doit etre compté
	 * @return le nombre d'occurrences du pattern spécifié dans la chaine de caractères spécifiée
	 */
	public final int regexOccur(String text, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(text);
		int occur = 0;
		while(matcher.find()) {
			occur ++;
		}
		return occur;
	}
	
	/**
	 * Méthode permettant de remplacer une chaîne de caractères, reconnue par le paramètre text
	 * @param text Regex
	 * @return Une nouvelle chaîne de caractères, résultante du remplacement de la chaîne initiale par le paramètre text
	 */
	public final String regexReplace(String text){
		String tampon = text;
		Pattern p = Pattern.compile("\\s\\S\\S\\S\\s");
		Matcher m = p.matcher(tampon);
		StringBuffer sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, " ");
		m.appendTail(sb);
		tampon = sb.toString();
		p = Pattern.compile("\\s\\S\\S\\s");
		m = p.matcher(tampon);
		sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, " ");
		m.appendTail(sb);
		tampon = sb.toString();
		p = Pattern.compile("\\s\\S\\s");
		m = p.matcher(tampon);
		sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, " ");
		m.appendTail(sb);
		return sb.toString();
	}
	
	
	
	
	
/************************************************************************************************************************************************************************************************************/
	
	/* GETTERs/SETTEURs*/
	
	
	
	
	/**
	 * Méthode permettant de modifier le booléen associé à Bigramme
	 * @param bool Le booléen de remplacement
	 */
	public void setBooleanBigramme(boolean bool) {
		this.isBigramme = bool;
	}
	
	/**
	 * Méthode permettant de modifier le booléen associé à Bigramme
	 * @param bool Le booléen de remplacement
	 */
	public void setBooleanUnigramme(boolean bool){
		this.isUnigramme = bool;
	}
	
	/**
	 * Méthode permettant de modifier le booléen de présence d'un mot de plus de 3 lettres
	 * @param b Le booléen associé à l'attribut isPLUSTroisLettres
	 */
	public void setBooleanNbrLetters(boolean bool) {
		this.isPLUSTroisLettres = bool;
	}
	
	/**
	 * Méthode permettant de calculer et sauver le nombre de mots au total
	 * @return Un entier contenant le nombre de mots au total
	 */
	public int setNombreMotsTotal(){
		int result = 0;
		for(Classe_e c : Classe_e.values()){
			result += getNombreMots(c);
		}
		return result;
	}
	
	public void setBooleanIsPresense(boolean bool){
		this.isPresence = bool;
	}
	
	
	
	/**
	 * Méthode permettant de retourner le nombre de tweets au total
	 * @return Un entier correspondant au nombre de tweets recueillis
	 */
	protected int getNombreTweetsTotal(){
		int result = 0;
		for(Classe_e c : Classe_e.values()){
			result += getNombreTweets(c);
		}
		return result;
	}
	
	/**
	 * Méthode permettant de retourner le nombre de mots contenus dans la classe c
	 * @param c La classe de tweet
	 * @return Un entier contenant le nombre de mots contenus dans la classe c
	 */
	//n(c) -> nombre de mot de la classe c 
	//utiliser les attributs "nombre total de mot de la classe c
	protected int getNombreMots(Classe_e c){
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
	
	public boolean getIsPresence(){
		return this.isPresence;
	}

	/**
	 * Méthode permettant de retourner le nombre de tweets de la catégorie associée à la classe donnée en paramètre
	 * @param c La classe de tweets
	 * @return Un entier contenant le nombre de tweets de la catégorie associée au paramètre c
	 */
	protected int getNombreTweets(Classe_e c){
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
	
	/**
	 * Méthode permettant de retourner la liste des tweets d'une catégorie - définie en fonction de la classe de tweet c
	 * @param c La classe de tweets : Positif, Négatif, Indéterminé
	 * @return La liste des tweets de la catégorie correspondant à la classe c
	 */
	protected ArrayList<String> getClasse(Classe_e c){
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
	
	/***********************************************************************************************************************************************************************************************************/
	
	/* FIN*/
	
}
