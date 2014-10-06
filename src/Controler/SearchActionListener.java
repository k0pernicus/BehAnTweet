package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;

import Model.Model;
import View.ResultPanel;

public class SearchActionListener implements ActionListener {

	private Model model;

	public SearchActionListener(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton sButton = (JButton) e.getSource();	
		ResultPanel parent = (ResultPanel) sButton.getParent();
		try {
			model.run(parent.getSearchText());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}

}
