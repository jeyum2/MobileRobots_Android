package kr.ac.uos.semix2.robot;

public interface Command {
	public int getId();
	public String getName();
	public String getGroupName();
	public String getDescription();
	public String getArgumentsDescription();
	public String getReturnValueDescription();
	public String[] getDataFlags();
}
