package com.game.gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.game.Configuration;
import com.game.Display;
import com.game.RunGame;
import com.game.graphics.Texture;
import com.game.input.InputHandler;

public class Launcher extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	protected JPanel window = new JPanel();
	private JButton play, options, help, quit;
	private Rectangle rplay, roptions, rhelp, rquit;

	public static Configuration config = new Configuration();

	private int width = 800; // 420
	private int height = 400; // 480
	protected int button_width = 120; // 120
	protected int button_height = 20; // 20

	public static boolean running = false;
	public static Thread thread;

	public Launcher(int id, Display display) {

		add(display);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setUndecorated(true);
		setTitle("Launcher");
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// getContentPane().add(window);
		// add(this);
		setLocationRelativeTo(null);
		setResizable(false);
		window.setLayout(null);

		if (id == 0) {
			drawButtons();
		}

		display.start();
		setVisible(true);
		repaint();
		startMenu();
	}

	public void startMenu() {
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}

	public void stopMenu() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() { /////////// MAIN RUN METHOD
		// main method
		requestFocus();
		while (running) {
			updateFrame();
		}
	}

	public void updateFrame() {  // movement of the window
		// updates overall window
		if (InputHandler.dragged) {   // needs fixing/ URGENT/ window moving is very laggy
			Point p = getLocation();
			setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX,
					p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}

	public static void openGame() {
		config.loadConfig("res/settings/config.xml");
		//dispose(); // closes the launcher
		new RunGame();
	}
	
	private void drawButtons() {
		play = new JButton("Play");
		rplay = new Rectangle((width / 2) - (button_width / 2), 100, button_width, button_height);
		play.setBounds(rplay);
		window.add(play);
		play.setVisible(true);

		options = new JButton("Options");
		roptions = new Rectangle((width / 2) - (button_width / 2), 150, button_width, button_height);
		options.setBounds(roptions);
		window.add(options);
		options.setVisible(true);

		help = new JButton("Help");
		rhelp = new Rectangle((width / 2) - (button_width / 2), 200, button_width, button_height);
		help.setBounds(rhelp);
		window.add(help);
		help.setVisible(true);

		quit = new JButton("Quit");
		rquit = new Rectangle((width / 2) - (button_width / 2), 250, button_width, button_height);
		quit.setBounds(rquit);
		window.add(quit);
		quit.setVisible(true);

		// action listeners
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfig("res/settings/config.xml");
				dispose(); // closes the launcher
				new RunGame();
			}
		});

		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Options();
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
