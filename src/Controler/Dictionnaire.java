package Controler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

abstract class Dictionnaire {

	protected String[] dictionnaire;
	protected String file_name;
	
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
