package kr.ac.uos.je;

import kr.ac.uos.je.accessories.EMesh;
import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;
import kr.ac.uos.je.controller.interfaces.IFacedeController;
import kr.ac.uos.je.model.EMapManager;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import kr.ac.uos.je.screens.IntroScreen;
import kr.ac.uos.je.screens.MainScreen;
import kr.ac.uos.je.screens.Screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class MobileRobotsFacade implements ApplicationListener, IFacedeController {

	private Screen screen;
	public enum EScreenStatus{IntroScreen, MainScreen}
	private EScreenStatus mScreenStatus;
	public void setScreen(EScreenStatus screenStatus){
		this.mScreenStatus = screenStatus;
		screen.dispose();
		switch (screenStatus) {
		case IntroScreen:
			screen = new IntroScreen(Gdx.app, mAndroidAdaptor);
			break;
		case MainScreen:
			screen = new MainScreen(Gdx.app, mAndroidAdaptor);
			break;
		default:
			break;
		}
	}
	
	private ResourceManager mResourceManager;
	private AndroidAdaptor mAndroidAdaptor;
	public MobileRobotsFacade(ResourceManager resourceManager, AndroidAdaptor androidAdaptor) {
		this.mResourceManager = resourceManager;
		this.mAndroidAdaptor = androidAdaptor;
	}
	@Override
	public void create() {
		EMapManager.MapManager.setScreenSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		screen = new IntroScreen(Gdx.app, mAndroidAdaptor);
		mScreenStatus = EScreenStatus.IntroScreen;
	}

	@Override
	public void resume() {
		render();
	}

	@Override
	public void render() {
		Application app = Gdx.app;
		
		// update the screen
		screen.update(app);
		// render the screen
		screen.render(app);
		
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
		screen.dispose();
		EMesh.dispose();
	}
	

}
