package examples.apps;
/*
 * colormat.java After initialization, the program will be in ColorMaterial mode. Interaction:
 * pressing the mouse buttons will change the diffuse reflection values.
 */

import java.awt.Frame;
import java.io.IOException;
import jgl.GL;
import jgl.wt.awt.GLCanvas;
import jgl.wt.awt.GLUT;

public class colormat extends GLCanvas {

  private float diffuseMaterial[] = {0.5f, 0.5f, 0.5f, 1.0f};

  /*
   * Initialize material property, light source, lighting model, and depth buffer.
   */
  private void myinit() {
    float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
    float light_position[] = {1.0f, 1.0f, 1.0f, 0.0f};

    myGL.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    myGL.glShadeModel(GL.GL_SMOOTH);
    myGL.glEnable(GL.GL_DEPTH_TEST);
    myGL.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, diffuseMaterial);
    myGL.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, mat_specular);
    myGL.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 25.0f);
    myGL.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light_position);
    myGL.glEnable(GL.GL_LIGHTING);
    myGL.glEnable(GL.GL_LIGHT0);

    myGL.glColorMaterial(GL.GL_FRONT, GL.GL_DIFFUSE);
    myGL.glEnable(GL.GL_COLOR_MATERIAL);
  }

  public void display() {
    myGL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    myUT.glutSolidSphere(1.0, 20, 16);
    myGL.glFlush();
  }

  public void myReshape(int w, int h) {
    myGL.glViewport(0, 0, w, h);
    myGL.glMatrixMode(GL.GL_PROJECTION);
    myGL.glLoadIdentity();
    if (w <= h) {
      myGL.glOrtho(-1.5f, 1.5f, -1.5f * (float) h / (float) w, 1.5f * (float) h / (float) w, -10.0f,
          10.0f);
    } else {
      myGL.glOrtho(-1.5f * (float) w / (float) h, 1.5f * (float) w / (float) h, -1.5f, 1.5f, -10.0f,
          10.0f);
    }
    myGL.glMatrixMode(GL.GL_MODELVIEW);
    myGL.glLoadIdentity();
  }

  /* ARGSUSED2 */
  public void mouse(int button, int state, int x, int y) {
    switch (button) {
      case GLUT.GLUT_LEFT_BUTTON:
        if (state == GLUT.GLUT_DOWN) {
          diffuseMaterial[0] += 0.1f;
          if (diffuseMaterial[0] > 1.0f)
            diffuseMaterial[0] = 0.0f;
          myGL.glColor4fv(diffuseMaterial);
          myUT.glutPostRedisplay();
        }
        break;
      case GLUT.GLUT_MIDDLE_BUTTON:
        if (state == GLUT.GLUT_DOWN) {
          diffuseMaterial[1] += 0.1f;
          if (diffuseMaterial[1] > 1.0f)
            diffuseMaterial[1] = 0.0f;
          myGL.glColor4fv(diffuseMaterial);
          myUT.glutPostRedisplay();
        }
        break;
      case GLUT.GLUT_RIGHT_BUTTON:
        if (state == GLUT.GLUT_DOWN) {
          diffuseMaterial[2] += 0.1f;
          if (diffuseMaterial[2] > 1.0f)
            diffuseMaterial[2] = 0.0f;
          myGL.glColor4fv(diffuseMaterial);
          myUT.glutPostRedisplay();
        }
        break;
      default:
        break;
    }
  }

  /* ARGSUSED1 */
  public void keyboard(char key, int x, int y) {
    switch (key) {
      case 27:
        System.exit(0);
      default:
        break;
    }
  }

  public void init() {
    myUT.glutInitWindowSize(500, 500);
    myUT.glutInitWindowPosition(0, 0);
    myUT.glutCreateWindow(this);
    myinit();
    myUT.glutDisplayFunc("display");
    myUT.glutReshapeFunc("myReshape");
    myUT.glutMouseFunc("mouse");
    myUT.glutKeyboardFunc("keyboard");
    myUT.glutMainLoop();
  }

  static public void main(String args[]) throws IOException {
    Frame mainFrame = new Frame();
    mainFrame.setSize(508, 527);
    colormat mainCanvas = new colormat();
    mainCanvas.init();
    mainFrame.add(mainCanvas);
    mainFrame.setVisible(true);
  }

}
