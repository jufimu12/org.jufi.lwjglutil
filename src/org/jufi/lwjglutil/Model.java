package org.jufi.lwjglutil;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.Renderable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Model implements Renderable {
	private ArrayList<float[]> vertices = new ArrayList<float[]>();
	private ArrayList<float[]> vtextures = new ArrayList<float[]>();
	private ArrayList<float[]> normals = new ArrayList<float[]>();
	private ArrayList<ModelCommand> commands = new ArrayList<ModelCommand>();
	private ArrayList<Material> materials = new ArrayList<Material>();
	
	public Model(String path) throws IOException {
			BufferedReader res = new BufferedReader(new FileReader(path));
			String line;
			
			float[] firstValue = {0, 0, 0};
			float[] vfirstValue = {0, 0};
			vertices.add(firstValue);
			vtextures.add(vfirstValue);
			normals.add(firstValue);
			while ((line = res.readLine()) != null) {
				readln(line, path);
			}
			res.close();
	}
	
	public void render() {
		glBegin(GL_TRIANGLES);
			for (ModelCommand command : commands) {
				command.execute(this);
			}
		glEnd();
	}
	
	public void renderFace(Face face) {
		if (face.getNormals()[0] != 0) glNormal3f(normals.get(face.getNormals()[0])[0], normals.get(face.getNormals()[0])[1], normals.get(face.getNormals()[0])[2]);
		if (face.getTextures()[0] != 0) glTexCoord2f(vtextures.get(face.getTextures()[0])[0], vtextures.get(face.getTextures()[0])[1]);
		glVertex3f(vertices.get(face.getVertices()[0])[0], vertices.get(face.getVertices()[0])[1], vertices.get(face.getVertices()[0])[2]);
		
		if (face.getNormals()[1] != 0) glNormal3f(normals.get(face.getNormals()[1])[0], normals.get(face.getNormals()[1])[1], normals.get(face.getNormals()[1])[2]);
		if (face.getTextures()[1] != 0) glTexCoord2f(vtextures.get(face.getTextures()[1])[0], vtextures.get(face.getTextures()[1])[1]);
		glVertex3f(vertices.get(face.getVertices()[1])[0], vertices.get(face.getVertices()[1])[1], vertices.get(face.getVertices()[1])[2]);
		
		if (face.getNormals()[2] != 0) glNormal3f(normals.get(face.getNormals()[2])[0], normals.get(face.getNormals()[2])[1], normals.get(face.getNormals()[2])[2]);
		if (face.getTextures()[2] != 0) glTexCoord2f(vtextures.get(face.getTextures()[2])[0], vtextures.get(face.getTextures()[2])[1]);
		glVertex3f(vertices.get(face.getVertices()[2])[0], vertices.get(face.getVertices()[2])[1], vertices.get(face.getVertices()[2])[2]);
	}
	public void renderFace(Face face, ArrayList<Byte> b) {
		if (face.getNormals()[0] != 0) {
			b.add(b(0x12));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[0])[0]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[0])[1]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[0])[2]));
		}
		if (face.getTextures()[0] != 0) {
			b.add(b(0x11));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[0])[0]));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[0])[1]));
		}
		b.add(b(0x10));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[0])[0]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[0])[1]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[0])[2]));
		
		if (face.getNormals()[1] != 0) {
			b.add(b(0x12));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[1])[0]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[1])[1]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[1])[2]));
		}
		if (face.getTextures()[1] != 0) {
			b.add(b(0x11));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[1])[0]));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[1])[1]));
		}
		b.add(b(0x10));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[1])[0]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[1])[1]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[1])[2]));
		
		if (face.getNormals()[2] != 0) {
			b.add(b(0x12));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[2])[0]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[2])[1]));
			addBytea(b, PBytes.byDouble(normals.get(face.getNormals()[2])[2]));
		}
		if (face.getTextures()[2] != 0) {
			b.add(b(0x11));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[2])[0]));
			addBytea(b, PBytes.byDouble(vtextures.get(face.getTextures()[2])[1]));
		}
		b.add(b(0x10));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[2])[0]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[2])[1]));
		addBytea(b, PBytes.byDouble(vertices.get(face.getVertices()[2])[2]));
	}
	
	public byte[] toBinary() {
		ArrayList<Byte> b = new ArrayList<Byte>();
		b.add(b(0x00));
		for (ModelCommand command : commands) {
			System.out.println("Converting: " + command.toString());
			command.execute(this, b);
		}
		b.add(b(0x01));
		byte[] value = new byte[b.size()];
		for (int i = 0; i < b.size(); i++) value[i] = b.get(i).byteValue();
		return value;
	}
	
	public void writeBinary(String path) throws IOException {
		PBytes.toFile(toBinary(), path);
	}
	
	private void readln(String line, String objpath) throws IOException {
		if (line.isEmpty() || line.startsWith("#")) {
			return;
		}
		
		String[] args = line.split(" ");
		
		if (args[0].equals("v") && args.length == 4) {
			float[] value = {Float.valueOf(args[1]), Float.valueOf(args[2]), Float.valueOf(args[3])};
			vertices.add(value);
		}
		if (args[0].equals("vn") && args.length == 4) {
			float[] value = {Float.valueOf(args[1]), Float.valueOf(args[2]), Float.valueOf(args[3])};
			normals.add(value);
		}
		if (args[0].equals("vt") && args.length == 3) {
			float[] value = {Float.valueOf(args[1]), Float.valueOf(args[2])};
			vtextures.add(value);
		}
		if (args[0].equals("f") && args.length == 4) {
			int[] v = new int[args.length - 1];
			int[] vt = new int[args.length - 1];
			int[] vn = new int[args.length - 1];
			for (int i = 0; i < vt.length; i++) vt[i] = 0;
			for (int i = 1; i <= 3; i++) {
				if (args[i].contains("/")) {
					if (args[i].contains("//")) {
						String[] splitted = args[i].split("//");
						v[i - 1] = Integer.valueOf(splitted[0]);
						vn[i - 1] = Integer.valueOf(splitted[1]);
					} else {
						String[] splitted = args[i].split("/");
						v[i - 1] = Integer.valueOf(splitted[0]);
						if (splitted.length >= 2) {
							vt[i - 1] = Integer.valueOf(splitted[1]);
						}
						if (splitted.length == 3) {
							vn[i - 1] = Integer.valueOf(splitted[2]);
						}
					}
				} else {
					v[i - 1] = Integer.valueOf(args[i]);
				}
			}
			commands.add(new Face(v, vt, vn));
		}
		if (args[0].equals("mtllib") && args.length == 2) {
			Material.readFile(materials, new File(objpath).toPath().getParent().toString() + "/" + args[1]);
			
		}
		if (args[0].equals("usemtl") && args.length == 2) {
			Material mtl = null;
			for (Material mat : materials) {
				if (mat.name.equals(args[1])) mtl = mat;
			}
			commands.add(new UseMaterial(mtl));
		}
	}
	private static Byte b(int b) {
		return Byte.valueOf((byte) b);
	}
	private static void addBytea(ArrayList<Byte> b, byte[] v) {
		for (byte e : v) {
			b.add(Byte.valueOf(e));
		}
	}
	public static void renderBinary(byte[] b) {
		int i = 0;
		while (i < b.length) {
			switch (b[i]) {
			case 0x00:
				glBegin(GL_TRIANGLES);
				i++;
				break;
			case 0x01:
				glEnd();
				i++;
				break;
			case 0x10:
				glVertex3d(
						PBytes.toDouble(b[i + 1], b[i + 2], b[i + 3], b[i + 4], b[i + 5], b[i + 6], b[i + 7], b[i + 8]),
						PBytes.toDouble(b[i + 9], b[i + 10], b[i + 11], b[i + 12], b[i + 13], b[i + 14], b[i + 15], b[i + 16]),
						PBytes.toDouble(b[i + 17], b[i + 18], b[i + 19], b[i + 20], b[i + 21], b[i + 22], b[i + 23], b[i + 24]));
				i += 25;
				break;
			case 0x11:
				glTexCoord2d(
						PBytes.toDouble(b[i + 1], b[i + 2], b[i + 3], b[i + 4], b[i + 5], b[i + 6], b[i + 7], b[i + 8]),
						PBytes.toDouble(b[i + 9], b[i + 10], b[i + 11], b[i + 12], b[i + 13], b[i + 14], b[i + 15], b[i + 16]));
				i += 17;
				break;
			case 0x12:
				glNormal3d(
						PBytes.toDouble(b[i + 1], b[i + 2], b[i + 3], b[i + 4], b[i + 5], b[i + 6], b[i + 7], b[i + 8]),
						PBytes.toDouble(b[i + 9], b[i + 10], b[i + 11], b[i + 12], b[i + 13], b[i + 14], b[i + 15], b[i + 16]),
						PBytes.toDouble(b[i + 17], b[i + 18], b[i + 19], b[i + 20], b[i + 21], b[i + 22], b[i + 23], b[i + 24]));
				i += 25;
				break;
			case 0x20:
				FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
				ambient.put(PBytes.toFloat(b[i + 1], b[i + 2], b[i + 3], b[i + 4]));
				ambient.put(PBytes.toFloat(b[i + 5], b[i + 6], b[i + 7], b[i + 8]));
				ambient.put(PBytes.toFloat(b[i + 9], b[i + 10], b[i + 11], b[i + 12]));
				ambient.put(PBytes.toFloat(b[i + 13], b[i + 14], b[i + 15], b[i + 16]));
				ambient.flip();
				glMaterial(GL_FRONT, GL_AMBIENT, ambient);
				i += 17;
				break;
			case 0x21:
				FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
				diffuse.put(PBytes.toFloat(b[i + 1], b[i + 2], b[i + 3], b[i + 4]));
				diffuse.put(PBytes.toFloat(b[i + 5], b[i + 6], b[i + 7], b[i + 8]));
				diffuse.put(PBytes.toFloat(b[i + 9], b[i + 10], b[i + 11], b[i + 12]));
				diffuse.put(PBytes.toFloat(b[i + 13], b[i + 14], b[i + 15], b[i + 16]));
				diffuse.flip();
				glMaterial(GL_FRONT, GL_DIFFUSE, diffuse);
				i += 17;
				break;
			case 0x22:
				FloatBuffer specular = BufferUtils.createFloatBuffer(4);
				specular.put(PBytes.toFloat(b[i + 1], b[i + 2], b[i + 3], b[i + 4]));
				specular.put(PBytes.toFloat(b[i + 5], b[i + 6], b[i + 7], b[i + 8]));
				specular.put(PBytes.toFloat(b[i + 9], b[i + 10], b[i + 11], b[i + 12]));
				specular.put(PBytes.toFloat(b[i + 13], b[i + 14], b[i + 15], b[i + 16]));
				specular.flip();
				glMaterial(GL_FRONT, GL_SPECULAR, specular);
				i += 17;
				break;
			}
		}
	}
	public static void renderBinary(String path) throws IOException {
		renderBinary(PBytes.byFile(path));
	}
	public static int getCallListFromBinary(String path) throws IOException {
		int calllist = glGenLists(1);
		glNewList(calllist, GL_COMPILE);
		renderBinary(path);
		glEndList();
		return calllist;
	}
	public static int getCallListFromOBJ(String path) throws IOException {
		int callist = glGenLists(1);
		glNewList(callist, GL_COMPILE);
		new Model(path).render();
		glEndList();
		return callist;
	}
}
