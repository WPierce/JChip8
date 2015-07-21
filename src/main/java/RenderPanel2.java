import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class RenderPanel2 extends JPanel {
	
	private Display display;
	
	public RenderPanel2(Display display){
		this.display = display;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		this.setBackground(Color.BLACK);
		
		
		
		for (int i = 0; i < display.getDispX(); i++) {
			for (int j = 0; j < display.getDispY(); j++) {
				int pixel = display.getValue(i, j);
				
				if (pixel > 0) {
					g.setColor(Color.WHITE);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(i*10, j*10, 10, 10);
			}//for
		}//for
	}//paintComponent
}//RenderPanel
