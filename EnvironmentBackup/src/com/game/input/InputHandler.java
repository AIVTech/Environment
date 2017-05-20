package com.game.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
	
	public boolean[] key = new boolean[68836];
	public static int MouseX, MouseY, MouseButton;
	public static int MouseDX, MouseDY; // d = drag
	public static int MousePX, MousePY; // p = pressed; mouse pressed coordinates
	public static boolean dragged = false;
	//////////
	
	public void mouseDragged(MouseEvent e) {
		// mouse dragged
		MouseDX = e.getX();
		MouseDY = e.getY();
		
		MouseButton = e.getButton();
	}

	public void mouseMoved(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
		
		MouseButton = e.getButton();
	}

	public void mouseClicked(MouseEvent e) {
		MouseButton = e.getButton();
	}

	public void mousePressed(MouseEvent e) {
		// mouse pressed
		MousePX = e.getX();
		MousePY = e.getY();
		dragged = true;
		
		MouseButton = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		dragged = false;
		MouseButton = 0;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void focusGained(FocusEvent e) {
		
	}

	public void focusLost(FocusEvent e) {
		// to stop moving when focus is lost over the program window
		for ( int i = 0; i < key.length; i++ ) {
			key[i] = false;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if( keyCode > 0 && keyCode < key.length ) {
			key[keyCode] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if( keyCode > 0 && keyCode < key.length ) {
			key[keyCode] = false;
		}
	}
	
	
	
}
