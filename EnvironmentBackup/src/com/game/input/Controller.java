package com.game.input;

import com.game.Display;
import com.game.graphics.Render3D;

public class Controller {

	public double x, z, y, rotation, xa, za, ya, rotationa;
	public static boolean turnRight = false, turnLeft = false;
	public static boolean canSprint = true;
	public static boolean isWalking = false;
	public static boolean crouchWalk = false;
	public static boolean sprintWalk = false;
	
	public void tick(boolean forward, boolean backward, boolean right, boolean left, boolean jump, boolean crouch, boolean sprint) {
		// code goes here
		double rotationSpeed = 0.002 * Display.mouseSpeed;  // rotation speed
		double walkSpeed = 1.4;
		double jumpHeight = 0.3;
		double crouchHeight = 0.3;
		double sprintSpeed = 0.7;
		double xMove = 0;
		double zMove = 0;
		
		isWalking = false;
		crouchWalk = false;
		sprintWalk = false;
		
		if( forward ) {
			zMove++;
			if(!jump) {
				isWalking = true;
			}
		}
		if( backward ) {
			zMove--;
			if(!jump) {
				isWalking = true;
			}
		}
		if( right ) {
			xMove++;
		}
		if( left ) {
			xMove--;
		}
		if( turnRight ) {
			rotationa += rotationSpeed;
		}
		if( turnLeft ) {
			rotationa -= rotationSpeed;
		}
		if( jump ) {
			y += jumpHeight;
			canSprint = false;
			isWalking = false;
		}
		if( crouch ) {
			y -= crouchHeight; 
			walkSpeed = walkSpeed / 2;
			canSprint = false;
			if(!jump) {
				isWalking = true;
			}
			crouchWalk = true;
		}
		if( !crouch && !jump ) {
			canSprint = true;
		}
		if( sprint && canSprint ) {
			walkSpeed += sprintSpeed;
			if(!jump) {
				isWalking = true;
			}
			sprintWalk = true;
		}
		
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation) ) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation) ) * walkSpeed;
		
		x += xa;
		y *= 0.9;  // height limit
		z += za;
		
		xa *= 0.1;
		za *= 0.1;
		
		rotation += rotationa;
		rotationa *= 0.5;
		
	}

}