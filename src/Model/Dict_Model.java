package Model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import Controler.Dictionnaire;

public class Dict_Model extends Model {

	/**
	 * Attribut contenant le dictionnaire de mots positif
	 */
	protected Dictionnaire dico_positif;

	/**
	 * Attribut contenant le dictionnaire de mots négatif
	 */
	protected Dictionnaire dico_negatif;
	
	public Dict_Model() {
		super();
	}

	/**
	 * Méthode permettant de générer les dictionnaires de mots positif/négatif
	 * @throws IOException Exception levée si la création des fichiers correspondant aux dictionnaires positifs/négatifs n'est pas possible
	 */
	public void generateDictionnaireFile() throws IOException {
		this.dico_positif = new Dictionnaire("src/resources/positive.txt", -1);
		this.dico_negatif = new Dictionnaire("src/resources/negative.txt", 1);
	}
	
	/**
	 * Méthode permettant de retourner l'évaluation d'un tweet nettoyé, pour la méthode de classification par dictionnaire
	 * @param tweet_clean Le tweet nettoyé
	 * @return Un entier contenant le résultat de l'algorithme : -1 pour positif / +1 pour négatif / 0 pour indéterminé
	 */
	public int getEvaluationDictTweet(String tweet_clean) {
		int result = 0;
		String[] split_tweet = tweet_clean.trim().split(" ");
		List<String> liste_positive = Arrays.asList(this.dico_positif.getDictionnaire());
		List<String> liste_negative = Arrays.asList(this.dico_negatif.getDictionnaire());
		for (String string : split_tweet) {
			if (liste_positive.contains(" "+string)) {
				result += this.dico_positif.getNbr();
			}
			if (liste_negative.contains(" "+string)) {
				result += this.dico_negatif.getNbr();
			}
		}
		return result;
	}

	/**
	 * Méthode permettant de renvoyer une chaîne de caractères, correspondant à la classification du tweet donné en paramètre
	 * @param tweet_clean Un tweet nettoyé
	 * @return Une chaîne de caractères correspondant à la classification du tweet
	 */
	public String getResultEvaluationDictTweet(String tweet_clean) {
		int result = getEvaluationDictTweet(tweet_clean);
		return this.getEvaluationByResult(result);
	}
	
}
