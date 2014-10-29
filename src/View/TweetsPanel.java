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
		for (Status status : model.getResult().getTweets()) {
			content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
			contentClean = model.cleanTweet(content);
			contentText = '@' + status.getText().replace('\n', ' ');
			if(!contentClean.equals("RT")) {
				int eval = model.getEvaluationTweet(contentClean);
				tweetsList.add(new Tweet(content, contentClean, contentText, eval));
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
		String str = "";
		for (Tweet tweet : tweetsList) {
			str += tweet.toString() + ";;;";
		}
		str = str.substring(0, str.length()-3);
		String[] result = str.split(";;;");

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
