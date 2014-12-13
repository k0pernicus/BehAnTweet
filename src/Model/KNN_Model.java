package Model;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Classe KNN_Model
 * Modèle contenant tous les attributs et méthodes nécessaires à la détermination des comportements via le KNN
 * @author antonin
 */
public class KNN_Model extends Dict_Model {
	
	/**
	 * Limite des voisins à prendre en compte
	 */
	private static final Integer K_LIMITS = 10;

	/**
	 * Constructeur permettant d'implémenter un modèle KNN
	 * @constructor
	 */
	public KNN_Model() {
		super();
		/*
		 * Chargement de la base d'apprentissage
		 */
		try {
			super.init_Array();
		} 
		/*
		 * Exception remontée si la base d'apprentissage n'a pas été trouvé
		 */
		catch (IOException e) {
			System.out.println("Fichier non trouvé");
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode permettant de calculer la distance entre deux tweets
	 * Cette distance sera calculé selon la formule ici : [Nombre_de_mots_en_tout - (nombre_de_mots_en_commun * 2)]
	 * @param first_tweet Le tweet à classifier
	 * @param second_tweet Le tweet déjà classifié
	 * @return Retourne l'évaluation du tweet (négatif, positif, nul)
	 */
	public int distanceKNNTweets(String first_tweet, String second_tweet) {
		
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
		int distance = 0;
		
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
		distance = (nb_words_tweet + nb_words_tweet_clean) - (compteur * 2);
		
		/*
		 * On retourne l'évaluation du tweet
		 */
		return distance;
	}
	
	/**
	 * Méthode permettant de retourner l'évaluation d'un tweet en fonction de sa similarité avec K_TWEETS plus proches, déjà évalués
	 * @param tweet Le tweet à évaluer
	 * @return Un entier représentant l'évaluation du tweet - <0 si majorité positive, >0 si majorité négative, =0 si indéterminé
	 */
	public int getEvaluationKNNTweet(String tweet) {
		
		/*
		 * Evaluation du tweet donné en paramètre
		 */
		int evaluation_positif = 0;
		int evaluation_negatif = 0;
		int evaluation_indetermine = 0;
		
		/*
		 * Tableau d'objets allant contenir les K_LIMITS voisins du tweet donné
		 */
		distanceCouple[] KNN_voisins = new distanceCouple[K_LIMITS];
		
		/*
		 * Méthode de comparaison pour des objets de type distanceCouple
		 */
		compareDistanceCouple distance_couple_comparaison = new compareDistanceCouple();
		
		/*
		 * Ajout des K_LIMITS premiers tweets
		 */
		for (int i = 0; i < K_LIMITS; i++) {
			Obj_tweet tweet_i = this.tableau_tweets.get(i);
			String tweet_tweet_i = tweet_i.getTweet();
			KNN_voisins[i] = new distanceCouple(this.distanceKNNTweets(tweet, tweet_tweet_i), tweet_tweet_i, tweet_i.getAvis());
		}
		
		/*
		 * Tri du tableau des KNN voisins afin de pouvoir récupérer facilement le tweet ayant la plus grande distance
		 */
		Arrays.sort(KNN_voisins, distance_couple_comparaison);
		
		/*
		 * Ajout des tweets ayant les meilleures correspondances avec le tweet courant
		 */
		for (int i = K_LIMITS; i < this.tableau_tweets.size(); i++) {
			Obj_tweet tweet_i = this.tableau_tweets.get(i);
			String tweet_tweet_i = tweet_i.getTweet();
			int distance_tweet_i = this.distanceKNNTweets(tweet, tweet_tweet_i);
			/*
			 * Si la distance du tweet courant calculé est inférieure à celle de celui ayant la plus grande distance, on le remplace
			 */
			if (distance_tweet_i < KNN_voisins[K_LIMITS - 1].getDistance()) {
				KNN_voisins[K_LIMITS - 1].setTweet(tweet_tweet_i);
				KNN_voisins[K_LIMITS - 1].setDistance(distance_tweet_i);
				KNN_voisins[K_LIMITS - 1].setAvis(tweet_i.getAvis());
			}
			/*
			 * Nouveau tri du tableau
			 */
			Arrays.sort(KNN_voisins, distance_couple_comparaison);
		}
		
		for (int i = 0; i < K_LIMITS; i++) {
			switch(KNN_voisins[i].getAvis()) {
			case "Positif":
				evaluation_positif += 1;
				break;
			case "Negatif":
				evaluation_negatif += 1;
				break;
			case "Indetermine":
				evaluation_indetermine += 1;
				break;
			}
		}
		
		if (evaluation_positif > evaluation_negatif) {
			if (evaluation_positif > evaluation_indetermine) {
				return -1;
			}
			return 0;
		}
		if (evaluation_negatif > evaluation_indetermine) {
			return 1;
		}
		return 0;
		
	}
	
	/**
	 * Classe distanceCouple
	 * Classe interne privée (appartenant à KNN_Model), permettant de représenter la distance entre le tweet courant et le tweet donné dans l'objet
	 * @author antonin
	 */
	private class distanceCouple {
		
		/**
		 * Distance entre le tweet courant et le tweet donné en attribut de l'objet
		 */
		private int distance;
		
		/**
		 * Contenu de tweet accusant la distance avec le tweet courant
		 */
		private String tweet;
		
		/**
		 * Chaîne de caractères représentant l'avis du tweet : Positif, Negatif, Indetermine
		 */
		private String avis;
		
		/**
		 * Constructeur de l'objet distanceCouple
		 * @param distance La distance entre tweet et le tweet courant
		 * @param tweet Chaîne de caractères représentant un contenu de tweet
		 */
		public distanceCouple(int distance, String tweet, String avis) {
			this.distance = distance;
			this.tweet = tweet;
			this.avis = avis;
		}
		
		/**
		 * Méthode permettant de retourner la distance de l'objet sur lequel on invoque la méthode
		 * @return La distance entre le tweet présent dans l'objet, et le tweet courant
		 */
		public int getDistance() {
			return this.distance;
		}
		
		/**
		 * Méthode permettant de modifier la distance de l'objet sur lequel on invoque la méthode
		 * @param distance Un entier correspondant à une distance
		 */
		public void setDistance(int distance) {
			this.distance = distance;
		}
		
		/**
		 * Méthode permettant de modifier le tweet de l'objet sur lequel on invoque la méthode
		 * @param tweet Une chaîne de caractères correspondant à un tweet
		 */
		public void setTweet(String tweet) {
			this.tweet = tweet;
		}
		
		/**
		 * Méthode permettant de retourner l'avis du tweet de l'objet sur lequel on invoque la méthode
		 * @return Une chaîne de caractères représentant l'avis du tweet
		 */
		public String getAvis() {
			return this.avis;
		}
		
		/**
		 * Méthode permettant de modifier l'avis du tweet de l'objet sur lequel on invoque la méthode
		 * @param avis Un avis : Positif, Negatif, Indetermine
		 */
		public void setAvis(String avis) {
			this.avis = avis;
		}
		
		/**
		 * Méthode permettant de retourner une chaîne de caractères représentant un objet distanceCouple
		 * @return Une chaîne de caractères représentant tous les attributs de l'objet sur lequel on invoque la méthode
		 */
		public String toString() {
			return "\"" +  this.tweet + "\" - distance : " + this.distance + " - avis : " + this.avis;
		}
	}
	
	/**
	 * Classe compareDistanceCouple
	 * Classe interne privée permettant de comparer deux objets distanceCouple
	 * @author antonin
	 */
	private class compareDistanceCouple implements Comparator<distanceCouple> {

		/**
		 * Méthode de comparaison entre deux objets distanceCouple
		 * La comparaison se fera sur la distance des objets
		 * @return -1 si l'objet1 est inférieur au 2ème, +1 si l'objet1 est supérieur au 2ème, 0 si la comparaison est la même
		 */
		public int compare(distanceCouple dC1, distanceCouple dC2) {
			return Integer.compare(dC1.getDistance(), dC2.getDistance());
		}
		
	}
	
}
