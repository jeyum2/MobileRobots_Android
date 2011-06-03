package kr.ac.uos.je;

import kr.ac.uos.je.view.MobileRobotsMainRenderer;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class MobileRobotsDesktopMain {
	public static void main(String[] args) {
		new JoglApplication(new MobileRobotsMainRenderer(), "MobileRobotsDesktop", 480, 800, false);
	}

}
