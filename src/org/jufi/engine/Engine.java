package org.jufi.engine;

import org.jufi.lwjglutil.Camera.CameraMode;

public class Engine extends org.jufi.lwjglutil.Engine {
	public static void main(String[] args) {
		System.out.println("Starting test Engine application");
		new Engine().start();
	}
	
	@Override
	protected void render3d() {
		
	}
	@Override
	protected void render3dNoLighting() {
		
	}
	@Override
	protected void render2d() {
		
	}
	@Override
	protected void tick() {
		
	}

	@Override
	protected void preInit() {
		
	}
	@Override
	protected void postInit() {
		
	}
	@Override
	 protected CameraMode initCameraMode() {
		CameraMode m = new CameraMode();
		m.setDisplayRes(1600, 900);
		m.setLightpos(1, 1, 1, 0);
		m.setMap(null);
		m.setOptions(true, false, false, true, true, true, false);
		m.setOrthoRes(1600, 900);
		m.setPerspective(70, 0.01f, 1000);
		m.setTitle("Engine");
		m.setTransformation(0, 0, 0, 0, 0, 0);
		return m;
	}
}
