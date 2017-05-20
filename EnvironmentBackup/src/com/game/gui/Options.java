package com.game.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.game.Display;

public class Options extends Launcher {
	private static final long serialVersionUID = 1L;	
	
	private int width = 540; // 540
	private int height = 440; // 440
	private JButton OK;
	private Rectangle rOK, rResolution;
	private JTextField twidth, theight;
	private JLabel lwidth, lheight;
	private Choice resolution;
	
	int w = 0, h = 0;
	
	public Options() {
		
		super(1, new Display());
		setTitle("Options - Launcher");
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		
		drawButtons();
	}
	
	private void drawButtons() {
		
		OK = new JButton("OK");
		rOK = new Rectangle((width - 145), (height-60), button_width, button_height);
		OK.setBounds(rOK);
		window.add(OK);
		
		resolution = new Choice();
		rResolution = new Rectangle(50, 80, 150, 28);
		resolution.setBounds(rResolution);
		resolution.add("640x480");
		resolution.add("800x600");
		resolution.add("1200x700");
		resolution.select(1);
		window.add(resolution);
		
		twidth = new JTextField();  // custom width
		twidth.setBounds(140, 150, 160, 20);
		window.add(twidth);
		
		lwidth = new JLabel("Custom Width:");
		lwidth.setBounds(30, 150, 120, 20);
		lwidth.setFont( new Font("Verdana", 2, 14) );
		window.add(lwidth);
		
		theight = new JTextField(); // custom height
		theight.setBounds(144, 170, 156, 20);
		window.add(theight);
		
		lheight = new JLabel("Custom Height:");
		lheight.setBounds(30, 170, 120, 20);
		lheight.setFont( new Font("Verdana", 2, 14) );
		window.add(lheight);
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Launcher(0, new Display());
				config.saveConfig( "width", parseWidth() ); // saving width to config.xml
				config.saveConfig( "height", parseHeight() ); // saving height to config.xml
			}
		});
		
	}
	
	private void drop() {
		
		int selection = resolution.getSelectedIndex();
		
		if(selection == 0) {
			w = 640;
			h = 480;
		}
		if(selection == 1 || selection == -1) {
			w = 800;
			h = 600;
		}
		if(selection == 2) {
			w = 1200;
			h = 700;
		}
		
		config.saveConfig("width", w); // saving width to config.xml
		config.saveConfig("height", h); // saving height to config.xml
		dispose();
		new Launcher(0, new Display());
		
	}
	
	public int parseWidth() {
		try {
		int w = Integer.parseInt( twidth.getText() );
		return w;
		} catch (NumberFormatException e) {
			drop();
			return w;
		}
	}
	
	public int parseHeight() {
		try {
		int h = Integer.parseInt( theight.getText() );
		return h;
		} catch (NumberFormatException e) {
			drop();
			return h;
		}
	}
	
}
