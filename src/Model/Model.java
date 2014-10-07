package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Observable;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Model extends Observable {
	
	private static final String FILE_NAME = "src/resources/tweets.csv";

	private QueryResult result;
	
	public void run(String request) throws IOException{
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(request);
		try {
			result = twitter.search(query);
			updateObservers();
			
		    for (Status status : result.getTweets()) {
		    	String tweet = new String();
		    	tweet = status.getId() + ";" + status.getUser().getScreenName() + ";" + status.getText();
		    	this.writeIntoCSVFile(tweet);
		    }
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	public QueryResult getResult() {
		return result;
	}

	void updateObservers(){
		setChanged();
		notifyObservers();
	}
	
	void writeIntoCSVFile(String tweet) throws IOException{
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
}
