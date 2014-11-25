package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author antonin
 */
public class KNN_Model extends Model {
	
	private static final Integer KNN_LIMITS = 10;

	/**
	 * @constructor
	 * Objet permettant d'implémenter un modèle selon l'algorithme KNN
	 */
	public KNN_Model() {
		super();
	}
	
	/**
	 * Méthode permettant de récupérer l'évaluation d'un groupe de tweets - renvoie "Positif", "Negatif" ou "Indetermine"
	 * @param tweets Une liste de tweets
	 * @return Le tableau de groupes avec son évaluation
	 */
	public String[] getEvaluationKNNTweet(ArrayList<String> tweets) {
		int[] groups = this.getGroups(tweets);
		int nbGroups = this.getNbGroups(groups);
		int sizetab = groups.length;
		String evaluation_groups[] = new String[nbGroups];
		for (int i = 0; i < nbGroups; i++) {
			int res = 0;
			int nbr = 0;
			for (int j = 0; j < sizetab; j++) {
				if (groups[j] == i) {
					res += getEvaluationDictTweet(tweets.get(j));
					nbr++;
				}
			}
			evaluation_groups[i] = this.getEvaluationByResult(res / nbr);
		}
		return evaluation_groups;
	}
	
	/**
	 * Méthode permettant de retourner le nombre de groupes formés
	 * @param groups Le tableau de groupes
	 * @return Le nombre de groupes formés - soit le plus grand entier contenu dans le tableau de groupes
	 */
	public int getNbGroups(int[] groups) {
		int max = 0;
		for (int i = 0; i < groups.length; i++) {
			if (max < groups[i])
				max = groups[i];
		}
		return max;
	}
	
	/**
	 * Méthode permettant de retourner les groupes de tweets
	 * @param tweets Une liste de tweets
	 * @return Un tableau contenant des entiers - chaque entier étant un groupe de tweets (en fonction de leur évaluation)
	 */
	public int[] getGroups(ArrayList<String> tweets) {
		int groupe_nbr = 0;
		int groupes[] = new int[tweets.size()];
		//Initialisation du tableau de groupes
		for(int i = 0; i < groupes.length; i++)
			groupes[i] = -1;
		//Calcul des groupes
		for(int i = 0; i < tweets.size(); i++) {
			if (groupes[i] == -1) {
				groupes[i] = groupe_nbr;
				groupe_nbr++;
			}
			for(int j = i+1; j < tweets.size(); j++) {
				if (this.calculKNNTweet(tweets.get(i), tweets.get(j)) <= KNN_LIMITS)
					groupes[j] = groupes[i];
			}
		}
		return groupes;
	}
	
	/**
	 * Méthode permettant de calculer l'évaluation sur un tweet
	 * @param first_tweet Le tweet à classifier
	 * @param second_tweet Le tweet déjà classifié
	 * @return Retourne l'évaluation du tweet (négatif, positif, nul)
	 */
	public int calculKNNTweet(String first_tweet, String second_tweet) {
		
		int compteur = 0;
		int nb_words_tweet_clean = 0;
		int nb_words_tweet = 0;
		int evaluation = 0;
		
		String[] tweet_clean_words = first_tweet.split(" ");
		String[] tweet_words = second_tweet.split(" ");
		
		nb_words_tweet_clean = tweet_clean_words.length;
		nb_words_tweet = tweet_words.length;
		
		//Transformation en liste afin de mieux pouvoir travailler sur les mots d'une chaîne de caractères
		List<String> liste_words = Arrays.asList(tweet_clean_words);
		
		//Le compteur augmente à chaque mot en commun
		for (String string : tweet_words) {
			if (liste_words.contains(string))
				compteur++;
		}
		
		//Formule : [Nombre de mots en tout - (nombre de mots en commun * 2)]
		evaluation = (nb_words_tweet + nb_words_tweet_clean) - (compteur * 2);
		
		return evaluation;
		
	}
	
}
