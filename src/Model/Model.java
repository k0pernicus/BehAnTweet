package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

/**
 * Classe Modèle, avec utilisation du modèle MVC
 * @extends Observable
 * @author antonin verkyndt
 */
public class Model extends Observable {

	/**
	 * Attribut contenant la chaîne de caractères contenant le chemin relatif du fichier CSV contenant tous les tweets (non-nettoyés)
	 */
	private static final String FILE_NAME = "src/resources/tweets.csv";

	private static final String CLEAN_FILE_NAME = "src/resources/tweets_clean.csv";

	/**
	 * Attribut contenant le résultat de la recherche
	 */
	private QueryResult result;
	
	/**
	 * Attribut contenant le nombre de tweets � recueillir pour une recherche
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
	 * Méthode permettant de faire une requête sur Twitter
	 * @param request La requête à envoyer
	 * @throws TwitterException Exception si la requête n'a pas être validée / si la réponse n'a pu nous parvenir
	 * @throws IOException Exception si l'écriture des tweets n'a pu se faire
	 * @throws FileNotFoundException Exception si le fichier .csv (utilisé pour y écrire les tweets) n'est pas trouvé
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
		//TEST
		this.cleanCSVFile();
		//TEST
	}

	//CSV FILE METHOD

	/**
	 * Méthode permettant d'écrire tous les tweets récupérés dans un fichier .csv
	 * @param tweet Une chaîne de caractères contenant le tweet
	 * @throws FileNotFoundException Le fichier .csv n'a pas été trouvé
	 * @throws IOException L'écriture du tweet dans le fichier n'a pu se faire
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
	 * Méthode permettant de générer un fichier .csv, contenant des tweets nettoyés (sans @, #, RT, URL)
	 * @throws IOException Non-possibilité d'écrire à l'intérieur du fichier
	 */
	void cleanCSVFile() throws IOException{//exception à gérer dans le main
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
			 * 	-> '@' (on kill tout le mot d'après) FAIT
			 * 	-> '#' (on kill tout le mot d'après) FAIT
			 *  -> 'RT' (on n'enregistre rien) FAIT
			 */
			
			//Fermeture du fichier
			in_CSVFile.close();

		}
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


}
