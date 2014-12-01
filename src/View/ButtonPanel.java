package View;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import Controler.SearchActionListener;
import Controler.ValidateActionListener;
import Model.Model;

/**
 * 
 * @author verkyndt
 *
 */
public class ButtonPanel extends JPanel {

	private JButton btnSearch;
	private JButton btnValidate;
	private Model model;

	public ButtonPanel(Model model) {
		super();
		this.model = model;
		
		setLayout(new BorderLayout(0, 0));
		btnSearch = new JButton("Search");
		btnSearch.setName("SearchButton");
		btnSearch.addActionListener(new SearchActionListener(this.model));
		add(btnSearch, BorderLayout.CENTER);
		
		btnValidate = new JButton("Validate");
		btnValidate.setName("ValidateButton");
		btnValidate.addActionListener(new ValidateActionListener(this.model));
		add(btnValidate, BorderLayout.PAGE_END);
	}

	public Model getModel() {
		return model;
	}
	
	public void setSearchButton(boolean b){
		this.btnSearch.setEnabled(b);
	}
	public void setValidateButton(boolean b){
		this.btnValidate.setEnabled(b);
	}
	
	
}
