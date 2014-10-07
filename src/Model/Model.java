package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Classe Modèle, avec utilisation du modèle MVC
 * @extends Observable
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
		try {
				result = twitter.search(query);
				updateObservers();
				
			    for (Status status : result.getTweets()) {
			    	String tweet = new String();
			    	tweet = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+"\";" + status.getCreatedAt() + ";" + request;
			    	this.writeIntoCSVFile(tweet);
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
	void writeIntoCSVFile(String tweet) throws FileNotFoundException, IOException{
		File csvFile = new File(FILE_NAME);
		if (!csvFile.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			FileOutputStream in_CSVFile = new FileOutputStream((FILE_NAME), true);
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
		File csvFile_clean   = new File(CLEAN_FILE_NAME);
		
		if (!csvFile_clean.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else
			if (!csvFile_clean.exists())
				throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
			else
			{
				
				BufferedReader in_CSVFile = new BufferedReader(new FileReader(FILE_NAME));
				FileOutputStream out_CSVFile = new FileOutputStream(CLEAN_FILE_NAME, true);
				
				while (in_CSVFile.readLine() != null) {
					
				}
				
				/* Ce que l'on veut:
				 * Pattern Matcher sur chaque ligne du tableau .csv
				 * 	-> '@' (on kill tout le mot d'après)
				 * 	-> '#' (on kill tout le mot d'après)
				 *  -> 'RT' (on n'enregistre rien)
				 */
				
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
