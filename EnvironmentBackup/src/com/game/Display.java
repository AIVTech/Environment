package com.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.game.graphics.Render;
import com.game.graphics.Screen;
import com.game.gui.Launcher;
import com.game.gui.Options;
import com.game.input.Controller;
import com.game.input.InputHandler;

public class Display extends Canvas implements Runnable {

	public static int width = 800;
	public static int height = 600;
	public static final String TITLE = "Environment Alpha";

	private Thread t1;
	private Game game;
	private Screen screen;
	private BufferedImage img;
	private boolean running = false;
	private Render render;
	private int[] pixels;
	private InputHandler input;
	private int fps;
	public static int selection = 1;
	public static boolean canRender = false;
	public static boolean canRenderMenu = true;

	public static double mouseSpeed;

	private int newX = 0, oldX = 0, newY = 0, oldY = 0;

	public Display() {
		// constructor
		Dimension size = new Dimension(getGameWidth(), getGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(getGameWidth(), getGameHeight());
		game = new Game();
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public static int getGameWidth() {

		return width;
	}

	public static int getGameHeight() {
		return height;
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		t1 = new Thread(this, "game");
		t1.start();

		// System.out.println("Working!"); for testing

	}

	public synchronized void stop() {
		if (running)
			return;
		running = false;
		try {
			t1.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void stopMenu() {
		try {
			Launcher.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		requestFocus();

		while (running) {
			// main game loop
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0; // 1 Billion

			// launcher.updateFrame();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;

				if (tickCount % 60 == 0) {
					// System.out.println("FPS: " + frames); // for testing
					fps = frames;
					previousTime += 1000;
					frames = 0;
				}

				if (ticked) { // fps counter
					if (canRender) {
						render();
					}
					frames++;
				}
				if (canRender) {
					render();
				}

				if (canRenderMenu) {
					try {
						requestFocus();
						renderMenu(); //////// rendering launcher/menu
					} catch (IllegalStateException e) {
						continue;
					} catch (Exception e) {
						continue;
					}
				}

			}

		}

		newX = InputHandler.MouseX; // right / left
		if (newX > oldX) {
			// going right
			Controller.turnRight = true;
		}
		if (newX < oldX) {
			// going left
			Controller.turnLeft = true;
		}
		if (newX == oldX) {
			// not moving
			Controller.turnRight = false;
			Controller.turnLeft = false;
		}

		mouseSpeed = Math.abs(newX - oldX);

		oldX = newX;

		newY = InputHandler.MouseY; // up / down
		if (newY > oldY) {
			// going down
		}
		if (newY < oldY) {
			// going up
		}
		if (newY == oldY) {
			// not moving
		}
		oldY = newY;

	}

	private void renderMenu() throws IllegalStateException {

		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3); // dimensions of the game (2D, 3D);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 400);
		try {
			g.drawImage(ImageIO.read(Launcher.class.getResource("/launcherBackground.jpg")), 0, 0, 800, 400, null);

			//
			// play
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 110
					&& InputHandler.MouseY < 110 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/play_on.png")), 650, 110, 130, 30,
						null);
				// arrow
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/arrow.png")), 650 + 68, 112, 60, 30,
						null);

				// play
				if (InputHandler.MouseButton == 1) { // opens up the actual game
					// run game
					canRender = true;
					canRenderMenu = false;
					g.dispose();
					Launcher.running = false;
					Launcher.openGame();
					stopMenu();
					new RunGame();
				}

			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/play_off.png")), 650, 110, 130, 30,
						null);
				// problem lies here
			}
			//
			// options
			if (InputHandler.MouseX > 641 && InputHandler.MouseX < 641 + 130 && InputHandler.MouseY > 170
					&& InputHandler.MouseY < 170 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/options_on.png")), 641, 160, 130, 30,
						null);
				// arrow
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/arrow.png")), 650 + 84, 159, 60, 30,
						null);
				
				if (InputHandler.MouseButton == 1) { // opens up the options window
					// open options
					
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/options_off.png")), 641, 160, 130, 30,
						null);
			}
			//
			// help
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 210
					&& InputHandler.MouseY < 210 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/help_on.png")), 650, 210, 130, 30,
						null);
				// arrow
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/arrow.png")), 650 + 68, 212, 60, 30,
						null);
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/help_off.png")), 650, 210, 130, 30,
						null);
				// problem lies here
			}
			//
			// exit
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 250
					&& InputHandler.MouseY < 250 + 30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/exit_on.png")), 650, 260, 130, 30,
						null);
				// arrow
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/arrow.png")), 650 + 60, 262, 60, 30,
						null);

				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}

			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/orig/exit_off.png")), 650, 260, 130, 30,
						null);
				// problem lies here
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", 0, 20));
		// g.drawString("Play", 664, 100);
		// g.drawString("Exit", 664, 300);

		g.dispose();
		bs.show();

	}

	private void tick() {
		requestFocus(); //// SUPER IMPORTANT TO FIX THE WINDOW NOT IN FOCUS BUG
		game.tick(input.key);
		
		newX = InputHandler.MouseX; // right / left
		if (newX > oldX) {
			// going right
			Controller.turnRight = true;
		}
		if (newX < oldX) {
			// going left
			Controller.turnLeft = true;
		}
		if (newX == oldX) {
			// not moving
			Controller.turnRight = false;
			Controller.turnLeft = false;
		}

		mouseSpeed = Math.abs(newX - oldX);

		oldX = newX;

		newY = InputHandler.MouseY; // up / down
		if (newY > oldY) {
			// going down
		}
		if (newY < oldY) {
			// going up
		}
		if (newY == oldY) {
			// not moving
		}
		oldY = newY;

	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			createBufferStrategy(3); // dimensions of the game (2D, 3D);
			return;
		}

		screen.render(game);

		for (int i = 0; i < getGameWidth() * getGameHeight(); i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
		g.setFont(new Font("Verdana", 0, 15));
		g.setColor(Color.CYAN);
		g.drawString("FPS: " + fps, 10, 30);
		g.setFont(new Font("Arial", 0, 20));
		g.drawString("+", getGameWidth() / 2, getGameHeight() / 2);
		g.dispose();
		bs.show();

	}

	public static void main(String[] args) {

		Display display = new Display();
		new Launcher(0, display);

	}

}
