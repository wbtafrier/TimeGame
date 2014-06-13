package game.component;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GlassPanel extends JPanel {

	private static final long serialVersionUID = 3441115155876456721L;
	private String timerContent = "3";
	
	public GlassPanel() {
		this.setBackground(Color.BLACK);
	}
	
	public void setTimerContent(String n) {
		this.timerContent = n;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		Rectangle clip = g.getClipBounds();
		
		g2d.setColor(this.getBackground());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AlphaComposite newComp = AlphaComposite.SrcOver.derive(0.3F);
		g2d.setComposite(newComp);
		g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
	}

}
