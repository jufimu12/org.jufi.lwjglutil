package org.jufi.engine;

import java.io.IOException;

import org.jufi.lwjglutil.Camera.CameraMode;
import org.jufi.lwjglutil.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine extends org.jufi.lwjglutil.Engine {
	private int dl_bunny;
	private boolean keydown_escape;
	
	public static void main(String[] args) {
		System.out.println("Starting test Engine application");
		new Engine().start();
	}
	
	@Override
	protected void render3d() {
		glCallList(dl_bunny);
	}
	@Override
	protected void render3dNoLighting() {
		
	}
	@Override
	protected void render3dRelative() {
		
	}
	@Override
	protected void render3dRelativeNoLighting() {
		
	}
	@Override
	protected void render2d() {
		glBindTexture(GL_TEXTURE_2D, 2);
		glBegin(GL_TRIANGLES);
			glColor3f(1, 0, 0);glTexCoord2d(0, 0);glVertex2f(100, 100);
			glColor3f(0, 1, 0);glTexCoord2d(1, 0);glVertex2f(200, 100);
			glColor3f(0, 0, 1);glTexCoord2d(0, 1);glVertex2f(100, 200);
		glEnd();
	}
	@Override
	protected void tick() {
		if (Keyboard.isKeyDown(KEY_ESCAPE)) {
			if (Keyboard.isKeyDown(KEY_SPACE)) exit(0);
			if (!keydown_escape) Mouse.setGrabbed(!Mouse.isGrabbed());
			keydown_escape = true;
		} else keydown_escape = false;
	}

	@Override
	protected void preInit() {
		
	}
	@Override
	protected void postInit() {
		try {
			ResourceLoader.loadDefaultShader(sh_main = new int[3]);
//			sh_main = null;

			dl_bunny = Model.getDL("res/obj/bunny.obj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		glClearColor(0.6f, 0.6f, 1, 1);
		Mouse.setGrabbed(true);
//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	@Override
	 protected void initCameraMode(CameraMode m) {
		m.setDisplayRes(1600, 900);
//		m.setLightpos(-0.7f, 2, 0.3f, 1);// point light
		m.setLightpos(1, 1, 1, 0);// directional light
		m.setMap(null);
		m.setOptions(true, 0, false);
		m.setOrthoRes(1600, 900);
		m.setPerspective(70, 0.01f, 1000);
		m.setTitle("Engine");
		m.setTransformation(0, 3, 5, -20, 0, 0);
	}
	@Override
	protected void move() {
		float speed;
		if (Keyboard.isKeyDown(KEY_LCONTROL)) speed = 0.02f;
		else if (Keyboard.isKeyDown(KEY_LSHIFT)) speed = 0.5f;
		else speed = 0.1f;
		if (isKeyDown(KEY_W)) cam.moveY(true, speed);
		if (isKeyDown(KEY_S)) cam.moveY(true, -speed);
		if (isKeyDown(KEY_A)) cam.moveY(false, speed);
		if (isKeyDown(KEY_D)) cam.moveY(false, -speed);
	}
	@Override
	protected void onExit() {
		
	}
}
