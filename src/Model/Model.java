package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import Controler.Dictionnaire;

/**
 * Classe Model
 * Modèle principal du patron MVC
 * @extends Observable
 * @author antonin
 * @author verkyndt
 */
public class Model extends Observable {

	/**
	 * Attribut contenant la chaîne de caractères contenant le chemin relatif du fichier CSV de tweets non-nettoyés
	 */
	protected final String FILE_NAME = "src/resources/tweets.csv";

	/**
	 * Attribut contenant la chaîne de caractères contenant le chemin relatif du fichier CSV de tweets nettoyés
	 */
	protected final String CLEAN_FILE_NAME = "src/resources/tweets_clean.csv";

	/**
	 * Attribut contenant le résultat de la recherche
	 */
	protected QueryResult result;

	/**
	 * Attribut contenant le dictionnaire de mots positif
	 */
	protected Dictionnaire dico_positif;

	/**
	 * Attribut contenant le dictionnaire de mots négatif
	 */
	protected Dictionnaire dico_negatif;

	/**
	 * Liste de tweets positifs
	 */
	protected ArrayList<String> tableau_Positif;

	/**
	 * Liste de tweets indéterminés
	 */
	protected ArrayList<String> tableau_Indetermine;

	/**
	 * Liste de tweets négatifs
	 */
	protected ArrayList<String> tableau_Negatif;

	/**
	 * Liste contenant tous les tweets : positifs/négatifs/indéterminés
	 */
	protected ArrayList<String> tableau_tweets;

	/**
	 * Attribut contenant le nom de la méthode de classification utilisée
	 */
	protected String classname;

	/**
	 * Attribut contenant le gramme utilisé
	 */
	protected String gramme;
	
	/**
	 * Attribut contenant le nombre de lettres utilisé
	 */
	protected String nbrLettres;

