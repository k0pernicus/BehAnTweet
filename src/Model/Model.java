package Model;

import java.util.Observable;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Model extends Observable {

	private QueryResult result;
	
	public void run(String request){
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query(request);
		try {
			result = twitter.search(query);
			updateObservers();
			
		    for (Status status : result.getTweets()) {
		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
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
	
}
