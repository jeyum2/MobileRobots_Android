package kr.ac.uos.je.model;

import java.util.Set;
import java.util.TreeSet;

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
		subObjectSet = new TreeSet<SubObject>();
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
	/**
	 * When RobotPosition : x, y, th, velocity, rotateVelocity
	 * @param vertices
	 */
	public void setVertices(float[] vertices){
		this.vertices = vertices;
	}
	/**
	 * When RobotPosition : x, y, th, velocity, rotateVelocity
	 * @param vertices
	 */
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
	private TreeSet<SubObject> subObjectSet;
	public void addSubObject(SubObject additionalMapObject){
		subObjectSet.add(additionalMapObject);
	}
	public Set<SubObject> getSubObjects(){
		return subObjectSet;
	}
	public String[] getSubObjectNames(){
		String[] nameList = new String[subObjectSet.size()];
		int i = 0;
		for (SubObject subObject : subObjectSet) {
			nameList[i] = subObject.getName();
			i++;
		}
		return nameList;
	}
	public static class SubObject implements Comparable<SubObject>{
		private final String description;
		private final String iconName;
		private final String name;
		private final boolean additionalFunc;
		private final float[] vertices;
		public SubObject(float[] vertices, String description, String iconName, String name, boolean additionalFunc) {
			this.vertices = vertices;
			this.description = description;
			this.iconName = iconName;
			this.name = name;
			this.additionalFunc = additionalFunc;
		}
		public float getX() {
			return vertices[0];
		}
		public float getY() {
			return vertices[1];
		}
		public float[] getVertices(){
			return vertices;
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
		@Override
		public int compareTo(SubObject s) {
			return name.compareTo(s.name);
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof SubObject){
				SubObject s = (SubObject) obj; 
				return name.equals(s.getName());
			}
			return false;
		}
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		
	}
	
}
