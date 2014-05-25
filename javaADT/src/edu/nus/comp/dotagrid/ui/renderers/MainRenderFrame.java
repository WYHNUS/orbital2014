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
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_3D_COLOR_TEXTURE);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// get shader
		final short gridWidth = 50, gridHeight = 50;
		//Renderer r = new CrossRenderer ();//gridWidth, gridHeight);
		Renderer r = new GridRenderer (gridWidth, gridHeight);
		int zoom = 100;
		boolean mouseClickLeft = false, mouseClickRight = false;
		int mouseClickStartX = 0, mouseClickStartY = 0;
		int mouseClickDeltaX = 0, mouseClickDeltaY = 0;
		long mouseClickStartTime = 0;
		final long singleClickTime = 10;
		MVPTransform t = new MVPTransform();
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			// main drawing loop
			boolean changeZoom = true, changeTranslation = true, changePerspective = true;
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0f, 0f, 0f, 1f);	// black
			// game control - input processing phase
			int mouseScroll = Mouse.getDWheel();
			if (mouseScroll < 0 && zoom > 0)
				zoom--;
			else if (mouseScroll > 0 && zoom < 501)
				zoom++;
			else
				changeZoom = false;
			if (Mouse.isButtonDown(0)) {	// mouse left click - higher priority
				if (!mouseClickLeft) {
					mouseClickStartX = Mouse.getX();
					mouseClickStartY = Mouse.getY();
					mouseClickStartTime = System.currentTimeMillis();
					// Assuming Window Mode
					// TODO: Add Full Screen Mode Support for mouse clicks
				}
				mouseClickLeft = true;
			} else if (Mouse.isButtonDown(1)) {	// mouse right click - low priority
				if (!mouseClickRight) {
					mouseClickStartX = Mouse.getX();
					mouseClickStartY = Mouse.getY();
					mouseClickStartTime = System.currentTimeMillis();
				}
				mouseClickRight = true;
			} else
				// clear mouse click flags
				mouseClickLeft = mouseClickRight = false;
			// TODO: mouse click reverse transforms
			// game control - render section
			// grid zoom
			if (changeZoom)
				r.setMVP(new float[]{zoom/100f, 0, 0, 0, 0, zoom/100f, 0, 0, 0, 0, zoom/100f, 0, 0, 0, 0, 1});
			if (mouseClickLeft && mouseClickRight) {
				// not supported
			} else if (mouseClickLeft) {
				// left - translation
				if (System.currentTimeMillis() - mouseClickStartTime > singleClickTime) {
					// translation recognised
					
				}
			} else if (mouseClickRight) {
			} else {
				// all cleared
			}
			// draw routine
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
