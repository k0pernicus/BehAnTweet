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



public class Model extends Observable {
	
	private static final String FILE_NAME = "src/resources/tweets.csv";
	private static final String CLEAN_FILE_NAME = "src/resources/tweets_clean.csv";
	
	private QueryResult result;
	
	
	
	
	
	//MAIN METHOD
	
	public void run(String request) throws IOException{
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(request);
		try {
			result = twitter.search(query);
			updateObservers();
			
		    for (Status status : result.getTweets()) {
		    	String tweet = new String();
		    	tweet = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+"\"";
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
	
	
	void writeIntoCSVFile(String tweet) throws IOException{//exception a gerer dans le main
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

	void cleanCSVFile() throws IOException{//exception a gerer dans le main
		File csvFile = new File(FILE_NAME);
		File csvFile_clean   = new File(CLEAN_FILE_NAME);
		
		if (!csvFile_clean.exists()) 
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else if (!csvFile_clean.exists())
			throw new FileNotFoundException("Le fichier "+csvFile.getAbsolutePath()+" n'existe pas..."); 
		else{
			
			BufferedReader buff = new BufferedReader(new FileReader(FILE_NAME));
			FileOutputStream out_CSVFile = new FileOutputStream(CLEAN_FILE_NAME, true);
			
			String line = "";
			String newline = "";
			boolean ignore = false;
			boolean maybe_RT = false;
			char c;
			while((line = buff.readLine()) != null){
				newline = "";
				for(int i = 0; i< line.length(); i++){
					c = line.charAt(i);
					if(c == '#' || c == '@')
						ignore = true;
					if(c == ' ') 
						ignore = false;
					if(c == 'R') 
						maybe_RT = true;
					if(maybe_RT && c == 'T'){
						maybe_RT = false;
						continue;
					}
					
					if(!ignore){
						newline += c;
					}
				}
				
			}
			
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
