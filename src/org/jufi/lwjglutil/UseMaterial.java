package org.jufi.lwjglutil;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class UseMaterial implements ModelCommand {
	
	private Material mtl;
	
	public UseMaterial(Material mtl) {
		this.mtl = mtl;
	}
	
	@Override
	public void execute(Model commander) {
		glEnd();
		glMaterial(GL_FRONT, GL_AMBIENT, mtl.ka);
		glMaterial(GL_FRONT, GL_DIFFUSE, mtl.kd);
		glMaterial(GL_FRONT, GL_SPECULAR, mtl.ks);
		glBindTexture(GL_TEXTURE_2D, mtl.texture);
		glBegin(GL_TRIANGLES);
	}
	@Override
	public void execute(Model commander, ArrayList<Byte> b) {
		b.add(b(0x01));
		b.add(b(0x20));
		addBytea(b, PBytes.byFloat(mtl.ka.get(0)));
		addBytea(b, PBytes.byFloat(mtl.ka.get(1)));
		addBytea(b, PBytes.byFloat(mtl.ka.get(2)));
		addBytea(b, PBytes.byFloat(mtl.ka.get(3)));
		b.add(b(0x21));
		addBytea(b, PBytes.byFloat(mtl.kd.get(0)));
		addBytea(b, PBytes.byFloat(mtl.kd.get(1)));
		addBytea(b, PBytes.byFloat(mtl.kd.get(2)));
		addBytea(b, PBytes.byFloat(mtl.kd.get(3)));
		b.add(b(0x22));
		addBytea(b, PBytes.byFloat(mtl.ks.get(0)));
		addBytea(b, PBytes.byFloat(mtl.ks.get(1)));
		addBytea(b, PBytes.byFloat(mtl.ks.get(2)));
		addBytea(b, PBytes.byFloat(mtl.ks.get(3)));
		b.add(b(0x00));
	}
	
	private static Byte b(int b) {
		return Byte.valueOf((byte) b);
	}
	private static void addBytea(ArrayList<Byte> b, byte[] v) {
		for (byte e : v) {
			b.add(Byte.valueOf(e));
		}
	}
}
