package com.ablesky.pdftool.validator;

import java.io.File;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class DestinationFolderValidator implements IValueValidator<File> {

	@Override
	public void validate(String name, File destFolder) throws ParameterException {
		if(destFolder != null && destFolder.exists() && !destFolder.isDirectory()) {
			throw new ParameterException("destination path exists but is not a folder!");
		}
	}

}
