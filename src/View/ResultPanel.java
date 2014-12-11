package View;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import Model.Model;

public class ResultPanel extends JPanel implements Observer {

	protected TweetsPanel tweets_panel;
	
	protected StatPanel stat_panel;
	
	protected Model model;
	
	public ResultPanel(Model model) {
		super();
		
		this.model = model;
		this.model.addObserver((Observer) this);
		
		this.tweets_panel = new TweetsPanel(model);
		add(this.tweets_panel, BorderLayout.WEST);
		
		this.stat_panel = new StatPanel(model);
		this.stat_panel.setVisible(false);
		add(this.stat_panel, BorderLayout.EAST);
		
	}
	
	public void setVisibilityStatPanel(boolean bool) {
		this.stat_panel.setVisible(bool);
	}
	
	public String[] getTweetList() throws NullPointerException {
		return this.tweets_panel.getTweetList();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
