package View;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.Model;
import Model.Validation_Model;

public class StatsPanel extends JPanel implements Observer{

	private Model model;
	
	private ArrayList<Color> colors;
	private ArrayList<Double> pourcentage;
	
	private PieChart pie;
	
	
	private JLabel  label_positif;
	private JLabel  label_negatif;
	private JLabel  label_indetermine;
	private JLabel  label_taux_erreur;
	
	private String text_label_positif;
	private String text_label_negatif;
	private String text_label_indetermine;
	private String text_label_taux_erreur;
	
	
	public StatsPanel(Model model){
		this.model = model;
		this.model.addObserver(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.CYAN);
		colors.add(Color.GRAY);
		
		pourcentage = new ArrayList<Double>();
		
		maj_Pie();
		
		
	
		
		
		text_label_positif =     "Tweets positifs : ";
		text_label_negatif =     "Tweets negatifs : ";
		text_label_indetermine = "Tweets neutres  : ";
		text_label_taux_erreur = "Taux d'erreur   : ";
		
		label_positif = new JLabel(text_label_positif);
		label_negatif = new JLabel(text_label_negatif);
		label_indetermine = new JLabel(text_label_indetermine);
		label_taux_erreur = new JLabel(text_label_taux_erreur);
//		this.add(new JLabel("Tweets positifs : Rouge"));
		this.add(label_positif);
		this.add(label_negatif);
		this.add(label_indetermine);
		this.add(new JLabel("    "));
		this.add(new JLabel("    "));
		this.add(label_taux_erreur);
		this.add(new JLabel("    "));
		this.add(new JLabel("    "));
		this.add(pie);
	}
	
	private void maj_Pie(){
		pourcentage.clear();
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("positifs")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("negatifs")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("indetermines")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pie = new PieChart(pourcentage,colors);
	}

	@Override
	public void update(Observable o, Object arg) {
		float nbrTweetTotal = ((Validation_Model)model).getNbrTweets("au total");
		label_positif.setText(text_label_positif + ((Validation_Model)model).getNbrTweets("positifs") *100/ nbrTweetTotal + "%"); 
		label_negatif.setText(text_label_negatif + ((Validation_Model)model).getNbrTweets("negatifs")*100/ nbrTweetTotal + "%");
		label_indetermine.setText(text_label_indetermine + ((Validation_Model)model).getNbrTweets("indetermines")*100/ nbrTweetTotal + "%");
		
		label_taux_erreur.setText(text_label_taux_erreur +  ((Validation_Model)model).getTauxErreur() + "%");
		maj_Pie();
		repaint();
		revalidate();
		
	}
	
	
}
