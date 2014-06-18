package com.ablesky.pdftool;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ablesky.pdftool.command.Command;
import com.ablesky.pdftool.command.SplitCommand;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class PdfToolMain {
	
	public static void main(String[] args) {
		
		Map<String, Command> commandMap = new HashMap<String, Command>();
		JCommander commander = new JCommander();
		
		for(Command command: Arrays.asList(new SplitCommand())) {
			commandMap.put(command.getName(), command);
			commander.addCommand(command.getName(), command);
		}
		
		try {
			commander.parse(args);
		} catch (ParameterException e) {
			System.err.println(e.getMessage());
			return;
		}
		
		Command invokedCommand = commandMap.get(commander.getParsedCommand());
		if(invokedCommand != null) {
			invokedCommand.invoke();
		} else {
			System.err.println("Invalid command!");
		}
	}
	
}
