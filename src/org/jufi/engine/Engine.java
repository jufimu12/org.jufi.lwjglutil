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
	protected void render2d() {
		
	}
	@Override
	protected void tick() {
		if (Keyboard.isKeyDown(KEY_ESCAPE)) {
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
			dl_bunny = Model.getCallListFromOBJ("res/obj/bunny.obj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}
	@Override
	 protected CameraMode initCameraMode() {
		CameraMode m = new CameraMode();
		m.setDisplayRes(1600, 900);
		m.setLightpos(1, 1, 1, 0);
		m.setMap(null);
		m.setOptions(true, false, false, true, true, false, true);
		m.setOrthoRes(1600, 900);
		m.setPerspective(70, 0.01f, 1000);
		m.setTitle("Engine");
		m.setTransformation(0, 0, 0, 0, 0, 0);
		return m;
	}
	@Override
	protected void move() {
		float speed;
		if (Keyboard.isKeyDown(KEY_LCONTROL)) speed = 0.02f;
		else if (Keyboard.isKeyDown(KEY_LSHIFT)) speed = 0.5f;
		else speed = 0.1f;
		if (isKeyDown(KEY_W)) cam.moveNoClip(true, speed);
		if (isKeyDown(KEY_S)) cam.moveNoClip(true, -speed);
		if (isKeyDown(KEY_A)) cam.moveNoClip(false, speed);
		if (isKeyDown(KEY_D)) cam.moveNoClip(false, -speed);
	}
}
