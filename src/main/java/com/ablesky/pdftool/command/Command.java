package com.ablesky.pdftool.command;

import com.beust.jcommander.JCommander;

public interface Command {

	public String getName();
	
	public void invoke(JCommander commander);
}
