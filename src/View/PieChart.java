package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * Classe PieChart
 * Classe permettant d'implémenter un PieChart - encore appelé camembert - pour la visualisation des statistiques
 * @author verkyndt
 */
public class PieChart extends JPanel {

	/**
	 * Énumération des types de PieChart
	 */
	enum Type {
		STANDARD, SIMPLE_INDICATOR, GRADED_INDICATOR
	}

	/**
	 * Le type de PieChart
	 */
	private Type type = null;

	/**
	 * Valeurs des parties du PieChart
	 */
    private ArrayList<Double> values;
    /**
     * Couleurs des parties du PieChart
     */
    private ArrayList<Color> colors;
    /**
     * Valeurs de notation du PieChart
     */
    private ArrayList<Double> gradingValues;
    /**
     * Couleurs de notation du PieChart
     */
    private ArrayList<Color> gradingColors;

    /**
     * Attribué utilisé pour le SIMPLE_INDICATOR et le GRADED_INDICATOR
     */
	double percent = 0;

	/**
	 * Constructeur PieChart
	 * @param percent Pourcentage utilisé pour le SIMPLE_INDICATOR
	 */
	public PieChart(int percent) {

		type = Type.SIMPLE_INDICATOR;
		this.percent = percent;
	}

	/**
	 * Constructeur PieChart
	 * @param values Valeurs utilisées pour le PieChart
	 * @param colors Couleurs utilisées pour le PieChart
	 */
	public PieChart(ArrayList<Double> values, ArrayList<Color> colors) {
		
		type = Type.STANDARD;

		this.values = values;
		this.colors = colors;
		
		setSize( 600, 400 );
	    setLocation( 70, 70 ) ;
	     
	}

	/**
	 * Constructeur PieChart
	 * @param percent Pourcentage utilisé pour le GRADED_INDICATOR
	 * @param gradingValues Valeurs de notation
	 * @param gradingColors Couleurs de notation
	 */
	public PieChart(int percent, ArrayList<Double> gradingValues, ArrayList<Color> gradingColors) {
		type = Type.GRADED_INDICATOR;

		this.gradingValues = gradingValues;
		this.gradingColors = gradingColors;
		this.percent = percent;

	}

	@Override
	protected void paintComponent(Graphics g) {

		int width = getSize().width;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (type == Type.SIMPLE_INDICATOR) {

			//Couleur utilisées pour le SIMPLE_INDICATOR
			Color backgroundColor = Color.WHITE;
			Color mainColor = Color.BLUE;

			g2d.setColor(backgroundColor);
			g2d.fillOval(0, 0, width, width);
			g2d.setColor(mainColor);
			Double angle = (percent / 100) * 360;
			g2d.fillArc(0, 0, width, width, -270, -angle.intValue());

		} else if (type == Type.STANDARD) {

			int lastPoint = -270;

			for (int i = 0; i < values.size(); i++) {
				g2d.setColor(colors.get(i));

				Double val = values.get(i);
				Double angle = (val / 100) * 360;

				g2d.fillArc(0, 0, width, width, lastPoint, -angle.intValue());

				lastPoint = lastPoint + -angle.intValue();
			}
		} else if (type == Type.GRADED_INDICATOR) {

			int lastPoint = -270;

			double gradingAccum = 0;

			for (int i = 0; i < gradingValues.size(); i++) { 				
				    g2d.setColor(gradingColors.get(i)); 				 				
				    Double val = gradingValues.get(i); 				
				    gradingAccum = gradingAccum + val; 				 				
				    Double angle = null; 				 				
				    /** 				 
				     * * If the sum of the gradings is greater than the percent, then we want to recalculate 				
				     * * the last wedge, and break out of drawing. 				 */ 				 				
				    if (gradingAccum > percent) {


					//Obtention des segments précédemment accumulés
					double gradingAccumMinusOneSegment = gradingAccum - val;

					angle = ((percent - gradingAccumMinusOneSegment) / 100) * 360;

					g2d.fillArc(0, 0, width, width, lastPoint, -angle.intValue());

					lastPoint = lastPoint + -angle.intValue();

					break;

				}else {

					angle = (val / 100) * 360;

					g2d.fillArc(0, 0, width, width, lastPoint, -angle.intValue());


					lastPoint = lastPoint + -angle.intValue();
				}
			}
		}
	}
}