package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
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
	private static final String FILE_NAME = "src/resources/tweets.csv";

	private static final String CLEAN_FILE_NAME = "src/resources/tweets_clean.csv";

	/**
	 * Attribut contenant le rÃ©sultat de la recherche
	 */
	private QueryResult result;
	
	/**
	 * Attribut contenant le nombre de tweets ˆ recueillir pour une recherche
	 */
	private int nbrTweets;
	
	/**
	 * Getter nbrTweets
	 */
	public int getNbrTweets() {
		
		return this.nbrTweets;
		
	}
	
	/**
	 * Setter nbrTweets
	 */
	public void setNbrTweets(int nbrTweets) {
		
		this.nbrTweets = nbrTweets;
		
	}

	//MAIN METHOD
	/**
	 * MÃ©thode permettant de faire une requÃªte sur Twitter
	 * @param request La requÃªte Ã  envoyer
	 * @throws TwitterException Exception si la requÃªte n'a pas Ãªtre validÃ©e / si la rÃ©ponse n'a pu nous parvenir
	 * @throws IOException Exception si l'Ã©criture des tweets n'a pu se faire
	 * @throws FileNotFoundException Exception si le fichier .csv (utilisÃ© pour y Ã©crire les tweets) n'est pas trouvÃ©
	 */
	public void run(String request) throws TwitterException, FileNotFoundException, IOException{

		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(request);
		query.setLang("fr");
		/*Nombre de tweets par requ�te*/
		query.setCount(nbrTweets);
		try {
			result = twitter.search(query);
			updateObservers();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

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
		
	public void generateDictionnaireFile() throws IOException {
		Dictionnaire posi_dict = new Dictionnaire("src/resources/positive.txt", 0);
		Dictionnaire nega_dict = new Dictionnaire("src/resources/negative.txt", 1);
		posi_dict.parse();
		nega_dict.parse();
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

	public void generateCSVFile() {
		new File("src/resources/", "tweets.csv");
		new File("src/resources/","tweets_clean.csv");
		
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

	
	public int getEvaluationTweet(String contentClean) {
		// TODO Auto-generated method stub
		return -1;
	}


}
