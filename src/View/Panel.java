package View;

import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel {

	public Panel(String text) {
		super();
		setLayout(new FlowLayout());
		JLabel textLabel = new JLabel(text);
		add(textLabel);
	}
}
