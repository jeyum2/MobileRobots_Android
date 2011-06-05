package kr.ac.uos.je.screens;

import kr.ac.uos.je.controller.interfaces.AndroidAdaptor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class IntroScreen implements Screen {
	
	private AndroidAdaptor mAndroidAdaptor;
	private SpriteBatch spriteBatch;
	private TextureRegion introTextureRegion;
	private Texture introTexture; 
	public IntroScreen(Application app, AndroidAdaptor androidAdaptor) {
		this.mAndroidAdaptor = androidAdaptor;
		this.spriteBatch = new SpriteBatch();
		
		Pixmap pixmap = new Pixmap(Gdx.files.internal("data/intro.png"));
		int pixMapWidth = MathUtils.nextPowerOfTwo(pixmap.getWidth());
		int pixMapHeight = MathUtils.nextPowerOfTwo(pixmap.getHeight());
		Pixmap potPixmap = new Pixmap(pixMapWidth, pixMapHeight, pixmap.getFormat());
		potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
		introTexture = new Texture(potPixmap);
		introTextureRegion = new TextureRegion(introTexture,0,0,pixmap.getWidth(),pixmap.getHeight());
		
		pixmap.dispose();
		potPixmap.dispose();
		
		this.timeStamp = System.currentTimeMillis();
	}

	private long timeStamp;
	private boolean requestedloginDialog = false;
	private int centerX;
	private int centerY;
	private static final int SPLASH_TIME = 3000;
	@Override
	public void update(Application app) {
		centerX = Gdx.graphics.getWidth() / 2;
		centerY = Gdx.graphics.getHeight() / 2;
	}

	@Override
	public void render(Application app) {
		// TODO Auto-generated method stub
		spriteBatch.begin();
		spriteBatch.draw(introTextureRegion, 
				  centerX - introTextureRegion.getRegionWidth() / 2, 
				  centerY - introTextureRegion.getRegionHeight() / 2 
				  );
		spriteBatch.end();
		
		if(!requestedloginDialog && (System.currentTimeMillis() - timeStamp) > SPLASH_TIME){
			requestedloginDialog = true;
			mAndroidAdaptor.sendEmptyMessage(AndroidAdaptor.SHOW_LOGIN_DIALOG);
		}

	}


	@Override
	public void dispose() {
		introTexture.dispose();
		spriteBatch.dispose();
	}

}
