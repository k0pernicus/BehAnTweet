package Controler;

import java.io.File;

/**
 * Classe permettant de récupérer des informations sur un fichier CSV
 */
public class CsvFile {

	/**
	 * Méthode permettant de récupérer le chemin du fichier
	 * @param fileName Le nom du fichier
	 * @return Le chemin du fichier entré en paramètre
	 */
    public static String getResourcePath(String fileName) {
       final String dossierPath = "../resources/" + fileName;
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
