package kr.ac.uos.je;

import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public enum EResourceManagerImpl implements ResourceManager{
	ResourceManger;
	
	@Override
	public String getStringByKey(String key) {
		return mPreferences.getString(key, "String not found");
	}


	private SharedPreferences mPreferences;
	public void initPreferences(Context applicationContext) {
		mPreferences = applicationContext.getSharedPreferences("resourceManager", 0);
		initResource();
	}


	public void saveResourceToPreference(){
		Editor edit = mPreferences.edit();
		for (EObjectType objectType : EObjectType.values()){
			
			edit.putFloat(objectType.R_KEY, objectType.getColor()[0]);
			edit.putFloat(objectType.G_KEY, objectType.getColor()[1]);
			edit.putFloat(objectType.B_KEY, objectType.getColor()[2]);
			edit.putFloat(objectType.ALPHA_KEY, objectType.getColor()[3]);
			edit.putBoolean(objectType.name(), objectType.isVisible());
			
		}
		edit.commit();
		
	}
	public void initResource(){
		for (EObjectType objectType : EObjectType.values()){
			objectType.setColor(new float[]{
			mPreferences.getFloat(objectType.R_KEY, objectType.getDefaultColor()[0])
			,mPreferences.getFloat(objectType.G_KEY, objectType.getDefaultColor()[1])
			,mPreferences.getFloat(objectType.B_KEY, objectType.getDefaultColor()[2])
			,mPreferences.getFloat(objectType.ALPHA_KEY, objectType.getDefaultColor()[3])
			});
			objectType.setVisibile(
					mPreferences.getBoolean(objectType.name(), true));
		}
		
	}


}
