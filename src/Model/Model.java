package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import Controler.NegativeDictionnaire;
import Controler.PositiveDictionnaire;

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

			for (Status status : result.getTweets()) {
				String tweet = new String();
				tweet = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + request;
				this.writeIntoCSVFile(FILE_NAME,tweet);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	//CSV FILE METHOD

	/**
	 * MÃ©thode permettant d'Ã©crire tous les tweets rÃ©cupÃ©rÃ©s dans un fichier .csv
	 * @param tweet Une chaÃ®ne de caractÃ¨res contenant le tweet
	 * @throws FileNotFoundException Le fichier .csv n'a pas Ã©tÃ© trouvÃ©
	 * @throws IOException L'Ã©criture du tweet dans le fichier n'a pu se faire
	 */
	void writeIntoCSVFile(String file_name, String tweet) throws FileNotFoundException, IOException{
		File csvFile = new File(file_name);
		if (!csvFile.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			FileOutputStream in_CSVFile = new FileOutputStream((file_name), true);
			in_CSVFile.write(tweet.getBytes());
			in_CSVFile.write("\n".getBytes());
			in_CSVFile.flush();
			in_CSVFile.close();
		}	
	}

	/**
	 * MÃ©thode permettant de gÃ©nÃ©rer un fichier .csv, contenant des tweets nettoyÃ©s (sans @, #, RT, URL)
	 * @throws IOException Non-possibilitÃ© d'Ã©crire Ã  l'intÃ©rieur du fichier
	 */
	public void cleanCSVFile() throws IOException{//exception Ã  gÃ©rer dans le main
		File csvFile = new File(FILE_NAME);

		if (!csvFile.exists())
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else
		{

			BufferedReader in_CSVFile = new BufferedReader(new FileReader(FILE_NAME));

			Pattern p;
			Matcher m;
			String line;
			String patternRT     = "RT ";
			String patternAROBAS_HASHTAG = "[@|#][[^\\s]&&\\p{ASCII}]*\\s"; // need espace autour des hashtags
			String patternHTTP = "http[[^\\s]&&\\p{ASCII}]*\\s";

			while ((line = in_CSVFile.readLine()) != null) {
				p = Pattern.compile(patternRT);	
				m = p.matcher(line);

				if(m.find()){
					continue;
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

				line = sb.toString();

				this.writeIntoCSVFile(CLEAN_FILE_NAME,line);

			}

			/* Ce que l'on veut:
			 * Pattern Matcher sur chaque ligne du tableau .csv
			 * 	-> '@' (on kill tout le mot d'aprÃ¨s) FAIT
			 * 	-> '#' (on kill tout le mot d'aprÃ¨s) FAIT
			 *  -> 'RT' (on n'enregistre rien) FAIT
			 */
			
			//Fermeture du fichier
			in_CSVFile.close();

		}
	}
	
	public void generateDictionnaireFile() throws IOException {
		PositiveDictionnaire posi_dict = new PositiveDictionnaire();
		NegativeDictionnaire nega_dict = new NegativeDictionnaire();
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


}
