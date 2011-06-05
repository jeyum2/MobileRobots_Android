package kr.ac.uos.je.view.interfaces;

import kr.ac.uos.je.model.EObjectType;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public interface MapObject {

	public EObjectType getObjectType();
	public void draw (Application app);
	public void dispose ();
	public void update(Application app, SpriteBatch spriteBatch);
}
