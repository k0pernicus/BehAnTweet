package Controler;

public class NegativeDictionnaire extends Dictionnaire {

	private int nb_display;
	private String file_name_pr = "src/resources/negative.txt";
	
	public NegativeDictionnaire() {
		super();
		nb_display = -1;
		this.file_name = file_name_pr;
	}

}
