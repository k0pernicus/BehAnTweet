package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import twitter4j.Status;
import Controler.Tweet;
import Model.Bayes_Model;
import Model.KNN_Model;
import Model.Model;

public class TweetsPanel extends JPanel implements Observer, Scrollable{
	/*
	 * Le model du projet
	 */
	private Model model;

	private ArrayList<Tweet> tweetsList;

	public TweetsPanel(Model model) {
		super();
		this.model = model;
		this.model.addObserver((Observer) this);
		this.tweetsList = new ArrayList<Tweet>();
		//Initialisation du BoxLayout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	@Override
	public void update(Observable o, Object arg) {
		tweetsList.clear();
		String content = "";
		String contentClean = "";
		String contentText = "";
		String classname  = this.model.getClassname();
		String gramme     = this.model.getGramme();
		String nbrLettres = this.model.getNbrLettres();

		if (classname == "KNN_Model") {
			System.out.println("========================");
			System.out.println("KNN");
			System.out.println("========================");
			ArrayList<String> contentArray = new ArrayList<String>();
			ArrayList<String> contentCleanArray = new ArrayList<String>();
			ArrayList<String> contentTweets = new ArrayList<String>();
			model.transformTweet();
			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					contentArray.add(content);
					contentCleanArray.add(contentClean);
					contentTweets.add(contentText);
				}
			}
			int[] groupsTweet = ((KNN_Model) model).getGroups(contentTweets);
			String[] getKNNTweets = ((KNN_Model) model).getEvaluationKNNTweet(contentTweets);
			for (int i = 0; i < contentTweets.size(); i++) {
				tweetsList.add(new Tweet(contentArray.get(i), contentCleanArray.get(i), contentTweets.get(i), getKNNTweets[groupsTweet[i]], "KNN","",""));
			}
		}
		if (classname == "Dict_Model") {
			System.out.println("========================");
			System.out.println("DICT");
			System.out.println("========================");
			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = model.getResultEvaluationDictTweet(contentClean);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Dictionnaire","",""));
				}
			}
		}


		((Bayes_Model) this.model).setBooleanNbrLetters((nbrLettres.equals("toutes les lettres"))?false:true);

		if(gramme.equals("Uni-Bigramme")){
			((Bayes_Model) this.model).setBooleanUnigramme(true);
			((Bayes_Model) this.model).setBooleanBigramme(true);
		}
		else if(gramme.equals("Bigramme")){
			((Bayes_Model) this.model).setBooleanUnigramme(false);
			((Bayes_Model) this.model).setBooleanBigramme(true);
		}
		else{
			((Bayes_Model) this.model).setBooleanUnigramme(true);
			((Bayes_Model) this.model).setBooleanBigramme(false);
		}

		
		
		if (classname == "Bayes_Model_Presence"){

			
			System.out.println("========================");
			System.out.println("BAYES");
			System.out.println("========================");


			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = ((Bayes_Model)model).getEvaluationTweetBayes(contentClean,true);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Bayes_Presence", nbrLettres ,gramme));
				}
			}
		}

		if (classname == "Bayes_Model_Frequence"){

			System.out.println("========================");
			System.out.println("BAYES");
			System.out.println("========================");
			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = ((Bayes_Model)model).getEvaluationTweetBayes(contentClean, false);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Bayes_Frequence", nbrLettres ,gramme));
				}
			}
		}

		this.removeAll();

		for (Tweet tweet : tweetsList) {
			this.add(tweet, BorderLayout.CENTER);
			this.add(tweet);
		}

		repaint();
		revalidate();
	}


	public String[] getTweetList() {
		
		if(tweetsList.isEmpty()){
			System.err.println("aucun tweet detecte");
			//TODO
			throw new NullPointerException();
		}
		
		int size = tweetsList.size();
		String str = "";
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = tweetsList.get(i).toString();
		}
//		for (Tweet tweet : tweetsList) {
//			result[size] = tweet.toString();
//		}
//		str = str.substring(0, str.length()-3);
//		 str.split(";;;");

		return result;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return (orientation == SwingConstants.VERTICAL)
				? visibleRect.height
						: visibleRect.width;
	}

	public boolean getScrollableTracksViewportWidth() {
		if (getParent() instanceof JViewport) {
			return
					(((JViewport) getParent()).getWidth() > getPreferredSize().width);
		}
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		if (getParent() instanceof JViewport) {
			return
					(((JViewport) getParent()).getHeight() > getPreferredSize().height);
		}
		return false;
	}
}
