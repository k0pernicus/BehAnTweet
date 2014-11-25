package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
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
 * Classe ModÃ¨le, avec utilisation du modÃ¨le MVC
 * @extends Observable
 */
public class Model extends Observable {

	/**
	 * Attribut contenant la chaÃ®ne de caractÃ¨res contenant le chemin relatif du fichier CSV contenant tous les tweets (non-nettoyÃ©s)
	 */
	protected final String FILE_NAME = "src/resources/tweets.csv";

	protected final String CLEAN_FILE_NAME = "src/resources/tweets_clean.csv";

	/**
	 * Attribut contenant le rÃ©sultat de la recherche
	 */
	protected QueryResult result;

	protected Dictionnaire dico_positif;

	protected Dictionnaire dico_negatif;
	
	protected ArrayList<String> tableau_Positif;

	protected ArrayList<String> tableau_Indetermine;

	protected ArrayList<String> tableau_Negatif;
	
	protected ArrayList<String> tableau_tweets;
	
	protected String classname;
	
	//CONSTRUCTEUR
	
	 
	public Model(){
		super();
		try {
			generateCSVFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//MAIN METHOD
	/**
	 * MÃ©thode permettant de faire une requÃªte sur Twitter
	 * @param request La requÃªte Ã  envoyer
	 * @throws TwitterException Exception si la requÃªte n'a pas Ãªtre validÃ©e / si la rÃ©ponse n'a pu nous parvenir
	 * @throws IOException Exception si l'Ã©criture des tweets n'a pu se faire
	 * @throws FileNotFoundException Exception si le fichier .csv (utilisÃ© pour y Ã©crire les tweets) n'est pas trouvÃ©
	 */
	public void run(String request, int nbrTweets, String classname) throws TwitterException, FileNotFoundException, IOException{

		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(request);
		query.setLang("fr");
		/*Nombre de tweets par requ�te*/
		query.setCount(nbrTweets);
		try {
			this.result = twitter.search(query);
			this.classname = classname;
			updateObservers();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}
	
	public String getClassname() {
		return this.classname;
	}


	//CSV FILE METHOD


	public void writeIntoCSVFile(String[] tweet) throws FileNotFoundException, IOException{
		File csvFile = new File(CLEAN_FILE_NAME);
		if (!csvFile.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			FileOutputStream in_CSVFile = new FileOutputStream((csvFile), true);
			for (String strTweet : tweet) {
				in_CSVFile.write(strTweet.getBytes());
				in_CSVFile.write("\n".getBytes());

			}
			in_CSVFile.flush();
			in_CSVFile.close();
		}	
	}
	
	/*
	 * Fonction permettant de stocker dans trois array list différentes (en fonction de 'negatif', 'positif', 'indetermine') tous les mots des tweets nettoyés
	 */
	public void getByCSVFile() throws IOException {
		File csvFile = new File(CLEAN_FILE_NAME);
		if (!csvFile.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			BufferedReader buffer = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] words = line.split(";");
				String tweet = words[2];
				String avis = words[6];
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
			buffer.close();
		}
	}
	
	/*
	 * Fonction permettant de stocker dans une array list tous les mots des tweets nettoyés
	 */
	public void getByCSVFile1Array() throws IOException {
		File csvFile = new File(CLEAN_FILE_NAME);
		if (!csvFile.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			BufferedReader buffer = new BufferedReader(new FileReader(csvFile));
			String line;
			while ((line = buffer.readLine()) != null) {
				String[] words = line.split(";");
				String tweet = words[2];
				String avis = words[6];
				tableau_Indetermine.add(tweet);
			}
			buffer.close();
		}
	}
	

	public void generateDictionnaireFile() throws IOException {
		this.dico_positif = new Dictionnaire("src/resources/positive.txt", -1);
		this.dico_negatif = new Dictionnaire("src/resources/negative.txt", 1);

	}

	//COMMUNICATION WITH THE VIEW

	void updateObservers(){
		setChanged();
		notifyObservers();
	}



	//GETTERS / SETTERS
	public QueryResult getResult() {
		return result;
	}

	public void resetCSVFile() throws IOException {
		new FileWriter(new File(FILE_NAME)).close();

	}

	public void generateCSVFile() throws IOException {
		new File("src/resources/", "tweets.csv").createNewFile();
		new File("src/resources/", "tweets_clean.csv").createNewFile();
		new File("src/resources/", "base_apprentissage.csv").createNewFile();
		 
		
	}

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
	
	public String getResultEvaluationDictTweet(String tweet_clean) {
		int result = getEvaluationDictTweet(tweet_clean);
		return this.getEvaluationByResult(result);
	}
	
	public String getEvaluationByResult(int result) {
		if (result > 0)
			return "Negatif";
		if (result < 0)
			return "Positif";
		else
			return "Indetermine";
	}
	
	public void transformTweet() {
		this.tableau_tweets = new ArrayList<String>();
		List<Status> liste_status = this.result.getTweets();
		//Transformation du Status en String
		for (Status status : liste_status) {
			String tweet = cleanTweet(status.getText().toString());
			this.tableau_tweets.add(tweet);
		}
	}
	
	public ArrayList<String> getTableauTweet() {
		return this.tableau_tweets;
	}
	
}
