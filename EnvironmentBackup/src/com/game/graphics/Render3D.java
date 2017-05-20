package com.game.graphics;

import java.awt.Color;

import com.game.Game;
import java.util.*;
import com.game.input.Controller;
import com.game.level.Block;
import com.game.level.Level;

public class Render3D extends Render {

	public double[] zBuffer;
	public double[] zBufferWall;
	private double renderDistance = 12000; // distance player can see
	private double forward, right, sine, cosine, up, walking;

	Random random = new Random();

	public static double floorPosition = 8;
	public static double ceilingPosition = 26;

	public Render3D(int width, int height) {
		// constructor
		super(width, height);
		zBuffer = new double[width * height];
		zBufferWall = new double[width];

	}

	// double speed = 0.0005;
	// double time = 0;

	public void floor(Game game) {
		
		for(int x = 0; x < width; x++ ) {
			zBufferWall[x] = 0;
		}

		floorPosition = 8; // original value = 4
		ceilingPosition = 26; // original value = 8
		forward = game.controls.z;
		right = game.controls.x;
		up = game.controls.y;
		walking = 0; // to reset the position of floor/ceiling

		double rotation = game.controls.rotation; /// game.controls.rotation
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double ceiling = (y - height / 2.0) / height;

			double z = (floorPosition + up) / ceiling;

			if (Controller.crouchWalk) {
				walking = Math.sin(game.time / 0.5) * 1.7; // (speed, height)
				walking = Math.sin(game.time / 2.16) * 0.5; // (speed, height)
				if (Controller.isWalking) {
					walking = Math.sin(game.time / 0.5) * 1.7; // (speed, height)
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}
			if (Controller.sprintWalk) {
				walking = Math.sin(game.time / 0.5) * 1.7; // (speed, height)
				walking = Math.sin(game.time / 1.3) * 1.5;
				if (Controller.isWalking) {
					walking = Math.sin(game.time / 0.5) * 1.7; // (speed, height)
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}

			if (Controller.isWalking) {
				z = (floorPosition + up + walking) / ceiling;
			}

			if (ceiling < 0) {

				z = (ceilingPosition - up) / -ceiling; // height of the world
				if (Controller.isWalking) {
					walking = Math.sin(game.time / 0.5) * 1.7; // (speed, height)
					z = (ceilingPosition - up - walking) / -ceiling;
				}
			}

			// time+=speed;

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) (xx + right); // horizontal movement (+/- time)
				int yPix = (int) (yy + forward); // forward movement (+/- time)
				zBuffer[x + y * width] = z;
				pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * Texture.width]; // creates
																										// a
																										// floor
																										// and
																										// ceiling,
																										// 3D
																										// in
																										// depth
																										// view

				if (x > width || z > 300 || y > height) {
					pixels[x + y * width] = 0;
				}
			}

		}
		
		Level level = game.level;   // random block/wall generator
		int size = 500;  // size of the maze (number of walls)
		for(int xBlock = -size; xBlock <= size; xBlock++ ) {
			for(int zBlock = -size; zBlock <= size; zBlock++ ) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock+1, zBlock);
				Block south = level.create(xBlock, zBlock+1);
				
				if( block.solid ) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					}
					if(!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
					}
				} else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					}
					if(south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
					}
				}
			}
		}
		for(int xBlock = -size; xBlock <= size; xBlock++ ) {
			for(int zBlock = -size; zBlock <= size; zBlock++ ) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock+1, zBlock);
				Block south = level.create(xBlock, zBlock+1);
				
				if( block.solid ) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					}
					if(!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
					}
				} else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					}
					if(south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
					}
				}
			}
		}
		
	} // rendering finished

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {

		double upCorrect = 0.062; // correct value
		double rightCorrect = 0.062; // correct value
		double forwardCorrect = 0.062; // correct value
		double walkCorrect = -0.062; // correct value

		// left side
		double xcLeft = ((xLeft/2) - (right * rightCorrect)) * 2; // 2
		double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2; // 2

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// right side
		double xcRight = ((xRight/2) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

		// tex declaration
		double tex30 = 0;
		double tex48 = 8;
		double clip = 0.5;
		
		if(rotLeftSideZ < clip && rotRightSideZ < clip) {
			return;
		}
		
		if( rotLeftSideZ < clip ) {  // left side
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex48 - tex30) * clip0;
		}
		if( rotRightSideZ < clip ) {  // right side
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex48 = tex30 + (tex48 - tex30) * clip0;
		}
		

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		int xPixelLeftI1 = (int) (xPixelLeft);
		int xPixelRightI1 = (int) (xPixelRight);

		if (xPixelLeftI1 >= xPixelRightI1) {
			return;
		}

		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}

		double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
		double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
		double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
		double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex48 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation); 
			
			if(zBufferWall[x] > zWall) {
				continue;
			}
			zBufferWall[x] = zWall;
			
			int xTexture = (int) (((tex3 + tex4) * pixelRotation) / zWall);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if (yPixelTopInt > height) {
				yPixelTopInt = height;
			}

			/// rendering walls
			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {

				double pixelRotationY = (int) (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY);

				try {
					pixels[x + y * width] = Texture.floor.pixels[(xTexture & 7) + (yTexture & 7) * Texture.width]; // applying
																													// textures
					// pixels[x+y*width] = xTexture * 100 + yTexture * 100 *
					// 350;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 12; // brightness
																							// of
																							// the
																							// wall
			}

		}

	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int color = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0) {
				brightness = 0;
			}
			if (brightness > 255) {
				brightness = 255;
			}

			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;

		}
	}

}
