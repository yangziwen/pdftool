package com.ablesky.pdftool.command;

public interface Command {

	public String getName();
	
	public void invoke();
}
