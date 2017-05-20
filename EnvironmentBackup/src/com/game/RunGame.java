package com.game;

import java.awt.*;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.image.*;

public class RunGame {
	
	public RunGame() {
		
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor crosshair = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "crosshair");
		Display game = new Display();
		JFrame frame = new JFrame();
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize( Display.getGameWidth(), Display.getGameHeight() );
		frame.getContentPane().setCursor(crosshair);
		frame.setTitle(Display.TITLE);
		//frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		game.start();
		
	}
}
