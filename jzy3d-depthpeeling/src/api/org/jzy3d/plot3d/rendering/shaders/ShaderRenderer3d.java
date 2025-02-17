package org.jzy3d.plot3d.rendering.shaders;

import org.jzy3d.painters.IPainter;
import org.jzy3d.plot3d.primitives.IGLRenderer;
import org.jzy3d.plot3d.rendering.view.Renderer3d;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.ViewportConfiguration;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class ShaderRenderer3d extends Renderer3d {
  protected IShaderable shaderable;
  protected boolean autoSwapBuffer = true;
  protected static boolean DEBUG = false;

  GLU glu = new GLU();

  public ShaderRenderer3d(final View view, boolean traceGL, boolean debugGL) {
    this(view, traceGL, debugGL, new Shaderable());
  }

  public ShaderRenderer3d(final View view, boolean traceGL, boolean debugGL,
      IShaderable shaderable) {
    super(view, traceGL, debugGL);
    this.shaderable = shaderable;// new Shaderable();
    this.shaderable.setTasksToRender(getShaderContentRenderer(view));
  }

  @Override
  public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();
    super.init(drawable);
    drawable.setAutoSwapBufferMode(autoSwapBuffer);
    shaderable.init(view.getPainter(), gl, width, height);
  }

  public static boolean DECOMPOSE_VIEW = true;

  @Override
  public void display(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();

    preDisplay(gl);
    shaderable.display(view.getPainter(), gl, glu); // will call taskToRender
    postDisplay(gl);

    if (!autoSwapBuffer)
      drawable.swapBuffers();
  }


  public void preDisplay(GL2 gl) {
    // decompose super.display, i.e. prevent to render scenegraph now,
    // and delegate to peeling algorithm
    synchronized (view) {
      view.clear();

      // render background
      view.renderBackground(0f, 1f);

      // render scene
      view.updateQuality();
      view.updateCamera(new ViewportConfiguration(width, height), view.computeScaledViewBounds());
    }
  }

  public void postDisplay(GL2 gl) {
    view.renderOverlay();
  }

  /* */

  public static IGLRenderer getShaderContentRenderer(final View view) {
    return new IGLRenderer() {
      @Override
      public void draw(IPainter painter) {
        view.renderSceneGraph(true);
      }
    };
  }

  /**
   * Rebuild all depth peeling buffers for the new screen size.
   */
  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL2 gl = drawable.getGL().getGL2();

    if (this.width != width || this.height != height) {
      this.width = width;
      this.height = height;
      shaderable.reshape(view.getPainter(), gl, width, height);
    }
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    shaderable.dispose(view.getPainter(), drawable.getGL().getGL2());
  }
}
