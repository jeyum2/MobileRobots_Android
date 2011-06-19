package kr.ac.uos.je;

import java.util.List;

import kr.ac.uos.je.model.EObjectType;
import kr.ac.uos.je.model.interfaces.ResourceManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.util.Log;

public enum EResourceManagerImpl implements ResourceManager{
	ResourceManger;
	


	private SharedPreferences mPreferences;
	private Context context;
	public void initPreferences(Context applicationContext) {
		this.context = applicationContext;
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
		Log.d("ROBOT", "SAVE PREFERENCE");
		
	}

	public void initResource(){
		
		 /*
		  * Load MapObject property
		  */
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


	/**
	 * because of localization, Load string resource from string.xml 
	 */
	@Override
	public String getStringByStringName(String name) {
		
		int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
		return (id != 0)? context.getResources().getString(id) : "String not found";
	}


	private List<String> sensorList;
	@Override
	public void setSensorList(List<String> sensorList) {
		this.sensorList = sensorList;
	}
	@Override
	public List<String> getSensorList(){
		return this.sensorList;
	}

	


}
