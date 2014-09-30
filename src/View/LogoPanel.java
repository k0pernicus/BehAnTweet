package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel{
	
	private Image img;
	
	public LogoPanel() {
		super();
		setSize(1000, 1000);
		try {
			img = ImageIO.read(new File ("./images/Alpha_Bahamut_LOGO_PROTO2.jpg"));
		} catch (IOException e) {
			//pas besoin de géré cette exception car cela fonctionnera toujours
			//puisque le dossier images sera toujours présent avec les fichers nécessaire
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		setBackground(Color.CYAN);
		g.drawImage(img,0,0,getWidth(), getHeight(),this);	
	}
}
