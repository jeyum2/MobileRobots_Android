package kr.ac.uos.je.view.interfaces;

import kr.ac.uos.je.model.EObjectType;

import com.badlogic.gdx.Application;


public interface MapObject {

	public EObjectType getObjectType();
	public void draw (Application app);
	public void dispose ();
}
