package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import twitter4j.Status;

import Model.Model;

public class ResultPanel extends JPanel implements Observer{
	
	protected JTextPane text_pane;
	private JTextField textField;
	private Model model;
	private JButton btnSearch;

	public ResultPanel(Model model) {
		super();
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(0, 0));
		this.model = model;
		model.addObserver(this);
		text_pane = new JTextPane();
		add(text_pane, BorderLayout.CENTER);
		text_pane.setEditable(false);
		textField = new JTextField();
		add(textField, BorderLayout.NORTH);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		add(btnSearch, BorderLayout.EAST);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
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