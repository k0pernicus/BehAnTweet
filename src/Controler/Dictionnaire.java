package Controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Classe Dictionnaire
 * Classe permettant d'instancier un dictionnaire - ensemble de mots ayant un même poids : positif/négatif
 * @author antonin
 */
public class Dictionnaire {

	/**
	 * Le dictionnaire - un ensemble de mots
	 */
	private String[] dictionnaire;
	/**
	 * Le nom de fichier du dictionnaire
	 */
	private String file_name;
	/**
	 * Le poids de chaque mot, contenus dans un dictionnaire
	 */
	private int nb_display;
	
	/**
	 * Constructeur de l'objet Dictionnaire
	 * @param file_name Le chemin vers le fichier à récupérer - tous les mots du fichier seront contenus dans le dictionnaire
	 * @param nb_display Le poids de chaque mot du dictionnaire à instancier
	 */
	public Dictionnaire(String file_name, int nb_display){
		this.file_name = file_name;
		this.nb_display = nb_display;
		/*
		 * Tentative d'ouverture du fichier + parser
		 * Si ok, on trie par ordre lexicographique le dictionnaire
		 */
		try {
			this.dictionnaire = this.parse();
			Arrays.sort(this.dictionnaire);
		}
		/*
		 * IOException -> Problème lors de l'ouverture du fichier - remonté par la méthode parse()
		 */
		catch (IOException e) {
			System.out.println("Il n'y a pas de fichier " + file_name);
			e.printStackTrace();
		}
	}
	
	/**
	 * Méthode permettant de retourner l'attribut dictionnaire
	 * @return L'attribut dictionnaire de l'objet sur lequel on invoque la méthode
	 */
	public String[] getDictionnaire() {
		return this.dictionnaire;
	}
	
	/**
	 * Méthode permettant d'ouvrir et parser le fichier file_name
	 * @return Un tableau de chaîne de caractères - chaînes contenues dans le fichier
	 * @throws IOException Exception renvoyée si le fichier ne peut être lu - file_path n'étant pas correct par exemple
	 */
	public String[] parse() throws IOException {
		String dictionnaire_string = null;
		File dictionnaire_file = new File(file_name);
		BufferedReader in_file;
		/*
		 * On renvoie une exception IOException si le fichier n'est pas trouvé
		 */
		if (!dictionnaire_file.exists()) 
			throw new FileNotFoundException("Le fichier "+dictionnaire_file.getAbsolutePath()+" n'existe pas..."); 
		/*
		 * Sinon, on le parse en stockant dans un tableau de chaînes de caractères chaque chaîne contenue dans le fichier
		 */
		else{
			String read = null;
			in_file = new BufferedReader(new FileReader(dictionnaire_file.getAbsolutePath()));
			while ((read = in_file.readLine()) != null) {
				dictionnaire_string += read;
		    }
			in_file.close();
			return dictionnaire_string.split(",");
		}
	}
	
	/**
	 * Méthode permettant de renvoyer le poids de chaque mot contenu dans le dictionnaire sur lequel on invoque la méthode
	 * @return Le poids de chaque mot contenu dans le dictionnaire
	 */
	public int getNbr() {
		return this.nb_display;
	}
}
