package com.game.graphics;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Texture {	
	
	public static int width;
	
	public static Render floor = loadBitmap("/textures/camo3.png");
	
	public static Render loadBitmap(String fileName) {
		try {
			BufferedImage image = ImageIO.read( Texture.class.getResource(fileName) );
			width = image.getWidth();
			int height = image.getHeight();
			Render result = new Render(width, height);
			image.getRGB(0, 0, width, height, result.pixels, 0, width);
			return result;
			
		}catch(Exception e) {
			throw new RuntimeException (e);
		}
	}
	
}