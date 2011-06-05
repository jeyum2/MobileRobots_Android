package kr.ac.uos.je.model;

import java.util.ArrayList;
import java.util.List;

public enum EObjectType{
	ROBOT_HOME(new float[]{0.5f,0.5f,1.0f,1.0f}),
	FORBIDDEN_AREA(new float[]{1.0f,1.0f,0.0f,1.0f}),
	MAP_LINE(new float[]{0.0f,0.0f,0.0f,1.0f}),
	MAP_POINT(new float[]{0.0f,0.0f,0.0f,1.0f}),
	FORBIDDEN_LINE(new float[]{1.0f,0.5f,0.0f,1.0f}),
	ROBOT_POSITION(new float[]{0.0f,0.0f,0.0f,1.0f}),
	SENSORS(new float[]{0.0f,0.5f,0.5f,1.0f}),
	GOALS(new float[]{1.0f,0.1f,0.1f,1.0f}),
	PATH(new float[]{0.0f,0.0f,1.0f,1.0f});
	
	public final String R_KEY;
	public final String G_KEY;
	public final String B_KEY;
	public final String ALPHA_KEY;
	private float[] defaultColor;
	private EObjectType(float[] defaultColor) {
		this.R_KEY = name()+"r";
		this.G_KEY = name()+"g";
		this.B_KEY = name()+"b";
		this.ALPHA_KEY = name()+"a";
		this.isColorChanged = false;
		additionalMapObjectList = new ArrayList<AdditionalMapObject>();
		this.defaultColor = defaultColor;
	}

	private boolean isVisible;
	public void setVisibile(boolean visibility){
		this.isVisible = visibility;
	}
	public boolean isVisible(){
		return this.isVisible;
	}
	private float[] color;

	public float[] getColor() {
		if(isColorChanged) {
			isColorChanged = false;
		}
		return color;
	}

	public void setColor(float[] color) {
		this.color = color;
		this.isColorChanged = true;
	}
	
	public boolean isColorChanged(){
		return isColorChanged;
	}
	private boolean isColorChanged;

	public float[] getDefaultColor() {
		return defaultColor.clone();
	}
	
	private float[] vertices;
	public void setVertices(float[] vertices){
		this.vertices = vertices;
	}
	public float[] getVertices(){
		return this.vertices;
	}
	private String description;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private String iconName;
	private String objectName;
	
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	private List<AdditionalMapObject> additionalMapObjectList;
	public void addAdditionalMapObject(AdditionalMapObject additionalMapObject){
		additionalMapObjectList.add(additionalMapObject);
	}
	public List<AdditionalMapObject> getAdditionalMapObject(){
		return additionalMapObjectList;
	}
	public static class AdditionalMapObject{
		private final int x;
		private final int y;
		private final String description;
		private final String iconName;
		private final String name;
		private final boolean additionalFunc;
		public AdditionalMapObject(int x, int y, String description, String iconName, String name, boolean additionalFunc) {
			this.x = x;
			this.y = y;
			this.description = description;
			this.iconName = iconName;
			this.name = name;
			this.additionalFunc = additionalFunc;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public String getDescription() {
			return description;
		}
		public String getIconName() {
			return iconName;
		}
		public String getName() {
			return name;
		}
		public boolean isAdditionalFunc() {
			return additionalFunc;
		}
		
	}
	
}
