package Controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionnaire {

	private String[] dictionnaire;
	private String file_name;
	private int nb_display;
	
	public Dictionnaire(String file_name, int nb_display){
		this.file_name = file_name;
		this.nb_display = nb_display;
		try {
			this.dictionnaire = this.parse();
		} catch (IOException e) {
			System.out.println("There is no file named " + file_name);
			e.printStackTrace();
		}
	}
	
	public String[] getDictionnaire() {
		return this.dictionnaire;
	}
	
	public String[] parse() throws IOException {
		String dictionnaire_string = null;
		File dictionnaire_file = new File(file_name);
		BufferedReader in_file;
		if (!dictionnaire_file.exists()) 
			throw new FileNotFoundException("Le fichier "+dictionnaire_file.getAbsolutePath()+" n'existe pas..."); 
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
	
}
