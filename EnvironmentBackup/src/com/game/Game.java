package com.game;

import java.awt.event.KeyEvent;
import java.util.*;

import com.game.input.Controller;
import com.game.level.Level;

public class Game {
	
	public double time;
	public double speed = 0.3;  // player's movement speed
	public Controller controls;
	public Level level;
	
	public Game() {
		
		controls = new Controller();
		level = new Level(80, 80);  // size of the maze (number of walls)
		
	}
	
	public void tick(boolean[] key) {
		time+=speed;
		boolean forward = key[KeyEvent.VK_W];
		boolean backward = key[KeyEvent.VK_S];
		boolean right = key[KeyEvent.VK_D];
		boolean left = key[KeyEvent.VK_A];
		boolean jump = key[KeyEvent.VK_SPACE];
		boolean crouch = key[KeyEvent.VK_ALT];
		boolean sprint = key[KeyEvent.VK_SHIFT];
		
		controls.tick(forward, backward, right, left, jump, crouch, sprint);
	}
	
}
