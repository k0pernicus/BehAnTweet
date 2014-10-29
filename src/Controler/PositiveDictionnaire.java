package Controler;

public class PositiveDictionnaire extends Dictionnaire {

	private int nb_display;
	private String file_name_pr = "src/resources/positive.txt";
	
	public PositiveDictionnaire() {
		super();
		nb_display = 1;
		this.file_name = file_name_pr;
	}

}
