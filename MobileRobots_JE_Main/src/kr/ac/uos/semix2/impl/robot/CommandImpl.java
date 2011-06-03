package kr.ac.uos.semix2.impl.robot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import kr.ac.uos.semix2.robot.Command;

public class CommandImpl implements Command {
	private final int					_id;
	private final String				_name;
	private final String				_description;
	private final List<String>			_dataFlagList;
	private String						_groupName;
	private String						_argumentsDescription;
	private String						_returnValueDescription;
	
	public CommandImpl(int commandId, String name, String description) {
		_id				= commandId;
		_name			= name;
		_description	= description;
		_dataFlagList	= new LinkedList<String>();
	}

	public int getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setCommandGroup(String commandGroup) {
		_groupName = commandGroup;
	}
	
	public String getGroupName() {
		return _groupName;
	}
	
	public String getDescription() {
		return _description;
	}
	
	public void setArgumentsDescription(String description) {
		_argumentsDescription = description;
	}
	
	public String getArgumentsDescription() {
		return _argumentsDescription;
	}
	
	public void setReturnValueDescription(String description) {
		_returnValueDescription = description;
	}
	
	public String getReturnValueDescription() {
		return _returnValueDescription;
	}
	
	public void addDataFlag(String dataFlag) {
		_dataFlagList.add(dataFlag);
	}
	
	private static final String[] NULL_STR = new String[0];
	public String[] getDataFlags() {
		return _dataFlagList.toArray(NULL_STR);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[").append(_id).append("] ").append(_name).append("\n");
		builder.append("\tGroup Name            : ").append(_groupName).append("\n");
		builder.append("\tDescription           : ").append(_description.replace("\n", " ")).append("\n");
		if (_dataFlagList.size() > 0) {
			builder.append("\tData Flag             : ");
			Iterator<String> iter = _dataFlagList.iterator();
			while(iter.hasNext()) {
				String dataFlag = iter.next();
				builder.append(dataFlag);
				if (iter.hasNext()) {
					builder.append(", ");
				}
			}
			builder.append("\n");
		}
		builder.append("\tArgument Description  : ").append(_argumentsDescription).append("\n");
		builder.append("\tReturn Description    : ").append(_returnValueDescription).append("\n");
		return builder.toString();
	}
}
