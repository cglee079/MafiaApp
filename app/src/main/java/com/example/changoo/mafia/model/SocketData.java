package com.example.changoo.mafia.model;

import java.io.Serializable;

public class SocketData implements Serializable{
	private static final long serialVersionUID = 8412342965210765619L;

	String command;
	String name;
	Object object;

	public SocketData(String command, String name, Object object) {
		this.command = command;
		this.name = name;
		this.object = object;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
