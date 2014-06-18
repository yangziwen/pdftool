package com.ablesky.pdftool.validator;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

public class SourceFileValidator implements IValueValidator<File> {


	@Override
	public void validate(String name, File srcFile) throws ParameterException {
		if(srcFile == null || !srcFile.isFile() || FileUtils.sizeOf(srcFile) == 0) {
			throw new ParameterException("Invalid source file input!");
		}
		if(!"pdf".equalsIgnoreCase(FilenameUtils.getExtension(srcFile.getAbsolutePath()))) {
			throw new ParameterException("The format of input source file is not pdf!");
		}
	}

}
