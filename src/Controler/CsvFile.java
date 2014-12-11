package Controler;

import java.io.File;

/**
 * Classe CsvFile
 * Classe permettant de récupérer des informations d'un fichier CSV
 * @author antonin
 */
public class CsvFile {
	
	/**
	 * Constante valant le chemin vers les fichiers ressources du programme
	 * La constante pointe vers le répertoire 'resources'
	 */
	final static String PATH = "../resources/";

	/**
	 * Méthode permettant de récupérer le chemin du fichier, dont le nom est passé en paramètre
	 * @param fileName Le nom du fichier
	 * @return Le chemin du fichier entré en paramètre
	 */
    public static String getResourcePath(String fileName) {
       final String dossierPath = PATH + fileName;
       return dossierPath;
   }

   /**
    * Méthode permettant de retourner un fichier
    * @param fileName Le nom du fichier à retourner
    * @return Un fichier dont le nom est prit en paramètre
    */
   public static File getResource(String fileName) {
       final String completeFileName = getResourcePath(fileName);
       File file = new File(completeFileName);
       return file;
   }
}
