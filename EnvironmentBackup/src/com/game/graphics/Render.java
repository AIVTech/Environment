package com.game.graphics;

import com.game.Display;

public class Render {
	
	public final int width;
	public final int height;
	public final int[] pixels;
	
	private Display display;
	
	public Render(int width, int height) {
		
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	
	public void draw(Render render, int xOffset, int yOffset) {
		// drawing pixels on the screen
		for( int y = 0; y < render.height; y++ ) {
			int yPixels = y + yOffset;
			if(yPixels < 0 || yPixels >= display.height) {  // if array is out of bounds for (y)
				continue;
			}
			
			for( int x = 0; x < render.width; x++ ) {
				int xPixels = x + xOffset;
				if(xPixels < 0 || xPixels >= display.width) {  // if array is out of bounds for (x)
					continue;
				}
				
				int alpha = render.pixels[x+y*render.width];
				if(alpha > 0) {
					pixels[xPixels+yPixels*width] = alpha;
				}
				
			}
		}
	}
	
}
