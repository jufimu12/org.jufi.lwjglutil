package org.jufi.lwjglutil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.input.Keyboard.*;

import java.io.IOException;

import org.jufi.lwjglutil.Camera.CameraMode;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

public abstract class Engine extends Thread {
	// Static stuff
	
	public static void exit(int exitArg) {
		if (Display.isCreated()) Display.destroy();
		if (AL.isCreated()) AL.destroy();
		System.exit(exitArg);
	}
	
	// Non-Static stuff
	protected Camera cam;
	private FPSCounter fps = new FPSCounter();
	
	public Engine() {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/natives");
	}
	
	public final void run() {
		initEverything();
		
		while (!Display.isCloseRequested()) {// Main loop
			input();
			tick();
			fps.tick();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			glLoadIdentity();
				cam.init3d();
				cam.tick();
				glEnable(GL_LIGHTING);
				render3d();
				glDisable(GL_LIGHTING);
				render3dNoLighting();
				
			glLoadIdentity();
				cam.init2d();
				render2d();
				glColor3f(0, 1, 0);
				SimpleText.drawString(fps.getFPS(), 1, cam.getResY() - 10);
				
			Display.update();
			Display.sync(60);
		}
		exit(0);
	}
	
	private final void input() {
		if (Mouse.isGrabbed()) {
			cam.rotateY(-(float)Mouse.getDX() / 15);
			cam.rotateX(-(float)Mouse.getDY() / 15);
		}
		move();
	}
	protected void move() {
		if (isKeyDown(KEY_W)) cam.moveNoClip(true, 0.1f);
		if (isKeyDown(KEY_S)) cam.moveNoClip(true, -0.1f);
		if (isKeyDown(KEY_A)) cam.moveNoClip(false, 0.1f);
		if (isKeyDown(KEY_D)) cam.moveNoClip(false, -0.1f);
	}
	private final void initEverything() {
		preInit();
		
		cam = new Camera(initCameraMode());
		try {
			cam.initDisplay();
		} catch (LWJGLException e) {
			e.printStackTrace();
			exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			exit(2);
		}
		cam.init();
		
		Mouse.setGrabbed(true);
		postInit();
		
		System.gc();
	}
	protected abstract void render3d();
	protected abstract void render3dNoLighting();
	protected abstract void render2d();
	protected abstract void tick();
	protected abstract void preInit();
	protected abstract void postInit();
	protected abstract CameraMode initCameraMode();
}
