package edu.nus.comp.dotagrid.ui.renderers;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import static edu.nus.comp.dotagrid.math.RenderMaths.*;

public class MainRenderFrame {
	public static void main (String[] args) throws LWJGLException {
		// setup
		final int displayWidth = 800, displayHeight = 600;
		Display.setDisplayMode(new DisplayMode(displayWidth,displayHeight));
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
		final short gridWidth = 10, gridHeight = 10;
		//Renderer r = new CrossRenderer ();//gridWidth, gridHeight);
		Renderer r = new GridRenderer (gridWidth, gridHeight);
		int zoom = 100;
		boolean mouseClickLeft = false, mouseClickRight = false;
		boolean changeZoom, changeTranslation = false, changePerspective = true, processingTranslation = false;
		boolean viewDirty;
		int mouseClickStartX = 0, mouseClickStartY = 0;
		long mouseClickStartTime = 0;
		final long singleClickTime = 10;
		MVPTransform t = new MVPTransform();
		float[] scaling = IdentityMatrix4x4(), translation = IdentityMatrix4x4();
		float[] processingTranslationMat = IdentityMatrix4x4();
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			// main drawing loop
			changeZoom = true;
			viewDirty = false;
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
			// mouse clicks
			if (mouseClickLeft && mouseClickRight) {
				// not supported
			} else if (mouseClickLeft) {
				// left - translation
				if (System.currentTimeMillis() - mouseClickStartTime > singleClickTime) {
					// translation recognised
					processingTranslation = true;
					changeTranslation = false;
					float deltaX = (Mouse.getX() - mouseClickStartX) / (float) displayWidth,
							deltaY = (Mouse.getY() - mouseClickStartY) / (float) displayHeight;
					float[] viewTransform = FlatTranslationMatrix4x4(deltaX, deltaY, 0);
					processingTranslationMat = FlatMatrix4x4Multiplication(viewTransform,translation);
				}
			} else if (mouseClickRight) {
				// not supported
			} else {
				// all cleared
				if (System.currentTimeMillis() - mouseClickStartTime <= singleClickTime) {
					// apply mouse single click
				} else if (processingTranslation) {
					// apply translation permanently
					float deltaX = (Mouse.getX() - mouseClickStartX) / (float) displayWidth,
							deltaY = (Mouse.getY() - mouseClickStartY) / (float) displayHeight;
					float[] viewTransform = FlatTranslationMatrix4x4(deltaX, deltaY, 0);
					translation = FlatMatrix4x4Multiplication (viewTransform, translation);
					changeTranslation = true;
					processingTranslation = false;
				} else if (changePerspective) {
					// not supported - orthographic at this time
				}
				mouseClickStartX = mouseClickStartY = -1;
				mouseClickStartTime = 0;
			}
			// grid zoom
			if (changeZoom) {
				scaling = FlatScalingMatrix4x4(zoom/100f,zoom/100f,zoom/100f);
				viewDirty = true;
			}
			// translation
			if (processingTranslation || changeTranslation)
				viewDirty = true;
			if (viewDirty) {
				// re-compose
				t.setView(FlatMatrix4x4Multiplication(scaling, processingTranslation ? processingTranslationMat : translation));
				changeZoom = changeTranslation = false;
			}
			// draw routine
			if (Display.isVisible()) {
				if (t.isDirty())
					r.setMVP(t.compose());
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
