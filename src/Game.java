import game.component.GlassPanel;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class Game
{
	
	public void buttonDoClick(ActionEvent e, JButton jb) {
		
		if (e != null && jb != null && !e.getActionCommand().equals(jb.getActionCommand())) {
			jb.doClick();
		}
	}
	
	private Action beginAction = new AbstractAction() {

		private static final long serialVersionUID = 696761829399357588L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			buttonDoClick(e, begin);
			
			difficulty = Integer.parseInt(diffButtons.getSelection().getActionCommand());
			
			intro.setVisible(false);
			game.setLocationRelativeTo(null);
			game.setVisible(true);
			game.getGlassPane().setVisible(true);
			startTimer.start();
		}
		
	};
	
	
	private Action verifyAction = new AbstractAction() {

		private static final long serialVersionUID = 5661618678712090033L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			buttonDoClick(e, verify);
			
			inputField.setText("");
			
			if (intro.getFocusOwner() == null || !intro.getFocusOwner().equals(inputField)) {
				inputField.requestFocus();
			}
		}
		
	};
	
	private Action startAction = new AbstractAction() {
		private static final long serialVersionUID = 1993742061665390391L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (startSecondsLeft > 1) {
				glassTimer.setText(Integer.toString(--startSecondsLeft));
			}
			else if (startSecondsLeft == 1) {
				glassTimer.setText("GO!!!");
				--startSecondsLeft;
			}
			else {
				startTimer.stop();
				game.getGlassPane().setVisible(false);
				if (!secondsLeftTimer.isRunning()) {
					secondsLeftTimer.start();
				}
			}
		}
	};
	
	private Action timerAction = new AbstractAction() {

		private static final long serialVersionUID = 1993742061665390391L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int newTime = --secondsLeft;
			gameTimer.setText(Integer.toString(newTime));
			if (newTime <= 0) {
				if (secondsLeftTimer.isRunning()) {
					secondsLeftTimer.stop();
				}
				verify.setEnabled(false);
				inputField.setEnabled(false);
				if (gameOptions.getComponentCount() > 1) {
					gameOptions.removeAll();
					gameOptions.revalidate();
					gameOptions.repaint();
					gameOptions.add(results);
				}
				letter.setText("");
				category.setText("");
				gameOverTimer.start();
			}
		}
	};
	
	private Action gameOverAction = new AbstractAction() {
		
		private static final long serialVersionUID = 1923158332655487869L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (timesDisplayed % 2 == 0) {
				Font oldFont = category.getFont();
				Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize());
				category.setFont(newFont);
				category.setForeground(Color.RED);
				category.setText("TIME");
			}
			else {
				category.setText("");
			}
			timesDisplayed++;
		}
	};
	
	private Action pauseAction = new AbstractAction() {

		private static final long serialVersionUID = 1923158332655487869L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (secondsLeftTimer.isRunning()) {
				verify.setEnabled(false);
				inputField.setEnabled(false);
				skip.setEnabled(false);
				
				savedCat = category.getText();
				savedLetter = letter.getText();
				
				category.setText("");
				letter.setText("");
				pause.setText("Unpause");
				
				secondsLeftTimer.stop();
			}
			else if (secondsLeft > 0) {
				verify.setEnabled(true);
				inputField.setEnabled(true);
				skip.setEnabled(true);
				
				category.setText(savedCat);
				letter.setText(savedLetter);
				pause.setText("Pause");
				
				gameTimer.setText(Integer.toString(--secondsLeft));
				secondsLeftTimer.start();
			}
		}
	};
	
	private Timer startTimer = new Timer(1000, startAction);
	private Timer secondsLeftTimer = new Timer(10, timerAction);
	private Timer gameOverTimer = new Timer(1000, gameOverAction);
	
	private JFrame intro = new JFrame("TimeGame");
	private JPanel introMain = new JPanel();
	private JPanel introImage = new JPanel();
	private JPanel introOptions = new JPanel();
	private JLabel logo = new JLabel();
	private JLabel welcome = new JLabel("<html>Welcome to TimeGame! You have TimeGame to name as many objects as you can for each category.<br>However, whatever you type must begin with a specific letter! Select your difficulty below. Harder difficulty means harder letters. Good luck!</html>");
	private JLabel diff = new JLabel("Select your difficulty:");
	private JRadioButton easy = new JRadioButton("Easy");
	private JRadioButton medium = new JRadioButton("Medium");
	private JRadioButton hard = new JRadioButton("Hard");
	private ButtonGroup diffButtons = new ButtonGroup();
	private JButton begin = new JButton(this.beginAction);
	
	private JFrame game = new JFrame("TimeGame");
	private JPanel gameGlass = new GlassPanel();
	private JPanel gameMain = new JPanel();
	private JPanel gameInfo = new JPanel();
	private JPanel gameInput = new JPanel();
	private JPanel gameOptions = new JPanel();
	private JLabel gameTimer = new JLabel("94");
	private JLabel glassTimer = new JLabel("3");
	private JLabel category = new JLabel("Category");
	private JLabel letter = new JLabel("A");
	private JTextField inputField = new JTextField(20);
	private JButton verify = new JButton(verifyAction);
	private JButton pause = new JButton(pauseAction);
	private JButton quit = new JButton("Quit");
	private JButton skip = new JButton("Skip");
	private JButton results = new JButton("See results");
	
	private int difficulty = 2;
	private int startSecondsLeft = Integer.parseInt(glassTimer.getText());
	private int secondsLeft = Integer.parseInt(gameTimer.getText());
	private int timesDisplayed = 0;
	
	private String savedCat = null;
	private String savedLetter = null;
	
	public Game()
	{
		//Intro Frame start
		introMain.setLayout(new BoxLayout(introMain, BoxLayout.X_AXIS));
		
		logo.setIcon(new ImageIcon(Game.class.getResource("logo.png")));
		
		introImage.add(logo);
		
		introMain.add(introImage);
		
		introOptions.setLayout(new GridLayout(0, 1));
		introOptions.add(welcome);
		introOptions.add(diff);
		
		easy.setMnemonic(KeyEvent.VK_E);
		easy.setActionCommand("1");
		
		medium.setMnemonic(KeyEvent.VK_M);
		medium.setActionCommand("2");
		medium.setSelected(true);
		
		hard.setMnemonic(KeyEvent.VK_H);
		hard.setActionCommand("3");
		
		diffButtons.add(easy);
		diffButtons.add(medium);
		diffButtons.add(hard);
		
		introOptions.add(easy);
		introOptions.add(medium);
		introOptions.add(hard);
		
		begin.setActionCommand("begin");
		begin.setMnemonic(KeyEvent.VK_B);
		begin.setText("Begin");
		begin.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "begin_bind");
		begin.getActionMap().put("begin_bind", begin.getAction());
		
		introOptions.add(begin);
		
		introMain.add(introOptions);
		introMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		intro.setResizable(false);
		intro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		intro.add(introMain);
		intro.pack();
		intro.setLocationRelativeTo(null);
		intro.setVisible(true);
		
		//Intro Frame end
		//Game Frame start
		
		gameMain.setLayout(new BoxLayout(gameMain, BoxLayout.Y_AXIS));
		
		gameInfo.setLayout(new BorderLayout());
		
		gameTimer.setFont(new Font("Dialog", Font.BOLD, 26));
		gameTimer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
		
		gameInfo.add(gameTimer, BorderLayout.LINE_START);
		
		category.setFont(new Font("Dialog", Font.PLAIN, 26));
		category.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		
		gameInfo.add(category, BorderLayout.CENTER);
		
		letter.setFont(new Font("Dialog", Font.PLAIN, 26));
		letter.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		
		gameInfo.add(letter, BorderLayout.LINE_END);
		
		gameMain.add(gameInfo);
		
		gameInput.add(inputField);
		
		verify.setActionCommand("verify");
		verify.setMnemonic(KeyEvent.VK_V);
		verify.setText("Verify");
		verify.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "verify_bind");
		verify.getActionMap().put("verify_bind", verify.getAction());
		
		gameInput.add(verify);
		
		gameMain.add(gameInput);
		
		pause.setActionCommand("pause");
		pause.setMnemonic(KeyEvent.VK_P);
		pause.setText("Pause");
		
		gameOptions.add(pause);
		gameOptions.add(quit);
		gameOptions.add(skip);
		
		gameMain.add(gameOptions);
		gameMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		game.setResizable(false);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.add(gameMain);
		
		Font glassFont = new Font(glassTimer.getFont().getName(), Font.BOLD, 64);
		glassTimer.setFont(glassFont);
		glassTimer.setForeground(Color.DARK_GRAY);
		gameGlass.add(glassTimer);
		
		game.setGlassPane(gameGlass);
		
		game.pack();
		//Game Frame end
	}
}
