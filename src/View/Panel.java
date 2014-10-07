package View;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Panel extends JPanel {

	public Panel(String text) {
		super();
		setLayout(new FlowLayout());
		JLabel textLabel = new JLabel(text);
		add(textLabel);
	}
}
