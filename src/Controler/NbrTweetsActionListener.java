package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Model.Model;

/**
 * 
 * @author antonin
 *
 */
public class NbrTweetsActionListener implements ActionListener {
	
	private Model model;
	
	public NbrTweetsActionListener(Model model){
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox rstComboBox = (JComboBox) e.getSource();
		this.model.setNbrTweets(Integer.parseInt((String) rstComboBox.getSelectedItem()));
	}

}
