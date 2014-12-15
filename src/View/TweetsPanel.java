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
import Model.Dict_Model;
import Model.Model;

/**
 * Classe TweetsPanel
 * Panel contenant l'ensemble des tweets résultants de la recherche utilisateur
 * @author antonin
 * @author verkyndt
 */
public class TweetsPanel extends JPanel implements Observer, Scrollable{

	/**
	 * Le modèle du projet
	 */
	private Model model;

	/**
	 * La liste des tweets résultants de la recherche utilisateur
	 */
	private ArrayList<Tweet> tweetsList;

	/**
	 * Constructeur de l'objet TweetsPanel
	 * @param model Le modèle du projet
	 */
	public TweetsPanel(Model model) {
		super();

		/*
		 * Relation Modèle-objet
		 */
		this.model = model;
		this.model.addObserver((Observer) this);

		this.tweetsList = new ArrayList<Tweet>();

		//Initialisation du BoxLayout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}

	@Override
	/**
	 * Méthode permettant de mettre à jour l'objet sur lequel on invoque la méthode
	 * Cette méthode, en fonction de la méthode de classification, évaluera les tweets différemment
	 */
	public void update(Observable o, Object arg) {
		System.out.println(model.getIsValidate());
		/* on évite de vider la liste de tweet et la recalculer lors de la validation*/
		if(model.getIsValidate()){
			model.setIsValidate(false);
			return;
		}
		System.out.println(model.getIsValidate());
		tweetsList.clear();
		String content = "";
		String contentClean = "";
		String contentText = "";
		String classname  = this.model.getClassname();
		String gramme     = this.model.getGramme();
		String nbrLettres = this.model.getNbrLettres();

		/*
		 * En fonction de la méthode de classification, on évaluera les tweets différemment
		 */

		/*
		 * Méthode KNN
		 */
		if (classname == "KNN_Model") {
			System.out.println("========================");
			System.out.println("KNN");
			System.out.println("========================");
			ArrayList<String> contentArray = new ArrayList<String>();
			ArrayList<String> contentCleanArray = new ArrayList<String>();
			ArrayList<String> contentTweets = new ArrayList<String>();
			/*
			 * Pour tous les status, on récupère le tweet en entier, on le nettoie et on supprime les retweets
			 */
			for (Status status : this.model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ').replace(';', ',')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = this.model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					contentArray.add(content);
					contentCleanArray.add(contentClean);
					contentTweets.add(contentText);
				}
			}
			for (int i = 0; i < contentTweets.size(); i++) {
				String tweet_i = contentTweets.get(i);
				/*
				 * Récupération de l'avis du tweet
				 */
				int int_avis = ((KNN_Model) this.model).getEvaluationKNNTweet(tweet_i);
				/*
				 * Transformation en chaîne de caractères
				 */
				String avis = ((KNN_Model) this.model).getEvaluationByResult(int_avis);
				tweetsList.add(new Tweet(contentArray.get(i), contentCleanArray.get(i), contentTweets.get(i), avis, "KNN","",""));
			}
		}

		/*
		 * Méthode Dictionnaire
		 */
		if (classname == "Dict_Model") {
			System.out.println("========================");
			System.out.println("DICT");
			System.out.println("========================");
			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ').replace(';', ',')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = ((Dict_Model) this.model).getResultEvaluationDictTweet(contentClean);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Dictionnaire","",""));
				}
			}
		}

		/*
		 * Méthode Bayes
		 */

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
			System.out.println("BAYES PRESENCE");
			System.out.println("========================");
			((Bayes_Model)this.model).setBooleanIsPresense(true);
			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ').replace(';', ',')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = ((Bayes_Model)model).getEvaluationTweetBayes(contentClean);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Bayes_Presence", nbrLettres ,gramme));
				}
			}
		}
		if (classname == "Bayes_Model_Frequence"){
			System.out.println("========================");
			System.out.println("BAYES FREQUENCE");
			System.out.println("========================");
			((Bayes_Model)this.model).setBooleanIsPresense(false);

			for (Status status : model.getResult().getTweets()) {
				content = status.getId() + ";" + status.getUser().getScreenName() + ";\"" + status.getText().replace('\"', '\'').replace('\n', ' ').replace(';', ',')+" \";" + status.getCreatedAt() + ";" + model.getResult().getQuery();
				contentClean = model.cleanTweet(content);
				contentText = status.getText().replace('\n', ' ');
				if(!contentClean.equals("RT")) {
					String eval = ((Bayes_Model)model).getEvaluationTweetBayes(contentClean);
					tweetsList.add(new Tweet(content, contentClean, contentText, eval, "Bayes_Frequence", nbrLettres ,gramme));
				}
			}
		}
		this.removeAll();
		/*
		 * Positionnement au centre de l'application
		 */
		for (Tweet tweet : tweetsList) {
			//this.add(tweet, BorderLayout.CENTER);
			this.add(tweet);
		}
		repaint();
		revalidate();
	}

	/**
	 * Méthode permettant de renvoyer la liste de tweets
	 * @return Un tableau de chaînes de caractères contenant la liste de tweets
	 */
	public String[] getTweetList() {
		/*
		 * Si la liste de tweets est nulle, on renvoie une exception
		 */
		if(tweetsList.isEmpty()){
			System.err.println("aucun tweet detecte");
			throw new NullPointerException();
		}
		/*
		 * Sinon, on remplit un tableau selon tous les tweets recueillis
		 */
		int size = tweetsList.size();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = tweetsList.get(i).toString();
		}
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
