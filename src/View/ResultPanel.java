package View;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import twitter4j.Status;

import Model.Model;

public class ResultPanel extends JPanel implements Observer{
	protected JTextPane text_pane;
	private Model model;

	public ResultPanel(Model model) {
		super();
		setSize(500,350);
		this.model = model;
		model.addObserver(this);
		text_pane = new JTextPane();
		add(text_pane);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		setBackground(Color.yellow);
		repaint(); 
		revalidate();
	}

	@Override
	public void update(Observable o, Object arg) {
		String content = "";
		for (Status status : model.getResult().getTweets()) {
			content += "@" + status.getUser().getScreenName() + ":" + status.getText() + "\n";
		}
		this.text_pane.setText(content);
		
		repaint();
		revalidate();

	}

}