	/**
	 * Constructeur de l'objet Model
	 * @constructor
	 */
	public Model(){
		super();
		/*
		 * Essaie de générer le fichier CSV
		 * Initialisation des tableaux de tweets positifs/négatifs/indéterminés
		 */
		try {
			generateCSVFile();
			tableau_Indetermine = new ArrayList<String>();
			tableau_Positif = new ArrayList<String>();
			tableau_Negatif = new ArrayList<String>();
		} 
		/*
		 * Capture de l'exception si jamais le fichier CSV à générer n'est pas trouvé
		 */
		catch (IOException e) {
			System.out.println("Fichier non trouvé");
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant de faire une requête sur Twitter
	 * @param request La requête à envoyer
	 * @param nbrTweets Le nombre de tweets à récupérer
	 * @param classname Le nom de la méthode de classification
	 * @param gramme Le gramme à utiliser
	 * @param nbrLettres Le nombre de lettres à utiliser
	 * @throws TwitterException Exception si la requête n'a pas être validée / si la réponse n'a pu nous parvenir
	 * @throws FileNotFoundException Exception si le fichier .csv (utilisé pour y écrire les tweets) n'est pas trouvé
	 * @throws IOException Exception si l'écriture des tweets n'a pu se faire
	 */
	public void run(String request, int nbrTweets, String classname, String gramme, String nbrLettres ) throws TwitterException, FileNotFoundException, IOException{

		/*
		 * Objet Twitter - utile pour effectuer les requêtes
		 */
		Twitter twitter = TwitterFactory.getSingleton();

		/*
		 * Instanciation d'une nouvelle requête
		 */
		Query query = new Query(request);
		/*
		 * On récupère les tweets français
		 */
		query.setLang("fr");
		/*
		 * Nombre de tweets par requête initialisé
		 */
		query.setCount(nbrTweets);
		/*
		 * On effectue la recherche et l'on avertie tous les observeurs de changer d'état
		 */
		try {
			this.result = twitter.search(query);
			this.classname = classname;
			this.nbrLettres = nbrLettres;
			this.gramme = gramme;
			updateObservers();
		} 
		/*
		 * On récupère l'exception s'il y a, et on imprime sa trace sur la console
		 */
		catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	//CSV FILE METHOD

	/**
	 * Méthode permettant de sauver un tableau de tweets dans le fichier CSV nettoyé
	 * @param tweets Un tableau de tweets
	 * @throws FileNotFoundException Exception levée si le chemin vers le fichier CSV nettoyé n'est pas correct
	 * @throws IOException Exception levée si l'écriture des octets dans le fichier demandé n'est pas possible
	 */
	public void writeIntoCSVFile(String[] tweets) throws FileNotFoundException, IOException{
		File csvFile = new File(CLEAN_FILE_NAME);
		if (!csvFile.exists())
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas...");
		else{
			FileOutputStream in_CSVFile = new FileOutputStream((csvFile), true);
			/*
			 * Pour chaque tweet, on l'écrit dans le fichier CSV nettoyé
			 */
			for (String strTweet : tweets) {
				in_CSVFile.write(strTweet.getBytes());
				in_CSVFile.write("\n".getBytes());
			}
			/*
			 * Nettoyage et fermeture du fichier
			 */
			in_CSVFile.flush();
			in_CSVFile.close();
		}
	}

	/**
	 * Méthode permettant de stocker dans trois listes différentes (en fonction de 'negatif', 'positif', 'indetermine') tous les mots des tweets nettoyés
	 * @param path Le chemin vers le fichier CSV
	 * @exception IOException Exception levée si l'écriture des octets dans le fichier demandé n'est pas possible
	 */
	public void getByCSVFile(String path) throws IOException {
		File csvFile = new File(path);
		if (!csvFile.exists())
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas...");
		else{
			BufferedReader buffer = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = buffer.readLine()) != null) {
				/*
				 * On split la ligne selon ';'
				 */
				String[] words = line.split(";");
				/*
				 * On récupère la chaîne à la position 2 - correspondant au tweet nettoyé
				 */
				String tweet = words[2];
				/*
				 * On récupère l'avis de la personne
				 */
				String avis = words[6];
				/*
				 * On ajoute le tweet dans la liste correspondante, en fonction de l'avis
				 */
				switch (avis) {
				case "Indetermine":
					tableau_Indetermine.add(tweet);
					break;
				case "Positif":
					tableau_Positif.add(tweet);
					break;
				case "Negatif":
					tableau_Negatif.add(tweet);
					break;
				}
			}
			/*
			 * On ferme le buffer
			 */
			buffer.close();
		}
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
	 * Méthode permettant de communiquer avec les vues
	 */
	void updateObservers(){
		setChanged();
		notifyObservers();
	}

	/**
	 * Méthode permettant de retourner le résultat de la requête
	 * @return Un objet de type QueryResult, correspondant au résultat de la requête
	 */
	public QueryResult getResult() {
		return result;
	}

	/**
	 * Méthode permettant de remettre à zéro le fichier CSV
	 * @throws IOException Exception levée si le fichier de chemin FILE_NAME n'existe pas, ou n'est pas disponible
	 */
	public void resetCSVFile() throws IOException {
		new FileWriter(new File(FILE_NAME)).close();
	}

	/**
	 * Méthode permettant de générer les fichiers CSV de tweets : tweets.csv, tweets_clean.csv et base_apprentissage.csv
	 * @throws IOException
	 */
	public void generateCSVFile() throws IOException {
		new File("src/resources/", "tweets.csv").createNewFile();
		new File("src/resources/", "tweets_clean.csv").createNewFile();
		new File("src/resources/", "base_apprentissage.csv").createNewFile();
	}

	/**
	 * Méthode permettant de renvoyer la méthode de classification choisie
	 * @return Une chaîne de caractères contenant la méthode de classification choisie
	 */
	public String getClassname() {
		return classname;
	}
	
	/**
	 * Méthode permettant de renvoyer la gramme choisie
	 * @return Une chaîne de caractères contenant la gramme choisie
	 */
	public String getGramme() {
		return gramme;
	}
	
	/**
	 * Méthode permettant de renvoyer le nombre de lettres choisi
	 * @return Une chaîne de caractères contenant le nombre de lettres choisi
	 */
	public String getNbrLettres() {
		return nbrLettres;
	}
	
	/**
	 * Méthode permettant de nettoyer un tweet (enlever les 'hashtags', les destinataires et ne garder que le corps du texte avec l'URL), via l'utilisation de regex
	 * @param content Le tweet non-nettoyé
	 * @return Une chaîne de caractères qui contiendra le tweet nettoyé
	 */
	public String cleanTweet(String content) {
		Pattern p;
		Matcher m;
		String line;
		String patternRT     = "RT ";
		String patternAROBAS_HASHTAG = "[@|#][[^\\s]&&\\p{ASCII}]*\\s"; // need espace autour des hashtags
		String patternHTTP = "http[[^\\s]&&\\p{ASCII}]*\\s";

		line = content;
		//LIGNE A IGNORER
		p = Pattern.compile(patternRT);
		m = p.matcher(line);

		if(m.find()){
			return "RT";
		}

		//LIGNE A CONSERVER
		p = Pattern.compile(patternAROBAS_HASHTAG);
		m = p.matcher(line);

		StringBuffer sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, "");
		m.appendTail(sb);
		//SB contient la ligne a conserver sans les @

		line = sb.toString();

		p = Pattern.compile(patternHTTP);
		m = p.matcher(line);
		sb = new StringBuffer();
		while(m.find())
			m.appendReplacement(sb, "");
		m.appendTail(sb);

		return sb.toString();
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

	/**
	 * Méthode permettant de renvoyer une chaîne de caractères, correspondant à une classification, via l'entier donné en paramètre
	 * @param result Un entier négatif, positif ou nul
	 * @return Négatif si l'entier est > 0, Positif si l'entier est < 0, Indeterminé si l'entier est  = 0
	 */
	public String getEvaluationByResult(int result) {
		if (result > 0)
			return "Negatif";
		if (result < 0)
			return "Positif";
		else
			return "Indetermine";
	}

	/**
	 * Méthode permettant de transformer un objet Status en chaîne de caractère
	 */
	public void transformTweet() {
		this.tableau_tweets = new ArrayList<String>();
		List<Status> liste_status = this.result.getTweets();
		//Transformation du Status en String
		for (Status status : liste_status) {
			String tweet = cleanTweet(status.getText().toString());
			this.tableau_tweets.add(tweet);
		}
	}

	/**
	 * Méthode permettant de renvoyer le tableau de tweet
	 * @return Une ArrayList de chaîne de caractères, correspondant à des tweets
	 */
	public ArrayList<String> getTableauTweet() {
		return this.tableau_tweets;
	}


	

}
