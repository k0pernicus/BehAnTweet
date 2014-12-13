package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe KNN_Model
 * Modèle contenant tous les attributs et méthodes nécessaires à la détermination des comportements via le KNN
 * @extends Model
 * @author antonin
 */
public class KNN_Model extends Dict_Model {
	
	/**
	 * Limite constante - servira quant à la détermination du groupe d'un tweet, en fonction de son score
	 */
	private static final Integer KNN_LIMITS = 10;

	/**
	 * Constructeur permettant d'implémenter un modèle KNN
	 * @constructor
	 */
	public KNN_Model() {
		super();
	}
	
	/**
	 * Méthode permettant de récupérer l'évaluation d'un groupe de tweets - renvoie "Positif", "Negatif" ou "Indetermine"
	 * @param tweets Une liste de tweets
	 * @return Un tableau de chaînes de caractères, correspondant aux évaluations de groupes
	 */
	public String[] getEvaluationKNNTweet(ArrayList<String> tweets) {
		/*
		 * Tableau d'entiers correspondant aux groupes de tweets
		 */
		int[] groups = this.getGroups(tweets);
		/*
		 * Récupération du nombre de groupes
		 */
		int nbGroups = this.getNbGroups(groups);
		/*
		 * Nombre de groupes
		 */
		int sizetab = groups.length;
		/*
		 * Déclaration d'un tableau allant contenir l'évaluation des différents nbGroups groupes
		 */
		String evaluation_groups[] = new String[nbGroups];
		/*
		 * Pour chacun de ces groupes, on va donc calculer son score, et obtenir son comportement : Positif, Négatif ou Indéterminé
		 */
		for (int i = 0; i < nbGroups; i++) {
			int res = 0;
			int nbr = 0;
			for (int j = 0; j < sizetab; j++) {
				if (groups[j] == i) {
					res += getEvaluationDictTweet(tweets.get(j));
					nbr++;
				}
			}
			/*
			 * On récupère l'évaluation
			 */
			evaluation_groups[i] = this.getEvaluationByResult(res / nbr);
		}
		/*
		 * On retourne le tableau d'évaluation
		 */
		return evaluation_groups;
	}
	
	/**
	 * Méthode permettant de retourner le nombre de groupes formés
	 * @param groups Le tableau de groupes
	 * @return Le nombre de groupes formés - soit le plus grand entier contenu dans le tableau de groupes
	 */
	public int getNbGroups(int[] groups) {
		int max = 0;
		/*
		 * On recherche après le groupe maximal
		 */
		for (int i = 0; i < groups.length; i++) {
			if (max < groups[i])
				max = groups[i];
		}
		/*
		 * On retourne ce groupe + 1 
		 */
		return max + 1;
	}
	
	/**
	 * Méthode permettant de retourner les groupes de tweets
	 * @param tweets Une liste de tweets
	 * @return Un tableau contenant des entiers - chaque entier étant un groupe de tweets (en fonction de leur évaluation)
	 */
	public int[] getGroups(ArrayList<String> tweets) {
		int groupe_nbr = 0;
		int groupes[] = new int[tweets.size()];
		/*
		 * Initialisation du tableau de groupes
		 */
		for(int i = 0; i < groupes.length; i++)
			groupes[i] = -1;
		/*
		 * Calcul des groupes
		 */
		for(int i = 0; i < tweets.size(); i++) {
			/*
			 * Si le groupe est -1, on l'initialise comme un nouveau groupe
			 */
			if (groupes[i] == -1) {
				groupes[i] = groupe_nbr;
				groupe_nbr++;
			}
			/*
			 * Pour chacun les tweets, on l'analyse et l'ajoute dans un groupe pré-établi s'il y a relation
			 */
			for(int j = i+1; j < tweets.size(); j++) {
				if (this.calculKNNTweet(tweets.get(i), tweets.get(j)) <= KNN_LIMITS)
					groupes[j] = groupes[i];
			}
		}
		/*
		 * On retourne les groupes de tweets
		 */
		return groupes;
	}
	
	/**
	 * Méthode permettant de calculer la distance entre deux tweets
	 * Cette distance sera calculé selon la formule ici : [Nombre_de_mots_en_tout - (nombre_de_mots_en_commun * 2)]
	 * @param first_tweet Le tweet à classifier
	 * @param second_tweet Le tweet déjà classifié
	 * @return Retourne l'évaluation du tweet (négatif, positif, nul)
	 */
	public int calculKNNTweet(String first_tweet, String second_tweet) {
		
		int compteur = 0;
		/*
		 * Nombre de mots du tweet nettoyé
		 */
		int nb_words_tweet_clean = 0;
		/*
		 * Nombre de mots du tweet à classifier
		 */
		int nb_words_tweet = 0;
		/*
		 * Evaluation du tweet à classifier
		 */
		int evaluation = 0;
		
		/*
		 * Mots du tweet à classifier
		 */
		String[] tweet_clean_words = first_tweet.split(" ");
		/*
		 * Mots du tweet déjà classifié
		 */
		String[] tweet_words = second_tweet.split(" ");
		
		nb_words_tweet_clean = tweet_clean_words.length;
		nb_words_tweet = tweet_words.length;
		
		/*
		 * Transformation en liste afin de mieux pouvoir travailler sur les mots d'une chaîne de caractères
		 */
		List<String> liste_words = Arrays.asList(tweet_clean_words);
		
		/*
		 * Le compteur augmente à chaque mot en commun
		 */
		for (String string : tweet_words) {
			if (liste_words.contains(string))
				compteur++;
		}
		
		/*
		 * Evaluation du tweet, selon la formule ci-dessous
		 * Formule : [Nombre de mots en tout - (nombre de mots en commun * 2)]
		 */
		evaluation = (nb_words_tweet + nb_words_tweet_clean) - (compteur * 2);
		
		/*
		 * On retourne l'évaluation du tweet
		 */
		return evaluation;
	}
}
