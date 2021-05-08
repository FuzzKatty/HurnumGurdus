package RadiumVestigiumGurdus;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener{
	public int currentKeyCode;
	public void keyPressed(KeyEvent e) {
		//System.out.println(e.getKeyCode());
		currentKeyCode = e.getKeyCode();
	}
	public void keyReleased(KeyEvent e) {
		currentKeyCode = 0;
	}
	public void keyTyped(KeyEvent e) {}
}
