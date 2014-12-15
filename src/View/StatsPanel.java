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

/**
 * Classe StatsPanel
 * Classe permettant d'instancier un panel de statistiques contenant:
 * - des résultats textes des tweets évalués
 * - des résultats textes des taux d'erreur
 * - un camembert (ou PieChart) permettant la visualisation des résultats
 * @author antonin
 * @author verkyndt
 */
public class StatsPanel extends JPanel implements Observer{

	/**
	 * Modèle du projet
	 */
	private Model model;
	
	/**
	 * Liste de couleurs - pour le PieChart
	 */
	private ArrayList<Color> colors;
	/**
	 * Liste de pourcentages - pour le PieChart
	 */
	private ArrayList<Double> pourcentage;
	
	/**
	 * Objet PieChart
	 */
	private PieChart pie;
	
	/**
	 * Label permettant la visualisation des pourcentages de tweets positifs, négatifs, indéterminés et du taux d'erreur
	 */
	private JLabel  label_positif;
	private JLabel  label_negatif;
	private JLabel  label_indetermine;
	private JLabel  label_taux_erreur;
	
	/**
	 * Conteneurs de texte contenant les poucentages de tweets positifs, négatifs, indéterminés et du taux d'erreur
	 */
	private String text_label_positif;
	private String text_label_negatif;
	private String text_label_indetermine;
	private String text_label_taux_erreur;
	
	/**
	 * Constructeur du panel de statistiques
	 * @param model
	 */
	public StatsPanel(Model model){
		this.model = model;
		this.model.addObserver(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.CYAN);
		colors.add(Color.GRAY);
		
		pourcentage = new ArrayList<Double>();

		/**
		 * Mise-à-jour du camembert
		 */
		maj_Pie();
		
		/**
		 * Ajout du texte
		 */
		text_label_positif =     "Tweets positifs : ";
		text_label_negatif =     "Tweets negatifs : ";
		text_label_indetermine = "Tweets neutres  : ";
		text_label_taux_erreur = "Taux d'erreur   : ";
		
		/**
		 * Ajout des labels dans la GUI
		 */
		label_positif = new JLabel(text_label_positif);
		label_negatif = new JLabel(text_label_negatif);
		label_indetermine = new JLabel(text_label_indetermine);
		label_taux_erreur = new JLabel(text_label_taux_erreur);
		this.add(label_positif);
		this.add(label_negatif);
		this.add(label_indetermine);
		this.add(new JLabel("    "));
		this.add(new JLabel("    "));
		this.add(label_taux_erreur);
		this.add(new JLabel("    "));
		this.add(new JLabel("    "));
		
		/**
		 * Ajout de la légende du camembert
		 */
		this.add(new JLabel("Positifs")).setForeground(Color.RED);
		this.add(new JLabel("Negatifs")).setForeground(Color.CYAN);
		this.add(new JLabel("Neutres ")).setForeground(Color.GRAY);
		
		/**
		 * Ajout du camembert
		 */
		this.add(pie);
	}
	
	/**
	 * Méthode permettant la mise-à-jour du camembert
	 * Cette mise-à-jour consistera au nouveau calcul des tweets positifs, négatifs et indéterminés parmis ceux recueillis
	 * Ce calcul permettra alors de donner au PieChart les nouveaux calculs permettant de tracer celui-ci
	 */
	private void maj_Pie(){
		pourcentage.clear();
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("positifs")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("negatifs")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pourcentage.add((double)((Validation_Model)model).getNbrTweets("indetermines")*100/ ((Validation_Model)model).getNbrTweets("au total"));
		pie = new PieChart(pourcentage,colors);
	}

	@Override
	public void update(Observable o, Object arg) {
		/*
		 * Ajout du flux texte contenant le nombre de tweets positifs, négatifs et indéterminés
		 */
		float nbrTweetTotal = ((Validation_Model)model).getNbrTweets("au total");
		label_positif.setText(text_label_positif + ((Validation_Model)model).getNbrTweets("positifs") *100/ nbrTweetTotal + "%"); 
		label_negatif.setText(text_label_negatif + ((Validation_Model)model).getNbrTweets("negatifs")*100/ nbrTweetTotal + "%");
		label_indetermine.setText(text_label_indetermine + ((Validation_Model)model).getNbrTweets("indetermines")*100/ nbrTweetTotal + "%");
		/*
		 * Ajout du taux d'erreur
		 */
		label_taux_erreur.setText(text_label_taux_erreur +  ((Validation_Model)model).getTauxErreur() + "%");
		/*
		 * Mise-à-jour du camembert
		 */
		maj_Pie();
		repaint();
		revalidate();	
	}
	
	
}
