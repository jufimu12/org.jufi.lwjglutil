package org.jufi.lwjglutil;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.WaveData;

public class ResourceLoader {
	public static int white;// Texture
	
	public static int loadTexturei(String path) throws IOException {
		return genTexture(ImageIO.read(ResourceLoader.class.getResourceAsStream(path)));
	}
	public static int loadTexture(String path) throws IOException {
		return genTexture(ImageIO.read(new File(path)));
	}
	public static int loadTexture(File res) throws IOException {
		return genTexture(ImageIO.read(res));
	}
	private static int genTexture(BufferedImage image) {
		int texId = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, texId);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, loadTextureIntoByteBuffer(image));
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return texId;
	}
	public static ByteBuffer loadTextureIntoByteBufferi(String path) throws IOException {
		return loadTextureIntoByteBuffer(ImageIO.read(ResourceLoader.class.getResourceAsStream(path)));
	}
	public static ByteBuffer loadTextureIntoByteBuffer(String path) throws IOException {
		return loadTextureIntoByteBuffer(new File(path));
	}
	public static ByteBuffer loadTextureIntoByteBuffer(File res) throws IOException {
		return loadTextureIntoByteBuffer(ImageIO.read(res));
	}
	private static ByteBuffer loadTextureIntoByteBuffer(BufferedImage image) {
		int[] pixels = new int[image.getHeight() * image.getWidth()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		
		int pixel = 0;
		for (int y = 0; y < image.getWidth(); y++) {
			for (int x = 0; x < image.getHeight(); x++) {
				pixel = pixels[y * image.getHeight() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	public static void initWhite() {
		int texId = glGenTextures();
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(4);
		buffer.put((byte) -1).put((byte) -1).put((byte) -1).put((byte) -1).flip();
		glBindTexture(GL_TEXTURE_2D, texId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		white = texId;
	}
	

	public static int[] loadShaderi(String vertexShaderPath, String fragmentShaderPath) throws IOException {// Shader
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceLoader.class.getResourceAsStream(vertexShaderPath)));
			String line;
			while ((line = reader.readLine()) != null) {
				vertexShaderSource.append(line).append('\n');
			}
			reader.close();
		}
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceLoader.class.getResourceAsStream(fragmentShaderPath)));
			String line;
			while ((line = reader.readLine()) != null) {
				fragmentShaderSource.append(line).append('\n');
			}
			reader.close();
		}
		glShaderSource(vertexShader,  vertexShaderSource);
		glCompileShader(vertexShader);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		
		return new int[] {shaderProgram, vertexShader, fragmentShader};
	}
	public static int[] loadShader(String vertexShaderPath, String fragmentShaderPath) throws IOException {// Shader
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		StringBuilder vertexShaderSource = new StringBuilder();
		StringBuilder fragmentShaderSource = new StringBuilder();
		{
			BufferedReader reader = new BufferedReader(new FileReader(vertexShaderPath));
			String line;
			while ((line = reader.readLine()) != null) {
				vertexShaderSource.append(line).append('\n');
			}
			reader.close();
		}
		{
			BufferedReader reader = new BufferedReader(new FileReader(fragmentShaderPath));
			String line;
			while ((line = reader.readLine()) != null) {
				fragmentShaderSource.append(line).append('\n');
			}
			reader.close();
		}
		glShaderSource(vertexShader,  vertexShaderSource);
		glCompileShader(vertexShader);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		
		return new int[] {shaderProgram, vertexShader, fragmentShader};
	}
	public static int[] loadShader(StringBuilder vertexShaderSource, StringBuilder fragmentShaderSource) {
		int shaderProgram = glCreateProgram();
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		
		glShaderSource(vertexShader,  vertexShaderSource);
		glCompileShader(vertexShader);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		
		return new int[] {shaderProgram, vertexShader, fragmentShader};
	}
	public static void loadDefaultShader(int[] sh_main) throws IOException {
		sh_main[0] = ResourceLoader.loadShaderi("/org/jufi/lwjglutil/shader/3d.vsh", "/org/jufi/lwjglutil/shader/3d.fsh")[0];
		sh_main[1] = ResourceLoader.loadShaderi("/org/jufi/lwjglutil/shader/3dnl.vsh", "/org/jufi/lwjglutil/shader/3dnl.fsh")[0];
		sh_main[2] = ResourceLoader.loadShaderi("/org/jufi/lwjglutil/shader/2d.vsh", "/org/jufi/lwjglutil/shader/2d.fsh")[0];
	}
	
	
	
	public static int getSourceFromWavi(String path, float volume) {// Sound
		WaveData data = WaveData.create(new BufferedInputStream(ResourceLoader.class.getResourceAsStream(path)));
		int buffer = alGenBuffers();
		alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();
		
		int source = alGenSources();
		alSourcei(source, AL_BUFFER, buffer);
		alSourcef(source, AL_GAIN, volume);
		alDeleteBuffers(buffer);
		return source;
	}
	public static int getSourceFromWav(String path, float volume) throws FileNotFoundException {
		WaveData data = WaveData.create(new BufferedInputStream(new FileInputStream(path)));
		int buffer = alGenBuffers();
		alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();
		
		int source = alGenSources();
		alSourcei(source, AL_BUFFER, buffer);
		alSourcef(source, AL_GAIN, volume);
		alDeleteBuffers(buffer);
		return source;
	}
	
	
	
	public static void loadNatives(String source, Class<?> executingClass) throws IOException {// Natives
		String dest = System.getProperty("java.io.tmpdir") + "/lwjglnatives/";
		String[] natives = new String[] {
				"jinput-dx8_64.dll",
				"jinput-dx8.dll",
				"jinput-raw_64.dll",
				"jinput-raw.dll",
				"libjinput-linux.so",
				"libjinput-linux64.so",
				"libjinput-osx.jnilib",
				"liblwjgl.jnilib",
				"liblwjgl.so",
				"liblwjgl64.so",
				"libopenal.so",
				"libopenal64.so",
				"lwjgl.dll",
				"lwjgl64.dll",
				"openal.dylib",
				"OpenAL32.dll",
				"OpenAL64.dll"
		};
		
		new File(System.getProperty("java.io.tmpdir") + "/lwjglnatives").mkdir();
		for (String s : natives) {
			if (!new File(dest + s).exists()) FileUtils.copyJARRes(source + s, dest + s, executingClass);
		}
		System.setProperty("org.lwjgl.librarypath", System.getProperty("java.io.tmpdir") + "/lwjglnatives");
	}
	public static void loadNatives() throws IOException {
		loadNatives("/org/jufi/lwjglutil/natives/", ResourceLoader.class);
	}
}
