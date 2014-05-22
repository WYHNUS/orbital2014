package edu.nus.comp.dotagrid.ui.renderers;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class MainRenderFrame {
	public static void main (String[] args) throws LWJGLException {
		// setup
		Display.setDisplayMode(new DisplayMode(800,600));
		Display.setFullscreen(false);
		Display.create(new PixelFormat(), new ContextAttribs(3,2).withProfileCompatibility(true));
		Keyboard.create();
		Mouse.setGrabbed(false);
		Mouse.create();
		// clear scene
		GL11.glViewport(0, 0, 800, 600);
		// get shader
		final short gridWidth = 100, gridHeight = 100;
		//Renderer r = new CrossRenderer ();//gridWidth, gridHeight);
		Renderer r = new GridRenderer (gridWidth, gridHeight);
		int zoom = 100;
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			// main drawing loop
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(1f, 0f, 0f, 1f);	//black
			// game control
			int mouseScroll = Mouse.getDWheel();
			if (mouseScroll < 0 && zoom > 0)
				zoom--;
			else if (mouseScroll > 0 && zoom < 201)
				zoom++;
			if (Display.isVisible()) {
				r.draw();
				Display.update();
			} else {
				if (Display.isDirty()) {
					r.draw();
					Display.update();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
				Display.sync(60);
			}
		}
	}
}
